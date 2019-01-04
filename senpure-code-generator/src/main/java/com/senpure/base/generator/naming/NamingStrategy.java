package com.senpure.base.generator.naming;

/**
 * NamingStrategy
 *
 * @author senpure
 * @time 2019-01-03 11:40:45
 */
public interface NamingStrategy {

    String classToTableName(String var1);



    String tableName(String var1);

    String columnName(String var1);


    String joinKeyColumnName(String var1, String var2);


}
