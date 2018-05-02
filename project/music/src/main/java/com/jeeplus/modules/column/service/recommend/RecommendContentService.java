/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.service.recommend;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.column.dao.recommend.RecommendContentDao;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.column.entity.recommend.Recommend;
import com.jeeplus.modules.column.entity.recommend.RecommendContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 推荐位内容管理Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class RecommendContentService extends CrudService<RecommendContentDao, RecommendContent> {

	public RecommendContent get(String id) {
		return super.get(id);
	}
	
	public List<RecommendContent> findList(RecommendContent recommendContent) {
		return super.findList(recommendContent);
	}
	
	public Page<RecommendContent> findPage(Page<RecommendContent> page, RecommendContent recommendContent) {
		return super.findPage(page, recommendContent);
	}
	
	@Transactional(readOnly = false)
	public void save(RecommendContent recommendContent) {
		super.save(recommendContent);
	}
	
	@Transactional(readOnly = false)
	public void delete(RecommendContent recommendContent) {
		super.delete(recommendContent);
	}
	
	public Page<Recommend> findPageByrecommend(Page<Recommend> page, Recommend recommend) {
		recommend.setPage(page);
		page.setList(dao.findListByrecommend(recommend));
		return page;
	}
	public Page<ColumnClassfy> findPageByclassfy(Page<ColumnClassfy> page, ColumnClassfy classfy) {
		classfy.setPage(page);
		page.setList(dao.findListByclassfy(classfy));
		return page;
	}
	
	
	
}