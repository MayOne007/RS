package core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

public class SpringUtils implements ApplicationContextAware {
	
	private static Logger logger = Logger.getLogger(SpringUtils.class);
	
	private static final String packageNames = "com.controller,com.service";
	
    //记录bean的上一次修改时间
    private static Map<String, Long> beanLoadTimes = new HashMap<String, Long>();
    
    private static ApplicationContext applicationContext;
    
    @Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		applicationContext = appContext;
	}
    
    /**
	 * 获取controller下所有的方法的权限
	 * @return
	 */
	public static void reloadBean() {
		String[] pkNames = packageNames.split(",");
		for(String pkName : pkNames) {
			String packageDir = pkName.replace(".", "/");
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Enumeration<URL> dirs = loader.getResources(packageDir);
				URL url = dirs.nextElement();
				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");			
				FileFilter cff = new FileFilter() {
					@Override
					public boolean accept(File file) {
						// 过滤规则(是否递归查找子目录、class文件)
						return file.isDirectory() || (file.getName().endsWith(".class") && file.getName().indexOf("$") == -1);
					}
				};			
				findAllClass(pkName, filePath, cff, loader);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("",e);
			}
		}
		System.out.println(beanLoadTimes);
	}
    
	/**
	 * 递归查找指定包下所有的class
	 * 
	 * @param packageName 指定的包名
	 * @param filePath 指定包的物理路径
	 * @param fileFilter 自定义文件过滤
	 * @param loader 类加载器
	 */
	private static void findAllClass(String packageName, String filePath, FileFilter fileFilter, ClassLoader loader) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(fileFilter);
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAllClass(packageName + "." + file.getName(), file.getAbsolutePath(), fileFilter, loader);
			} else {				
				//String className = file.getName().substring(0, file.getName().length() - 6);
				String beanClazz = packageName + '.' + file.getName().replace(".class", "");				
				load(file, beanClazz, loader);	
			}
		}	
	}
    
	/**
	 * 执行加载
	 * @param clazz 类文件
	 * @param beanClazz 内全名（包含包名）
	 * @param loader classloader
	 */
	private static void load(File clazz, String beanClazz, ClassLoader loader) {
		//判断class文件修改时间是否大于上次加载时间
		if (beanLoadTimes.containsKey(beanClazz) && beanLoadTimes.get(beanClazz) >= clazz.lastModified()) {
			return;
		}
		
		try {
			Class<?> cls = loader.loadClass(beanClazz);
			Controller controller = cls.getAnnotation(Controller.class);
			Service service = cls.getAnnotation(Service.class);
			if(controller==null && service==null) {
				return;
			}else {
				String beanId = captureName(clazz.getName().replace(".class", ""));
				if(controller!=null && !StringUtils.isEmpty(controller.value())) {
					beanId = controller.value();
				}
				if(service!=null && !StringUtils.isEmpty(service.value())) {
					beanId = service.value();
				}
				//getBeanById("testController");
				//不加载系统中不存在的bean
				/*if(==null) {
					return;
				}*/
				
				//重新注册bean
				registerBean(beanId, beanClazz);
				//记录注册时间
				beanLoadTimes.put(beanClazz, clazz.lastModified());
				System.out.println("reload bean: { id: " +beanId+", class: "+ beanClazz+ " }");
				logger.info("reload bean: { id: " +beanId+", class: "+ beanClazz+ " }");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("",e);
		}				
	}
	
	
	/**
	 * 首字母小写
	 * @param name
	 * @return
	 */
	private static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] += 32;
		return String.valueOf(cs);
	}
 
    /**
     * 向spring注册bean
     * @param beanId
     * @param beanName
     */
    public static void registerBean(String beanId, String beanName) {
    	DefaultListableBeanFactory fty = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    	BeanDefinition bean = new GenericBeanDefinition();
    	bean.setBeanClassName(beanName);
    	fty.registerBeanDefinition(beanId, bean);
    	fty.autowireBean(bean);
    }
    
    
    public static Object getBeanById(String beanId){
        return applicationContext.getBean(beanId);
    }  
    
    public static <T> T getBeanByClass(Class<T> cls){
        return applicationContext.getBean(cls);
    }
    
}