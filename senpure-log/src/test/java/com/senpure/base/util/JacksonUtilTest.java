package com.senpure.base.util;

import org.junit.Test;


/**
 * JacksonUtilTest
 *
 * @author senpure
 * @time 2019-03-07 10:00:35
 */
public class JacksonUtilTest {

    @Test
    public void toJSONString() {
        User user=new User();
        user.setAge(15);
        user.setName("user");
        System.out.println(JacksonUtil.toJSONString(user));
    }

    static class User {

        private Integer id;
        private String name;
        private int age = 1;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}