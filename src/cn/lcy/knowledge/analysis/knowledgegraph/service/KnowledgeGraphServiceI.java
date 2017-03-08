package cn.lcy.knowledge.analysis.knowledgegraph.service;

import java.util.List;

import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.vo.KnowledgeGraphVO;

public interface KnowledgeGraphServiceI {
	
	/**
	 * 获取命名实体的知识图谱
	 * @param namedEntity
	 * @return
	 */
	public List<KnowledgeGraphVO> getKnowledgeGraphVO(List<PolysemantNamedEntity> polysemantEntities);
	
	
}
