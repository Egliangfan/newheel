/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.attachment.web;

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
import com.jeeplus.modules.attachment.entity.Attachment;
import com.jeeplus.modules.attachment.service.AttachmentService;

/**
 * 素材管理Controller
 * @author L-JH
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/attachment/attachment")
public class AttachmentController extends BaseController {

	@Autowired
	private AttachmentService attachmentService;
	
	@ModelAttribute
	public Attachment get(@RequestParam(required=false) String id) {
		Attachment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = attachmentService.get(id);
		}
		if (entity == null){
			entity = new Attachment();
		}
		return entity;
	}
	
	/**
	 * 素材列表页面
	 */
	@RequiresPermissions("attachment:attachment:list")
	@RequestMapping(value = {"list", ""})
	public String list(Attachment attachment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Attachment> page = attachmentService.findPage(new Page<Attachment>(request, response), attachment); 
		model.addAttribute("page", page);
		return "modules/attachment/attachmentList";
	}

	/**
	 * 查看，增加，编辑素材表单页面
	 */
	@RequiresPermissions(value={"attachment:attachment:view","attachment:attachment:add","attachment:attachment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Attachment attachment, Model model) {
		model.addAttribute("attachment", attachment);
		return "modules/attachment/attachmentForm";
	}

	/**
	 * 保存素材
	 */
	@RequiresPermissions(value={"attachment:attachment:add","attachment:attachment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Attachment attachment, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, attachment)){
			return form(attachment, model);
		}
		if(!attachment.getIsNewRecord()){//编辑表单保存
			Attachment t = attachmentService.get(attachment.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(attachment, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			attachmentService.save(t);//保存
		}else{//新增表单保存
			attachmentService.save(attachment);//保存
		}
		addMessage(redirectAttributes, "保存素材成功");
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
	}
	
	/**
	 * 删除素材
	 */
	@RequiresPermissions("attachment:attachment:del")
	@RequestMapping(value = "delete")
	public String delete(Attachment attachment, RedirectAttributes redirectAttributes) {
		attachmentService.delete(attachment);
		addMessage(redirectAttributes, "删除素材成功");
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
	}
	
	/**
	 * 批量删除素材
	 */
	@RequiresPermissions("attachment:attachment:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			attachmentService.delete(attachmentService.get(id));
		}
		addMessage(redirectAttributes, "删除素材成功");
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("attachment:attachment:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Attachment attachment, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "素材"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Attachment> page = attachmentService.findPage(new Page<Attachment>(request, response, -1), attachment);
    		new ExportExcel("素材", Attachment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出素材记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("attachment:attachment:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Attachment> list = ei.getDataList(Attachment.class);
			for (Attachment attachment : list){
				try{
					attachmentService.save(attachment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条素材记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条素材记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入素材失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
    }
	
	/**
	 * 下载导入素材数据模板
	 */
	@RequiresPermissions("attachment:attachment:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "素材数据导入模板.xlsx";
    		List<Attachment> list = Lists.newArrayList(); 
    		new ExportExcel("素材数据", Attachment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/attachment/attachment/?repage";
    }
	
	
	

}