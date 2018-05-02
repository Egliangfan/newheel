/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.dao.template;

import com.jeeplus.modules.demand.entity.template.DemandTemplate;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.demand.entity.template.DemandTemplateDetail;

/**
 * 需求模板DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface DemandTemplateDetailDao extends CrudDao<DemandTemplateDetail> {

	public List<DemandTemplate> findListBydemandTemplate(DemandTemplate demandTemplate);
	public List<ColumnClassfy> findListByroleCategory(ColumnClassfy roleCategory);
	
}