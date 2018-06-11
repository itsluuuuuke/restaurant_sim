/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restaurant_sim;

/**
 *
 * @author Jiayun
 */
public class Restaurant_Monitor {
    private volatile boolean isPaused;
    private volatile boolean running;
    private String log;
    public Restaurant_Monitor(){
        isPaused = false;
        running = true;
    }
    public boolean checkPaused(){
        return isPaused;
    }
    public boolean checkRunning(){
        return running;
    }
    public void Pause(){
        isPaused = true;
    }
    public void Resume(){
        isPaused = false;
    }
    public void Stop(){
        running = false;
    }
    public void updateLog(String newlog){
        log += newlog;
    }
    @Override
    public String toString(){
        return log;
    }
}
