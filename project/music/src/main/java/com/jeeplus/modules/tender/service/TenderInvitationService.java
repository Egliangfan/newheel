/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.tender.dao.TenderInvitationDao;
import com.jeeplus.modules.tender.dao.TenderScheduleDao;
import com.jeeplus.modules.tender.dao.TenderSubmissionDao;
import com.jeeplus.modules.tender.entity.Tender;
import com.jeeplus.modules.tender.entity.TenderInvitation;
import com.jeeplus.modules.tender.entity.TenderSchedule;
import com.jeeplus.modules.tender.entity.TenderSubmission;
import com.jeeplus.modules.workplace.entity.Workplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投标管理Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class TenderInvitationService extends CrudService<TenderInvitationDao, TenderInvitation> {

	@Autowired
	private TenderScheduleDao tenderScheduleDao;
	@Autowired
	private TenderSubmissionDao tenderSubmissionDao;
	
	public TenderInvitation get(String id) {
		TenderInvitation tenderInvitation = super.get(id);
		tenderInvitation.setTenderScheduleList(tenderScheduleDao.findList(new TenderSchedule(tenderInvitation)));
		tenderInvitation.setTenderSubmissionList(tenderSubmissionDao.findList(new TenderSubmission(tenderInvitation)));
		return tenderInvitation;
	}
	
	public List<TenderInvitation> findList(TenderInvitation tenderInvitation) {
		return super.findList(tenderInvitation);
	}
	
	public Page<TenderInvitation> findPage(Page<TenderInvitation> page, TenderInvitation tenderInvitation) {
		return super.findPage(page, tenderInvitation);
	}
	
	@Transactional(readOnly = false)
	public void save(TenderInvitation tenderInvitation) {
		super.save(tenderInvitation);
		for (TenderSchedule tenderSchedule : tenderInvitation.getTenderScheduleList()){
			if (tenderSchedule.getId() == null){
				continue;
			}
			if (TenderSchedule.DEL_FLAG_NORMAL.equals(tenderSchedule.getDelFlag())){
				if (StringUtils.isBlank(tenderSchedule.getId())){
					tenderSchedule.setTender(tenderInvitation);
					tenderSchedule.preInsert();
					tenderScheduleDao.insert(tenderSchedule);
				}else{
					tenderSchedule.preUpdate();
					tenderScheduleDao.update(tenderSchedule);
				}
			}else{
				tenderScheduleDao.delete(tenderSchedule);
			}
		}
		for (TenderSubmission tenderSubmission : tenderInvitation.getTenderSubmissionList()){
			if (tenderSubmission.getId() == null){
				continue;
			}
			if (TenderSubmission.DEL_FLAG_NORMAL.equals(tenderSubmission.getDelFlag())){
				if (StringUtils.isBlank(tenderSubmission.getId())){
					tenderSubmission.setTender(tenderInvitation);
					tenderSubmission.preInsert();
					tenderSubmissionDao.insert(tenderSubmission);
				}else{
					tenderSubmission.preUpdate();
					tenderSubmissionDao.update(tenderSubmission);
				}
			}else{
				tenderSubmissionDao.delete(tenderSubmission);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TenderInvitation tenderInvitation) {
		super.delete(tenderInvitation);
		tenderScheduleDao.delete(new TenderSchedule(tenderInvitation));
		tenderSubmissionDao.delete(new TenderSubmission(tenderInvitation));
	}

	public Page<Tender> findPageBytender(Page<Tender> page, Tender tender) {
		tender.setPage(page);
		page.setList(dao.findListBytender(tender));
		return page;
	}

	public Page<ColumnClassfy> findPageBycolum(Page<ColumnClassfy> page, ColumnClassfy colum) {
		colum.setPage(page);
		page.setList(dao.findListBycolum(colum));
		return page;
	}

	public Page<Workplace> findPageByworkplace(Page<Workplace> page, Workplace workplace) {
		workplace.setPage(page);
		page.setList(dao.findListBylocation(workplace));
		return page;
	}
}