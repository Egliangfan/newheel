/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.demand.entity.Demand;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 投标内容Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class Tender extends DataEntity<Tender> {
	
	private static final long serialVersionUID = 1L;
	private Demand demand;		// 需求项
	private String status;		// 完成状态
	private String reviewStatus;		// 审核状态
	private List<TenderInvitation> tenderInvitationList = Lists.newArrayList();		// 子表列表
	
	public Tender() {
		super();
	}

	public Tender(String id){
		super(id);
	}

	@NotNull(message="需求项不能为空")
	@ExcelField(title="需求项", align=2, sort=6)
	public Demand getDemand() {
		return demand;
	}

	public void setDemand(Demand demand) {
		this.demand = demand;
	}
	
	@ExcelField(title="完成状态", dictType="tender_status", align=2, sort=7)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="审核状态", dictType="review_status", align=2, sort=8)
	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
	public List<TenderInvitation> getTenderInvitationList() {
		return tenderInvitationList;
	}

	public void setTenderInvitationList(List<TenderInvitation> tenderInvitationList) {
		this.tenderInvitationList = tenderInvitationList;
	}
}