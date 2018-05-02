/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.jeeplus.modules.demand.entity.Demand;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.demand.entity.DemandDetail;
import com.jeeplus.modules.demand.service.DemandDetailService;

/**
 * 需求细节Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/demand/demandDetail")
public class DemandDetailController extends BaseController {

	@Autowired
	private DemandDetailService demandDetailService;
	
	@ModelAttribute
	public DemandDetail get(@RequestParam(required=false) String id) {
		DemandDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = demandDetailService.get(id);
		}
		if (entity == null){
			entity = new DemandDetail();
		}
		return entity;
	}
	
	/**
	 * 需求细节列表页面
	 */
	@RequiresPermissions("demand:demandDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list(DemandDetail demandDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DemandDetail> page = demandDetailService.findPage(new Page<DemandDetail>(request, response), demandDetail); 
		model.addAttribute("page", page);
		return "modules/demand/demandDetailList";
	}

	/**
	 * 查看，增加，编辑需求细节表单页面
	 */
	@RequiresPermissions(value={"demand:demandDetail:view","demand:demandDetail:add","demand:demandDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(DemandDetail demandDetail, Model model) {
		model.addAttribute("demandDetail", demandDetail);
		return "modules/demand/demandDetailForm";
	}

	/**
	 * 保存需求细节
	 */
	@RequiresPermissions(value={"demand:demandDetail:add","demand:demandDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(DemandDetail demandDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, demandDetail)){
			return form(demandDetail, model);
		}
		if(!demandDetail.getIsNewRecord()){//编辑表单保存
			DemandDetail t = demandDetailService.get(demandDetail.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(demandDetail, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			demandDetailService.save(t);//保存
		}else{//新增表单保存
			demandDetailService.save(demandDetail);//保存
		}
		addMessage(redirectAttributes, "保存需求细节成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
	}
	
	/**
	 * 删除需求细节
	 */
	@RequiresPermissions("demand:demandDetail:del")
	@RequestMapping(value = "delete")
	public String delete(DemandDetail demandDetail, RedirectAttributes redirectAttributes) {
		demandDetailService.delete(demandDetail);
		addMessage(redirectAttributes, "删除需求细节成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
	}
	
	/**
	 * 批量删除需求细节
	 */
	@RequiresPermissions("demand:demandDetail:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			demandDetailService.delete(demandDetailService.get(id));
		}
		addMessage(redirectAttributes, "删除需求细节成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("demand:demandDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DemandDetail demandDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求细节"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<DemandDetail> page = demandDetailService.findPage(new Page<DemandDetail>(request, response, -1), demandDetail);
    		new ExportExcel("需求细节", DemandDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出需求细节记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("demand:demandDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<DemandDetail> list = ei.getDataList(DemandDetail.class);
			for (DemandDetail demandDetail : list){
				try{
					demandDetailService.save(demandDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条需求细节记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条需求细节记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入需求细节失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
    }
	
	/**
	 * 下载导入需求细节数据模板
	 */
	@RequiresPermissions("demand:demandDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求细节数据导入模板.xlsx";
    		List<DemandDetail> list = Lists.newArrayList(); 
    		new ExportExcel("需求细节数据", DemandDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demandDetail/?repage";
    }
	
	
	/**
	 * 选择需求项
	 */
	@RequestMapping(value = "selectdemand")
	public String selectdemand(Demand demand, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Demand> page = demandDetailService.findPageBydemand(new Page<Demand>(request, response),  demand);
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
	/**
	 * 选择角色类型
	 */
	@RequestMapping(value = "selectcategory")
	public String selectcategory(ColumnClassfy category, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ColumnClassfy> page = demandDetailService.findPageBycategory(new Page<ColumnClassfy>(request, response),  category);
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
		model.addAttribute("obj", category);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}