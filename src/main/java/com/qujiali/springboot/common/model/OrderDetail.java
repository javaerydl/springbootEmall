package com.qujiali.springboot.common.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author YangDeLong
 * @date 2019/1/17 0017 17:04
 **/
public class OrderDetail {
    //收货地址
    private String address;
    //收货人
    private String name;
    //联系方式
    private String phone;
    //商品规格
    private String goodStandard;
    //商品数量
    private int goodCount;
    //商品图片
    private String goodImg;
    //订单状态
    private int enable;
    //创建时间
    private Date createTime;
    //商品价格
    private BigDecimal price;
    //订单总价
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGoodStandard() {
        return goodStandard;
    }

    public void setGoodStandard(String goodStandard) {
        this.goodStandard = goodStandard;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodImg() {
        return goodImg;
    }

    public void setGoodImg(String goodImg) {
        this.goodImg = goodImg;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", goodStandard='" + goodStandard + '\'' +
                ", goodCount=" + goodCount +
                ", goodImg='" + goodImg + '\'' +
                ", enable=" + enable +
                ", createTime=" + createTime +
                ", price=" + price +
                '}';
    }
}
