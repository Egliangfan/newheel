/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity;

import com.jeeplus.modules.demand.entity.Demand;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 需求管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class DemandSchedule extends DataEntity<DemandSchedule> {
	
	private static final long serialVersionUID = 1L;
	private Demand demand;		// 需求Id 父类
	private Date startTime;		// 开始时间
	private Date stopTime;		// 结束时间
	
	public DemandSchedule() {
		super();
	}

	public DemandSchedule(String id){
		super(id);
	}

	public DemandSchedule(Demand demand){
		this.demand = demand;
	}

	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	@ExcelField(title="开始时间", align=2, sort=8)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	@ExcelField(title="结束时间", align=2, sort=9)
	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
}