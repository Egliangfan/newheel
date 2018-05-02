/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.web.recommend;

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
import com.jeeplus.modules.column.entity.recommend.Recommend;
import com.jeeplus.modules.column.entity.recommend.RecommendContent;
import com.jeeplus.modules.column.service.recommend.RecommendContentService;
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
 * 推荐位内容管理Controller
 * @author M1n9
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/column/recommend/recommendContent")
public class RecommendContentController extends BaseController {

	@Autowired
	private RecommendContentService recommendContentService;
	
	@ModelAttribute
	public RecommendContent get(@RequestParam(required=false) String id) {
		RecommendContent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = recommendContentService.get(id);
		}
		if (entity == null){
			entity = new RecommendContent();
		}
		return entity;
	}
	
	/**
	 * 推荐位内容管理列表页面
	 */
	@RequiresPermissions("column:recommend:recommendContent:list")
	@RequestMapping(value = {"list", ""})
	public String list(RecommendContent recommendContent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RecommendContent> page = recommendContentService.findPage(new Page<RecommendContent>(request, response), recommendContent); 
		model.addAttribute("page", page);
		return "modules/column/recommend/recommendContentList";
	}

	/**
	 * 查看，增加，编辑推荐位内容管理表单页面
	 */
	@RequiresPermissions(value={"column:recommend:recommendContent:view","column:recommend:recommendContent:add","column:recommend:recommendContent:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RecommendContent recommendContent, Model model) {
		model.addAttribute("recommendContent", recommendContent);
		return "modules/column/recommend/recommendContentForm";
	}

	/**
	 * 保存推荐位内容管理
	 */
	@RequiresPermissions(value={"column:recommend:recommendContent:add","column:recommend:recommendContent:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RecommendContent recommendContent, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, recommendContent)){
			return form(recommendContent, model);
		}
		if(!recommendContent.getIsNewRecord()){//编辑表单保存
			RecommendContent t = recommendContentService.get(recommendContent.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(recommendContent, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			recommendContentService.save(t);//保存
		}else{//新增表单保存
			recommendContentService.save(recommendContent);//保存
		}
		addMessage(redirectAttributes, "保存推荐位内容管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
	}
	
	/**
	 * 删除推荐位内容管理
	 */
	@RequiresPermissions("column:recommend:recommendContent:del")
	@RequestMapping(value = "delete")
	public String delete(RecommendContent recommendContent, RedirectAttributes redirectAttributes) {
		recommendContentService.delete(recommendContent);
		addMessage(redirectAttributes, "删除推荐位内容管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
	}
	
	/**
	 * 批量删除推荐位内容管理
	 */
	@RequiresPermissions("column:recommend:recommendContent:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			recommendContentService.delete(recommendContentService.get(id));
		}
		addMessage(redirectAttributes, "删除推荐位内容管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("column:recommend:recommendContent:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RecommendContent recommendContent, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "推荐位内容管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RecommendContent> page = recommendContentService.findPage(new Page<RecommendContent>(request, response, -1), recommendContent);
    		new ExportExcel("推荐位内容管理", RecommendContent.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出推荐位内容管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("column:recommend:recommendContent:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RecommendContent> list = ei.getDataList(RecommendContent.class);
			for (RecommendContent recommendContent : list){
				try{
					recommendContentService.save(recommendContent);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条推荐位内容管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条推荐位内容管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入推荐位内容管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
    }
	
	/**
	 * 下载导入推荐位内容管理数据模板
	 */
	@RequiresPermissions("column:recommend:recommendContent:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "推荐位内容管理数据导入模板.xlsx";
    		List<RecommendContent> list = Lists.newArrayList(); 
    		new ExportExcel("推荐位内容管理数据", RecommendContent.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/column/recommend/recommendContent/?repage";
    }
	
	
	/**
	 * 选择推荐位
	 */
	@RequestMapping(value = "selectrecommend")
	public String selectrecommend(Recommend recommend, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Recommend> page = recommendContentService.findPageByrecommend(new Page<Recommend>(request, response),  recommend);
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
		model.addAttribute("obj", recommend);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择内容分类
	 */
	@RequestMapping(value = "selectclassfy")
	public String selectclassfy(ColumnClassfy classfy, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ColumnClassfy> page = recommendContentService.findPageByclassfy(new Page<ColumnClassfy>(request, response),  classfy);
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
		model.addAttribute("obj", classfy);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}