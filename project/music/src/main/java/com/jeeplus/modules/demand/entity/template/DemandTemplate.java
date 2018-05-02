/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.entity.template;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 需求模板Entity
 * @author M1n9
 * @version 2018-04-29
 */
public class DemandTemplate extends DataEntity<DemandTemplate> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String version;		// 版本标识
	private ColumnClassfy category;		// 业务类型
	private String status;		// 状态
	private List<DemandTemplateDetail> demandTemplateDetailList = Lists.newArrayList();		// 子表列表
	
	public DemandTemplate() {
		super();
	}

	public DemandTemplate(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="版本标识", align=2, sort=2)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@NotNull(message="业务类型不能为空")
	@ExcelField(title="业务类型", align=2, sort=3)
	public ColumnClassfy getCategory() {
		return category;
	}

	public void setCategory(ColumnClassfy category) {
		this.category = category;
	}
	
	@ExcelField(title="状态", dictType="status", align=2, sort=10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public List<DemandTemplateDetail> getDemandTemplateDetailList() {
		return demandTemplateDetailList;
	}

	public void setDemandTemplateDetailList(List<DemandTemplateDetail> demandTemplateDetailList) {
		this.demandTemplateDetailList = demandTemplateDetailList;
	}
}