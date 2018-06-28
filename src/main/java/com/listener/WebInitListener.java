package com.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;


public class WebInitListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(WebInitListener.class);
	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//SpringUtils.reloadBean();
		
		/*test = new TestThread("test");
		test.start();*/
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		test.cancel();
		destroyMySql();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	TestThread test = null;
	class TestThread extends Thread{
		
		List<Timer> timers = new ArrayList<Timer>();
		boolean isContinue = true;
		
		TestThread(String name){
			super(name);
		}
		
		@Override
		public void run() {		
			while(isContinue) {
				System.out.println("test thread");
				Timer timer = new Timer();
		        Task task = new Task();
		        timer.schedule(task, new Date(), 5000);
		        timers.add(timer);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void cancel() {  
			isContinue = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(Timer timer:timers) {		
				timer.cancel();
			}
	    } 
		
		class Task extends TimerTask{
		    @Override
		    public void run() {
		        System.out.println("Do work...");
		    }
		}
	}
	
	/**
	 * 销毁连接池内存
	 */
	private void destroyMySql() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
	    while (drivers.hasMoreElements()) {
	    	Driver driver = null;
	        try {
	        	driver = drivers.nextElement();
	            DriverManager.deregisterDriver(driver);
	            logger.info(String.format("Driver %s deregistered:", driver));
	        } catch (SQLException e) {
	        	if(driver!=null) {
	        		logger.error(String.format("Driver %s deregistered error:", driver));
	        	}
	        	e.printStackTrace();	        
	        }
	    }
	    try {
	        AbandonedConnectionCleanupThread.uncheckedShutdown();
	    } catch (Exception e) {
	        logger.error("AbandonedConnectionCleanupThread problem cleaning up: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	/*private void destroySpecifyThreads() {
		// Handle remaining threads.
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for (Thread t:threadArray) {
            for (ThreadInfo i:this.threads) {
                if (t.getName().contains(i.getCue())) {
                    synchronized (t) {
                        try {
                            Class<?> cls = Class.forName(i.getName());
                            if (cls != null) {
                                Method mth = cls.getMethod(i.getStop());
                                if (mth != null) {
                                    mth.invoke(null);
                                    logger.info( String.format( "Connection cleanup thread %s shutdown successfully.", //$NON-NLS-1$
                                    		i.getName()));
                                }
                            }
                        } catch (Throwable thr) {
                        	logger.error(String.format("Failed to shutdown connection cleanup thread %s: ", //$NON-NLS-1$
                            	i.getName(),thr.getMessage()));
                            thr.printStackTrace();
                        }
                    }
                }
            }
        }
	}	

    private class ThreadInfo {

        private final String name;
        private final String cue;
        private final String stop;

        ThreadInfo(final String n, final String c, final String s) {
            this.name = n;
            this.cue  = c;
            this.stop = s;
        }

        public String getName() {
            return this.name;
        }

        public String getCue() {
            return this.cue;
        }

        public String getStop() {
            return this.stop;
        }
    }

    private List<ThreadInfo> threads = Arrays.asList(
        new ThreadInfo(
            "com.mysql.jdbc.AbandonedConnectionCleanupThread", //$NON-NLS-1$
            "Abandoned connection cleanup thread", //$NON-NLS-1$
            "shutdown" //$NON-NLS-1$
        )
    );*/
	
}
