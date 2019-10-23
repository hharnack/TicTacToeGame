package clientside.application;

import clientside.managers.ClientManagement;

/**
 * Class Description: Runs the client management class
 * 
 * @author 806547 Holly Harnack
 *
 */
public class ClientDriver {
	
	public static void main(String[] args) {
		
		ClientManagement cm = new ClientManagement();
		
		cm.displayMain();
		
		cm.init();
	}

}