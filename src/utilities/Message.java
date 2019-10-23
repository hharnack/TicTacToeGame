
package utilities;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class description: Class that encompasses all Message objects
 * 
 * @author 806547 Holly Harnack
 *
 */
public abstract class Message implements Serializable {
	// Attributes

	/**
	 * Default serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;

	private String user;
	private String msg;
	private String time;

	// Constructors

	/**Initializes the message
	 * @param user - client's name
	 * @param msg - text that is sent 
	 * @param time - time the message was sent 
	 */
	public Message(String user, String msg, Date time) {
		this.user = user;
		this.msg = msg;
		this.time = new SimpleDateFormat("dd/mm/yy hh:mm:ss").format(time);
	}
	
	//Accessor and Mutator Methods

	/**gets the client's name
	 * @return the client's name
	 */
	public String getUser() {
		return user;
	}

	/**sets the client's name
	 * @param user - the client's name
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**gets the message sent 
	 * @return the message sent 
	 */
	public String getMsg() {
		return msg;
	}

	/**sets the message to send 
	 * @param msg - the message to send 
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**gets the time the message was sent 
	 * @return the time the message was sent 
	 */
	public String getTime() {
		return time;
	}

	/**creates the time stamp for the message 
	 * @param time - the time the message was sent 
	 */
	public void setTime(Date time) {
		this.time = new SimpleDateFormat("dd/mm/yy hh:mm:ss").format(time);
	}

	// Operational Methods
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

}
