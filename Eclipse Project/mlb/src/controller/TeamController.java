package controller;

import java.util.List;

import view.TeamView;
import bo.Player;
import bo.PlayerCareerStats;
import bo.Team;
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
		Team t = (Team) HibernateUtil.retrieveTeamById(Integer.valueOf(id));//TODO
        if (t == null) {
			System.out.println("Player not found in processDetails by id: " + id);
			return;
		}
        buildSearchResultsTableTeamDetail(t);
        view.buildLinkToSearch();
	}

	private void buildSearchResultsTableTeamDetail(Team t) {
		// TODO Auto-generated method stub			
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
}
