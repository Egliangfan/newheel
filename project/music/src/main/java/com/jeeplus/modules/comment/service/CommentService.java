/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.demand.entity.DemandDetail;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.comment.entity.Comment;
import com.jeeplus.modules.comment.dao.CommentDao;

/**
 * 评论区管理Service
 * @author jeff
 * @version 2018-04-28
 */
@Service
@Transactional(readOnly = true)
public class CommentService extends CrudService<CommentDao, Comment> {

	public Comment get(String id) {
		return super.get(id);
	}
	
	public List<Comment> findList(Comment comment) {
		return super.findList(comment);
	}
	
	public Page<Comment> findPage(Page<Comment> page, Comment comment) {
		return super.findPage(page, comment);
	}
	
	@Transactional(readOnly = false)
	public void save(Comment comment) {
		super.save(comment);
	}
	
	@Transactional(readOnly = false)
	public void delete(Comment comment) {
		super.delete(comment);
	}
	
	public Page<DemandDetail> findPageBydemanddetail(Page<DemandDetail> page, DemandDetail demanddetail) {
		demanddetail.setPage(page);
		page.setList(dao.findListBydemanddetail(demanddetail));
		return page;
	}
	
	
	
}