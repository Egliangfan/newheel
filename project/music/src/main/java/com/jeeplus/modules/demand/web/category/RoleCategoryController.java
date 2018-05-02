/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.web.category;

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
import com.jeeplus.modules.demand.entity.category.RoleCategory;
import com.jeeplus.modules.demand.service.category.RoleCategoryService;

/**
 * 角色类型Controller
 * @author M1n9
 * @version 2018-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/demand/category/roleCategory")
public class RoleCategoryController extends BaseController {

	@Autowired
	private RoleCategoryService roleCategoryService;
	
	@ModelAttribute
	public RoleCategory get(@RequestParam(required=false) String id) {
		RoleCategory entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = roleCategoryService.get(id);
		}
		if (entity == null){
			entity = new RoleCategory();
		}
		return entity;
	}
	
	/**
	 * 角色类型列表页面
	 */
	@RequiresPermissions("demand:category:roleCategory:list")
	@RequestMapping(value = {"list", ""})
	public String list(RoleCategory roleCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RoleCategory> page = roleCategoryService.findPage(new Page<RoleCategory>(request, response), roleCategory); 
		model.addAttribute("page", page);
		return "modules/demand/category/roleCategoryList";
	}

	/**
	 * 查看，增加，编辑角色类型表单页面
	 */
	@RequiresPermissions(value={"demand:category:roleCategory:view","demand:category:roleCategory:add","demand:category:roleCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RoleCategory roleCategory, Model model) {
		model.addAttribute("roleCategory", roleCategory);
		return "modules/demand/category/roleCategoryForm";
	}

	/**
	 * 保存角色类型
	 */
	@RequiresPermissions(value={"demand:category:roleCategory:add","demand:category:roleCategory:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RoleCategory roleCategory, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, roleCategory)){
			return form(roleCategory, model);
		}
		if(!roleCategory.getIsNewRecord()){//编辑表单保存
			RoleCategory t = roleCategoryService.get(roleCategory.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(roleCategory, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			roleCategoryService.save(t);//保存
		}else{//新增表单保存
			roleCategoryService.save(roleCategory);//保存
		}
		addMessage(redirectAttributes, "保存角色类型成功");
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
	}
	
	/**
	 * 删除角色类型
	 */
	@RequiresPermissions("demand:category:roleCategory:del")
	@RequestMapping(value = "delete")
	public String delete(RoleCategory roleCategory, RedirectAttributes redirectAttributes) {
		roleCategoryService.delete(roleCategory);
		addMessage(redirectAttributes, "删除角色类型成功");
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
	}
	
	/**
	 * 批量删除角色类型
	 */
	@RequiresPermissions("demand:category:roleCategory:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			roleCategoryService.delete(roleCategoryService.get(id));
		}
		addMessage(redirectAttributes, "删除角色类型成功");
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("demand:category:roleCategory:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RoleCategory roleCategory, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "角色类型"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RoleCategory> page = roleCategoryService.findPage(new Page<RoleCategory>(request, response, -1), roleCategory);
    		new ExportExcel("角色类型", RoleCategory.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出角色类型记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("demand:category:roleCategory:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RoleCategory> list = ei.getDataList(RoleCategory.class);
			for (RoleCategory roleCategory : list){
				try{
					roleCategoryService.save(roleCategory);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条角色类型记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条角色类型记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入角色类型失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
    }
	
	/**
	 * 下载导入角色类型数据模板
	 */
	@RequiresPermissions("demand:category:roleCategory:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "角色类型数据导入模板.xlsx";
    		List<RoleCategory> list = Lists.newArrayList(); 
    		new ExportExcel("角色类型数据", RoleCategory.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/demand/category/roleCategory/?repage";
    }
	
	
	

}