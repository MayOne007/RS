package com.dao.impl;

import org.springframework.stereotype.Repository;

import com.dao.DictDao;
import com.entity.Dict;

import core.dao.BaseDaoImpl;

@Repository
public class DictDaoImpl extends BaseDaoImpl<Dict, Integer> implements DictDao {

}
