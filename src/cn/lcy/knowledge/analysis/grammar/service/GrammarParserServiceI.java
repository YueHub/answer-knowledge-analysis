package cn.lcy.knowledge.analysis.grammar.service;

import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

public interface GrammarParserServiceI {

	public CoNLLSentence dependencyParser(List<Term> terms);
	
}
