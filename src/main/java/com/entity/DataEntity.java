package com.entity;

import lombok.Data;

/**
 * @program: ohhhh
 * @description: DataEntity
 * @author: wangnie
 * @create: 2022-06-24 09:31
 **/

@Data
public class DataEntity {

    //内容
    private String value;
    //字体颜色
    private String color;


    public DataEntity(String value, String color) {
        this.value = value;
        this.color = color;
    }
}
