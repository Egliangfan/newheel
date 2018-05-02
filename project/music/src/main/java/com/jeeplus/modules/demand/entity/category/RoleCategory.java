/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity.category;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 角色类型Entity
 * @author M1n9
 * @version 2018-04-25
 */
public class RoleCategory extends DataEntity<RoleCategory> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String description;		// 描述
	private String status;		// 状态(1开启，0关闭)
	
	public RoleCategory() {
		super();
	}

	public RoleCategory(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="描述", align=2, sort=7)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="状态(1开启，0关闭)", dictType="status", align=2, sort=8)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}