package bo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
 

@SuppressWarnings("serial")
@Entity(name="team")
public class Team implements Serializable{
	
	//Removed one-to-one annotation
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer teamId;
	
	@Column
	String name;
	
	@Column
	String league;
	
	@Column
	Integer yearFounded;
	
	@Column
	Integer yearLast;
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="id.team")
	@Fetch(FetchMode.JOIN)
	Set<TeamSeason> seasons = new HashSet<TeamSeason>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="id.player")
	@Fetch(FetchMode.JOIN)
	Set<TeamSeasonPlayer> seasonPlayer = new HashSet<TeamSeasonPlayer>();
	
	
	//public Team() {System.out.println("created Team");}
	
//	public Team(Integer teamId, String name, String league,
//			Integer yearFounded, Integer yearLast) {
//		super();
//		this.teamId = teamId;
//		this.name = name;
//		this.league = league;
//		this.yearFounded = yearFounded;
//		this.yearLast = yearLast;
//	}
	
	
	
	public void addSeason(TeamSeason s) {
		seasons.add(s);
	}

	public Set<TeamSeason> getSeasons() {
		return seasons;
	}
	
	public void setSeasons(Set<TeamSeason> seasons) {
		this.seasons = seasons;
	}
	
	public void addSeasonPlayer(TeamSeasonPlayer sP){
		this.seasonPlayer.add(sP);
	}
	
	public TeamSeasonPlayer getSeasonPlayer(Player p) {
		if(p!=null){
			for (TeamSeasonPlayer ts : seasonPlayer) {
				if (ts.getPlayer().equals(p)) return ts;
			}
			return null;
		}else
			System.out.println("poop");
		return null;
		
	}
	
	public Set<TeamSeasonPlayer> getSeasonPlayer(){
		return seasonPlayer;
	}	
	
	public Integer getId() {
		return teamId;
	}
	public void setId(Integer id) {
		this.teamId = id;
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

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Team)){
			return false;
		}
		Team other = (Team) obj;
		return (this.getId()==other.getId());
	}
	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}
	@Override
	public String toString(){
		return "TeamId: " + teamId + "\nName: " + name;
	}
}
