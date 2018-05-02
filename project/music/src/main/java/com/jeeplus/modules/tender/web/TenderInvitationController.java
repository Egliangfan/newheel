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
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.tender.entity.Tender;
import com.jeeplus.modules.tender.entity.TenderInvitation;
import com.jeeplus.modules.tender.service.TenderInvitationService;
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
 * 投标管理Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/tender/tenderInvitation")
public class TenderInvitationController extends BaseController {

	@Autowired
	private TenderInvitationService tenderInvitationService;
	
	@ModelAttribute
	public TenderInvitation get(@RequestParam(required=false) String id) {
		TenderInvitation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tenderInvitationService.get(id);
		}
		if (entity == null){
			entity = new TenderInvitation();
		}
		return entity;
	}
	
	/**
	 * 投标管理列表页面
	 */
	@RequiresPermissions("tender:tenderInvitation:list")
	@RequestMapping(value = {"list", ""})
	public String list(TenderInvitation tenderInvitation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TenderInvitation> page = tenderInvitationService.findPage(new Page<TenderInvitation>(request, response), tenderInvitation); 
		model.addAttribute("page", page);
		return "modules/tender/tenderInvitationList";
	}

	/**
	 * 查看，增加，编辑投标管理表单页面
	 */
	@RequiresPermissions(value={"tender:tenderInvitation:view","tender:tenderInvitation:add","tender:tenderInvitation:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TenderInvitation tenderInvitation, Model model) {
		model.addAttribute("tenderInvitation", tenderInvitation);
		return "modules/tender/tenderInvitationForm";
	}

	/**
	 * 保存投标管理
	 */
	@RequiresPermissions(value={"tender:tenderInvitation:add","tender:tenderInvitation:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TenderInvitation tenderInvitation, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, tenderInvitation)){
			return form(tenderInvitation, model);
		}
		if(!tenderInvitation.getIsNewRecord()){//编辑表单保存
			TenderInvitation t = tenderInvitationService.get(tenderInvitation.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(tenderInvitation, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			tenderInvitationService.save(t);//保存
		}else{//新增表单保存
			tenderInvitationService.save(tenderInvitation);//保存
		}
		addMessage(redirectAttributes, "保存投标管理成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
	}
	
	/**
	 * 删除投标管理
	 */
	@RequiresPermissions("tender:tenderInvitation:del")
	@RequestMapping(value = "delete")
	public String delete(TenderInvitation tenderInvitation, RedirectAttributes redirectAttributes) {
		tenderInvitationService.delete(tenderInvitation);
		addMessage(redirectAttributes, "删除投标管理成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
	}
	
	/**
	 * 批量删除投标管理
	 */
	@RequiresPermissions("tender:tenderInvitation:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			tenderInvitationService.delete(tenderInvitationService.get(id));
		}
		addMessage(redirectAttributes, "删除投标管理成功");
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tender:tenderInvitation:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TenderInvitation tenderInvitation, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TenderInvitation> page = tenderInvitationService.findPage(new Page<TenderInvitation>(request, response, -1), tenderInvitation);
    		new ExportExcel("投标管理", TenderInvitation.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出投标管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tender:tenderInvitation:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TenderInvitation> list = ei.getDataList(TenderInvitation.class);
			for (TenderInvitation tenderInvitation : list){
				try{
					tenderInvitationService.save(tenderInvitation);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条投标管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条投标管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入投标管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
    }
	
	/**
	 * 下载导入投标管理数据模板
	 */
	@RequiresPermissions("tender:tenderInvitation:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "投标管理数据导入模板.xlsx";
    		List<TenderInvitation> list = Lists.newArrayList(); 
    		new ExportExcel("投标管理数据", TenderInvitation.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tender/tenderInvitation/?repage";
    }
	
	
	/**
	 * 选择投标项
	 */
	@RequestMapping(value = "selecttender")
	public String selecttender(Tender tender, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tender> page = tenderInvitationService.findPageBytender(new Page<Tender>(request, response),  tender);
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
		model.addAttribute("obj", tender);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择栏目
	 */
	@RequestMapping(value = "selectcolum")
	public String selectcolum(ColumnClassfy colum, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ColumnClassfy> page = tenderInvitationService.findPageBycolum(new Page<ColumnClassfy>(request, response),  colum);
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
		model.addAttribute("obj", colum);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}

	/**
	 * 选择场地信息
	 */
	@RequestMapping(value = "selectworkplace")
	public String selectworkplace(Workplace workplace, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Workplace> page = tenderInvitationService.findPageByworkplace(new Page<Workplace>(request, response),  workplace);
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