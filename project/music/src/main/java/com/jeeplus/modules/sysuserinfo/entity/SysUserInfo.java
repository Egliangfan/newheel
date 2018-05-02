/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysuserinfo.entity;

import com.jeeplus.modules.sys.entity.User;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 个人信息管理Entity
 * @author chiweineng
 * @version 2018-04-25
 */
public class SysUserInfo extends DataEntity<SysUserInfo> {
	
	private static final long serialVersionUID = 1L;
	private User tuser;		// 所属用户
	private String projectCategory;		// 项目分类
	private String mobile;		// 联系电话
	private String linkman;		// 联系人
	private String serviceAddress;		// 服务地址
	private String serviceRegion;		// 服务地区
	private String logo;		// logo
	private String introducion;		// 简介
	private String priceRange;		// 价格范围
	
	public SysUserInfo() {
		super();
	}

	public SysUserInfo(String id){
		super(id);
	}

	@NotNull(message="所属用户不能为空")
	@ExcelField(title="所属用户", fieldType=User.class, value="tuser.name", align=2, sort=6)
	public User getTuser() {
		return tuser;
	}

	public void setTuser(User tuser) {
		this.tuser = tuser;
	}
	
	@ExcelField(title="项目分类", align=2, sort=7)
	public String getProjectCategory() {
		return projectCategory;
	}

	public void setProjectCategory(String projectCategory) {
		this.projectCategory = projectCategory;
	}
	
	@ExcelField(title="联系电话", align=2, sort=8)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@ExcelField(title="联系人", align=2, sort=9)
	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
	@ExcelField(title="服务地址", align=2, sort=10)
	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	@ExcelField(title="服务地区", align=2, sort=11)
	public String getServiceRegion() {
		return serviceRegion;
	}

	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}
	
	@ExcelField(title="logo", align=2, sort=12)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@ExcelField(title="简介", align=2, sort=13)
	public String getIntroducion() {
		return introducion;
	}

	public void setIntroducion(String introducion) {
		this.introducion = introducion;
	}
	
	@ExcelField(title="价格范围", align=2, sort=14)
	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}
	
}