/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.demand.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.demand.dao.DemandDao;
import com.jeeplus.modules.demand.dao.DemandDetailDao;
import com.jeeplus.modules.demand.dao.DemandScheduleDao;
import com.jeeplus.modules.demand.entity.Demand;
import com.jeeplus.modules.demand.entity.DemandDetail;
import com.jeeplus.modules.demand.entity.DemandSchedule;
import com.jeeplus.modules.workplace.entity.Workplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 需求管理Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class DemandService extends CrudService<DemandDao, Demand> {

	@Autowired
	private DemandDetailDao demandDetailDao;
	@Autowired
	private DemandScheduleDao demandScheduleDao;
	
	public Demand get(String id) {
		Demand demand = super.get(id);
		demand.setDemandDetailList(demandDetailDao.findList(new DemandDetail(demand)));
		demand.setDemandScheduleList(demandScheduleDao.findList(new DemandSchedule(demand)));
		return demand;
	}
	
	public List<Demand> findList(Demand demand) {
		return super.findList(demand);
	}
	
	public Page<Demand> findPage(Page<Demand> page, Demand demand) {
		return super.findPage(page, demand);
	}
	
	@Transactional(readOnly = false)
	public void save(Demand demand) {
		super.save(demand);
		for (DemandDetail demandDetail : demand.getDemandDetailList()){
			if (demandDetail.getId() == null){
				continue;
			}
			if (DemandDetail.DEL_FLAG_NORMAL.equals(demandDetail.getDelFlag())){
				if (StringUtils.isBlank(demandDetail.getId())){
					demandDetail.setDemand(demand);
					demandDetail.preInsert();
					demandDetailDao.insert(demandDetail);
				}else{
					demandDetail.preUpdate();
					demandDetailDao.update(demandDetail);
				}
			}else{
				demandDetailDao.delete(demandDetail);
			}
		}
		for (DemandSchedule demandSchedule : demand.getDemandScheduleList()){
			if (demandSchedule.getId() == null){
				continue;
			}
			if (DemandSchedule.DEL_FLAG_NORMAL.equals(demandSchedule.getDelFlag())){
				if (StringUtils.isBlank(demandSchedule.getId())){
					demandSchedule.setDemand(demand);
					demandSchedule.preInsert();
					demandScheduleDao.insert(demandSchedule);
				}else{
					demandSchedule.preUpdate();
					demandScheduleDao.update(demandSchedule);
				}
			}else{
				demandScheduleDao.delete(demandSchedule);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Demand demand) {
		super.delete(demand);
		demandDetailDao.delete(new DemandDetail(demand));
		demandScheduleDao.delete(new DemandSchedule(demand));
	}

    public Page<Workplace> findPageByworkplace(Page<Workplace> page, Workplace workplace) {
		workplace.setPage(page);
		page.setList(dao.findListByworkplace(workplace));
		return page;
    }
}