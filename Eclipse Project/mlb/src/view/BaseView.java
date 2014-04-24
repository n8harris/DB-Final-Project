/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 *
 * @author user
 */
public abstract class BaseView {
    
    protected String title;
    protected StringBuffer body = new StringBuffer();

    public abstract void buildSearchForm();
    
    public final String buildPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\r\n");
        sb.append("<HTML>\r\n");
        sb.append("<head>\r\n");
        sb.append("<meta charset='utf-8'>\r\n");
        sb.append("<title>Baseball Database</title>\r\n");
        sb.append("<meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1' />\r\n");
        sb.append("<link rel='stylesheet' href='style.css'>\r\n");
        sb.append("<link href='http://fonts.googleapis.com/css?family=Oswald|Open+Sans:400,600,700' rel='stylesheet' type='text/css'>\r\n");
        sb.append("</head>\r\n");
        sb.append("<body class='home page page-id-272 page-template page-template-index-php'>\r\n");
        sb.append("<div class='container'>\r\n");
        sb.append("<header role='banner' id='logo'>\r\n");
        sb.append("<a href='index.htm' title='Baseball Database'>\r\n");
        sb.append("<img src='/images/logo.png' width='280px' alt='Baseball Database'>\r\n");
        sb.append("</a>\r\n");  	
        sb.append("</header>\r\n");	
        sb.append("<div class='sixteen columns'>\r\n");
        sb.append("<div class='section remove-top'>\r\n");
        sb.append("<div id='main-menu' class='menu-desktop-navigation-container'><ul id='menu' class='menu'>");	
        sb.append("<li id='menu-item-271' class='menu-item menu-item-type-post_type menu-item-object-page menu-item-271'><a href='player.ssp?action=searchform'>Find Player</a></li>\r\n");  
        sb.append("<li id='menu-item-282' class='menu-item menu-item-type-post_type menu-item-object-page menu-item-282'><a href='team.ssp?action=searchform'>Find Team</a></li>\r\n");	
        sb.append("</ul></div></div></div>\r\n");
        sb.append("<div class='sixteen columns content'>\r\n");
        sb.append("<div class='section'>\r\n");
        sb.append("<h1>Search</h1>\r\n");			
        sb.append("</div></div>\r\n");				
        sb.append("<div class='ten columns'>\r\n");			
        sb.append("<div class='section remove-top remove-bottom'>\r\n");
        sb.append(body);
        sb.append("<footer role='contentinfo' class='sixteen columns'>\r\n");
		sb.append("<div class='section' id='footer'>\r\n");
		sb.append("<p>&copy; 2014 Baseball Database</p>\r\n");
		sb.append("<a href='#top'><img src='/images/up.png' alt='Back To Top' /></a>\r\n");	
		sb.append("</div>\r\n");
	    sb.append("</footer>\r\n");
	    sb.append("</div> <!-- container -->\r\n"); 
	    sb.append("<script src='/js/jquery-2.0.2.js'></script>\r\n");
        sb.append("<script src='/js/site-main.js'></script>\r\n");
        sb.append("</body>");
        sb.append("</html>");
        
        return sb.toString();
    }
    
    public final void buildLinkToSearch() {
        body.append("<br/><br/>\r\n");
        body.append("<a href=\"");
        body.append(title.toLowerCase());
        body.append(".ssp?action=searchform\">Search for a ");
        body.append(title);
        body.append("</a>\r\n");  
    }
    
    public final void printMessage(String msg) {
        body.append("<p>");
        body.append(msg);
        body.append("</p>\r\n");
    }
    
    public final void printSearchResultsMessage(String name, boolean exact) {
        body.append("<p>");
        body.append(title);
        if (exact) {
            body.append("s with name matching '");
        } else {
            body.append("s with name containing '");
        } 
        body.append(name);
        body.append("':</p>\r\n");
        
    }

    public final void buildTable(String[][] table) {
        body.append("<table class='one-column-emphasis'>\r\n");
        // print table header row
        body.append("<col /><thead><tr>");
        for (int i = 0; i < table[0].length; i++) {
            body.append("<th>");
            body.append(table[0][i]);
            body.append("</th>\r\n");
        }
        body.append("</tr></thead><tbody>\r\n");
        // print table rows
        for (int row = 1; row < table.length; row++) {
            body.append("<tr>\r\n");
            for (int col = 0; col < table[row].length; col++) {
                body.append("<td>");
                body.append(table[row][col]);
                body.append("</td>\r\n");
            }
            body.append("</tr>\r\n");
        }
        body.append("</tbody></table>\r\n");
    }
    
    /** 
     * Encode a link in the proper format.
     * 
     * @param key String[] of keys of the different args--length must match val[]
     * @param val String[] of vals of the different args--length must match key[]
     * @param display is what will be displayed as the link to click on
     * @param action is the action to take
     * @param ssp is either 'player' or 'team'
     */
    public final String encodeLink(String[] key, String[] val, String display, String action, String ssp) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"");
        sb.append(ssp);
        sb.append(".ssp?");
        for (int i=0; i<key.length; i++) {
        	sb.append(key[i]);
        	sb.append("=");
        	sb.append(encodeURL(val[i]));
        	sb.append("&");
        }
        sb.append("action=");
        sb.append(action);
        sb.append("\">");
        sb.append(display);
        sb.append("</a>");
        return sb.toString();
    }
   
    protected final String encodeURL(String s) {
        s = s.replace(" ", "+");
        return s;
    }
}
