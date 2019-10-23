package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class description: Represents the Messages each client sends to each other
 * 
 * @author 806547 Holly Harnack
 *
 */
public class ClientMessage extends Message {

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
	 *            - name of the client who sent the message
	 * @param msg
	 *            - text the client sent
	 * @param time
	 *            - time stamp of the sent message
	 */
	public ClientMessage(String user, String msg, Date time) {
		super(user, msg, time);
		this.user = user;
		this.msg = msg;
		this.time = new SimpleDateFormat("dd/mm/yy hh:mm:ss").format(time);
	}

	// Operational Methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see problemdomain.Message#toString()
	 * 
	 * Creates the string of the message that will be displayed to both clients
	 */
	@Override
	public String toString() {
		return "[" + this.time + "]" + this.user + ": " + msg;
	}

}
