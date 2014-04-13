package bo;

import java.io.Serializable;
import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity(name = "teamseason")
public class TeamSeason implements Serializable{
	@Id
	@Column
	Integer teamId;
	@Id
	@Column
	Integer year;

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

	public TeamSeason(Integer teamId, Integer year, Integer gamesPlayed,
			Integer wins, Integer losses, Integer team_rank,
			Integer totalAttendance) {
		super();
		this.teamId = teamId;
		this.year = year;
		this.gamesPlayed = gamesPlayed;
		this.wins = wins;
		this.losses = losses;
		this.team_rank = team_rank;
		this.totalAttendance = totalAttendance;
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
	
	
	
}
