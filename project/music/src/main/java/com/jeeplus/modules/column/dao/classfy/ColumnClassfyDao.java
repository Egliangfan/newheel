/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.dao.classfy;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;

/**
 * 分类管理DAO接口
 * @author M1n9
 * @version 2018-04-27
 */
@MyBatisDao
public interface ColumnClassfyDao extends TreeDao<ColumnClassfy> {
	
}