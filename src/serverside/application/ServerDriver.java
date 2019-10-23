package serverside.application;

import serverside.managers.ServerManagement;

/**
 * Class Description: Runs the server management class
 * 
 * @author 806547 Holly Harnack
 *
 */
public class ServerDriver {

	public static void main(String[] args) {
		
		ServerManagement sm = new ServerManagement();
		
		sm.display();

	}

}
