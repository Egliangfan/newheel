/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.attachment.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.attachment.entity.Attachment;

/**
 * 素材管理DAO接口
 * @author L-JH
 * @version 2018-04-26
 */
@MyBatisDao
public interface AttachmentDao extends CrudDao<Attachment> {

	
}