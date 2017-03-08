package cn.lcy.knowledge.analysis.sem.graph.service;

import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;

import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.sem.model.SemanticGraph;
import cn.lcy.knowledge.analysis.sem.model.Word;
import cn.lcy.knowledge.analysis.vo.SemanticGraphVO;

public interface SemanticGraphServiceI {
	
	/**
	 * 创建语义图
	 * @param coNLLsentence
	 * @param polysemantNamedEntities
	 * @return
	 */
	public SemanticGraph buildSemanticGraph(CoNLLSentence coNLLsentence, List<PolysemantNamedEntity> polysemantNamedEntities);
	
	/**
	 * 创建备用语义图
	 * @param polysemantNamedEntities
	 * @return
	 */
	public SemanticGraph buildBackUpSemanticGraph(List<Word> words);
	
	/**
	 * 获取语义图 便于前端显示
	 * @param queryStatements
	 * @return
	 */
	public SemanticGraphVO getSemanticGraphVO(List<AnswerStatement> queryStatements);
	
	
}
