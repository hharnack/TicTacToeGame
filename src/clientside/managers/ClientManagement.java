package clientside.managers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import utilities.*;

/**
 * 
 * Class description: Manager for the client side of the game. Runs Tic Tac Toe
 * and manages the socket of the client. This class observes the object input
 * stream of the socket.
 *
 * @author Holly Harnack (806547)
 * @version September 24, 2019
 *
 */
public class ClientManagement implements PropertyChangeListener {

	// Attributes

	// variables
	
	// tracks the number of turns of the game.
	private int turns;
	private final int port = 5555;
	// Tracks client move locations on the board.
	private int[] clientMoves = new int[9];
	// Tracks opponent's move locations on the board.
	private int[] opponentMoves = new int[9];
	//Checks for win or tie
	private boolean tieOrWin;

	// objects
	
	private Client c;
	private Move clientMove;
	private Socket s;
	private Thread t;
	private ClientMessage incomingMsg;
	private String messages = "";
	private InputListener inListener;
	private ClientMessage msgClient;
	private ServerMessage msgServer;
	private ObjectOutputStream oos;
	private ArrayList<PropertyChangeListener> observers;
	private ArrayList<JButton> btnList = new ArrayList<>();
	private MyActionListener actionListener = new MyActionListener();

	// frames
	
	private JFrame mainFrame;
	private JFrame credentialsFrame;
	private JFrame playAgainFrame;
	private JFrame reconnectFrame;

	// buttons
	
	private JButton button0;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private JButton disconnect;
	private JButton send;
	private JButton playAgainYes;
	private JButton playAgainNo;
	private JButton reconnectYes;
	private JButton reconnectNo;
	private JButton connect;

	// text areas
	
	private JTextField nameField;
	private JTextField ipField;
	private JTextArea messageInput;
	private JTextArea messageOutput;
	private JScrollPane scrollOutput;
	private JScrollPane scrollInput;

	// text
	
	private JLabel chat;
	private JLabel userNameField;
	private JLabel playerField;
	private JLabel userNameLabel;
	private JLabel playerLabel;

	// Constructors

	/**
	 * Constructor for Client Manager. Initializes all components and constructs the
	 * GUI.
	 */
	public ClientManagement() {

		this.mainFrame = new JFrame("Tic Tac Toe");

		this.mainFrame.setSize(700, 600);
		this.mainFrame.setLocationRelativeTo(null);
		this.mainFrame.getContentPane().setLayout(new BorderLayout());
		this.actionListener = new MyActionListener();

		// Add buttons to button list
		this.btnList.add(button0 = new JButton());
		this.btnList.add(button1 = new JButton());
		this.btnList.add(button2 = new JButton());
		this.btnList.add(button3 = new JButton());
		this.btnList.add(button4 = new JButton());
		this.btnList.add(button5 = new JButton());
		this.btnList.add(button6 = new JButton());
		this.btnList.add(button7 = new JButton());
		this.btnList.add(button8 = new JButton());

		for (JButton btn : btnList) {
			btn.setFont(new Font("Arial", Font.BOLD, 40));
		}

		this.createTopLabels();
		this.createCenterPanels();
		this.createBottomButtons();

		this.createCredentialsFrame();
		this.createPlayAgainFrame();
		this.createReconnectFrame();
	}

	// Operational Methods

	/**
	 * Creates the window that asks the client if they would like to reconnect.
	 */
	private void createReconnectFrame() {
		this.reconnectFrame = new JFrame("Re-connect?");

		this.reconnectFrame.setSize(300, 100);
		this.reconnectFrame.setLocationRelativeTo(null);
		this.reconnectFrame.getContentPane().setLayout(new GridLayout(2, 1));

		JLabel reconnect = new JLabel("Client Disconnected. Re-connect?", SwingConstants.CENTER);
		JPanel reconnectButtons = new JPanel();
		reconnectButtons.setLayout(new GridLayout(1, 2));

		this.reconnectYes = new JButton("Yes");
		this.reconnectNo = new JButton("No");
		this.reconnectYes.addActionListener(this.actionListener);
		this.reconnectNo.addActionListener(this.actionListener);

		reconnectButtons.add(this.reconnectYes);
		reconnectButtons.add(this.reconnectNo);

		this.reconnectFrame.add(reconnect);
		this.reconnectFrame.add(reconnectButtons);
	}

	/**
	 * Creates the window that asks the client if they would like to play again.
	 */
	private void createPlayAgainFrame() {

		this.playAgainFrame = new JFrame("Play Again?");

		this.playAgainFrame.setSize(300, 100);
		this.playAgainFrame.setLocationRelativeTo(null);
		this.playAgainFrame.getContentPane().setLayout(new GridLayout(2, 1));

		JPanel playAgainTxt = new JPanel();
		JLabel txt = new JLabel("Would you like to play again?");
		playAgainTxt.add(txt);
		JPanel playAgainButtons = new JPanel();
		playAgainButtons.setLayout(new GridLayout(1, 2));

		this.playAgainYes = new JButton("Yes");
		this.playAgainNo = new JButton("No");
		this.playAgainYes.addActionListener(this.actionListener);
		this.playAgainNo.addActionListener(this.actionListener);

		playAgainButtons.add(this.playAgainYes);
		playAgainButtons.add(this.playAgainNo);

		this.playAgainFrame.add(playAgainTxt);
		this.playAgainFrame.add(playAgainButtons);
	}

	/**
	 * Creates the window that asks for the clients credentials. Displayed when the
	 * client initially connects.
	 */
	private void createCredentialsFrame() {

		this.credentialsFrame = new JFrame("Welcome to Tic Tac Toe");

		this.credentialsFrame.setSize(300, 150);
		this.credentialsFrame.setLocationRelativeTo(null);
		this.credentialsFrame.getContentPane().setLayout(new BorderLayout());

		JPanel input = new JPanel();
		input.setLayout(new GridLayout(1, 2));

		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(2, 1));

		JLabel nameLabel = new JLabel("User Name:", SwingConstants.CENTER);
		JLabel ipLabel = new JLabel("IP Address:", SwingConstants.CENTER);

		labels.add(nameLabel);
		labels.add(ipLabel);

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(2, 1));

		this.nameField = new JTextField();
		this.ipField = new JTextField();

		fields.add(this.nameField);
		fields.add(this.ipField);

		input.add(labels);
		input.add(fields);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 1));
		buttons.setBorder(new EmptyBorder(10, 10, 10, 10));

		this.connect = new JButton("connect");
		this.connect.addActionListener(actionListener);
		buttons.add(this.connect);

		this.credentialsFrame.add(input, BorderLayout.CENTER);
		this.credentialsFrame.add(buttons, BorderLayout.SOUTH);
	}

	/**
	 * Displays the main window.
	 */
	public void displayMain() {

		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setVisible(true);

	}

	/**
	 * Displays the credentials window.
	 */
	public void displayCredentials() {

		this.credentialsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.credentialsFrame.setVisible(true);

	}

	/**
	 * Displays the window that asks if a player wants to play again.
	 */
	public void displayPlayAgain() {
		
		for (JButton btn : btnList) {
			btn.setEnabled(false);
		}

		this.playAgainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.playAgainFrame.setVisible(true);

	}

	/**
	 * Displays the window that asks if a player wants to reconnect. Displayed when
	 * opponent disconnects.
	 */
	public void displayReconnectFrame() {

		this.reconnectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.reconnectFrame.setVisible(true);

	}

	/**
	 * Creates the top label of the main GUI.
	 */
	private void createTopLabels() {

		JPanel topLabels = new JPanel();

		topLabels.setLayout(new GridLayout(1, 2));
		topLabels.setBorder(new EtchedBorder());

		this.userNameLabel = new JLabel("User Name: ");
		this.userNameLabel.setFont(new Font("Arial", Font.BOLD, 15));

		this.userNameField = new JLabel();
		this.userNameField.setFont(new Font("Arial", Font.PLAIN, 15));

		topLabels.add(this.userNameLabel);
		topLabels.add(this.userNameField);

		this.mainFrame.getContentPane().add(topLabels, BorderLayout.NORTH);

	}

	/*
	 * Creates the Center of the main GUI which holds the game board and the chat.
	 */
	private void createCenterPanels() {

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 2));

		// Holds the game board and label for player
		JPanel playerArea = new JPanel();
		playerArea.setLayout(new BorderLayout());

		// Sets the header for the player. Lets the player know if they are X or O
		JPanel playerHeader = new JPanel();
		playerHeader.setLayout(new GridLayout(1, 2, 10, 10));
		playerHeader.setBorder(new EtchedBorder());
		this.playerLabel = new JLabel("You Are Player: ");
		this.playerLabel.setFont(new Font("Arial", Font.BOLD, 20));
		playerHeader.add(this.playerLabel);
		this.playerField = new JLabel();
		this.playerField.setFont(new Font("Arial", Font.PLAIN, 15));
		playerHeader.add(this.playerField);

		// add header to player area
		playerArea.add(playerHeader, BorderLayout.NORTH);

		// create board
		JPanel board = new JPanel();
		board.setLayout(new GridLayout(3, 3));

		for (JButton btn : btnList) {
			btn.addActionListener(actionListener);
			board.add(btn);
		}

		// add board to player area
		playerArea.add(board, BorderLayout.CENTER);

		// Creates the chat area. Includes the message input and output areas
		JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());

		JPanel msgInput = new JPanel();
		msgInput.setLayout(new FlowLayout());
		msgInput.setBorder(new EtchedBorder());

		this.messageOutput = new JTextArea();
		this.messageOutput.setEditable(false);
		this.scrollOutput = new JScrollPane(this.messageOutput);

		// holds the header for the chat area
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new GridLayout(1, 1, 10, 10));
		chatPanel.setBorder(new EtchedBorder());

		// add chat components
		this.chat = new JLabel("Chat", SwingConstants.CENTER);
		this.chat.setFont(new Font("Arial", Font.BOLD, 20));
		chatPanel.add(this.chat);
		chatArea.add(chatPanel, BorderLayout.NORTH);

		// add message output to chat area
		chatArea.add(this.scrollOutput, BorderLayout.CENTER);
		this.messageInput = new JTextArea(3, 22);
		this.scrollInput = new JScrollPane(messageInput);
		msgInput.add(this.scrollInput);
		this.send = new JButton("Send");
		this.send.addActionListener(this.actionListener);
		msgInput.add(this.send);

		// add message input to chat area
		chatArea.add(msgInput, BorderLayout.SOUTH);

		centerPanel.add(playerArea);
		centerPanel.add(chatArea);

		this.mainFrame.getContentPane().add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates the bottom area of the main GUI. Holds the disconnect button.
	 */
	private void createBottomButtons() {

		JPanel bottomButtons = new JPanel();
		bottomButtons.setLayout(new GridLayout(1, 1, 40, 0));
		bottomButtons.setBorder(new EmptyBorder(10, 250, 5, 250));

		this.disconnect = new JButton("Disconnect");
		this.disconnect.addActionListener(actionListener);

		bottomButtons.add(this.disconnect);

		this.mainFrame.getContentPane().add(bottomButtons, BorderLayout.SOUTH);
	}

	/**
	 * Tracks when this client has mad a move. Manages the buttons for this move and
	 * checks for a win and/or tie.
	 * 
	 * @param moveLocation
	 *            - Integer value given by the button that the client clicked,
	 *            tracking location of client move.
	 */
	void moved(int moveLocation) {

		// Since the player made a move disable all of the buttons.
		for (JButton btn : btnList) {
			btn.setEnabled(false);
		}

		// Establish this move by disabling the correct button and setting to the
		//player's symbol
		this.btnList.get(moveLocation).setEnabled(false);
		this.btnList.get(moveLocation).setText(this.c.getPlayer());
		this.btnList.get(moveLocation).setPressedIcon(null);

		// Track the client moves. 1 is a button occupied by the client and 0 is free
		//space.
		this.clientMoves[moveLocation] = 1;
		this.turns++;

		this.clientMove = new Move();

		// Create a move object for this client's move.
		this.clientMove.setLocation(moveLocation);
		this.clientMove.setUser(this.c.getName());

		boolean tie = false;
		boolean win = checkWin();

		// Only check for a tie if there is no win
		if (win == false) {
			tie = checkTie();
		}

		try {
			// Send the move
			this.oos.writeObject(clientMove);
			// Only ask to play again if there was a win or a tie
			if (win == true || tie == true) {
				this.displayPlayAgain();
			}
		} catch (IOException e) {
			this.displayReconnectFrame();
		}

	}

	/**
	 * Checks if there is a tie and returns a true if there is and false if there
	 * isn't
	 * 
	 * @return boolean - value that is true if there is a tie and false if there is
	 *         no tie
	 */
	private boolean checkTie() {

		// Check if the turns is equal to 9, if it is this means there was a tie
		if (this.turns == 9) {

			this.messages += "Tie!\n";
			this.messageOutput.setText(messages);
			this.msgServer = new ServerMessage("", " Tie!", new Date());
			try {
				this.oos.writeObject(this.msgServer);
			} catch (IOException e) {
				this.displayReconnectFrame();
			}
			JOptionPane.showMessageDialog(null, "There was a tie.");

			// Since there was a tie this client will go first in the next round
			this.c.setFirst(true);
			return true;
		}
		return false;
	}

	/**
	 * Method that runs if this client has lost the game.
	 */
	private void youLost() {
		
		this.messages += "You Lost.\n";
		this.messageOutput.setText(this.messages);

		this.msgServer = new ServerMessage(this.c.getName(), " lost", new Date());

		try {
			oos.writeObject(this.msgServer);
		} catch (IOException e) {
			this.displayReconnectFrame();
		}

		// Ask client if they would like to play again
		this.displayPlayAgain();
	}

	/**
	 * Method that runs if it is this clients turn. This method runs in the
	 * beginning of the game and every time a move is received
	 */
	private void yourTurn() {

		// Enable all of the available buttons for the client to choose from
		for (int i = 0; i < 9; i++) {
			if (this.clientMoves[i] == 0 && this.opponentMoves[i] == 0)
				this.btnList.get(i).setEnabled(true);
		}

	}

	/*
	 * Method that runs when a move is received. Manages the opponents move.
	 */
	private void opponentMove(Move move) {

		int moveLocation = move.getLocation();

		this.opponentMoves[moveLocation] = 1;
		this.turns++;

		// Find location of button move and disable the button
		this.btnList.get(moveLocation).setPressedIcon(null);
		this.btnList.get(moveLocation).setEnabled(false);

		// Set the symbol of the button depending on the player's symbol
		if (this.c.getPlayer().equals("X")) {
			this.btnList.get(moveLocation).setText("O");
		} else {
			this.btnList.get(moveLocation).setText("X");
		}

		// Now it is the client's turn
		if (tieOrWin == false) {
			this.yourTurn();
		}
	}

	/**
	 * Method that checks if the client has won after they have made a move
	 * 
	 * @return boolean - true if this client has won and false if they haven't
	 */
	private boolean checkWin() {

		// Calculation that checks this clients moves on the board for a winning
	 	//combination
		int sum1 = this.clientMoves[0] + this.clientMoves[1] + this.clientMoves[2];
		int sum2 = this.clientMoves[3] + this.clientMoves[4] + this.clientMoves[5];
		int sum3 = this.clientMoves[6] + this.clientMoves[7] + this.clientMoves[8];
		int sum4 = this.clientMoves[0] + this.clientMoves[3] + this.clientMoves[6];
		int sum5 = this.clientMoves[1] + this.clientMoves[4] + this.clientMoves[7];
		int sum6 = this.clientMoves[2] + this.clientMoves[5] + this.clientMoves[8];
		int sum7 = this.clientMoves[0] + this.clientMoves[4] + this.clientMoves[8];
		int sum8 = this.clientMoves[2] + this.clientMoves[4] + this.clientMoves[6];

		// If any of the combinations adds to a 3 then this client has won
		if (sum1 == 3 || sum2 == 3 || sum3 == 3 || sum4 == 3 || sum5 == 3 || sum6 == 3 || sum7 == 3 || sum8 == 3) {

			// Since this client has won set this client to be the first person to go in the
			//next game.
			this.c.setFirst(true);
			JOptionPane.showMessageDialog(null, "You Won!");
			this.messages += "You Won!\n";
			this.messageOutput.setText(messages);
			this.msgServer = new ServerMessage(this.c.getName(), " has won!", new Date());

			try {
				this.oos.writeObject(this.msgServer);
			} catch (IOException e) {
				this.displayReconnectFrame();
			}
			return true;
		}
		return false;
	}

	/**
	 * Sends the message to the opponent. This method is run when the client clicks
	 * the send button.
	 */
	void sendMessage() {

		if (!(this.messageInput.getText().equals(""))) {

			this.msgClient = new ClientMessage(this.c.getName(), this.messageInput.getText(), new Date());

			this.messages += msgClient.toString() + "\n";

			this.messageOutput.setText(messages);

			try {
				this.oos.writeObject(msgClient);
			} catch (IOException e) {
				this.displayReconnectFrame();
			}
		} else {
			this.messages += "Enter a valid Message.\n";
			this.messageOutput.setText(messages);
		}
	}

	/**
	 * Disconnects the client from the game and closes the window. This will be ran if the player clicks the disconnect button.
	 */
	public void disconnect() {

		try {
			this.messages += "Disconnecting.\n";
			this.messageOutput.setText(messages);
			this.msgServer = new ServerMessage(this.c.getName(), " has disconnected.", new Date());
			this.oos.writeObject(msgServer);
			//Must close the thread and socket before closing 
			this.t = null;
			this.oos.close();
			this.s.close();
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Problem Disconnecting.");
			System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Problem Disconnecting.");
			System.exit(0);
		}
	}

	/**
	 * Initializes the player attributes and connects client. Ran in the very beginning of the game.
	 */
	public void init() {

		this.displayCredentials();

	}

	/**
	 * Resets the board if a player chooses to play again
	 */
	public void resetBoard() {

		//Set all client and opponent moves to 0 
		for (int i = 0; i < 9; i++) {
			this.clientMoves[i] = 0;
			this.opponentMoves[i] = 0;
		}

		//Set all board moves to available 
		for (JButton btn : btnList) {
			btn.setEnabled(false);
			btn.setText(" ");
		}

		this.tieOrWin = false;
		this.turns = 0;

		// Whoever wins goes first
		if (this.c.getFirst() == true) {
			for (JButton btn : btnList) {
				btn = new JButton();
				btn.setEnabled(true);
			}
			this.yourTurn();
		}
		
		this.c.setFirst(false);

	}

	/**
	 * Connects the client initially. Resets the board and resets all sockets and threads to null to create a new game.
	 */
	public void connect() {

		//Set all client and opponent moves to 0 
		for (int i = 0; i < 9; i++) {
			this.clientMoves[i] = 0;
			this.opponentMoves[i] = 0;
		}

		//Set all board moves to available 
		for (JButton btn : btnList) {
			btn.setEnabled(false);
			btn.setText(" ");
		}

		this.tieOrWin = false;
		this.turns = 0;
		
		//reset who goes first
		this.c.setFirst(false);

		//Reset all sockets and threads so this game is completely new
		this.s = null;
		this.t = null;
		this.inListener = null;
		this.oos = null;
		this.observers = new ArrayList<>();

		try {

			this.s = new Socket(c.getIP(), c.getPort());

			this.messages += "Connected.\n";
			this.messageOutput.setText(messages);

			this.observers.add(this);

			//Create a listener on the ois of the socket to constantly read any incoming objects 
			this.inListener = new InputListener(s, 0, observers);
			//Start thread so listener is always listening and doesn't interrupt the GUI
			this.t = new Thread(inListener);

			this.t.start();

			OutputStream os = s.getOutputStream();
			this.oos = new ObjectOutputStream(os);

			this.msgServer = new ServerMessage(this.c.getName(), " has connected.", new Date());

			this.oos.writeObject(msgServer);

		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Not a valid host.");
			this.init();
		} catch (IOException e) {
			this.displayReconnectFrame();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 * 
	 *  This method handles all of the incoming objects. The objects it receives are Moves, Messages and Clients
	 *
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		Object o = evt.getNewValue();

		// Receives a message object from the other client.
		if (o instanceof Message) {

			// Check if this is a server message which handles the game play. Checks from the other client if 
			// they won, they disconnected, or there was a tie. 
			if (o instanceof ServerMessage) {
				ServerMessage sm = ((ServerMessage) o);

				if (sm.getMsg().equals(" has won!")) {
					JOptionPane.showMessageDialog(null, "You Lost.");
					this.tieOrWin = true;
					this.youLost();
				} else if (sm.getMsg().equals(" has disconnected.")) {
					this.displayReconnectFrame();
				} else if (sm.getMsg().equals(" Tie!")) {
					JOptionPane.showMessageDialog(null, "There was a tie.");
					this.messages += "Tie!\n";
					this.messageOutput.setText(messages);
					this.tieOrWin = true;
					this.displayPlayAgain();
				} else if (sm.getMsg().equals(" has connected.")) {
					this.messages += sm.toString() + "\n";
					this.messageOutput.setText(messages);
				} else if (sm.getMsg().equals(" wants to play again.")) {
					this.messages += sm.toString() + "\n";
					this.messageOutput.setText(messages);
				}
			}

			//Prints all of the private messages received from the client
			if (o instanceof ClientMessage) {

				this.incomingMsg = ((ClientMessage) o);

				this.incomingMsg.setTime(new Date());

				this.messages += incomingMsg.toString() + "\n";

				this.messageOutput.setText(messages);

			}

		}

		// Receives a client object from the server. Only receives a client object
		// before start of the game.
		if (o instanceof Client) {

			Client c2 = ((Client) o);

			// Evaluates the player received from the server and assigns the client as X or
			// O depending on the player. Player X goes first.
			if (c2.getPlayer().equals("X")) {

				this.playerField.setText("X");
				this.c.setPlayer("X");

				// Enables buttons for player X as it is player X's turn.
				for (JButton btn : btnList) {
					btn = new JButton();
					btn.setEnabled(true);
				}

				this.yourTurn();

			} else {
				this.playerField.setText("O");
				this.c.setPlayer("O");

				// Disables buttons for player O as it is player X's turn.
				for (JButton btn : btnList) {
					btn = new JButton();
					btn.setEnabled(false);
				}
			}

		}

		// Receives a move object from the other client. This Object determines which
		// location the opponent clicked
		// and processes that information in the opponent move method.
		if (o instanceof Move) {

			Move opponentMove = ((Move) o);

			this.opponentMove(opponentMove);
		}

	}

	/**
	 * Class Description: Handles changes to the buttons.
	 * 
	 * @author 806547 Holly Harnack
	 *
	 */
	private class MyActionListener implements ActionListener {

		@Override

		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == disconnect) {
				ClientManagement.this.disconnect();
			} else if (e.getSource() == button0) {
				ClientManagement.this.moved(0);
			} else if (e.getSource() == button1) {
				ClientManagement.this.moved(1);
			} else if (e.getSource() == button2) {
				ClientManagement.this.moved(2);
			} else if (e.getSource() == button3) {
				ClientManagement.this.moved(3);
			} else if (e.getSource() == button4) {
				ClientManagement.this.moved(4);
			} else if (e.getSource() == button5) {
				ClientManagement.this.moved(5);
			} else if (e.getSource() == button6) {
				ClientManagement.this.moved(6);
			} else if (e.getSource() == button7) {
				ClientManagement.this.moved(7);
			} else if (e.getSource() == button8) {
				ClientManagement.this.moved(8);
			} else if (e.getSource() == send) {
				ClientManagement.this.sendMessage();
			} else if (e.getSource() == playAgainYes) {

				//Lets other client know they want to play again. If both players want to play again it resets the board.
				ClientManagement.this.msgServer = new ServerMessage(ClientManagement.this.c.getName(),
						" wants to play again.", new Date());
				ClientManagement.this.playAgainFrame.setVisible(false);
				try {
					ClientManagement.this.oos.writeObject(msgServer);
					ClientManagement.this.resetBoard();
				} catch (IOException e1) {
					try {
						// If the Client catches an IOException it is assumed the other client does not
						// want to play again
						// so the Client is reconnected and sockets are closed.
						ClientManagement.this.oos.close();
						ClientManagement.this.s.close();
						ClientManagement.this.connect();
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(null, "Problem Disconnecting.");
					}
				}
			} else if (e.getSource() == playAgainNo) {
				ClientManagement.this.playAgainFrame.setVisible(false);
				ClientManagement.this.disconnect();
			} else if (e.getSource() == connect) {
				//Initializes the client and game
				ClientManagement.this.c = new Client();
				ClientManagement.this.c.setPort(port);
				ClientManagement.this.c.setIP(ClientManagement.this.ipField.getText());
				ClientManagement.this.c.setName(ClientManagement.this.nameField.getText());
				ClientManagement.this.userNameField.setText(c.getName());
				ClientManagement.this.credentialsFrame.setVisible(false);
				ClientManagement.this.connect();
			} else if (e.getSource() == reconnectYes) {
				try {
					ClientManagement.this.oos.close();
					ClientManagement.this.s.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Problem Disconnecting.");
				}
				ClientManagement.this.reconnectFrame.setVisible(false);
				ClientManagement.this.connect();
			} else if (e.getSource() == reconnectNo) {
				ClientManagement.this.reconnectFrame.setVisible(false);
				ClientManagement.this.disconnect();
			}

		}

	}
}
