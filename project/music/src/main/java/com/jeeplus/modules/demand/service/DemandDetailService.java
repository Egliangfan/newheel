/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.demand.dao.DemandDetailDao;
import com.jeeplus.modules.demand.dao.RoleScheduleDao;
import com.jeeplus.modules.demand.entity.Demand;
import com.jeeplus.modules.demand.entity.DemandDetail;
import com.jeeplus.modules.demand.entity.RoleSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 需求细节Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class DemandDetailService extends CrudService<DemandDetailDao, DemandDetail> {

	@Autowired
	private RoleScheduleDao roleScheduleDao;
	
	public DemandDetail get(String id) {
		DemandDetail demandDetail = super.get(id);
		demandDetail.setRoleScheduleList(roleScheduleDao.findList(new RoleSchedule(demandDetail)));
		return demandDetail;
	}
	
	public List<DemandDetail> findList(DemandDetail demandDetail) {
		return super.findList(demandDetail);
	}
	
	public Page<DemandDetail> findPage(Page<DemandDetail> page, DemandDetail demandDetail) {
		return super.findPage(page, demandDetail);
	}
	
	@Transactional(readOnly = false)
	public void save(DemandDetail demandDetail) {
		super.save(demandDetail);
		for (RoleSchedule roleSchedule : demandDetail.getRoleScheduleList()){
			if (roleSchedule.getId() == null){
				continue;
			}
			if (RoleSchedule.DEL_FLAG_NORMAL.equals(roleSchedule.getDelFlag())){
				if (StringUtils.isBlank(roleSchedule.getId())){
					roleSchedule.setRole(demandDetail);
					roleSchedule.preInsert();
					roleScheduleDao.insert(roleSchedule);
				}else{
					roleSchedule.preUpdate();
					roleScheduleDao.update(roleSchedule);
				}
			}else{
				roleScheduleDao.delete(roleSchedule);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(DemandDetail demandDetail) {
		super.delete(demandDetail);
		roleScheduleDao.delete(new RoleSchedule(demandDetail));
	}

	public Page<Demand> findPageBydemand(Page<Demand> page, Demand demand) {
		demand.setPage(page);
		page.setList(dao.findListBydemand(demand));
		return page;
	}

	public Page<ColumnClassfy> findPageBycategory(Page<ColumnClassfy> page, ColumnClassfy category) {
		category.setPage(page);
		page.setList(dao.findListBycategory(category));
		return page;
	}
}