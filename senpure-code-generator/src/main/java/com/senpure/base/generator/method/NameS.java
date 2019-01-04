package com.senpure.base.generator.method;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

/**
 * NameS
 *
 * @author senpure
 * @time 2019-01-03 11:05:50
 */
public class NameS {
    public static void main(String[] args) {
        PhysicalNamingStrategy namingStrategy = new SpringPhysicalNamingStrategy();


        Identifier identifier = namingStrategy.toPhysicalSchemaName(Identifier.toIdentifier("User"), null);
        System.out.println(identifier.getText());



    }
}
