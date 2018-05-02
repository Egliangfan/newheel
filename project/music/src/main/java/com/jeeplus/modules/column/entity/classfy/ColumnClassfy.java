/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.entity.classfy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jeeplus.common.persistence.TreeEntity;

import javax.validation.constraints.NotNull;

/**
 * 分类管理Entity
 * @author M1n9
 * @version 2018-04-27
 */
public class ColumnClassfy extends TreeEntity<ColumnClassfy> {
	
	private static final long serialVersionUID = 1L;
	private ColumnClassfy parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private String name;		// 分类名称
	private String icon;		// 分类图标
	private Integer sort;		// 排序
	
	public ColumnClassfy() {
		super();
	}

	public ColumnClassfy(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="父级编号不能为空")
	public ColumnClassfy getParent() {
		return parent;
	}

	public void setParent(ColumnClassfy parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@NotNull(message="排序不能为空")
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}