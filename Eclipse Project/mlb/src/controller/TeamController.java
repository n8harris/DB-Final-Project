package controller;

import view.TeamView;
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
}
