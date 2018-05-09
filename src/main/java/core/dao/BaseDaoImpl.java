package core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<E,PK extends Serializable> implements BaseDao<E,PK> {
	
	public final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	SessionFactory sessionFactory;
	
	Class<E> entityClass;	
 
    @SuppressWarnings("unchecked")  
    public BaseDaoImpl(){  
    	this.entityClass=(Class<E>)getSuperClassGenricType(getClass(), 0);  
    }  
 
   
   @SuppressWarnings({ "unchecked", "rawtypes" })  
   public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {  
       Type genType = clazz.getGenericSuperclass();  
       if (!(genType instanceof ParameterizedType)) {  
          return Object.class;  
       }  
       Type[] params = ((ParameterizedType) genType).getActualTypeArguments(); 
       if (index >= params.length || index < 0) {  
                    return Object.class;  
       }  
       if (!(params[index] instanceof Class)) {  
             return Object.class;  
       }  
 
       return (Class) params[index];  
   } 
	
	@SuppressWarnings("unchecked")
	@Override
	public PK save(E e) {
		return (PK)sessionFactory.getCurrentSession().save(e);
	}
	
	@Override	
	public void deleteById(PK id) {
		sessionFactory.getCurrentSession().delete(getById(id));
	}
	
	@Override
	public void update(E e) {
		sessionFactory.getCurrentSession().update(e);
	}
	
	@Override
	public E getById(PK id) {
		return sessionFactory.getCurrentSession().get(entityClass,id);
	}
	
	@Override
	public E loadById(PK id) {
		return sessionFactory.getCurrentSession().load(entityClass,id);
	}
	
}
