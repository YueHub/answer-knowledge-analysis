package cn.lcy.knowledge.analysis.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import cn.lcy.knowledge.analysis.util.Configuration;

public class Config {
	
	/**
	 * 配置文件名
	 */
	public static final String PROPERTY_FILE = "knowledge-analysis.properties";
	
	/**
	 * 配置
	 */
	public static Properties properties;
	
	/**
	 * 本体标识
	 */
	public static String pizzaNs;
	
	/**
	 * 根路径
	 */
	public static String rootPath;
	
	/**
	 * 本体文件路径
	 */
	public static String ontologyPath;
	
	/**
	 * 实体词典路径
	 */
	public static String individualDictPath;
	
	/**
	 * 图片保存地址
	 */
	public static String picSavePath;
	
	public static OntModel model;
	
	public static Model loadModel;
	
	/**
	 * 读取配置
	 */
	static {
		try {
			properties = Configuration.propertiesLoader(PROPERTY_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pizzaNs = properties.getProperty("pizzaNs").toString();
		
		rootPath = properties.getProperty("rootPath").toString(); 
		ontologyPath = rootPath + properties.get("ontologyPath").toString();
		individualDictPath = rootPath + properties.get("individualDictPath").toString();
		picSavePath = rootPath + properties.getProperty("picSavePath");
		model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		loadModel = FileManager.get().readModel(model, Config.ontologyPath);
	}
	
	/**
	 * 配置类 不需要生成实例
	 */
	private Config() {}
	
	
	

}
