package cn.lcy.knowledge.analysis.namedentity.service;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;

public interface NamedEntityServiceI {
	
	/**
	 * 填充命名实体的相关属性
	 * @param namedEntity
	 * @return
	 */
	public boolean fillNamedEntities(List<PolysemantNamedEntity> namedEntity);
	
	
}
