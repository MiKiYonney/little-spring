package com.ioc.logic.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yonney.yang on 2015/6/26.
 */
@NoArgsConstructor
@Data
public class Student {
    private String userName;
    private String sex;
    private Integer age;
    private Course course;
}
