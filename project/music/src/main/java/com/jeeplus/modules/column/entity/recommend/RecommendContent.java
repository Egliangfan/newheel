/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.entity.recommend;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;

import javax.validation.constraints.NotNull;

/**
 * 推荐位内容管理Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class RecommendContent extends DataEntity<RecommendContent> {
	
	private static final long serialVersionUID = 1L;
	private Recommend recommend;		// 推荐位
	private ColumnClassfy classfy;		// 内容分类
	private String name;		// 推荐位名称
	private String picture;		// 图片
	private Integer sort;		// 排序
	
	public RecommendContent() {
		super();
	}

	public RecommendContent(String id){
		super(id);
	}

	@NotNull(message="推荐位不能为空")
	@ExcelField(title="推荐位", align=2, sort=6)
	public Recommend getRecommend() {
		return recommend;
	}

	public void setRecommend(Recommend recommend) {
		this.recommend = recommend;
	}
	
	@NotNull(message="内容分类不能为空")
	@ExcelField(title="内容分类", align=2, sort=7)
	public ColumnClassfy getClassfy() {
		return classfy;
	}

	public void setClassfy(ColumnClassfy classfy) {
		this.classfy = classfy;
	}
	
	@ExcelField(title="推荐位名称", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="图片", align=2, sort=9)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@NotNull(message="排序不能为空")
	@ExcelField(title="排序", align=2, sort=10)
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}