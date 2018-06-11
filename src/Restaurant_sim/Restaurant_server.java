/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * @author Jiayun
 */
public class Restaurant_server {
    
    public Restaurant_server() {
    }
    public static void main(String args[]) {
        try {
            //Instance of the object to register
            Restaurant_GUI_RMI_obj obj = new Restaurant_GUI_RMI_obj();
            //Naming.rebind("//127.0.0.1/ObjetGreetings",obj);
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/Restaurant",obj);
            System.out.println("Restaurant has been registered");
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
