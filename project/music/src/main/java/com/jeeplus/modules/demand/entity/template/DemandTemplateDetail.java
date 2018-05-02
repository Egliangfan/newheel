/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity.template;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;

import javax.validation.constraints.NotNull;

/**
 * 需求模板Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class DemandTemplateDetail extends DataEntity<DemandTemplateDetail> {
	
	private static final long serialVersionUID = 1L;
	private DemandTemplate demandTemplate;		// 需求模板 父类
	private ColumnClassfy roleCategory;		// 角色类型
	private String categoryDetail;		// 角色需求详述
	private Integer quantity;		// 数量
	
	public DemandTemplateDetail() {
		super();
	}

	public DemandTemplateDetail(String id){
		super(id);
	}

	public DemandTemplateDetail(DemandTemplate demandTemplate){
		this.demandTemplate = demandTemplate;
	}

	@NotNull(message="需求模板不能为空")
	public DemandTemplate getDemandTemplate() {
		return demandTemplate;
	}

	public void setDemandTemplate(DemandTemplate demandTemplate) {
		this.demandTemplate = demandTemplate;
	}
	
	@NotNull(message="角色类型不能为空")
	@ExcelField(title="角色类型", align=2, sort=6)
	public ColumnClassfy getRoleCategory() {
		return roleCategory;
	}

	public void setRoleCategory(ColumnClassfy roleCategory) {
		this.roleCategory = roleCategory;
	}
	
	@ExcelField(title="角色需求详述", align=2, sort=7)
	public String getCategoryDetail() {
		return categoryDetail;
	}

	public void setCategoryDetail(String categoryDetail) {
		this.categoryDetail = categoryDetail;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=8)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}