/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.massmassage.web;

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
import com.jeeplus.modules.massmassage.entity.MassMessage;
import com.jeeplus.modules.massmassage.service.MassMessageService;

/**
 * 群发消息Controller
 * @author L-JH
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/massmassage/massMessage")
public class MassMessageController extends BaseController {

	@Autowired
	private MassMessageService massMessageService;
	
	@ModelAttribute
	public MassMessage get(@RequestParam(required=false) String id) {
		MassMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = massMessageService.get(id);
		}
		if (entity == null){
			entity = new MassMessage();
		}
		return entity;
	}
	
	/**
	 * 群发信息列表页面
	 */
	@RequiresPermissions("massmassage:massMessage:list")
	@RequestMapping(value = {"list", ""})
	public String list(MassMessage massMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MassMessage> page = massMessageService.findPage(new Page<MassMessage>(request, response), massMessage); 
		model.addAttribute("page", page);
		return "modules/massmassage/massMessageList";
	}

	/**
	 * 查看，增加，编辑群发信息表单页面
	 */
	@RequiresPermissions(value={"massmassage:massMessage:view","massmassage:massMessage:add","massmassage:massMessage:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MassMessage massMessage, Model model) {
		model.addAttribute("massMessage", massMessage);
		return "modules/massmassage/massMessageForm";
	}

	/**
	 * 保存群发信息
	 */
	@RequiresPermissions(value={"massmassage:massMessage:add","massmassage:massMessage:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MassMessage massMessage, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, massMessage)){
			return form(massMessage, model);
		}
		if(!massMessage.getIsNewRecord()){//编辑表单保存
			MassMessage t = massMessageService.get(massMessage.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(massMessage, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			massMessageService.save(t);//保存
		}else{//新增表单保存
			massMessageService.save(massMessage);//保存
		}
		addMessage(redirectAttributes, "保存群发信息成功");
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
	}
	
	/**
	 * 删除群发信息
	 */
	@RequiresPermissions("massmassage:massMessage:del")
	@RequestMapping(value = "delete")
	public String delete(MassMessage massMessage, RedirectAttributes redirectAttributes) {
		massMessageService.delete(massMessage);
		addMessage(redirectAttributes, "删除群发信息成功");
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
	}
	
	/**
	 * 批量删除群发信息
	 */
	@RequiresPermissions("massmassage:massMessage:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			massMessageService.delete(massMessageService.get(id));
		}
		addMessage(redirectAttributes, "删除群发信息成功");
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("massmassage:massMessage:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MassMessage massMessage, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "群发信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MassMessage> page = massMessageService.findPage(new Page<MassMessage>(request, response, -1), massMessage);
    		new ExportExcel("群发信息", MassMessage.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出群发信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("massmassage:massMessage:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MassMessage> list = ei.getDataList(MassMessage.class);
			for (MassMessage massMessage : list){
				try{
					massMessageService.save(massMessage);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条群发信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条群发信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入群发信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
    }
	
	/**
	 * 下载导入群发信息数据模板
	 */
	@RequiresPermissions("massmassage:massMessage:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "群发信息数据导入模板.xlsx";
    		List<MassMessage> list = Lists.newArrayList(); 
    		new ExportExcel("群发信息数据", MassMessage.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/massmassage/massMessage/?repage";
    }
	
	
	

}