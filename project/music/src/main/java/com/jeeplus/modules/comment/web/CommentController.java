/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.comment.web;

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

import com.jeeplus.modules.demand.entity.DemandDetail;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.comment.entity.Comment;
import com.jeeplus.modules.comment.service.CommentService;

/**
 * 评论区管理Controller
 * @author jeff
 * @version 2018-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/comment/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	
	@ModelAttribute
	public Comment get(@RequestParam(required=false) String id) {
		Comment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = commentService.get(id);
		}
		if (entity == null){
			entity = new Comment();
		}
		return entity;
	}
	
	/**
	 * 评论列表页面
	 */
	@RequiresPermissions("comment:comment:list")
	@RequestMapping(value = {"list", ""})
	public String list(Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Comment> page = commentService.findPage(new Page<Comment>(request, response), comment); 
		model.addAttribute("page", page);
		return "modules/comment/commentList";
	}

	/**
	 * 查看，增加，编辑评论表单页面
	 */
	@RequiresPermissions(value={"comment:comment:view","comment:comment:add","comment:comment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Comment comment, Model model) {
		model.addAttribute("comment", comment);
		return "modules/comment/commentForm";
	}

	/**
	 * 保存评论
	 */
	@RequiresPermissions(value={"comment:comment:add","comment:comment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Comment comment, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, comment)){
			return form(comment, model);
		}
		if(!comment.getIsNewRecord()){//编辑表单保存
			Comment t = commentService.get(comment.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(comment, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			commentService.save(t);//保存
		}else{//新增表单保存
			commentService.save(comment);//保存
		}
		addMessage(redirectAttributes, "保存评论成功");
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
	}
	
	/**
	 * 删除评论
	 */
	@RequiresPermissions("comment:comment:del")
	@RequestMapping(value = "delete")
	public String delete(Comment comment, RedirectAttributes redirectAttributes) {
		commentService.delete(comment);
		addMessage(redirectAttributes, "删除评论成功");
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
	}
	
	/**
	 * 批量删除评论
	 */
	@RequiresPermissions("comment:comment:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			commentService.delete(commentService.get(id));
		}
		addMessage(redirectAttributes, "删除评论成功");
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("comment:comment:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Comment comment, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Comment> page = commentService.findPage(new Page<Comment>(request, response, -1), comment);
    		new ExportExcel("评论", Comment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出评论记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("comment:comment:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Comment> list = ei.getDataList(Comment.class);
			for (Comment comment : list){
				try{
					commentService.save(comment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条评论记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条评论记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入评论失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
    }
	
	/**
	 * 下载导入评论数据模板
	 */
	@RequiresPermissions("comment:comment:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "评论数据导入模板.xlsx";
    		List<Comment> list = Lists.newArrayList(); 
    		new ExportExcel("评论数据", Comment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/comment/comment/?repage";
    }
	
	
	/**
	 * 选择具体需求
	 */
	@RequestMapping(value = "selectdemanddetail")
	public String selectdemanddetail(DemandDetail demanddetail, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DemandDetail> page = commentService.findPageBydemanddetail(new Page<DemandDetail>(request, response),  demanddetail);
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
		model.addAttribute("obj", demanddetail);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}