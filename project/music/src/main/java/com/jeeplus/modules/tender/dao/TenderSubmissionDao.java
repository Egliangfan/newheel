/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.tender.entity.TenderSubmission;

/**
 * 投标管理DAO接口
 * @author M1n9
 * @version 2018-04-29
 */
@MyBatisDao
public interface TenderSubmissionDao extends CrudDao<TenderSubmission> {

	
}