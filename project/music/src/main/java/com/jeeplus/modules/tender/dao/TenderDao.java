/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.dao;

import com.jeeplus.modules.demand.entity.Demand;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tender.entity.Tender;

/**
 * 投标内容DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface TenderDao extends CrudDao<Tender> {

	public List<Demand> findListBydemand(Demand demand);
	
}