package com.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * @program: ohhhh
 * @description: 消息日志
 * @author: wangnie
 * @create: 2022-06-23 16:54
 **/

@Data
@TableName("msgLog")
public class MsgLog {


    @TableId(type=IdType.ASSIGN_UUID)
    private String id;

    private String content;

    private String createTime;

    private String msgId;

    private String status;
}
