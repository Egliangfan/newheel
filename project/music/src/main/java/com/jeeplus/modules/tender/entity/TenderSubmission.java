/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.entity;

import com.jeeplus.modules.tender.entity.TenderInvitation;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 投标管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class TenderSubmission extends DataEntity<TenderSubmission> {
	
	private static final long serialVersionUID = 1L;
	private TenderInvitation tender;		// 投标项 父类
	private User user;		// 投标者
	private String status;		// 应标状态
	
	public TenderSubmission() {
		super();
	}

	public TenderSubmission(String id){
		super(id);
	}

	public TenderSubmission(TenderInvitation tender){
		this.tender = tender;
	}

	@NotNull(message="投标项不能为空")
	public TenderInvitation getTender() {
		return tender;
	}

	public void setTender(TenderInvitation tender) {
		this.tender = tender;
	}
	
	@NotNull(message="投标者不能为空")
	@ExcelField(title="投标者", fieldType=User.class, value="user.name", align=2, sort=6)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="应标状态", dictType="bid_status", align=2, sort=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}