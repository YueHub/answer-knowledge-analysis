package cn.lcy.knowledge.analysis.seg.service;

import cn.lcy.knowledge.analysis.sem.model.WordSegmentResult;

/**
 * @author NarutoKu
 *
 */
public interface WordSegmentationServiceI {
	
	/**
	 * 分词处理以及命名实体识别
	 * @param question
	 * @return
	 */
	public WordSegmentResult wordSegmentation(String question);
	
}
