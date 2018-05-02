/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysuserinfo.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.sysuserinfo.entity.SysUserInfo;

/**
 * 个人信息管理DAO接口
 * @author chiweineng
 * @version 2018-04-25
 */
@MyBatisDao
public interface SysUserInfoDao extends CrudDao<SysUserInfo> {

	
}