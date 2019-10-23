package utilities;

import java.io.Serializable;

/**
 * Class description: Represents the client playing Tic Tac Toe. Each player is a client 
 * 
 * @author 806547 Holly Harnack
 *
 */
public class Client implements Serializable {
	
	//Attributes
	
	/**
	 * Default serial version UID for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int port;
	private String IP;
	private String player;
	private boolean first; 


	/**
	 * Default constructor 
	 */
	public Client() {
		
	}	
	
	/**
	 * Constructor that initializes all the client attributes 
	 * 
	 * @param name - name of the client 
	 * @param port - port that the client wants to connect to 
	 * @param IP - ip address the client wants to connect to 
	 * @param player - the player symbol of the client (either X or O)
	 * @param first  - boolean value stating if the player is first in the next game or not 
	 */
	public Client(String name, int port, String IP, String player, boolean first) {
		this.name = name;
		this.port = port;
		this.IP = IP;
		this.player = player;
		this.first=first;
	}
	
	//Accessor and Mutator Methods
	
	/** returns the boolean value stating if the client goes first the next game 
	 * @return - true if the client goes first in the next game, false if they go last 
	 */
	public boolean getFirst() {
		return first;
	}

	/** set when it is known that this client will go first in the next game 
	 * @param first - true if the client goes first next game, false if they dont 
	 */
	public void setFirst(boolean first) {
		this.first = first;
	}

	
	/**gets the client's name 
	 * @return the name of the client 
	 */
	public String getName() {
		return name;
	}

	
	/**sets the clients name 
	 * @param name - set the name of the client 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**gets the port client is connected to 
	 * @return port the client is connected to 
	 */
	public int getPort() {
		return port;
	}

	/**sets the port the client is connected to 
	 * @param port - the port the client is connected to 
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**gets the ip address the client is connected to 
	 * @return the IP address the client is connected to 
	 */
	public String getIP() {
		return IP;
	}

	/**sets the ip address the client is connected to 
	 * @param iP - the ip address the client is connected to 
	 */
	public void setIP(String iP) {
		IP = iP;
	}

	/**gets the symbol of the client (X or O)
	 * @return the symbol the client is playing as, either X or O
	 */
	public String getPlayer() {
		return player;
	}

	/**sets the symbol of the client (X or O)
	 * @param p - the symbol he client is playing as, either X or O
	 */
	public void setPlayer(String p) {
		this.player = p;
	}

}
