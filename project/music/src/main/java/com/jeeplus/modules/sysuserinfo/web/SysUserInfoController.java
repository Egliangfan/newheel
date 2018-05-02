/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sysuserinfo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.sysuserinfo.entity.SysUserInfo;
import com.jeeplus.modules.sysuserinfo.service.SysUserInfoService;

/**
 * 个人信息管理Controller
 * @author chiweineng
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sysuserinfo/sysUserInfo")
public class SysUserInfoController extends BaseController {

	@Autowired
	private SysUserInfoService sysUserInfoService;
	
	@ModelAttribute
	public SysUserInfo get(@RequestParam(required=false) String id) {
		SysUserInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysUserInfoService.get(id);
		}
		if (entity == null){
			entity = new SysUserInfo();
		}
		return entity;
	}
	
	/**
	 * 个人信息列表页面
	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysUserInfo sysUserInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysUserInfo> page = sysUserInfoService.findPage(new Page<SysUserInfo>(request, response), sysUserInfo); 
		model.addAttribute("page", page);
		return "modules/sysuserinfo/sysUserInfoList";
	}

	/**
	 * 查看，增加，编辑个人信息表单页面
	 */
	@RequiresPermissions(value={"sysuserinfo:sysUserInfo:view","sysuserinfo:sysUserInfo:add","sysuserinfo:sysUserInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysUserInfo sysUserInfo, Model model) {
		model.addAttribute("sysUserInfo", sysUserInfo);
		return "modules/sysuserinfo/sysUserInfoForm";
	}

	/**
	 * 保存个人信息
	 */
	@RequiresPermissions(value={"sysuserinfo:sysUserInfo:add","sysuserinfo:sysUserInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(SysUserInfo sysUserInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, sysUserInfo)){
			return form(sysUserInfo, model);
		}
		if(!sysUserInfo.getIsNewRecord()){//编辑表单保存
			SysUserInfo t = sysUserInfoService.get(sysUserInfo.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(sysUserInfo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			sysUserInfoService.save(t);//保存
		}else{//新增表单保存
			sysUserInfoService.save(sysUserInfo);//保存
		}
		addMessage(redirectAttributes, "保存个人信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
	}
	
	/**
	 * 删除个人信息
	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:del")
	@RequestMapping(value = "delete")
	public String delete(SysUserInfo sysUserInfo, RedirectAttributes redirectAttributes) {
		sysUserInfoService.delete(sysUserInfo);
		addMessage(redirectAttributes, "删除个人信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
	}
	
	/**
	 * 批量删除个人信息
	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			sysUserInfoService.delete(sysUserInfoService.get(id));
		}
		addMessage(redirectAttributes, "删除个人信息成功");
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(SysUserInfo sysUserInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "个人信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SysUserInfo> page = sysUserInfoService.findPage(new Page<SysUserInfo>(request, response, -1), sysUserInfo);
    		new ExportExcel("个人信息", SysUserInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出个人信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SysUserInfo> list = ei.getDataList(SysUserInfo.class);
			for (SysUserInfo sysUserInfo : list){
				try{
					sysUserInfoService.save(sysUserInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条个人信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条个人信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入个人信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
    }
	
	/**
	 * 下载导入个人信息数据模板
	 */
	@RequiresPermissions("sysuserinfo:sysUserInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "个人信息数据导入模板.xlsx";
    		List<SysUserInfo> list = Lists.newArrayList(); 
    		new ExportExcel("个人信息数据", SysUserInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/sysuserinfo/sysUserInfo/?repage";
    }
	
	
	

}