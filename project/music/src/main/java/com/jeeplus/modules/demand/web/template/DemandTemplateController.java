/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.web.template;

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
import com.jeeplus.modules.demand.entity.template.DemandTemplate;
import com.jeeplus.modules.demand.service.template.DemandTemplateService;

/**
 * 需求模板Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/demand/template/demandTemplate")
public class DemandTemplateController extends BaseController {

	@Autowired
	private DemandTemplateService demandTemplateService;
	
	@ModelAttribute
	public DemandTemplate get(@RequestParam(required=false) String id) {
		DemandTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = demandTemplateService.get(id);
		}
		if (entity == null){
			entity = new DemandTemplate();
		}
		return entity;
	}
	
	/**
	 * 需求模板列表页面
	 */
	@RequiresPermissions("demand:template:demandTemplate:list")
	@RequestMapping(value = {"list", ""})
	public String list(DemandTemplate demandTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DemandTemplate> page = demandTemplateService.findPage(new Page<DemandTemplate>(request, response), demandTemplate); 
		model.addAttribute("page", page);
		return "modules/demand/template/demandTemplateList";
	}

	/**
	 * 查看，增加，编辑需求模板表单页面
	 */
	@RequiresPermissions(value={"demand:template:demandTemplate:view","demand:template:demandTemplate:add","demand:template:demandTemplate:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DemandTemplate demandTemplate, Model model) {
		model.addAttribute("demandTemplate", demandTemplate);
		return "modules/demand/template/demandTemplateForm";
	}

	/**
	 * 保存需求模板
	 */
	@RequiresPermissions(value={"demand:template:demandTemplate:add","demand:template:demandTemplate:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DemandTemplate demandTemplate, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, demandTemplate)){
			return form(demandTemplate, model);
		}
		if(!demandTemplate.getIsNewRecord()){//编辑表单保存
			DemandTemplate t = demandTemplateService.get(demandTemplate.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(demandTemplate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			demandTemplateService.save(t);//保存
		}else{//新增表单保存
			demandTemplateService.save(demandTemplate);//保存
		}
		addMessage(redirectAttributes, "保存需求模板成功");
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
	}
	
	/**
	 * 删除需求模板
	 */
	@RequiresPermissions("demand:template:demandTemplate:del")
	@RequestMapping(value = "delete")
	public String delete(DemandTemplate demandTemplate, RedirectAttributes redirectAttributes) {
		demandTemplateService.delete(demandTemplate);
		addMessage(redirectAttributes, "删除需求模板成功");
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
	}
	
	/**
	 * 批量删除需求模板
	 */
	@RequiresPermissions("demand:template:demandTemplate:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			demandTemplateService.delete(demandTemplateService.get(id));
		}
		addMessage(redirectAttributes, "删除需求模板成功");
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("demand:template:demandTemplate:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DemandTemplate demandTemplate, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求模板"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DemandTemplate> page = demandTemplateService.findPage(new Page<DemandTemplate>(request, response, -1), demandTemplate);
    		new ExportExcel("需求模板", DemandTemplate.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出需求模板记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("demand:template:demandTemplate:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DemandTemplate> list = ei.getDataList(DemandTemplate.class);
			for (DemandTemplate demandTemplate : list){
				try{
					demandTemplateService.save(demandTemplate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条需求模板记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条需求模板记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入需求模板失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
    }
	
	/**
	 * 下载导入需求模板数据模板
	 */
	@RequiresPermissions("demand:template:demandTemplate:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求模板数据导入模板.xlsx";
    		List<DemandTemplate> list = Lists.newArrayList(); 
    		new ExportExcel("需求模板数据", DemandTemplate.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/template/demandTemplate/?repage";
    }
	
	
	

}