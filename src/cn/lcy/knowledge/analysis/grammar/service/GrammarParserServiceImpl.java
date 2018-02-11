package cn.lcy.knowledge.analysis.grammar.service;

import java.util.List;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.dependency.IDependencyParser;
import com.hankcs.hanlp.dependency.nnparser.NeuralNetworkDependencyParser;
import com.hankcs.hanlp.seg.common.Term;

public class GrammarParserServiceImpl implements GrammarParserServiceI {
	
	private volatile static GrammarParserServiceI singleInstance;
	
	/**
	 * 私有化构造方法，实现单例
	 */
	private GrammarParserServiceImpl() {}
	
	/**
	 * 获取单例
	 * @return
	 */
	public static GrammarParserServiceI getInstance() {
		if (singleInstance == null) {
			synchronized (GrammarParserServiceImpl.class) {
				if (singleInstance == null) {
					singleInstance = new GrammarParserServiceImpl();
				}
			}
		}
		return singleInstance;
	}
	
	/**
	 * 依存句法分析
	 */
	public CoNLLSentence dependencyParser(List<Term> terms) {
	    // 基于神经网络的高性能依存句法分析器
	    IDependencyParser parser = new NeuralNetworkDependencyParser().enableDeprelTranslator(false);
    	CoNLLSentence coNLLsentence = parser.parse(terms);
    	return coNLLsentence;
	}
}