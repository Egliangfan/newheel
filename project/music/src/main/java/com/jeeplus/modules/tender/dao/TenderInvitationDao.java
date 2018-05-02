/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.dao;

import com.jeeplus.modules.tender.entity.Tender;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.workplace.entity.Workplace;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tender.entity.TenderInvitation;

/**
 * 投标内容DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface TenderInvitationDao extends CrudDao<TenderInvitation> {

	public List<Tender> findListBytender(Tender tender);
	public List<ColumnClassfy> findListBycolum(ColumnClassfy colum);
	public List<Workplace> findListBylocation(Workplace location);
	
}