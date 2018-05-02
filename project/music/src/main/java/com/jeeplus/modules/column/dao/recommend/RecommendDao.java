/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.dao.recommend;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.column.entity.recommend.Recommend;

/**
 * 推荐位管理DAO接口
 * @author M1n9
 * @version 2018-04-27
 */
@MyBatisDao
public interface RecommendDao extends CrudDao<Recommend> {

	
}