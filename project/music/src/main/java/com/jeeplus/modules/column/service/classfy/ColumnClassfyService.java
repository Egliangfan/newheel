/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.column.service.classfy;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.column.entity.classfy.ColumnClassfy;
import com.jeeplus.modules.column.dao.classfy.ColumnClassfyDao;

/**
 * 分类管理Service
 * @author M1n9
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class ColumnClassfyService extends TreeService<ColumnClassfyDao, ColumnClassfy> {

	public ColumnClassfy get(String id) {
		return super.get(id);
	}
	
	public List<ColumnClassfy> findList(ColumnClassfy columnClassfy) {
		if (StringUtils.isNotBlank(columnClassfy.getParentIds())){
			columnClassfy.setParentIds(","+columnClassfy.getParentIds()+",");
		}
		return super.findList(columnClassfy);
	}
	
	@Transactional(readOnly = false)
	public void save(ColumnClassfy columnClassfy) {
		super.save(columnClassfy);
	}
	
	@Transactional(readOnly = false)
	public void delete(ColumnClassfy columnClassfy) {
		super.delete(columnClassfy);
	}
	
}