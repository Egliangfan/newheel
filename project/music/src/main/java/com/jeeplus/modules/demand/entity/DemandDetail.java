/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 需求管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class DemandDetail extends DataEntity<DemandDetail> {
	
	private static final long serialVersionUID = 1L;
	private Demand demand;		// 需求项 父类
	private ColumnClassfy category;		// 角色类型
	private String categoryDetail;		// 角色要求详述
	private Integer quantity;		// 数量
	private String status;		// 状态
	private List<RoleSchedule> roleScheduleList = Lists.newArrayList();		// 子表列表
	
	public DemandDetail() {
		super();
	}

	public DemandDetail(String id){
		super(id);
	}

	public DemandDetail(Demand demand){
		this.demand = demand;
	}

	@NotNull(message="需求项不能为空")
	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}
	
	@NotNull(message="角色类型不能为空")
	@ExcelField(title="角色类型", align=2, sort=7)
	public ColumnClassfy getCategory() {
		return category;
	}

	public void setCategory(ColumnClassfy category) {
		this.category = category;
	}
	
	@ExcelField(title="角色要求详述", align=2, sort=8)
	public String getCategoryDetail() {
		return categoryDetail;
	}

	public void setCategoryDetail(String categoryDetail) {
		this.categoryDetail = categoryDetail;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=9)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@ExcelField(title="状态", dictType="demand_detail", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<RoleSchedule> getRoleScheduleList() {
		return roleScheduleList;
	}

	public void setRoleScheduleList(List<RoleSchedule> roleScheduleList) {
		this.roleScheduleList = roleScheduleList;
	}
	
}