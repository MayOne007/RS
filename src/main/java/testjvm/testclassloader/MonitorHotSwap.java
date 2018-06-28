package testjvm.testclassloader;

import java.lang.reflect.Method;

public class MonitorHotSwap implements Runnable {

    private String className = "testjvm.testclassloader.Hot";

    public static void main(String[] args) throws Exception {
        //开启线程，如果class文件有修改，就热替换
        Thread t = new Thread(new MonitorHotSwap());
        t.start();
    }
    static Class<?> hotClazz1 =  null;
    
    @Override
    public void run() {
        try {
        	boolean isFirst = true;
            while (true) {
            	HotSwapURLClassLoader hotSwapCL = HotSwapURLClassLoader.getClassLoader();
            	Class<?> hotClazz = null;
            	/*if(isFirst) {
            		hotClazz1 = hotSwapCL.loadClass(className);
            		isFirst = false;
            	}else {*/
            		hotClazz = hotSwapCL.loadClass(className);
            	//}
            	if(hotClazz1!=null && hotClazz!=null) {
            		/*System.out.println(hotClazz1.newInstance().equals(hotClazz2.newInstance()));
            		Hot obj1 = (Hot) hotClazz1.newInstance();  
            		Hot obj2 = (Hot) hotClazz2.newInstance();  
	                hotClazz1.getMethod("setHot", Hot.class).invoke(obj1, obj2);*/
            	}
            	System.out.println(hotClazz.getClassLoader());
                Object hot = hotClazz.newInstance();
                Method m = hotClazz.getMethod("hot");
                m.invoke(hot);
                Thread.sleep(3000);
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 加载class
     */
    public void initLoad() throws Exception {
       
        // 如果Hot类被修改了，那么会重新加载，hotClass也会返回新的
        
    }
}
