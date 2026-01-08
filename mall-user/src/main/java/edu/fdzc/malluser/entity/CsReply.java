package edu.fdzc.malluser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cs_reply")
public class CsReply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long csId;
    private Long userId;
    private String content;
    private Date createTime;
}