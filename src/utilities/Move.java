package utilities;

import java.io.Serializable;

/**
 * Class description: class that represents a move that is sent between client's 
 * 
 * @author 806547 Holly Harnack 
 *
 */
public class Move implements Serializable {
	
	//Attributes
	
	/**
	 * Default serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private int location;
	private String user;
	
	//Constructors
	
	/**Initializes the move object 
	 * @param user - name of the client 
	 * @param location - location of the client's move 
	 */
	public Move(String user, int location) {
		this.setLocation(location);
		this.setUser(user);
	}

	/**
	 * Default Constructor 
	 */
	public Move() {
	}
	
	//Accessor and Mutator Methods

	/**gets the location of the move 
	 * @return the location of the move 
	 */
	public int getLocation() {
		return location;
	}

	/**sets the location of the move 
	 * @param location - the location of the move
	 */
	public void setLocation(int location) {
		this.location = location;
	}

	/**gets the name of the client who made the move 
	 * @return the clients name who made the move 
	 */
	public String getUser() {
		return user;
	}

	/**sets the name of the client who made the move 
	 * @param user - the name of client who made the move 
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
