/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.massmassage.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.massmassage.entity.MassMessage;
import com.jeeplus.modules.massmassage.dao.MassMessageDao;

/**
 * 群发消息Service
 * @author L-JH
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class MassMessageService extends CrudService<MassMessageDao, MassMessage> {

	public MassMessage get(String id) {
		return super.get(id);
	}
	
	public List<MassMessage> findList(MassMessage massMessage) {
		return super.findList(massMessage);
	}
	
	public Page<MassMessage> findPage(Page<MassMessage> page, MassMessage massMessage) {
		return super.findPage(page, massMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(MassMessage massMessage) {
		super.save(massMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(MassMessage massMessage) {
		super.delete(massMessage);
	}
	
	
	
	
}