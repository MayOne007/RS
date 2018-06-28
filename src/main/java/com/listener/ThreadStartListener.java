package com.listener;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ThreadStartListener implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
        	/*new Thread(new Runnable() {
    			@Override
    			public void run() {
    				while(true) {
    					System.out.println("test thread");
    					Timer timer = new Timer();
    			        Task task = new Task();
    			        timer.schedule(task, new Date(), 1000);
    					try {
    						Thread.sleep(3000);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
    				}
    			}
    		}).start();*/
        }
        
	}
	class Task extends TimerTask{

	    @Override
	    public void run() {
	        System.out.println("Do work...");
	    }
	}
}
