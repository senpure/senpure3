package com.senpure.base.generator.naming;

/**
 * ImprovedNamingStrategy
 *
 * @author senpure
 * @time 2019-01-03 11:43:00
 */
public class ImprovedNamingStrategy implements NamingStrategy {
    private org.hibernate.cfg.ImprovedNamingStrategy namingStrategy = new org.hibernate.cfg.ImprovedNamingStrategy();
    @Override
    public String classToTableName(String var1) {
        return namingStrategy.classToTableName(var1);
    }

    @Override
    public String tableName(String var1) {
        return namingStrategy.tableName(var1);
    }

    @Override
    public String columnName(String var1) {
        return namingStrategy.columnName(var1);
    }

    @Override
    public String joinKeyColumnName(String var1, String var2) {
        return namingStrategy.joinKeyColumnName(var1,var2);
    }
}
