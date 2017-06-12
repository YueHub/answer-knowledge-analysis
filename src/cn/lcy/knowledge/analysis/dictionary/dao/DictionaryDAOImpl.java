package cn.lcy.knowledge.analysis.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.util.FmtUtils;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import cn.lcy.knowledge.analysis.config.Config;

public class DictionaryDAOImpl implements DictionaryDAOI {
	
	public static final OntModel model;
	static {
		model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	}

	public static final Model loadModel;
	static {
		loadModel = FileManager.get().readModel(model, Config.ontologyPath);
	}
    
    /**
     * TODO 可以使用listSameAs方法  查询等价实体 如查询周星驰的所有等价实体
     */
    public List<String> querySameIndividuals(String individualName) {
    	List<String> sameIndividuals = new ArrayList<String>();
        String prefix = "prefix mymo: <" + Config.pizzaNs + ">\n" +
                "prefix rdfs: <" + RDFS.getURI() + ">\n" +
                "prefix owl: <" + OWL.getURI() + ">\n";
		String QL = "SELECT ?等价实体   WHERE {mymo:"  + individualName + " owl:sameAs ?等价实体.\n}";
		String SPARQL = prefix + QL;
		Query query = QueryFactory.create(SPARQL);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();
        ResultSetRewindable resultSetRewindable = ResultSetFactory.makeRewindable(results);
        int numCols = resultSetRewindable.getResultVars().size();
        while (resultSetRewindable.hasNext()) {
        	QuerySolution querySolution = resultSetRewindable.next();
        	for (int col = 0; col < numCols;col++) {
	        	String rVar = results.getResultVars().get(col);
	        	RDFNode obj = querySolution.get(rVar);
	        	String sameIndividual = FmtUtils.stringForRDFNode(obj).split(":")[1];
	        	sameIndividuals.add(sameIndividual);
        	}
        	
        }
        return sameIndividuals;
    }
    
}