package com.qf.ratelimiter.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesParser {
    private static final Map<String, String> propertyMap = new HashMap<>();

    // 导入文件路径，相对路径即可
    public static void loadProperties(String path) {
        InputStream is = PropertiesParser.class.getResourceAsStream(path);
        loadProperties(is);
    }

    public static void loadProperties(InputStream is) {
        Properties properties = new Properties();

        // 这里的异常可以接收也可以抛出
        try {
            properties.load(is);
            // 读取文件中键集
            Set<Object> keySet = properties.keySet();
            // 遍历键集，依次将键值对放入map中
            for (Object o : keySet) {
                String key = (String) o;
                String value = properties.getProperty(key);
                propertyMap.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取配置文件
    private static void readConfig() throws IOException {
        InputStream in = PropertiesParser.class.getClassLoader().getResourceAsStream("database.properties");
        Properties prop = new Properties();
        prop.load(in);
        // 使用getProperties(key)，通过key获得需要的值
        // driver = prop.getProperty("driver");
        //  url = prop.getProperty("url");
        // user = prop.getProperty("user");
        // password = prop.getProperty("password");
    }

    // 获取map的键集
    public static Set<String> keySet() {
        return propertyMap.keySet();
    }

    // 模仿map中的get()方法，通过key得到value
    public static String value(String key) {
        return propertyMap.get(key);
    }
}