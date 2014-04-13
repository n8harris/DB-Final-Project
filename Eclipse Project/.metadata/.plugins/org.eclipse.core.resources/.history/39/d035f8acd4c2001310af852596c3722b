package bo;

import java.io.Serializable;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity(name = "teamSeasonPlayer")
public class TeamSeasonPlayer implements Serializable {
	@Id
	@Column
	Integer playerId;
	@Id
	@Column
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
	
}
