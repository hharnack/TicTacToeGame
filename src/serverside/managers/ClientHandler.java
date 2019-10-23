package serverside.managers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import utilities.Client;
import utilities.InputListener;

/**Class Description : class that handles the thread and object streams of the two clients 
 * 
 * @author 806547 Holly Harnack
 *
 */
public class ClientHandler implements Runnable, PropertyChangeListener {

	// Attributes
	private Socket s1;
	private Socket s2;
	private ObjectOutputStream oos;
	private ObjectOutputStream oos2;
	private InputListener inListener1;
	private InputListener inListener2;
	private ServerManagement sm;
	private ArrayList<PropertyChangeListener> observers = new ArrayList<>();
	private Client c = new Client();

	//Constructors
	
	/**Initializes the client handler 
	 * @param s1 socket of the first client
	 * @param s2 socket of the next client 
	 * @param sm server management class
	 * @throws IOException - throws the file io exception 
	 */
	public ClientHandler(Socket s1, Socket s2, ServerManagement sm) throws IOException {
		this.s1 = s1;
		this.s2 = s2;
		this.sm = sm;
	}
	
	//Operational Methods 

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * runs the client handler 
	 */
	@Override
	public void run() {

		try {

			this.oos2 = new ObjectOutputStream(s2.getOutputStream());
			this.oos = new ObjectOutputStream(s1.getOutputStream());

			//add the client handler and server management class as observers
			observers.add(this);
			observers.add(sm);

			//send client object to the first client connected to initialize their symbol 
			c.setPlayer("X");
			oos.writeObject(c);

			c.setPlayer("O");
			oos2.writeObject(c);

			inListener1 = new InputListener(s1, 1, observers);
			inListener2 = new InputListener(s2, 2, observers);

			//Start the threads that will listen for the input streams of the sockets 
			Thread t = new Thread(inListener1);
			Thread t2 = new Thread(inListener2);
			t.start();
			t2.start();

			while ((s1.isConnected() && s2.isConnected()))
				;

		} catch (IOException e) {
			System.out.print("Server was disconnected.");
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 * 
	 * Handles which socket is written to depending on which listener notified 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		int id = (Integer) evt.getOldValue();
		Object o = evt.getNewValue();

		try {

			//writes to the second client if the listener read from the first client 
			if (id == 1) {
				oos2.writeObject(o);

			} else {
				//writes to the first client if the listener read from the second client 
				oos.writeObject(o);
			}

		} catch (IOException e) {
			System.out.print("Server was disconnected.");
		}

	}
}
