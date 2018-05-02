/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysuserinfo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.sysuserinfo.entity.SysUserInfo;
import com.jeeplus.modules.sysuserinfo.dao.SysUserInfoDao;

/**
 * 个人信息管理Service
 * @author chiweineng
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class SysUserInfoService extends CrudService<SysUserInfoDao, SysUserInfo> {

	public SysUserInfo get(String id) {
		return super.get(id);
	}
	
	public List<SysUserInfo> findList(SysUserInfo sysUserInfo) {
		return super.findList(sysUserInfo);
	}
	
	public Page<SysUserInfo> findPage(Page<SysUserInfo> page, SysUserInfo sysUserInfo) {
		return super.findPage(page, sysUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(SysUserInfo sysUserInfo) {
		super.save(sysUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysUserInfo sysUserInfo) {
		super.delete(sysUserInfo);
	}
	
	
	
	
}