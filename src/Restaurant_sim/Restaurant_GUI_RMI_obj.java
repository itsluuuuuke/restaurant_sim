
package Restaurant_sim;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;


//the object that we want to remotely access
public class Restaurant_GUI_RMI_obj extends UnicastRemoteObject implements Restaurant_RMI{
    private final Restaurant_GUI rGUI;
    private String Restaurant_status;
    private ArrayList<InetAddress> vClients;
    

  
    public Restaurant_GUI_RMI_obj() throws RemoteException{
        rGUI = new Restaurant_GUI();
        rGUI.main(null);
        vClients = new ArrayList<>();
        Restaurant_status = rGUI.getLog();
    }
    @Override
    public void update_status() throws RemoteException{
        Restaurant_status = rGUI.getLog();
    }

    @Override
    public String get_status() throws RemoteException {
        return Restaurant_status;
    }

    @Override
    public boolean authorize(String username, String password, InetAddress id) throws RemoteException {
        if(rGUI.authenticate(username, password)){
            if(!checkAuthExists(id))
                vClients.add(id);
            return true;
        }
        return false;
    }
    
    private boolean checkAuthExists(InetAddress id){
        Iterator it = vClients.iterator();
        while(it.hasNext())
            if(it.next().equals(id))
                return true;
        return false;
    }
    
    
    @Override
    public boolean openTable(int tableID, InetAddress id) {
        if(!checkAuthExists(id)){
            System.out.println("Unauthorized!");
            return false;
        }
        return rGUI.openTable(tableID);
    }

    @Override
    public boolean closeTable(int tableID, InetAddress id) {
        if(!checkAuthExists(id)){
            System.out.println("Unauthorized!");
            return false;
        }
        return rGUI.closeTable(tableID);
    }
    
}
