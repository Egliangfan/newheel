/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.attachment.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 素材管理Entity
 * @author L-JH
 * @version 2018-04-26
 */
public class Attachment extends DataEntity<Attachment> {
	
	private static final long serialVersionUID = 1L;
	private String userid;		// 用户ID
	private Date creatDate;		// 创建时间
	private String auditType;		// 审核状态
	private String filePath;		// 文件路径
	private String fileType;		// 文件类型
	private Long fileSize;		// 文件大小
	private String filename;		// 文件名
	private String title;		// 标题
	
	public Attachment() {
		super();
	}

	public Attachment(String id){
		super(id);
	}

	@ExcelField(title="用户ID", align=2, sort=1)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="创建时间", align=2, sort=2)
	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}
	
	@ExcelField(title="审核状态", align=2, sort=3)
	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	@ExcelField(title="文件路径", align=2, sort=4)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@ExcelField(title="文件类型", align=2, sort=5)
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	@ExcelField(title="文件大小", align=2, sort=6)
	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	@ExcelField(title="文件名", align=2, sort=7)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@ExcelField(title="标题", align=2, sort=8)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}