/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class Restaurant_Auth_Client {
    private Restaurant_RMI rObj;
    private InetAddress myID;
    
    public Restaurant_Auth_Client(){
        try {
            myID = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Restaurant_Auth_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean connect(){
        try {
            rObj = 
                (Restaurant_RMI) Naming.lookup("//127.0.0.1/Restaurant");
        } catch (Exception e) {
            System.out.println("Excepction : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean getAuth(String username, String password){
        try {
            connect();
            if(rObj.authorize(username, password,myID)){
                System.out.println("Authorized!");
                return true;
            }
                
        } catch (RemoteException ex) {}
        return false;
    }
    public boolean openTable(int tableID) throws RemoteException{
        connect();
        if(rObj.openTable(tableID, myID))
            return true;
        return false;
    }
    public boolean closeTable(int tableID) throws RemoteException{
        connect();
        if(rObj.closeTable(tableID, myID))
            return true;
        return false;
    }
}
