/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.entity.recommend;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 推荐位管理Entity
 * @author M1n9
 * @version 2018-04-27
 */
public class Recommend extends DataEntity<Recommend> {
	
	private static final long serialVersionUID = 1L;
	private String tag;		// 推荐位标志
	private String name;		// 推荐位名称
	
	public Recommend() {
		super();
	}

	public Recommend(String id){
		super(id);
	}

	@ExcelField(title="推荐位标志", align=2, sort=6)
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@ExcelField(title="推荐位名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}