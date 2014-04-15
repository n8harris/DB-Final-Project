package conversion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import bo.BattingStats;
import bo.CatchingStats;
import bo.FieldingStats;
import bo.PitchingStats;
import bo.Player;
import bo.PlayerSeason;
import bo.Team;
import bo.TeamSeason;
import bo.TeamSeasonPlayer;
import dataaccesslayer.HibernateUtil;

public class Convert {

	static Connection conn;
	static final String MYSQL_CONN_URL = "jdbc:mysql://192.168.56.1:3306/mlb?user=root&password=password"; 

	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
			conn = DriverManager.getConnection(MYSQL_CONN_URL);
			System.out.println("Conversion started");
			//convertPlayers();
			convertTeam();
			System.out.println("Conversion finished");
		long endTime = System.currentTimeMillis();	
			long elapsed = (endTime - startTime) / (1000*60);
			System.out.println("Elapsed time in mins: " + elapsed);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!conn.isClosed()) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void convertPlayers() {
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
						"playerID, " + 
						"nameFirst, " + 
						"nameLast, " + 
						"NameNick, "+ 
						"birthDay, " + 
						"birthMonth, " + 
						"birthYear, " + 
						"deathDay, "+ 
						"deathMonth, " + 
						"deathYear, " + 
						"bats, " + 
						"throws, " + 
						"birthCity, " + 
						"birthState, " + 
						"debut, " + 
						"finalGame, " + 
						"college " +
						"from MASTER");
						// for debugging comment previous line, uncomment next line
						//"from MASTER where playerID = 'bondsba01' or playerID = 'youklke01';");
			ResultSet rs = ps.executeQuery();
			int count=0; // for progress feedback only
			while (rs.next()) {
				count++;
				// this just gives us some progress feedback
				if (count % 1000 == 0) System.out.println("num players: " + count);
				String pid = rs.getString("playerID").trim();
				String firstName = rs.getString("nameFirst").trim();
				String lastName = rs.getString("nameLast").trim();
				// this check is for data scrubbing
				// don't want to bring anybody over that doesn't have a pid, firstname and lastname
				if (pid == null	|| pid.isEmpty() || 
					firstName == null || firstName.isEmpty() ||
					lastName == null || lastName.isEmpty()) continue;
				Player p = new Player();
				p.setName(firstName + " " + lastName);
				p.setNickName(rs.getString("nameNick"));
				java.util.Date birthDay = convertIntsToDate(rs.getInt("birthYear"), rs.getInt("birthMonth"), rs.getInt("birthDay"));
				if (birthDay!=null) p.setBirthDay(birthDay);
				java.util.Date deathDay = convertIntsToDate(rs.getInt("deathYear"), rs.getInt("deathMonth"), rs.getInt("deathDay"));
				if (deathDay!=null) p.setDeathDay(deathDay);
				// need to do some data scrubbing for bats and throws columns
				String hand = rs.getString("bats").trim();
				if (hand.equalsIgnoreCase("B")) {
					hand = "S";
				} else if (hand.isEmpty()) {
					hand = null;
				}
				p.setBattingHand(hand);
				hand = rs.getString("throws").trim();
				if (hand.isEmpty()) {
					hand = null;
				}
				p.setThrowingHand(hand);
				p.setBirthCity(rs.getString("birthCity"));
				p.setBirthState(rs.getString("birthState"));
				java.util.Date firstGame = convertStringToDate(rs.getString("debut"));
				if (firstGame!=null) p.setFirstGame(firstGame);
				java.util.Date lastGame = convertStringToDate(rs.getString("finalGame"));
				if (lastGame!=null) p.setLastGame(lastGame);
				p.setCollege(rs.getString("college"));
				addPositions(p, pid);
				// players bio collected, now go after stats
				addSeasons(p, pid);
				// we can now persist player, and the seasons and stats will cascade
				HibernateUtil.persistPlayer(p);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static java.util.Date convertIntsToDate(int year, int month, int day) {
		Calendar c = new GregorianCalendar();
		java.util.Date d=null;
		// if year is 0, then date wasn't populated in MySQL database
		if (year!=0) {
			c.set(year, month, day);
			d = c.getTime();
		}
		return d;
	}
	
	private static java.util.Date convertStringToDate(String dateString) {
		Calendar c = new GregorianCalendar();
		java.util.Date d=null;
		if (!(dateString == null || dateString.isEmpty())) {
			String parts[] = dateString.split("/");
			c.set(Integer.parseInt(parts[2]),
					Integer.parseInt(parts[0]),
					Integer.parseInt(parts[1]));
			d = c.getTime();
		}		
		return d;
	}
	
	public static void addPositions(Player p, String pid) {
		Set<String> positions = new HashSet<String>();
		try {
			PreparedStatement ps = conn.prepareStatement("select " +
					"distinct pos " +
					"from fielding " +
					"where playerID = ?;");
			ps.setString(1, pid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String pos = rs.getString("pos");
				positions.add(pos);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		p.setPositions(positions);
	}

	public static void addSeasons(Player p, String pid) {
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
					"yearID, " + 
					"teamID, " +
					"lgId, " +
					"sum(G) as gamesPlayed " + 
					"from batting " + 
					"where playerID = ? " + 
					"group by yearID, teamID, lgID;");
			ps.setString(1, pid);
			ResultSet rs = ps.executeQuery();
			PlayerSeason s = null;
			while (rs.next()) {
				int yid = rs.getInt("yearID");
				s = p.getPlayerSeason(yid);
				// it is possible to see more than one of these per player if he switched teams
				// set all of these attrs the first time we see this playerseason
				if (s==null) {
					s = new PlayerSeason(p,yid);
					p.addSeason(s);
					s.setGamesPlayed(rs.getInt("gamesPlayed"));
					double salary = getSalary(pid, yid);
					s.setSalary(salary);
					BattingStats batting = getBatting(s,pid,yid);
					s.setBattingStats(batting);
					FieldingStats fielding = getFielding(s,pid,yid);
					s.setFieldingStats(fielding);
					PitchingStats pitching = getPitching(s,pid,yid);
					s.setPitchingStats(pitching);
					CatchingStats catching = getCatching(s,pid,yid);
					s.setCatchingStats(catching);
				// set this the consecutive time(s) so it is the total games played regardless of team	
				} else {
					s.setGamesPlayed(rs.getInt("gamesPlayed")+s.getGamesPlayed());
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static double getSalary(String pid, Integer yid) {
		double salary = 0;
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
					"sum(salary) as salary " + 
					"from salaries " + 
					"where playerID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, pid);
			ps.setInt(2, yid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				salary = rs.getDouble("salary");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salary;
	}
	
	

	public static BattingStats getBatting(PlayerSeason psi, String pid, Integer yid) {
		BattingStats s = new BattingStats();
		try {
			PreparedStatement ps = conn.prepareStatement("select "	+ "" +
					"sum(AB) as atBats, " + 
					"sum(H) as hits, " + 
					"sum(2B) as doubles, " + 
					"sum(3B) as triples, " + 
					"sum(HR) as homeRuns, " + 
					"sum(RBI) as runsBattedIn, " + 
					"sum(SO) as strikeouts, " + 
					"sum(BB) as walks, " + 
					"sum(HBP) as hitByPitch, " + 
					"sum(IBB) as intentionalWalks, " + 
					"sum(SB) as steals, " + 
					"sum(CS) as stealsAttempted " + 
					"from batting " + 
					"where playerID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, pid);
			ps.setInt(2, yid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				s.setId(psi);
				s.setAtBats(rs.getInt("atBats"));
				s.setHits(rs.getInt("hits"));
				s.setDoubles(rs.getInt("doubles"));
				s.setTriples(rs.getInt("triples"));
				s.setHomeRuns(rs.getInt("homeRuns"));
				s.setRunsBattedIn(rs.getInt("runsBattedIn"));
				s.setStrikeouts(rs.getInt("strikeouts"));
				s.setWalks(rs.getInt("walks"));
				s.setHitByPitch(rs.getInt("hitByPitch"));
				s.setIntentionalWalks(rs.getInt("intentionalWalks"));
				s.setSteals(rs.getInt("steals"));
				s.setStealsAttempted(rs.getInt("stealsAttempted"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static FieldingStats getFielding(PlayerSeason psi, String pid, Integer yid) {
		FieldingStats s = new FieldingStats();
		try {
			PreparedStatement ps = conn.prepareStatement("select " +
					"sum(E) as errors, " +
					"sum(PO) as putOuts " +
					"from fielding " +
					"where playerID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, pid);
			ps.setInt(2, yid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				s.setId(psi);
				s.setErrors(rs.getInt("errors"));
				s.setPutOuts(rs.getInt("putOuts"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static PitchingStats getPitching(PlayerSeason psi, String pid, Integer yid) {
		PitchingStats s = new PitchingStats();
		try {
			PreparedStatement ps = conn.prepareStatement("select " +
					"sum(IPOuts) as outsPitched, " + 
					"sum(ER) as earnedRunsAllowed, " +
					"sum(HR) as homeRunsAllowed, " + 
					"sum(SO) as strikeouts, " +
					"sum(BB) as walks, " + 
					"sum(W) as wins, " +
					"sum(L) as losses, " + 
					"sum(WP) as wildPitches, " +
					"sum(BFP) as battersFaced, " + 
					"sum(HBP) as hitBatters, " +
					"sum(SV) as saves " + 
					"from pitching " +
					"where playerID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, pid);
			ps.setInt(2, yid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				s.setId(psi);
				s.setOutsPitched(rs.getInt("outsPitched"));
				s.setEarnedRunsAllowed(rs.getInt("earnedRunsAllowed"));
				s.setHomeRunsAllowed(rs.getInt("homeRunsAllowed"));
				s.setStrikeouts(rs.getInt("strikeouts"));
				s.setWalks(rs.getInt("walks"));
				s.setWins(rs.getInt("wins"));
				s.setLosses(rs.getInt("losses"));
				s.setWildPitches(rs.getInt("wildPitches"));
				s.setBattersFaced(rs.getInt("battersFaced"));
				s.setHitBatters(rs.getInt("hitBatters"));
				s.setSaves(rs.getInt("saves"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static CatchingStats getCatching(PlayerSeason psi, String pid, Integer yid) {
		CatchingStats s = new CatchingStats();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("select " +
					"sum(PB) as passedBalls, " +
					"sum(WP) as wildPitches, " +
					"sum(SB) as stealsAllowed, " +
					"sum(CS) as stealsCaught " +
					"from fielding " +
					"where playerID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, pid);
			ps.setInt(2, yid);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				s.setId(psi);
				s.setPassedBalls(rs.getInt("passedBalls"));
				s.setWildPitches(rs.getInt("wildPitches"));
				s.setStealsAllowed(rs.getInt("stealsAllowed"));
				s.setStealsCaught(rs.getInt("stealsCaught"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			ps.toString();
			e.printStackTrace();
		}
		return s;
	}
	
	public static void convertTeam() {
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
						"teamID, " + 
						"name, " + 
						"lgID " +
						"from teams");
		
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Team t = new Team();
				String teamName = rs.getString("name").trim();
				t.setName(teamName);
				String teamId = rs.getString("teamId").trim();
				if (teamName == null	|| teamName.isEmpty() || 
						teamId == null || teamId.isEmpty()) continue;
				String league = rs.getString("lgID").trim();
				t.setLeague(league);
				//System.out.println("."+teamId+".");
				
				addTeamSeasons(t, teamId);
				HibernateUtil.persistTeam(t);
			
			}
			// For year founded, just select the first recorded year?
			// And for yearLast, the most recent year recorded
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addTeamSeasons(Team t, String tid) {
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
					"teamID, " +
					"yearID, " + 
					"G, " + 
					"W, " +
					"L, " + 
					"Rank, " +
					"attendance " +
					"from teams " +
					"where teamID = ? " + 
					"group by yearID, teamID;");
			//year, gamesPlayed, wins, losses, team_rank, totalAttendance, 
			ps.setString(1, tid);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				// For each year of this team's existence, make an entry in TeamSeason
				Integer gamesPlayed = Integer.parseInt(rs.getString("G").trim());
				Integer wins = Integer.parseInt(rs.getString("W").trim());
				Integer losses = Integer.parseInt(rs.getString("L").trim());
				Integer teamRank = Integer.parseInt(rs.getString("Rank").trim());
				Integer attendance = Integer.parseInt(rs.getString("attendance").trim());
				Integer yearID = Integer.parseInt(rs.getString("yearID").trim());

//				TeamSeason tS = new TeamSeason(tid, yearID, 
//						gamesPlayed, wins, losses, teamRank, attendance);
				TeamSeason tS = new TeamSeason(t, yearID);
				t.addSeason(tS);
				tS.setGamesPlayed(gamesPlayed);
				tS.setLosses(losses);
				tS.setTeam_rank(teamRank);
				tS.setTotalAttendance(attendance);
				tS.setWins(wins);
				//System.out.println(tS);
				//addTeamSeasonPlayer(tS, tid, yearID);

			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private static void addTeamSeasonPlayer(TeamSeason tS, String tid,
			Integer yearID) {
		// The Salaries table seems to be the only link between players and teams. 
		// So that should work for populating the TeamSeasonPlayer table
		try {
			PreparedStatement ps = conn.prepareStatement("select " + 
					"playerID, " +
					"teamID, " +
					"yearID, " + 
					"from salaries " +
					"where teamID = ? " + 
					"and yearID = ? ;");
			ps.setString(1, tid);
			ps.setString(2, Integer.toString(yearID));
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Integer playerID = Integer.parseInt(rs.getString("playerID").trim());
				TeamSeasonPlayer tsp = new TeamSeasonPlayer(playerID, Integer.parseInt(tid), yearID);
				HibernateUtil.persistTeamSeasonPlayer(tsp);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}