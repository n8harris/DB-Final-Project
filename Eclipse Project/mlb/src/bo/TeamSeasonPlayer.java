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
	
	@Embeddable
	static class TeamSeasonPlayerId implements Serializable {
		@ManyToOne
		@JoinColumns({
			@JoinColumn(name="teamid"),
			@JoinColumn(name = "year")
		})
		TeamSeason id;
		
		
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof TeamSeasonPlayerId)){
				return false;
			}
			TeamSeasonPlayerId other = (TeamSeasonPlayerId)obj;
			// in order for two different object of this type to be equal,
			// they must be for the same year and for the same team
			return (this.id.getTeam()==other.id.getTeam());/* &&
					this.playerId==other.playerId);*/
		}
		
		@Override
		public int hashCode() {
			Integer hash = 0;
			if (this.id.getTeam() != null) hash += this.id.getTeam().hashCode();
			if (this.id.getYear() != null) hash += this.id.getYear().hashCode();
			//if (this.playerId != null) hash += this.playerId.hashCode();
			return hash;
		}
	}
	
	@JoinColumn(name="playerId")
	Integer playerId;
	
	
	private TeamSeasonPlayerId getId() {
		
		return this.id;
	}
	
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
	public TeamSeasonPlayer(){System.out.println("creating");}
	
	public TeamSeasonPlayer(Integer playerId, Team team, Integer year){
		//this.id.playerId = playerId;
		this.id.id.setTeam(team);
		this.id.id.setYear(year);
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
			Integer player1 = tsp1.getPlayer();
			Integer player2 = tsp2.getPlayer();
			return player1.compareTo(player2);
		}

	};

	protected Integer getPlayer() {
		return null;
		//return this.id.playerId;
	}
}
