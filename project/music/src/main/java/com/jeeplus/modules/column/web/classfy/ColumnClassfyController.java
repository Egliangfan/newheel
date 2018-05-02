/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.web.classfy;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.column.service.classfy.ColumnClassfyService;

/**
 * 分类管理Controller
 * @author M1n9
 * @version 2018-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/column/classfy/columnClassfy")
public class ColumnClassfyController extends BaseController {

	@Autowired
	private ColumnClassfyService columnClassfyService;
	
	@ModelAttribute
	public ColumnClassfy get(@RequestParam(required=false) String id) {
		ColumnClassfy entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = columnClassfyService.get(id);
		}
		if (entity == null){
			entity = new ColumnClassfy();
		}
		return entity;
	}
	
	/**
	 * 分类管理列表页面
	 */
	@RequiresPermissions("column:classfy:columnClassfy:list")
	@RequestMapping(value = {"list", ""})
	public String list(ColumnClassfy columnClassfy, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ColumnClassfy> list = columnClassfyService.findList(columnClassfy); 
		model.addAttribute("list", list);
		return "modules/column/classfy/columnClassfyList";
	}

	/**
	 * 查看，增加，编辑分类管理表单页面
	 */
	@RequiresPermissions(value={"column:classfy:columnClassfy:view","column:classfy:columnClassfy:add","column:classfy:columnClassfy:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ColumnClassfy columnClassfy, Model model) {
		if (columnClassfy.getParent()!=null && StringUtils.isNotBlank(columnClassfy.getParent().getId())){
			columnClassfy.setParent(columnClassfyService.get(columnClassfy.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(columnClassfy.getId())){
				ColumnClassfy columnClassfyChild = new ColumnClassfy();
				columnClassfyChild.setParent(new ColumnClassfy(columnClassfy.getParent().getId()));
				List<ColumnClassfy> list = columnClassfyService.findList(columnClassfy); 
				if (list.size() > 0){
					columnClassfy.setSort(list.get(list.size()-1).getSort());
					if (columnClassfy.getSort() != null){
						columnClassfy.setSort(columnClassfy.getSort() + 30);
					}
				}
			}
		}
		if (columnClassfy.getSort() == null){
			columnClassfy.setSort(30);
		}
		model.addAttribute("columnClassfy", columnClassfy);
		return "modules/column/classfy/columnClassfyForm";
	}

	/**
	 * 保存分类管理
	 */
	@RequiresPermissions(value={"column:classfy:columnClassfy:add","column:classfy:columnClassfy:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ColumnClassfy columnClassfy, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, columnClassfy)){
			return form(columnClassfy, model);
		}
		if(!columnClassfy.getIsNewRecord()){//编辑表单保存
			ColumnClassfy t = columnClassfyService.get(columnClassfy.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(columnClassfy, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			columnClassfyService.save(t);//保存
		}else{//新增表单保存
			columnClassfyService.save(columnClassfy);//保存
		}
		addMessage(redirectAttributes, "保存分类管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/classfy/columnClassfy/?repage";
	}
	
	/**
	 * 删除分类管理
	 */
	@RequiresPermissions("column:classfy:columnClassfy:del")
	@RequestMapping(value = "delete")
	public String delete(ColumnClassfy columnClassfy, RedirectAttributes redirectAttributes) {
		columnClassfyService.delete(columnClassfy);
		addMessage(redirectAttributes, "删除分类管理成功");
		return "redirect:"+Global.getAdminPath()+"/column/classfy/columnClassfy/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ColumnClassfy> list = columnClassfyService.findList(new ColumnClassfy());
		for (int i=0; i<list.size(); i++){
			ColumnClassfy e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}