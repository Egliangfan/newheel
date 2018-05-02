/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tender.service;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.demand.entity.Demand;
import com.jeeplus.modules.tender.dao.TenderDao;
import com.jeeplus.modules.tender.dao.TenderInvitationDao;
import com.jeeplus.modules.tender.entity.Tender;
import com.jeeplus.modules.tender.entity.TenderInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 投标内容Service
 * @author M1n9
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class TenderService extends CrudService<TenderDao, Tender> {

	@Autowired
	private TenderInvitationDao tenderInvitationDao;
	
	public Tender get(String id) {
		Tender tender = super.get(id);
		tender.setTenderInvitationList(tenderInvitationDao.findList(new TenderInvitation(tender)));
		return tender;
	}
	
	public List<Tender> findList(Tender tender) {
		return super.findList(tender);
	}
	
	public Page<Tender> findPage(Page<Tender> page, Tender tender) {
		return super.findPage(page, tender);
	}
	
	@Transactional(readOnly = false)
	public void save(Tender tender) {
		super.save(tender);
		for (TenderInvitation tenderInvitation : tender.getTenderInvitationList()){
			if (tenderInvitation.getId() == null){
				continue;
			}
			if (TenderInvitation.DEL_FLAG_NORMAL.equals(tenderInvitation.getDelFlag())){
				if (StringUtils.isBlank(tenderInvitation.getId())){
					tenderInvitation.setTender(tender);
					tenderInvitation.preInsert();
					tenderInvitationDao.insert(tenderInvitation);
				}else{
					tenderInvitation.preUpdate();
					tenderInvitationDao.update(tenderInvitation);
				}
			}else{
				tenderInvitationDao.delete(tenderInvitation);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Tender tender) {
		super.delete(tender);
		tenderInvitationDao.delete(new TenderInvitation(tender));
	}

    public Page<Demand> findPageBydemand(Page<Demand> page, Demand demand) {
		demand.setPage(page);
		page.setList(dao.findListBydemand(demand));
		return page;
    }
}