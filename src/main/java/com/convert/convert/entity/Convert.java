package com.convert.convert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
@TableName(value = "converts")
public class Convert implements Serializable {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 短链接
     */
    private String shortUrl;
    
    /**
     * 长链接
     */
    private String longUrl;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 调用次数
     */
    private Integer count ;

    /**
     *  是否失效
     */
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Convert{" +
                "id='" + id + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", createDate=" + createDate +
                ", count=" + count +
                ", type=" + type +
                '}';
    }
}
