/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jiayun
 */
public interface Restaurant_RMI extends Remote{
    void update_status() throws RemoteException;
    String get_status() throws RemoteException;
    boolean authorize(String username, String password, InetAddress id) throws RemoteException;
    boolean openTable(int tableID, InetAddress id) throws RemoteException;
    boolean closeTable(int tableID, InetAddress id) throws RemoteException;
}
