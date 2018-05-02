/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.web.recommend;

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
import com.jeeplus.modules.column.entity.recommend.Recommend;
import com.jeeplus.modules.column.service.recommend.RecommendService;

/**
 * 推荐位管理Controller
 * @author M1n9
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/column/recommend/recommend")
public class RecommendController extends BaseController {

	@Autowired
	private RecommendService recommendService;
	
	@ModelAttribute
	public Recommend get(@RequestParam(required=false) String id) {
		Recommend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = recommendService.get(id);
		}
		if (entity == null){
			entity = new Recommend();
		}
		return entity;
	}
	
	/**
	 * 推荐位管理列表页面
	 */
	@RequiresPermissions("column:recommend:recommend:list")
	@RequestMapping(value = {"list", ""})
	public String list(Recommend recommend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Recommend> page = recommendService.findPage(new Page<Recommend>(request, response), recommend); 
		model.addAttribute("page", page);
		return "modules/column/recommend/recommendList";
	}

	/**
	 * 查看，增加，编辑推荐位管理表单页面
	 */
	@RequiresPermissions(value={"column:recommend:recommend:view","column:recommend:recommend:add","column:recommend:recommend:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Recommend recommend, Model model) {
		model.addAttribute("recommend", recommend);
		return "modules/column/recommend/recommendForm";
	}

	/**
	 * 保存推荐位管理
	 */
	@RequiresPermissions(value={"column:recommend:recommend:add","column:recommend:recommend:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Recommend recommend, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, recommend)){
			return form(recommend, model);
		}
		if(!recommend.getIsNewRecord()){//编辑表单保存
			Recommend t = recommendService.get(recommend.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(recommend, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			recommendService.save(t);//保存
		}else{//新增表单保存
			recommendService.save(recommend);//保存
		}
		addMessage(redirectAttributes, "保存推荐位管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
	}
	
	/**
	 * 删除推荐位管理
	 */
	@RequiresPermissions("column:recommend:recommend:del")
	@RequestMapping(value = "delete")
	public String delete(Recommend recommend, RedirectAttributes redirectAttributes) {
		recommendService.delete(recommend);
		addMessage(redirectAttributes, "删除推荐位管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
	}
	
	/**
	 * 批量删除推荐位管理
	 */
	@RequiresPermissions("column:recommend:recommend:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			recommendService.delete(recommendService.get(id));
		}
		addMessage(redirectAttributes, "删除推荐位管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("column:recommend:recommend:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Recommend recommend, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "推荐位管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Recommend> page = recommendService.findPage(new Page<Recommend>(request, response, -1), recommend);
    		new ExportExcel("推荐位管理", Recommend.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出推荐位管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("column:recommend:recommend:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Recommend> list = ei.getDataList(Recommend.class);
			for (Recommend recommend : list){
				try{
					recommendService.save(recommend);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条推荐位管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条推荐位管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入推荐位管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
    }
	
	/**
	 * 下载导入推荐位管理数据模板
	 */
	@RequiresPermissions("column:recommend:recommend:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "推荐位管理数据导入模板.xlsx";
    		List<Recommend> list = Lists.newArrayList(); 
    		new ExportExcel("推荐位管理数据", Recommend.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommend/?repage";
    }
	
	
	

}