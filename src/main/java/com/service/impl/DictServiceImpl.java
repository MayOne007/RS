package com.service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dao.DictDao;
import com.entity.Dict;
import com.service.DictService;

import core.service.BaseServiceImpl;

@Service
public class DictServiceImpl extends BaseServiceImpl<Dict, Integer> implements DictService {
	
	@Autowired
	DictDao dictDao;
	
	@Override
	public void txOne() {
		System.out.println("txOne");
		
		/*System.out.println(AopUtils.isAopProxy(AopContext.currentProxy()));
		System.out.println(AopUtils.isCglibProxy(AopContext.currentProxy()));
		System.out.println(AopUtils.isJdkDynamicProxy(AopContext.currentProxy()));*/

		Dict d = new Dict();
		d.setKey("txOnekey");
		d.setValue("txOnevalue");
		dictDao.save(d);
		
		//((DictService)AopContext.currentProxy()).txTwo();//内部调用另起事务成功，txTwo()不受下面RuntimeException影响
		
		txTwo();//内部调用另起事务失败，txTwo()受下面RuntimeException影响发生回滚
		
		throw new RuntimeException("测试回滚");
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void txTwo() {
		System.out.println("txTwo");
		Dict d = new Dict();
		d.setKey("txTwokey");
		d.setValue("txTwovalue");
		dictDao.save(d);
		
	}

	/*@CacheEvict(value="user", key="#id",beforeInvocation=true)*/
	@Cacheable(value="user", key="#id")
	@Override
	public Object cacheOne(Integer id) {
		System.out.println("cacheOne");
		return dictDao.getById(id);
	}
	
}
