/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author Jiayun
 */
public class Customer {
    private final int TOTAL;
    private final int RESTAURANT_CAPACITY;
    private ArrayBlockingQueue<Customer_Group> waiting; //stores a list of groups waiting outside
    private ArrayBlockingQueue<Customer_Group> entered;
    private Customer_Generator cg;
    private ArrayList threadArrayList;

    public Customer(int newTOTAL, int newRestaurant_capacity, ArrayBlockingQueue<Customer_Group> newWaiting,ArrayBlockingQueue<Customer_Group> newEntered, ArrayList mArrayList,Restaurant_Monitor newRc){
        TOTAL = newTOTAL;
        entered = newEntered;
        threadArrayList = mArrayList;
        RESTAURANT_CAPACITY = newRestaurant_capacity;
        waiting = newWaiting;
        cg = new Customer_Generator(newTOTAL,waiting,threadArrayList,newRc);
        cg.start();
    }
    public ArrayBlockingQueue<Customer_Group> getWaiting(){
        return waiting;
    }

    //flag set to 0 -> check waiting queue, 1 -> check dining queue
    public Customer_Group LocateEnteredGroupByGnumber(int Gnumber){
        //Must ensure that entered is not empty before envoke this function!
        Iterator<Customer_Group> ci = entered.iterator(); //used to traverse the list without modifying it
        Customer_Group resultGroup = null;
        boolean found = false;
        while(ci.hasNext()){
            Customer_Group tmpGroup = ci.next();
//            System.out.println("--In LocateGroupByGnumber--");
//            System.out.println("tmoGroup Gnumber: "+ tmpGroup.getGroupNumber());
//            System.out.println("Target Gnumber: " + Gnumber);
            
            if(tmpGroup.getGroupNumber() == Gnumber){
                resultGroup = tmpGroup;
                found = true;
                break;
            }
        }
        if(found){
//            System.out.println("Found!");
            return resultGroup;
        }
            
        else{
//            System.out.println("In LocateGroupByGnumber");
//              System.out.println("-NOT FOUND-");
              return null;
        }
    }
    
/*    public void updateEntered(){
        //make a copy of the current queue, which then allow us to do more operations
        ArrayBlockingQueue<Customer_Group> copyCustomerGroupQueue = waiting;
        while(!copyCustomerGroupQueue.isEmpty()){
            //store the first element
            Customer_Group cg = copyCustomerGroupQueue.poll();
            if(cg.hasEntered())
                enter.add(cg);
        }
        printEntered();
    }
*/   
}
