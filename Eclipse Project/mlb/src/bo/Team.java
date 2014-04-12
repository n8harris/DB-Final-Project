package bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity(name="team")
public class Team implements Serializable{
	

	@Id
	@OneToOne
	Integer teamId;
	
	@Column
	String name;
	
	@Column
	String league;
	
	@Column
	Integer yearFounded;
	
	@Column
	Integer yearLast;
	
	
	public Team() {
		super();
	}
	
	public Team(Integer teamId, String name, String league,
			Integer yearFounded, Integer yearLast) {
		super();
		this.teamId = teamId;
		this.name = name;
		this.league = league;
		this.yearFounded = yearFounded;
		this.yearLast = yearLast;
	}
	
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	public Integer getYearFounded() {
		return yearFounded;
	}
	public void setYearFounded(Integer yearFounded) {
		this.yearFounded = yearFounded;
	}
	public Integer getYearLast() {
		return yearLast;
	}
	public void setYearLast(Integer yearLast) {
		this.yearLast = yearLast;
	}
}
