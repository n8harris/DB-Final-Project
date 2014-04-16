package bo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import bo.PlayerSeason.PlayerSeasonId;


@SuppressWarnings("serial")
@Entity(name = "teamseason")
public class TeamSeason implements Serializable{
	@EmbeddedId
	TeamSeasonId id;
	
	@Embeddable
	static class TeamSeasonId implements Serializable {
		@ManyToOne
		@JoinColumn(name = "teamid", referencedColumnName = "teamid", insertable = false, updatable = false)
		Team team;
		@Column
		Integer year;
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof TeamSeasonId)){
				return false;
			}
			TeamSeasonId other = (TeamSeasonId)obj;
			// in order for two different object of this type to be equal,
			// they must be for the same year and for the same team
			return (this.team==other.team &&
					this.year==other.year);
		}
		 
		@Override
		public int hashCode() {
			Integer hash = 0;
			if (this.team != null) hash += this.team.hashCode();
			if (this.year != null) hash += this.year.hashCode();
			return hash;
		}
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="id")
	@Fetch(FetchMode.JOIN)
	Set<TeamSeasonPlayer> seasonPlayer = new HashSet<TeamSeasonPlayer>();

	
	/*@Id
	@JoinColumn(name = "teamId", referencedColumnName="teamId")
	String teamId;*/
		
	/*@Id
	Integer year;
*/
	@Column
	Integer gamesPlayed;

	@Column
	Integer wins;
	
	@Column
	Integer losses;
	
	@Column
	Integer team_rank;
	
	@Column
	Integer totalAttendance;

	public TeamSeason() {}
	
	public TeamSeason(Team t, Integer year){
		TeamSeasonId tsi = new TeamSeasonId();
		tsi.team = t;
		tsi.year = year;
		id = tsi;
		//System.out.println("Creating teamSeason");
	}
	
	public TeamSeason(String teamId, Integer year, Integer gamesPlayed,
			Integer wins, Integer losses, Integer team_rank,
			Integer totalAttendance) {
		super();
		this.id = new TeamSeasonId();
		//this.year = year;
		this.gamesPlayed = gamesPlayed;
		this.wins = wins;
		this.losses = losses;
		this.team_rank = team_rank;
		this.totalAttendance = totalAttendance;
	}

	public TeamSeasonId getId() {
		return id;
	}

	public void setId(TeamSeasonId teamId) {
		this.id = teamId;
	}
	
	public void addSeasonPlayer(TeamSeasonPlayer sP){
		this.seasonPlayer.add(sP);
	}
	
	public Set<TeamSeasonPlayer> getSeasonPlayer(){
		return seasonPlayer;
	}

	public Integer getYear() {
		return this.id.year;
	}

	public void setYear(Integer year) {
		this.id.year = year;
	}

	public Integer getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(Integer gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	public Integer getTeam_rank() {
		return team_rank;
	}

	public void setTeam_rank(Integer team_rank) {
		this.team_rank = team_rank;
	}

	public Integer getTotalAttendance() {
		return totalAttendance;
	}

	public void setTotalAttendance(Integer totalAttendance) {
		this.totalAttendance = totalAttendance;
	}
	
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof TeamSeason)){
			return false;
		}
		TeamSeason other = (TeamSeason) obj;
		return other.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
	
	@Override
	public String toString(){
		return "team: " + this.id.team + "\nyear: " + this.id.year;
	}
	
	public static Comparator<TeamSeason> teamSeasonsComparator = new Comparator<TeamSeason>() {

		public int compare(TeamSeason ts1, TeamSeason ts2) {
			Integer year1 = ts1.getYear();
			Integer year2 = ts2.getYear();
			return year1.compareTo(year2);
		}

	};
	/*@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TeamSeason)){
			return false;
		}
		TeamSeason other = (TeamSeason) obj;
		return (this.getYear()==other.getYear() &&
				this.getTeamId()==other.getTeamId());
	}
	*/
/*	@Override
	public int hashCode() {
		Integer hash = 0;
		if (this.getTeamId()!=null) hash += this.getTeamId().hashCode(); 
		if (this.getYear()!=null) hash += this.getYear().hashCode();
		return hash;
	}*/

	public Team getTeam() {
		return this.id.team;
	}
	
	public void setTeam(Team t){
		this.id.team = t;
	}
	
}
