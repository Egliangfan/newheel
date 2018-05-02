/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.dao.recommend;

import com.jeeplus.modules.column.entity.recommend.Recommend;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.column.entity.recommend.RecommendContent;

/**
 * 推荐位内容管理DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface RecommendContentDao extends CrudDao<RecommendContent> {

	public List<Recommend> findListByrecommend(Recommend recommend);
	public List<ColumnClassfy> findListByclassfy(ColumnClassfy classfy);
	
}