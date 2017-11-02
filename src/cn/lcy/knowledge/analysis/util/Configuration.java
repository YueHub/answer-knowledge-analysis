package cn.lcy.knowledge.analysis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Configuration {
	
    public static Properties propertiesLoader(String fileName) throws IOException {
        // 文件在class的根路径  
        InputStream is = Configuration.class.getClassLoader().getResourceAsStream(fileName);  

        BufferedReader br = new BufferedReader(new InputStreamReader(is));  
        Properties props = new Properties();
        
        props.load(br);  
        
        return props;
    }
}
