package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: ohhhh
 * @description: 用户
 * @author: wangnie
 * @create: 2022-06-23 16:52
 **/


@Data
@TableName("user")
public class User {

    @TableId(type= IdType.ASSIGN_UUID)
    private String id;

    private String userName;

    private String address;

    private String email;

    private String phone;

}
