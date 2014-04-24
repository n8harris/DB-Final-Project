/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author user
 */
public class PlayerView extends BaseView {

    public PlayerView() {
        title = "Player";
    }
    
    @Override
    public void buildSearchForm() {
        body.append("<form action=\"");
        body.append(title.toLowerCase());
        body.append(".ssp\" method=\"get\">\r\n");
        body.append("<div class='row'>\r\n");
        body.append("<div class='five columns alpha'>\r\n");
        body.append("<div>\r\n");
	    body.append("<label for='player'>Player Name</label>\r\n");
	    body.append("<input type='text' name='name' />\r\n");
	    body.append("</div>\r\n");
		body.append("</div>\r\n");
		body.append("<div class='five columns alpha'>\r\n");
        body.append("<div>\r\n");
		body.append("<label for='exact'>Exact Match?</label>\r\n");
		body.append("<input type='checkbox' name='exact' />\r\n");
	    body.append("</div>\r\n");
		body.append("</div>\r\n");
		body.append("<div class='five columns alpha'>\r\n");
		body.append("<input type='hidden' name='action' value='search' />\r\n");
        body.append("<input type='submit' value='Search' />\r\n");
        body.append("</div>\r\n");
        body.append("</div>\r\n");
        body.append("</form></div></div>\r\n"); 
    }

}
