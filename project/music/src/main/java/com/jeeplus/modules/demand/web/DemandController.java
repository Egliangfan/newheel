/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.web;

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
import com.jeeplus.modules.demand.service.DemandService;
import com.jeeplus.modules.workplace.entity.Workplace;
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
 * 需求管理Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/demand/demand")
public class DemandController extends BaseController {

	@Autowired
	private DemandService demandService;
	
	@ModelAttribute
	public Demand get(@RequestParam(required=false) String id) {
		Demand entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = demandService.get(id);
		}
		if (entity == null){
			entity = new Demand();
		}
		return entity;
	}
	
	/**
	 * 需求管理列表页面
	 */
	@RequiresPermissions("demand:demand:list")
	@RequestMapping(value = {"list", ""})
	public String list(Demand demand, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Demand> page = demandService.findPage(new Page<Demand>(request, response), demand); 
		model.addAttribute("page", page);
		return "modules/demand/demandList";
	}

	/**
	 * 查看，增加，编辑需求管理表单页面
	 */
	@RequiresPermissions(value={"demand:demand:view","demand:demand:add","demand:demand:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Demand demand, Model model) {
		model.addAttribute("demand", demand);
		return "modules/demand/demandForm";
	}

	/**
	 * 保存需求管理
	 */
	@RequiresPermissions(value={"demand:demand:add","demand:demand:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Demand demand, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, demand)){
			return form(demand, model);
		}
		if(!demand.getIsNewRecord()){//编辑表单保存
			Demand t = demandService.get(demand.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(demand, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			demandService.save(t);//保存
		}else{//新增表单保存
			demandService.save(demand);//保存
		}
		addMessage(redirectAttributes, "保存需求管理成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
	}
	
	/**
	 * 删除需求管理
	 */
	@RequiresPermissions("demand:demand:del")
	@RequestMapping(value = "delete")
	public String delete(Demand demand, RedirectAttributes redirectAttributes) {
		demandService.delete(demand);
		addMessage(redirectAttributes, "删除需求管理成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
	}
	
	/**
	 * 批量删除需求管理
	 */
	@RequiresPermissions("demand:demand:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			demandService.delete(demandService.get(id));
		}
		addMessage(redirectAttributes, "删除需求管理成功");
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("demand:demand:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Demand demand, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Demand> page = demandService.findPage(new Page<Demand>(request, response, -1), demand);
    		new ExportExcel("需求管理", Demand.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出需求管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("demand:demand:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Demand> list = ei.getDataList(Demand.class);
			for (Demand demand : list){
				try{
					demandService.save(demand);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条需求管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条需求管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入需求管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
    }
	
	/**
	 * 下载导入需求管理数据模板
	 */
	@RequiresPermissions("demand:demand:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "需求管理数据导入模板.xlsx";
    		List<Demand> list = Lists.newArrayList(); 
    		new ExportExcel("需求管理数据", Demand.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/demand/?repage";
    }
	
	
	/**
	 * 选择场地信息
	 */
	@RequestMapping(value = "selectworkplace")
	public String selectworkplace(Workplace workplace, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Workplace> page = demandService.findPageByworkplace(new Page<Workplace>(request, response),  workplace);
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
		model.addAttribute("obj", workplace);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}