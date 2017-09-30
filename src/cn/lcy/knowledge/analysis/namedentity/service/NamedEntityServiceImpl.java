package cn.lcy.knowledge.analysis.namedentity.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import cn.lcy.knowledge.analysis.ontology.query.dao.QueryDAOImpl;
import cn.lcy.knowledge.analysis.ontology.query.dao.QueryDAOI;
import cn.lcy.knowledge.analysis.sem.model.PolysemantNamedEntity;
import cn.lcy.knowledge.analysis.util.StringHandle;

public class NamedEntityServiceImpl implements NamedEntityServiceI {

	private volatile static NamedEntityServiceI singleInstance;
	
	private static QueryDAOI queryDAO;
	
	static {
		queryDAO = QueryDAOImpl.getInstance();
	}

	/**
	 * 私有化构造方法，实现单例模式
	 */
	private NamedEntityServiceImpl() {}
	
	/**
	 * 获取单例
	 * @return
	 */
	public static NamedEntityServiceI getInstance() {
		if (singleInstance == null) {
			synchronized (NamedEntityServiceImpl.class) {
				if (singleInstance == null) {
					singleInstance = new NamedEntityServiceImpl();
				}
			}
		}
		return singleInstance;
	}
	
	@Override
	public boolean fillNamedEntities(List<PolysemantNamedEntity> polysemantNamedEntities) {
		for (PolysemantNamedEntity polysemantNameEntity : polysemantNamedEntities) {
			// 先查询其等价实体 避免搜索星爷时 无法正确返回其属性
			String sameEntityUUID = queryDAO.querySameIndividual(polysemantNameEntity.getUUID());
			// 得到等价实体名
			String entityUUID = sameEntityUUID == null ? polysemantNameEntity.getUUID() : sameEntityUUID;
			// 查询该实体的所有属性
			StmtIterator propertyStatements = queryDAO.queryIndividualProperties(entityUUID);
			
			Map<String, String> dataProperties = new LinkedHashMap<String, String>();
			Map<String, String> objectProperties =  new LinkedHashMap<String, String>();
			if (propertyStatements != null) {
				while (propertyStatements.hasNext()) {
					Statement propertyStatement = propertyStatements.next();
					String predicateNodeName = propertyStatement.getPredicate().getLocalName();	// 属性名
					RDFNode objectNode = propertyStatement.getObject();	// 属性值
					if (predicateNodeName.equals("有picSrc")) {
						polysemantNameEntity.setPicSrc(objectNode.toString());	// 设置图片Src
					}
					if (predicateNodeName.equals("有描述")) {
						polysemantNameEntity.setLemmaSummary(objectNode.toString());	// 设置描述
					}
					if (objectNode.isLiteral()) { // 数据属性
						dataProperties.put(predicateNodeName, objectNode.toString());
					} else {
						String objectNodeValue = objectNode.toString().split("#")[1];
						String objectName = null;
						if (!StringHandle.isIncludeChinese(objectNodeValue) && objectNodeValue.length() == 32) // 该属性值不是中文且字符长度为32  则表示UUID
							objectName = queryDAO.queryIndividualComment(objectNodeValue);
						else 
							objectName = objectNodeValue;
						objectProperties.put(predicateNodeName, objectName);
					}
				}
			}
			polysemantNameEntity.setDataProperties(dataProperties);
			polysemantNameEntity.setObjectProperties(objectProperties);
		}
		return true;
	}
}