package bo;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.*;

import bo.TeamSeason.TeamSeasonId;

@SuppressWarnings("serial")
@Entity(name = "teamSeasonPlayer")
public class TeamSeasonPlayer implements Serializable {
	
	@EmbeddedId
	TeamSeasonPlayerId id;
	
	/*@Id
	@ManyToOne
	@JoinColumn(name="playerid")
	Player play;
	
	@Id
	@ManyToOne
	@JoinColumn(name="teamid")
	Team team;
	
	@Id
	@Column
	Integer year;*/
/*
	public Player getPlay() {
		return play;
	}

	public void setPlay(Player play) {
		this.play = play;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public TeamSeasonPlayer() {
		super();
	}	
*/
	
	@Embeddable
	static class TeamSeasonPlayerId implements Serializable {
			
		@ManyToOne
		@JoinColumn(name = "playerid", referencedColumnName = "playerid", insertable = false, updatable = false)
		Player player;
		
		@ManyToOne
		@JoinColumn(name = "teamid", referencedColumnName = "teamid", insertable = false, updatable = false)
		Team team;
		
		
		
		public boolean equals(Object obj) {
			if(!(obj instanceof TeamSeasonId)){
				return false;
			}
			TeamSeasonPlayerId other = (TeamSeasonPlayerId)obj;
			// in order for two different object of this type to be equal,
			// they must be for the same year and for the same team
			return (this.team==other.team &&
					this.player==other.player);
		}
		 
		@Override
		public int hashCode() {
			Integer hash = 0;
			if (this.team != null) hash += this.team.hashCode();
			
			if (this.player != null) hash += this.player.hashCode();
			return hash;
		}
	}
	
	@Column
	Integer year;
	/*
	 @JoinColumn(name="playerId")
	 Integer playerId;*/
	
	
/*	@Id
	@JoinColumn(name = "playerId")
	Integer playerId;
	@Id
	@JoinColumn(name = "teamId")
	Integer teamId;
	@Id
	@Column
	Integer year;*/
	/*public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}*/
	/*public TeamSeasonPlayer(Integer playerId, Integer teamId, Integer year) {
		super();
		this.playerId = playerId;
		this.teamId = teamId;
		this.year = year;
	}*/
	public TeamSeasonPlayer(){System.out.println("creating");
	//	this.id.playerId = playerId;
		TeamSeasonPlayerId tsp = new TeamSeasonPlayerId();
		/*this.play = p;
		this.team = t;
		this.year = year;*/
		id = tsp;
		this.year = null;
	}
	
	public TeamSeasonPlayer(Player p, Team t, Integer year){
//		this.id.playerId = playerId;
		TeamSeasonPlayerId tsp = new TeamSeasonPlayerId();
		/*this.play = p;
		this.team = t;
		this.year = year;*/
		tsp.team = t;
		tsp.player = p;
		id = tsp;
		this.year = year;
	}
	
	public TeamSeasonPlayerId getId() {
		return id;
	}

	public void setId(TeamSeasonPlayerId teamId) {
		this.id = teamId;
	}
	
	public Integer getYear() {
		//return this.id.year;
		return this.year;
	}

	public void setYear(Integer year) {
		//this.id.year = year;
		this.year = year;
	}
	
	public Player getPlayer() {
		return this.id.player;
	}

	public void setPlayer(Player p) {
		this.id.player = p;
	}
	
	public Team getTeam() {
		return this.id.team;
	}

	public void setTeam(Team p) {
		this.id.team = p;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TeamSeasonPlayer)){
			return false;
		}
		TeamSeasonPlayer other = (TeamSeasonPlayer) obj;
		return (other.getId().equals(this.getId()));
	}
	
	

	@Override
	public int hashCode() {
		
		return this.getId().hashCode();
	}
	
	public static Comparator<TeamSeasonPlayer> teamSeasonsPlayerComparator = new Comparator<TeamSeasonPlayer>() {

		public int compare(TeamSeasonPlayer tsp1, TeamSeasonPlayer tsp2) {
			TeamSeasonPlayerId id1 = tsp1.getId();
			TeamSeasonPlayerId id2 = tsp2.getId();
			if(id1.equals(id2) && tsp1.getYear()== tsp2.getYear()) {
				return 0;
			}
			else {
				return -1;
			}
			
		}

	};

	
}
