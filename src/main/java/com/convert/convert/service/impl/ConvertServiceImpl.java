package com.convert.convert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.convert.convert.config.RedisUtil;
import com.convert.convert.entity.Convert;
import com.convert.convert.mapper.ConvertMapper;
import com.convert.convert.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
@Service
public class ConvertServiceImpl extends ServiceImpl<ConvertMapper, Convert> implements ConvertService {

    @Autowired
    private ConvertMapper convertMapper;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public String convert(String url) {
        String shortUrl;

        Convert convert = new Convert();
        convert.setLongUrl(url);
        convert.setType(0);
        QueryWrapper<Convert> queryWrapper = new QueryWrapper<>(convert) ;
        // 先判断数据库是否有
        Convert selectOne = convertMapper.selectOne(queryWrapper);
        if (selectOne == null) {
            // 生成短链接
            shortUrl = this.getLongUrl();
            convert.setShortUrl(shortUrl);
            convert.setCreateDate(new Date());
            convert.setCount(convert.getCount());
            convertMapper.insert(convert);
        } else {
            shortUrl = selectOne.getShortUrl();
        }
            return shortUrl;
    }

    /**
     * 第一种：利用redis获取全局分布式id
     * @return
     */
    public String getLongUrl(){
        long id = getId();
        byte[] byteArray = long2byteArray(id);
        return byteArray2Hex(byteArray);
    }

    private String byteArray2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(byte2Hex(aByte));
        }
        return sb.toString();
    }

    /**
     * byte转16进制
     * @return
     */
    private String byte2Hex(byte b) {
        return Integer.toHexString(b);
    }

    /**
     * long转byte数组
     * @param in 输入
     * @return
     */
    private byte[] long2byteArray(long in) {
        byte[] result = new byte[8];
        result[7] = (byte) (in & 0xFFFF);
        result[6] = (byte) ((in & 0xFFFF) >>> 8);
        result[5] = (byte) ((in & 0xFFFF) >>> 16);
        result[4] = (byte) ((in & 0xFFFF) >>> 24);
        result[3] = (byte) ((in & 0xFFFF) >>> 32);
        result[2] = (byte) ((in & 0xFFFF) >>> 40);
        result[1] = (byte) ((in & 0xFFFF) >>> 48);
        result[0] = (byte) ((in & 0xFFFF) >>> 56);
        return result;
    }

    /**
     * 利用redis自增获取唯一id
     * @return
     */
    private long getId() {
        return redisUtil.incrBy("id",1);
    }
}
