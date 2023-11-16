package com.es.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "user")
public class UserDO {
    public final static String INDEX_NAME = "user";
    public final static Long TIME_OUT = 8000L;
    public final static String NAME = "name";
    public final static String AGE = "age";
    public final static String DESC = "desc";
    public final static String DELETED = "deleted";
    public final static String STATUS = "status";




    @Id
    private Long id;
    /**
     * 姓名
     */
    @Field(type = FieldType.Keyword)
    private String name;
    /**
     * 年龄
     */
    @Field(type = FieldType.Integer)
    private Integer age;
    /**
     * 邮箱地址
     */
    @Field(type = FieldType.Keyword)
    private String email;
    /**
     * 出生日期
     */
    @Field(type = FieldType.Date)
    private Date birthDay;
    /**
     * 介绍
     */
    @Field(type = FieldType.Text,analyzer = "ik_smart")
    private String desc;

    /**
     * 状态
     */
    @Field(type = FieldType.Keyword)
    private String status;
    /**
     * 是否删除
     */
    @Field(type = FieldType.Boolean)
    private Boolean deleted;

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", birthDay=" + birthDay +
                ", desc='" + desc + '\'' +
                ", status='" + status + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
