package controller;

import java.util.List;

import view.TeamView;
import bo.Player;
import bo.PlayerCareerStats;
import bo.PlayerSeason;
import bo.Team;
import bo.TeamSeason;
import bo.TeamSeasonPlayer;
import dataaccesslayer.HibernateUtil;

public class TeamController extends BaseController {

	@Override
    public void init(String query) {
        System.out.println("building dynamic html for teams");
        view = new TeamView();
        process(query);
    }
	
	@Override
	protected void performAction() {
		String action = keyVals.get("action");
        System.out.println("teamcontroller performing action: " + action);
        if (action.equalsIgnoreCase(ACT_SEARCHFORM)) {
            processSearchForm();
        } else if (action.equalsIgnoreCase(ACT_SEARCH)) {
            processSearch();
        } else if (action.equalsIgnoreCase(ACT_DETAIL)) {
            processDetails();
        } 
	}

	private void processSearchForm() {
		view.buildSearchForm();
	}

	private void processSearch() {
		String name = keyVals.get("name");
        if (name == null) {
			System.out.println("name is null in processSearch");
            return;
        }
        String v = keyVals.get("exact");
        boolean exact = (v != null && v.equalsIgnoreCase("on"));
        // List<Team> bos = HibernateUtil.retrieveTeamByName(name, exact);
        view.printSearchResultsMessage(name, exact);
        //buildSearchResultsTableTeam(bos);
        view.buildLinkToSearch();
	}

	private void processDetails() {
		String id = keyVals.get("id");
		if (id == null) {
			System.out.println("id is null in processDetails");
            return;
        }
		//Team t = (Team) HibernateUtil.retrieveTeamById(Integer.valueOf(id));//TODO
        //if (t == null) {
			System.out.println("Player not found in processDetails by id: " + id);
			return;
		//}
        //buildSearchResultsTableTeamDetail(t);
        //view.buildLinkToSearch();
	}

	private void buildSearchResultsTableTeamDetail(Team t, List<TeamSeason> info) {
		// TODO Auto-generated method stub
		String[][] table = new String[2][5];
        table[0][0] = "Name";
        table[0][1] = "League";
        table[0][2] = "Year Founded";
        table[0][3] = "Most Recent Year";
        table[1][0] = t.getName();
        table[1][1] = t.getLeague();
        table[1][2] = Integer.toString(t.getYearFounded());
        table[1][3] = Integer.toString(t.getYearLast());
        view.buildTable(table);
        
        String[][] teamSeasons = new String[info.size() + 1][7];
        teamSeasons[0][0] = "Year";
        teamSeasons[0][1] = "Games Played";
        teamSeasons[0][2] = "Roster";
        teamSeasons[0][3] = "Wins";
        teamSeasons[0][4] = "Losses";
        teamSeasons[0][5] = "Rank";
        teamSeasons[0][6] = "Attendance";
        
        String tid = Integer.toString(t.getId());
        
        for (int i = 0; i < info.size(); i++) {
            TeamSeason ts = info.get(i);
            String year = Integer.toString(ts.getYear());
            table[i + 1][0] = Integer.toString(ts.getYear());
            table[i + 1][1] = Integer.toString(ts.getGamesPlayed());
            // Ex: localhost/team.ssp?id=10365&yid=1976&action=roster
            table[i + 1][2] = view.encodeLink(new String[]{"tid", "yid"}, new String[]{tid, year}, "Roster", ACT_ROSTER, SSP_TEAM);
            table[i + 1][3] = Integer.toString(ts.getWins());
            table[i + 1][4] = Integer.toString(ts.getLosses());
            table[i + 1][5] = Integer.toString(ts.getTeam_rank());
            table[i + 1][6] = Integer.toString(ts.getTotalAttendance());
            
        }
        view.buildTable(teamSeasons);
	}
	
	private void buildSearchResultsTableTeam(List<Team> bos) {
        // need a row for the table headers
        String[][] table = new String[bos.size() + 1][5];
        table[0][0] = "Id";
        table[0][1] = "Name";
        table[0][2] = "League";
        table[0][3] = "Year Founded";
        table[0][4] = "Most Recent Year";
        
        for (int i = 0; i < bos.size(); i++) {
            Team t = bos.get(i);
            String tid = t.getId().toString();
            table[i + 1][0] = view.encodeLink(new String[]{"id"}, new String[]{tid}, tid, ACT_DETAIL, SSP_TEAM);
            table[i + 1][1] = t.getName();
            table[i + 1][2] = t.getLeague();
            table[i + 1][3] = Integer.toString(t.getYearFounded());
            table[i + 1][4] = Integer.toString(t.getYearLast());
            
        }
        view.buildTable(table);
    }
	
	private void buildSearchResultsRoster(Team t, int year, List<Player> lop) {
			
		String[][] table = new String[2][4];
        table[0][0] = "Name";
        table[0][1] = "League";
        table[0][2] = "Year";
        table[0][3] = "Player Payroll";
        table[1][0] = t.getName();
        table[1][1] = t.getLeague();
        table[1][2] = Integer.toString(year);
        table[1][3] = "$0.00"; // This needs fixed. Is this an aggregate we have to calculate?
        view.buildTable(table);
        
        String[][] players = new String[lop.size() + 1][3];
        players[0][0] = "Name";
        players[0][1] = "Games Played";
        players[0][2] = "Salary";
        
        for (int i = 0; i < lop.size(); i++) {
        	Player p = lop.get(i);
        	String name = p.getName();
        	String pid = p.getId().toString();
        	players[i + 1][0] = view.encodeLink(new String[]{"id"}, new String[]{pid}, name, ACT_DETAIL, SSP_PLAYER);
        	PlayerSeason ps = p.getPlayerSeason(year); 
        	players[i + 1][1] = ps.getGamesPlayed().toString();
        	players[i + 1][2] = DOLLAR_FORMAT.format(ps.getSalary());
        }
        
        view.buildTable(players);
	}
}
