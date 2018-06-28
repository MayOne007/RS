package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.entity.Dict;
import com.service.DictService;

/** 声明用的是Spring的测试类 **/
@RunWith(SpringJUnit4ClassRunner.class)
/** 声明spring主配置文件位置 **/
@ContextConfiguration(locations={"classpath*:applicationContext*.xml"})
/** 事务自动回滚  **/
/*@Rollback*/
public class SpringJunitTest extends SessionTestCase{

	
	@Autowired
	private DictService dictService;
	
	@Test
	public void testLazy() {
		//Dict d = dictService.loadById(13);
		//Set l = d.getChildDicts();
		//System.out.println(d.getParentDict());
	}
	
	/**
	 * 测试事务内部另起事务
	 */
	@Test
	public void testTxAaop() {
		//dictService.txOne();
	}
	
	/*public static void main(String[] args) {
		System.out.println("main");
	}*/
}
