/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.service.recommend;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.column.entity.recommend.Recommend;
import com.jeeplus.modules.column.dao.recommend.RecommendDao;

/**
 * 推荐位管理Service
 * @author M1n9
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class RecommendService extends CrudService<RecommendDao, Recommend> {

	public Recommend get(String id) {
		return super.get(id);
	}
	
	public List<Recommend> findList(Recommend recommend) {
		return super.findList(recommend);
	}
	
	public Page<Recommend> findPage(Page<Recommend> page, Recommend recommend) {
		return super.findPage(page, recommend);
	}
	
	@Transactional(readOnly = false)
	public void save(Recommend recommend) {
		super.save(recommend);
	}
	
	@Transactional(readOnly = false)
	public void delete(Recommend recommend) {
		super.delete(recommend);
	}
	
	
	
	
}