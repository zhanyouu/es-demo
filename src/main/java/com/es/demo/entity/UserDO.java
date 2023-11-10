package com.es.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "user")
public class UserDO {
    @Id
    private Long id;
    @Field(store = true, type = FieldType.Text)
    private String name;
    @Field(store = true, type = FieldType.Integer)
    private Integer age;
}
