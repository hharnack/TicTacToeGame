package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class description: Represents the Messages sent to the server and sometimes
 * sent to clients
 * 
 * @author 806547 Holly Harnack
 *
 */
public class ServerMessage extends Message {

	// Attributes

	/**
	 * Default serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;

	private String user;
	private String msg;
	private String time;

	//Constructors 
	
	/**
	 * @param user
	 *            - name of the client who the message is involved with 
	 * @param msg
	 *            - text sent to the server
	 * @param time
	 *            - time stamp of the sent message
	 */
	public ServerMessage(String user, String msg, Date time) {
		super(user, msg, time);
		this.user = user;
		this.msg = msg;
		this.time = new SimpleDateFormat("dd/mm/yy hh:mm:ss").format(time);

	}

	// Operational Methods

	/* (non-Javadoc)
	 * @see problemdomain.Message#toString()
	 * 
	 * Creates the string of the message that will be displayed to the server and the clients 
	 */
	@Override
	public String toString() {
		return "[" + time + "]" + this.user + " " + this.msg;
	}

}
