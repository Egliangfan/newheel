/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.dao;

import com.jeeplus.modules.demand.entity.Demand;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.demand.entity.DemandSchedule;

/**
 * 需求管理DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface DemandScheduleDao extends CrudDao<DemandSchedule> {

	public List<Demand> findListBydemand(Demand demand);
	
}