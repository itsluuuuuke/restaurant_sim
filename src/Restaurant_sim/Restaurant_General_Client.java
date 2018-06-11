/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jiayun
 */
public class Restaurant_General_Client {
    private String log = "";
    private String client_status;
    private Restaurant_RMI rObj;
    public Restaurant_General_Client(){
       client_status = "Nothing to be done";
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
    
    public String getStatus() throws RemoteException{
        if(connect())
            client_status = "Retrieving object...";
        rObj.update_status();
        return rObj.get_status();
    }
    public String getClientStatus(){
        return client_status;
    }
}
