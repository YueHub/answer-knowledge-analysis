package cn.lcy.knowledge.analysis.dictionary.dao;

import java.util.List;

public interface DictionaryDAOI {

    /**
     * 查询等价实体 查询周星驰的所有等价实体们
     *
     * @param individualName
     * @return
     */
    public List<String> querySameIndividuals(String individualName);

}
