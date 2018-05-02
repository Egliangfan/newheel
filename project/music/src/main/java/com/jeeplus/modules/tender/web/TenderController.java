/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.demand.entity.Demand;
import com.jeeplus.modules.tender.entity.Tender;
import com.jeeplus.modules.tender.service.TenderService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 投标内容Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/tender/tender")
public class TenderController extends BaseController {

	@Autowired
	private TenderService tenderService;
	
	@ModelAttribute
	public Tender get(@RequestParam(required=false) String id) {
		Tender entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tenderService.get(id);
		}
		if (entity == null){
			entity = new Tender();
		}
		return entity;
	}
	
	/**
	 * 投标内容列表页面
	 */
	@RequiresPermissions("tender:tender:list")
	@RequestMapping(value = {"list", ""})
	public String list(Tender tender, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tender> page = tenderService.findPage(new Page<Tender>(request, response), tender); 
		model.addAttribute("page", page);
		return "modules/tender/tenderList";
	}

	/**
	 * 查看，增加，编辑投标内容表单页面
	 */
	@RequiresPermissions(value={"tender:tender:view","tender:tender:add","tender:tender:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Tender tender, Model model) {
		model.addAttribute("tender", tender);
		return "modules/tender/tenderForm";
	}

	/**
	 * 保存投标内容
	 */
	@RequiresPermissions(value={"tender:tender:add","tender:tender:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Tender tender, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tender)){
			return form(tender, model);
		}
		if(!tender.getIsNewRecord()){//编辑表单保存
			Tender t = tenderService.get(tender.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tender, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tenderService.save(t);//保存
		}else{//新增表单保存
			tenderService.save(tender);//保存
		}
		addMessage(redirectAttributes, "保存投标内容成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
	}
	
	/**
	 * 删除投标内容
	 */
	@RequiresPermissions("tender:tender:del")
	@RequestMapping(value = "delete")
	public String delete(Tender tender, RedirectAttributes redirectAttributes) {
		tenderService.delete(tender);
		addMessage(redirectAttributes, "删除投标内容成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
	}
	
	/**
	 * 批量删除投标内容
	 */
	@RequiresPermissions("tender:tender:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tenderService.delete(tenderService.get(id));
		}
		addMessage(redirectAttributes, "删除投标内容成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tender:tender:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Tender tender, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标内容"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Tender> page = tenderService.findPage(new Page<Tender>(request, response, -1), tender);
    		new ExportExcel("投标内容", Tender.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出投标内容记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tender:tender:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Tender> list = ei.getDataList(Tender.class);
			for (Tender tender : list){
				try{
					tenderService.save(tender);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条投标内容记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条投标内容记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入投标内容失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
    }
	
	/**
	 * 下载导入投标内容数据模板
	 */
	@RequiresPermissions("tender:tender:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标内容数据导入模板.xlsx";
    		List<Tender> list = Lists.newArrayList(); 
    		new ExportExcel("投标内容数据", Tender.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tender/?repage";
    }
	
	
	/**
	 * 选择需求项
	 */
	@RequestMapping(value = "selectdemand")
	public String selectdemand(Demand demand, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Demand> page = tenderService.findPageBydemand(new Page<Demand>(request, response),  demand);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", demand);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}