package core.service;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import core.dao.BaseDao;

@Transactional
public class BaseServiceImpl<E, PK extends Serializable> implements BaseService<E, PK>{
	
	public final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private BaseDao<E, PK> baseDao;

	@Override
	public PK save(E e) {
		return baseDao.save(e);
	}

	@Override
	public void deleteById(PK id) {
		baseDao.deleteById(id);
	}

	@Override
	public void update(E e) {
		baseDao.update(e);
	}	
	
	@Override
	public E getById(PK id) {
		return baseDao.getById(id);
	}

	@Override
	public E loadById(PK id) {
		return baseDao.loadById(id);
	}
	
}
