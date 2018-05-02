/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.comment.dao;

import com.jeeplus.modules.demand.entity.DemandDetail;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.comment.entity.Comment;

/**
 * 评论区管理DAO接口
 * @author jeff
 * @version 2018-04-28
 */
@MyBatisDao
public interface CommentDao extends CrudDao<Comment> {

	public List<DemandDetail> findListBydemanddetail(DemandDetail demanddetail);
	
}