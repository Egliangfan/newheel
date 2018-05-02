/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.workplace.entity.Workplace;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 需求管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class Demand extends DataEntity<Demand> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 需求/主题名称
	private ColumnClassfy category;		// 类型
	private String description;		// 需求详述
	private String phone;		// 联系方式
	private Workplace workplace;		// 场地信息
	private Date startDate;		// 正式发布日期
	private Date stopDate;		// 截止日期
	private String status;		// 状态
	private List<DemandDetail> demandDetailList = Lists.newArrayList();		// 子表列表
	private List<DemandSchedule> demandScheduleList = Lists.newArrayList();		// 子表列表
	
	public Demand() {
		super();
	}

	public Demand(String id){
		super(id);
	}

	@ExcelField(title="需求/主题名称", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="类型不能为空")
	@ExcelField(title="类型", align=2, sort=7)
	public ColumnClassfy getCategory() {
		return category;
	}

	public void setCategory(ColumnClassfy category) {
		this.category = category;
	}
	
	@ExcelField(title="需求详述", align=2, sort=8)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@ExcelField(title="联系方式", align=2, sort=9)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@NotNull(message="场地信息不能为空")
	@ExcelField(title="场地信息", align=2, sort=10)
	public Workplace getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Workplace workplace) {
		this.workplace = workplace;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="正式发布日期不能为空")
	@ExcelField(title="正式发布日期", align=2, sort=11)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="截止日期不能为空")
	@ExcelField(title="截止日期", align=2, sort=12)
	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	
	@ExcelField(title="状态", dictType="demand_status", align=2, sort=13)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<DemandDetail> getDemandDetailList() {
		return demandDetailList;
	}

	public void setDemandDetailList(List<DemandDetail> demandDetailList) {
		this.demandDetailList = demandDetailList;
	}
	public List<DemandSchedule> getDemandScheduleList() {
		return demandScheduleList;
	}

	public void setDemandScheduleList(List<DemandSchedule> demandScheduleList) {
		this.demandScheduleList = demandScheduleList;
	}
}