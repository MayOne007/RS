package core.service;

import java.io.Serializable;

public interface BaseService<E, PK extends Serializable> {
	
	public PK save(E e);
	
	public void deleteById(PK id);
	
	public void update(E e);
	
	public E getById(PK id);
	
	public E loadById(PK id);
	
}
