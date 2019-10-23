package utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class description: Listens on the client's socket's input streams to detect any objects sent back and forth
 * 
 * @author 806547 Holly Harnack
 *
 */
public class InputListener implements Runnable {

	//Attributes
	private int id;
	private ObjectInputStream ois;
	private Socket socket;
	private ArrayList<PropertyChangeListener> observers = new ArrayList<>();
	private Object o;

	//Constructors
	/**
	 * Initializes the input listener 
	 * 
	 * @param socket - the client's socket 
	 * @param id - id to differentiate between client's object streams
	 * @param observers - the list of observers observing the stream
	 */
	public InputListener(Socket socket, int id, ArrayList<PropertyChangeListener> observers) {
		this.socket = socket;
		this.id = id;
		this.observers = observers;
	}

	/**
	 * gets the object that was recieved
	 * @return the object sent over the stream
	 */
	public synchronized Object getObject() {

		return this.o;

	}

	/**sets the object to be sent to the observers
	 * @param o - the object that is sent to the observers
	 */
	public synchronized void setObject(Object o) {

		this.o = o;

	}

	/**gets the id of the listener 
	 * @return the id of this listener 
	 */
	public int getID() {
		return this.id;
	}

	/**notifies the observers with the object and id of the listener when a change is determined
	 * @param id - the id of the listener 
	 * @param o - the object that was received 
	 */
	private void notifyListeners(int id, Object o) {

		for (PropertyChangeListener l : observers) {

			l.propertyChange(new PropertyChangeEvent(this, null, id, o));

		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 
	 * runs the thread
	 */
	@Override
	public void run() {

		try {

			this.ois = new ObjectInputStream(socket.getInputStream());

			while (true) {
				setObject(ois.readObject());
				//once an object is read notify all observers
				notifyListeners(this.id, this.getObject());
			}

		} catch (IOException e) {
			System.out.print("Problem with reading the input");
		} catch (ClassNotFoundException e) {
			System.out.print("Problem with reading the input");
		}

	}

}