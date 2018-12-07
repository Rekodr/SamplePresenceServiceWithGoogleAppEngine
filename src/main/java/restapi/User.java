package restapi;

import org.json.JSONObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * A POJO representing the actual widget resource.
 *
 * @author Recodeo Rekod.   Credit to: Jonathan Engelsma (http://themobilemontage.com)
 *
 */

@Entity
public class User {

	@Id String username = null;
	private boolean status;
	private int port;
	private String host;

    /**
     * Determine the name of the user.
     * @return The name of the user.
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * Determine the host the user is on.
     * @return The name of the host client resides on.
     */
    public String getHost()
    {
        return this.host;
    }

    /**
     * Get the port the client is listening for connections on.
     * @return port value.
     */
    public int getPort()
    {
        return this.port;
    }

    /**
     * Get the status of the client - true means availability, false means don't disturb.
     * @return status value.
     */
    public boolean getStatus()
    {
        return this.status;
    }

    /**
     * @param username the userName to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

	/**
	 * Convert this object to a JSON object for representation
	 */
	public JSONObject toJSON() {
		try{
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("username", this.username );
			jsonobj.put( "host", this.host );
			jsonobj.put( "port", this.port );
			jsonobj.put( "status", this.status );

			return jsonobj;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * Convert this object to a string for representation
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("username:");
		sb.append(this.username );
		return sb.toString();
	}

	/**
	 * Convert this object into an HTML representation.
	 * @param fragment if true, generate an html fragment, otherwise a complete document.
	 * @return an HTML representation.
	 */
	public String toHtml(boolean fragment)
	{
		String retval = "";
		if(fragment) {
			StringBuffer sb = new StringBuffer();
			sb.append("<b> Username: </b>");
			sb.append( username );
			sb.append(" <a href=\"/users/" + this.username + "\">View</a>");
			sb.append("<br/>");
			retval = sb.toString();
		} else {
			StringBuffer sb = new StringBuffer("<html><head><title>User Resource</title></head><body><h1>User Representation</h1>");
			sb.append("<b>username:</b> ");
			sb.append(this.username);
            sb.append("  <b>status:</b> ");
            sb.append(this.status);
			sb.append("<br/><br/>Return to <a href=\"/users\">Users list<a>.</body></html>");
			retval = sb.toString();
		}
		return retval;
	}
}
