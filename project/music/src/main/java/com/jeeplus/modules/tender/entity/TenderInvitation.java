/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.entity;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.workplace.entity.Workplace;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 投标内容Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class TenderInvitation extends DataEntity<TenderInvitation> {
	
	private static final long serialVersionUID = 1L;
	private Tender tender;		// 投标项 父类
	private User publisher;		// 发布人
	private String phone;		// 发布人联系方式
	private ColumnClassfy colum;		// 栏目
	private Integer quantity;		// 数量
	private Workplace location;		// 详细地址
	private String price;		// 预算价格
	private String status;		// 栏目状态
	private List<TenderSchedule> tenderScheduleList = Lists.newArrayList();		// 子表列表
	private List<TenderSubmission> tenderSubmissionList = Lists.newArrayList();		// 子表列表
	
	public TenderInvitation() {
		super();
	}

	public TenderInvitation(String id){
		super(id);
	}

	public TenderInvitation(Tender tender){
		this.tender = tender;
	}

	public Tender getTender() {
		return tender;
	}

	public void setTender(Tender tender) {
		this.tender = tender;
	}
	
	@NotNull(message="发布人不能为空")
	@ExcelField(title="发布人", fieldType=User.class, value="publisher.name", align=2, sort=7)
	public User getPublisher() {
		return publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}
	
	@ExcelField(title="发布人联系方式", align=2, sort=8)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@NotNull(message="栏目不能为空")
	@ExcelField(title="栏目", align=2, sort=9)
	public ColumnClassfy getColum() {
		return colum;
	}

	public void setColum(ColumnClassfy colum) {
		this.colum = colum;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=10)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@NotNull(message="详细地址不能为空")
	@ExcelField(title="详细地址", align=2, sort=11)
	public Workplace getLocation() {
		return location;
	}

	public void setLocation(Workplace location) {
		this.location = location;
	}
	
	@ExcelField(title="预算价格", align=2, sort=12)
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	@ExcelField(title="栏目状态", dictType="tender_status", align=2, sort=13)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TenderSchedule> getTenderScheduleList() {
		return tenderScheduleList;
	}

	public void setTenderScheduleList(List<TenderSchedule> tenderScheduleList) {
		this.tenderScheduleList = tenderScheduleList;
	}
	public List<TenderSubmission> getTenderSubmissionList() {
		return tenderSubmissionList;
	}

	public void setTenderSubmissionList(List<TenderSubmission> tenderSubmissionList) {
		this.tenderSubmissionList = tenderSubmissionList;
	}
	
}