package cn.lcy.knowledge.analysis.grammar.service;

import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

import cn.lcy.knowledge.analysis.vo.DependencyVO;

public interface GrammarParserServiceI {

	public CoNLLSentence dependencyParser(List<Term> terms);
	
	public DependencyVO getDependencyGraphVO(CoNLLSentence coNLLsentence);
	
}
