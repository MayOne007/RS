package testjvm.testclassloader;

import java.lang.reflect.Method;

public class MonitorHotSwap implements Runnable {

    private String className = "testjvm.testclassloader.Hot";

    public static void main(String[] args) throws Exception {
        //开启线程，如果class文件有修改，就热替换
        Thread t = new Thread(new MonitorHotSwap());
        t.start();
    }
    
    @Override
    public void run() {
        try {
            while (true) {
            	HotSwapURLClassLoader hotSwapCL = HotSwapURLClassLoader.getClassLoader();
            	Class<?> hotClazz = hotSwapCL.loadClass(className);
            	
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
