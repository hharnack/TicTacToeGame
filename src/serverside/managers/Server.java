package serverside.managers;

import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import utilities.InputListener;


/**
 * Class description: class that runs the main server for the game. Constantly
 * accepts new clients
 * 
 * @author 806547 Holly Harnack
 *
 */
public class Server implements Runnable {

	// Attributes
	ServerManagement sm;
	String messageOutput;
	Socket socket;
	ServerSocket server;
	ArrayList<Socket> socketList = new ArrayList<Socket>();
	ArrayList<PropertyChangeListener> observers = new ArrayList<>();
	InputListener inListener1;
	InputListener inListener2;

	// Constructors
	/**
	 * Initializes the server
	 * 
	 * @param sm - server management class
	 * @param server - the server socket 
	 * @param observers - the list of observers 
	 */
	public Server(ServerManagement sm, ServerSocket server, ArrayList<PropertyChangeListener> observers) {
		this.sm = sm;
		this.server = server;
	}
	
	//Operational Methods

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * runs the server
	 */
	@Override
	public void run() {

		while (true) {
			try {
				socket = server.accept();
				socketList.add(socket);
				System.out.print("Accepted a client connection.\n");

				//once the server has two clients connect them and start the thread that will start their game
				if (socketList.size() == 2) {
					ClientHandler ch = new ClientHandler(socketList.get(0), socketList.get(1), sm);
					Thread thread = new Thread(ch);
					thread.start();

					socketList.clear();
				}
			} catch (IOException e) {
				System.out.print("Client Disconnected.\n");
			}

		}

	}

}
