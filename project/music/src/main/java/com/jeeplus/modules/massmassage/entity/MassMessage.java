/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.massmassage.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 群发消息Entity
 * @author L-JH
 * @version 2018-04-26
 */
public class MassMessage extends DataEntity<MassMessage> {
	
	private static final long serialVersionUID = 1L;
	private Date senddate;		// 发送日期
	private String content;		// 内容
	private String sender;		// 发件人
	private String title;		// 标题
	private String week;		// 星期
	
	public MassMessage() {
		super();
	}

	public MassMessage(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发送日期", align=2, sort=1)
	public Date getSenddate() {
		return senddate;
	}

	public void setSenddate(Date senddate) {
		this.senddate = senddate;
	}
	
	@ExcelField(title="内容", align=2, sort=2)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="发件人", align=2, sort=3)
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	@ExcelField(title="标题", align=2, sort=4)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="星期", align=2, sort=5)
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
}