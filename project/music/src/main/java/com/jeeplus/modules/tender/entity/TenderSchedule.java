/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.entity;

import com.jeeplus.modules.tender.entity.TenderInvitation;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 投标管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class TenderSchedule extends DataEntity<TenderSchedule> {
	
	private static final long serialVersionUID = 1L;
	private TenderInvitation tender;		// 投标项 父类
	private Date startDate;		// 档期起始时间
	private Date stopDate;		// 档期结束时间
	
	public TenderSchedule() {
		super();
	}

	public TenderSchedule(String id){
		super(id);
	}

	public TenderSchedule(TenderInvitation tender){
		this.tender = tender;
	}

	@NotNull(message="投标项不能为空")
	public TenderInvitation getTender() {
		return tender;
	}

	public void setTender(TenderInvitation tender) {
		this.tender = tender;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="档期起始时间不能为空")
	@ExcelField(title="档期起始时间", align=2, sort=7)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="档期结束时间不能为空")
	@ExcelField(title="档期结束时间", align=2, sort=8)
	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	
}