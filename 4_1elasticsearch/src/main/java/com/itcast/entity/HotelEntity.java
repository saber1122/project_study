package com.itcast.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_hotel")
public class HotelEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    //酒店名称
    @TableField("name")
    private String address;

    //品牌
    @TableField("brand")
    private String brand;

    //类型
    @TableField("type")
    private String type;

    //酒店价格
    @TableField("price")
    private int price;

    //规格
    @TableField("specs")
    private String specs;

    //销量
    @TableField("salesVolume")
    private int salesVolume;

    //酒店简介
    @TableField("synopsis")
    private String synopsis;

    //所处区域
    @TableField("area")
    private String area;

    //图片路径
    @TableField("imageUrl")
    private String imageUrl;

    //创建时间
    @TableField("createTime")
    private Date createTime;
	
	//是否广告
    @TableField("isAd")
    private Integer isAd;
}
