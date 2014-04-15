package bo;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity(name = "teamSeasonPlayer")
public class TeamSeasonPlayer implements Serializable {
	@Id
	@JoinColumn(name = "playerId")
	Integer playerId;
	@Id
	@JoinColumn(name = "teamId")
	Integer teamId;
	@Id
	@Column
	Integer year;
	public Integer getPlayerId() {
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
	}
	public TeamSeasonPlayer(Integer playerId, Integer teamId, Integer year) {
		super();
		this.playerId = playerId;
		this.teamId = teamId;
		this.year = year;
	}
	public TeamSeasonPlayer(){}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TeamSeasonPlayer)){
			return false;
		}
		TeamSeasonPlayer other = (TeamSeasonPlayer) obj;
		return (this.getYear()==other.getYear() &&
				this.getTeamId()==other.getTeamId() &&
				this.getPlayerId()==other.getPlayerId());
	}
	
	@Override
	public int hashCode() {
		Integer hash = 0;
		if (this.getPlayerId()!=null) hash += this.getPlayerId().hashCode(); 
		if (this.getTeamId()!=null) hash += this.getTeamId().hashCode();
		if (this.getYear()!=null) hash += this.getYear().hashCode();
		return hash;
	}
}
