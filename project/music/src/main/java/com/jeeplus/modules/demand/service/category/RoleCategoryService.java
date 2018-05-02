/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.service.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.demand.entity.category.RoleCategory;
import com.jeeplus.modules.demand.dao.category.RoleCategoryDao;

/**
 * 角色类型Service
 * @author M1n9
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class RoleCategoryService extends CrudService<RoleCategoryDao, RoleCategory> {

	public RoleCategory get(String id) {
		return super.get(id);
	}
	
	public List<RoleCategory> findList(RoleCategory roleCategory) {
		return super.findList(roleCategory);
	}
	
	public Page<RoleCategory> findPage(Page<RoleCategory> page, RoleCategory roleCategory) {
		return super.findPage(page, roleCategory);
	}
	
	@Transactional(readOnly = false)
	public void save(RoleCategory roleCategory) {
		super.save(roleCategory);
	}
	
	@Transactional(readOnly = false)
	public void delete(RoleCategory roleCategory) {
		super.delete(roleCategory);
	}
	
	
	
	
}