/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.comment.entity;

import com.jeeplus.modules.demand.entity.DemandDetail;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 评论区管理Entity
 * @author jeff
 * @version 2018-04-28
 */
public class Comment extends DataEntity<Comment> {
	
	private static final long serialVersionUID = 1L;
	private DemandDetail demanddetail;		// 具体需求
	private String comment;		// 评论内容
	private Integer commentRank;		// 评价等级
	
	public Comment() {
		super();
	}

	public Comment(String id){
		super(id);
	}

	@ExcelField(title="具体需求", align=2, sort=7)
	public DemandDetail getDemanddetail() {
		return demanddetail;
	}

	public void setDemanddetail(DemandDetail demanddetail) {
		this.demanddetail = demanddetail;
	}
	
	@ExcelField(title="评论内容", align=2, sort=8)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@NotNull(message="评价等级不能为空")
	@ExcelField(title="评价等级", align=2, sort=9)
	public Integer getCommentRank() {
		return commentRank;
	}

	public void setCommentRank(Integer commentRank) {
		this.commentRank = commentRank;
	}
	
}