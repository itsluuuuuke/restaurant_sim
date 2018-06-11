/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Jiayun
 */
public class Waiter {
    private final ExecutorService WaiterseExecutorService;
    private final int numberOfWaiters;
    private final Restaurant_Table rt;
    private static ArrayList<Restaurant_Control> threadArrayList;
    private Restaurant_Monitor rc;
    private WaiterThread[] wt;
    
    public Waiter(int newNumberOfWaiters, Restaurant_Table newRt, ArrayList<Restaurant_Control> mArrayList, Restaurant_Monitor newRc) {
        numberOfWaiters = newNumberOfWaiters;
        rt = newRt;
        threadArrayList = mArrayList;
        rc = newRc;
        wt = new WaiterThread[newNumberOfWaiters];
        WaiterseExecutorService = Executors.newFixedThreadPool(numberOfWaiters);
        for(int i = 0; i < numberOfWaiters; i++){
            wt[i] = new WaiterThread(i, rt, threadArrayList,rc);
            WaiterseExecutorService.submit(wt[i]);
        }
            
    }
    
    public String[] waiterToString(){
        String[] result = new String[numberOfWaiters];
        for(int i = 0; i < numberOfWaiters; i++)
            result[i] = wt[i].toString();
        return result;
    }
    
   
    
}
