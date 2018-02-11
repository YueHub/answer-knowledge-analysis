package cn.lcy.knowledge.analysis;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.seg.common.Term;

import cn.lcy.knowledge.analysis.grammar.service.GrammarParserServiceImpl;
import cn.lcy.knowledge.analysis.grammar.service.GrammarParserServiceI;
import cn.lcy.knowledge.analysis.namedentity.service.NamedEntityServiceImpl;
import cn.lcy.knowledge.analysis.namedentity.service.NamedEntityServiceI;
import cn.lcy.knowledge.analysis.ontology.query.service.QueryServiceImpl;
import cn.lcy.knowledge.analysis.ontology.query.service.QueryServiceI;
import cn.lcy.knowledge.analysis.seg.service.WordSegmentationServiceImpl;
import cn.lcy.knowledge.analysis.seg.service.WordSegmentationServiceI;
import cn.lcy.knowledge.analysis.sem.graph.service.SemanticGraphServiceImpl;
import cn.lcy.knowledge.analysis.sem.graph.service.SemanticGraphServiceI;
import cn.lcy.knowledge.analysis.sem.model.Answer;
import cn.lcy.knowledge.analysis.sem.model.AnswerStatement;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.sem.model.PolysemantStatement;
import cn.lcy.knowledge.analysis.sem.model.QueryResult;
import cn.lcy.knowledge.analysis.sem.model.SemanticGraph;
import cn.lcy.knowledge.analysis.sem.model.Word;
import cn.lcy.knowledge.analysis.sem.model.WordSegmentResult;

public class Main {
	
	public static void main(String args[]) {
		
		WordSegmentationServiceI wordSegmentationService = WordSegmentationServiceImpl.getInstance();
		NamedEntityServiceI namedEntityService = NamedEntityServiceImpl.getInstance();
		GrammarParserServiceI grammarParserService = GrammarParserServiceImpl.getInstance(); 
		SemanticGraphServiceI semanticGraphService = SemanticGraphServiceImpl.getInstance(); 
		QueryServiceI queryService = QueryServiceImpl.getInstance();
		
		// 第一步：HanLP分词
		WordSegmentResult wordSegmentResult = wordSegmentationService.wordSegmentation("美人鱼的导演？");
		List<Term> terms = wordSegmentResult.getTerms();
		List<PolysemantNamedEntity> polysemantNamedEntities = wordSegmentResult.getPolysemantEntities();
		List<Word> words = wordSegmentResult.getWords();
		System.out.println("HanLP分词的结果为:"+terms);
		
		// :查询本体库、取出命名实体的相关数据属性和对象属性
		namedEntityService.fillNamedEntities(polysemantNamedEntities);
		
		// 第二步：使用HanLP进行依存句法分析
		CoNLLSentence coNLLsentence = grammarParserService.dependencyParser(terms);
		System.out.println("HanLP依存语法解析结果：\n" + coNLLsentence);
		
		// 第三步：语义图构建
		SemanticGraph semanticGraph = semanticGraphService.buildSemanticGraph(coNLLsentence, polysemantNamedEntities);
		if(semanticGraph.getAllVertices().size() == 0) { // 说明没有语义图算法无法解析该问句
			semanticGraph = semanticGraphService.buildBackUpSemanticGraph(words);
		}
		
		// 第四步：语义图断言构建
		List<AnswerStatement> semanticStatements = queryService.createStatement(semanticGraph);
		
		// 第五步：获取歧义断言
		List<PolysemantStatement> polysemantStatements = queryService.createPolysemantStatements(semanticStatements);
		
		for(PolysemantStatement polysemantStatement : polysemantStatements) {
			// 第六步：实体消岐
			List<AnswerStatement> individualsDisambiguationStatements = queryService.individualsDisambiguation(polysemantStatement.getAnswerStatements());
			List<AnswerStatement> individualsDisambiguationStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : individualsDisambiguationStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				try {
					BeanUtils.copyProperties(subjectNew, subject);
					BeanUtils.copyProperties(predicateNew, predicate);
					BeanUtils.copyProperties(objectNew, object);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				individualsDisambiguationStatementsNew.add(answerStatementNew);
			}
			
			// 第七步：谓语消岐
			List<AnswerStatement> predicateDisambiguationStatements = queryService.predicateDisambiguation(individualsDisambiguationStatementsNew);
			List<AnswerStatement> predicateDisambiguationStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : predicateDisambiguationStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				try {
					BeanUtils.copyProperties(subjectNew, subject);
					BeanUtils.copyProperties(predicateNew, predicate);
					BeanUtils.copyProperties(objectNew, object);
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				predicateDisambiguationStatementsNew.add(answerStatementNew);
			}
			
			// 第八步：构造用于Jena查询的断言
			List<AnswerStatement> queryStatements = queryService.createQueryStatements(predicateDisambiguationStatementsNew);
			List<AnswerStatement> queryStatementsNew = new ArrayList<AnswerStatement>();
			for(AnswerStatement answerStatement : queryStatements) {
				Word subject = answerStatement.getSubject();
				Word predicate = answerStatement.getPredicate();
				Word object = answerStatement.getObject();
				Word subjectNew = new Word();
				Word predicateNew = new Word();
				Word objectNew = new Word();
				try {
					BeanUtils.copyProperties(subjectNew, subject);
					BeanUtils.copyProperties(predicateNew, predicate);
					BeanUtils.copyProperties(objectNew, object);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				AnswerStatement answerStatementNew = new AnswerStatement();
				answerStatementNew.setSubject(subjectNew);
				answerStatementNew.setPredicate(predicateNew);
				answerStatementNew.setObject(objectNew);
				queryStatementsNew.add(answerStatementNew);
			}
			
			// 第九步：根据查询断言构建查询语句
			List<String> SPARQLS = queryService.createSparqls(queryStatementsNew);
			List<QueryResult> queryResults = new ArrayList<QueryResult>();
			for(String SPARQL : SPARQLS) {
				// 执行查询语句
				QueryResult queryResult = queryService.queryOntology(SPARQL);
				List<Answer> answersNew = new ArrayList<Answer>();
				for(Answer answer : queryResult.getAnswers()) {
					String[] uuidArr = answer.getContent().split(":");
					String uuid = null;
					if(uuidArr.length > 1) {
						uuid = uuidArr[1];
					} else {
						uuid = uuidArr[0];
					}
					if(uuidArr.length <= 1 || answer.getContent().length() != 33) {
						answersNew.add(answer);
					} else {
						String comment = queryService.queryIndividualComment(uuid);
						Answer answerNew = new Answer();
						answerNew.setContent(comment);
						answersNew.add(answerNew);
					}
				}
				queryResult.setAnswers(answersNew);
				queryResults.add(queryResult);
			}
			
			List<PolysemantNamedEntity> activePolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
			int index = 0;
			for(AnswerStatement answerStatementNew : polysemantStatement.getAnswerStatements()) {
				PolysemantNamedEntity subjectActivePolysemantNamedEntity = answerStatementNew.getSubject().acquireActiveEntity();
				PolysemantNamedEntity objectActivePolysemantNamedEntity = answerStatementNew.getObject().acquireActiveEntity();
				if(index == 0) {
					activePolysemantNamedEntities.add(subjectActivePolysemantNamedEntity);
					activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
				} else {
					activePolysemantNamedEntities.add(objectActivePolysemantNamedEntity);
				}
			}
		}
	}
}
