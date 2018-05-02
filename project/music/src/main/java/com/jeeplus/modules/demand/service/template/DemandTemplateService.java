/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.service.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.demand.entity.template.DemandTemplate;
import com.jeeplus.modules.demand.dao.template.DemandTemplateDao;
import com.jeeplus.modules.demand.entity.template.DemandTemplateDetail;
import com.jeeplus.modules.demand.dao.template.DemandTemplateDetailDao;

/**
 * 需求模板Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class DemandTemplateService extends CrudService<DemandTemplateDao, DemandTemplate> {

	@Autowired
	private DemandTemplateDetailDao demandTemplateDetailDao;
	
	public DemandTemplate get(String id) {
		DemandTemplate demandTemplate = super.get(id);
		demandTemplate.setDemandTemplateDetailList(demandTemplateDetailDao.findList(new DemandTemplateDetail(demandTemplate)));
		return demandTemplate;
	}
	
	public List<DemandTemplate> findList(DemandTemplate demandTemplate) {
		return super.findList(demandTemplate);
	}
	
	public Page<DemandTemplate> findPage(Page<DemandTemplate> page, DemandTemplate demandTemplate) {
		return super.findPage(page, demandTemplate);
	}
	
	@Transactional(readOnly = false)
	public void save(DemandTemplate demandTemplate) {
		super.save(demandTemplate);
		for (DemandTemplateDetail demandTemplateDetail : demandTemplate.getDemandTemplateDetailList()){
			if (demandTemplateDetail.getId() == null){
				continue;
			}
			if (DemandTemplateDetail.DEL_FLAG_NORMAL.equals(demandTemplateDetail.getDelFlag())){
				if (StringUtils.isBlank(demandTemplateDetail.getId())){
					demandTemplateDetail.setDemandTemplate(demandTemplate);
					demandTemplateDetail.preInsert();
					demandTemplateDetailDao.insert(demandTemplateDetail);
				}else{
					demandTemplateDetail.preUpdate();
					demandTemplateDetailDao.update(demandTemplateDetail);
				}
			}else{
				demandTemplateDetailDao.delete(demandTemplateDetail);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(DemandTemplate demandTemplate) {
		super.delete(demandTemplate);
		demandTemplateDetailDao.delete(new DemandTemplateDetail(demandTemplate));
	}
	
}