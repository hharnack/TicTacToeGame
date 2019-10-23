package serverside.managers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import utilities.Move;
import utilities.ServerMessage;

/**
 Class description: Manager for the server side of the game. Runs Tic Tac Toe Server 
 * and manages the socket of the server. This class observes the object input
 * stream of the sockets.
 * 
 * @author 806547 Holly Harnack 
 * @version September 24, 2019
 *
 */
public class ServerManagement implements PropertyChangeListener {
	//Attributes
	
	//Components of the GUI
	private JButton disconnect;
	private JButton connect;
	private JTextArea serverOutput;
	private JFrame frame;
	private MyActionListener actionListener;
	private JScrollPane scroll;

	//Variables and objects for the server
	private ServerSocket serverSocket = null;
	private Thread t = null;
	private String messages = "";
	private Server server;
	private int port = 5555;
	private ArrayList<PropertyChangeListener> observers = new ArrayList<>();
	private ServerMessage msgServer;
	private Move move;

	//Constructors
	 
	/**
	 * Constructor that creates the main GUI for the server
	 */
	public ServerManagement() {

		this.frame = new JFrame("Tic Tac Toe Server");
		this.frame.setSize(600, 600);
		this.frame.setLocationRelativeTo(null);
		this.frame.getContentPane().setLayout(new BorderLayout());
		this.actionListener = new MyActionListener();

		this.createButtonPanel();
		this.createServerOutput();

	}
	
	//Operational

	
	/**
	 * Method that creates the server output for the GUI
	 */
	private void createServerOutput() {
		
		//Text Area for all server output
		this.serverOutput = new JTextArea();
		this.serverOutput.setEditable(false);
		this.scroll = new JScrollPane(this.serverOutput);
		this.frame.getContentPane().add(scroll, BorderLayout.CENTER);

	}

	
	/**
	 * Method that creates the area for the buttons on the server GUI
	 */
	private void createButtonPanel() {

		JPanel buttons = new JPanel(new GridLayout(1, 2));
		this.connect = new JButton("connect");
		this.disconnect = new JButton("disconnect");

		this.disconnect.addActionListener(actionListener);
		this.connect.addActionListener(actionListener);
		buttons.add(connect);
		buttons.add(disconnect);

		this.frame.getContentPane().add(buttons, BorderLayout.NORTH);

	}

	
	/**
	 * Displays the main frame of the GUI
	 */
	public void display() {

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);

	}

	
	/**
	 * Disconnects and closes the server
	 */
	public void disconnectServer() {

			messages += ("Connections Closed.\n");
			serverOutput.setText(messages);
			System.exit(0);
	}

	
	/**
	 * Connects the server and allows client connections
	 */
	public void connectServer() {

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			messages += ("Problem starting server.\n");
			serverOutput.setText(messages);
			return;
		}

		messages += ("Waiting for a connection........\n");
		serverOutput.setText(messages);

		//Thread that continually runs the server looking for connections
		server = new Server(this, serverSocket, observers);
		t = new Thread(server);

		t.start();
	}

	/*
	 *  (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * 
	 * This method runs once one of the input listeners detects a change on the socket's input stream. Handles the
	 * object that is received.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		Object o = evt.getNewValue();

		//Tracks each player's move and prints it to the GUI
		if (o instanceof Move) {

			move = (Move) o;

			msgServer = new ServerMessage(move.getUser(), " moved to location " + move.getLocation(), new Date());

			messages += msgServer.toString() + ".\n";

			this.serverOutput.setText(messages);

		}

		//Prints all instances of server messages 
		if (o instanceof ServerMessage) {

			msgServer = (ServerMessage) o;

			messages += msgServer.toString() + "\n";

			this.serverOutput.setText(messages);

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
				ServerManagement.this.disconnectServer();
			} else if (e.getSource() == connect) {
				ServerManagement.this.connectServer();
			}

		}
	}
}
