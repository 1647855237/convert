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
import java.util.Random;

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

    private static final char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };


    @Override
    public String convert(String url) {
        String shortUrl;

        Convert convert = new Convert();
        convert.setLongUrl(url);
        convert.setType(0);
        QueryWrapper<Convert> queryWrapper = new QueryWrapper<>(convert);
        // 先判断数据库是否有
        Convert selectOne = convertMapper.selectOne(queryWrapper);
        if (selectOne == null) {
            // 生成短链接
            shortUrl = this.getShortUrl();
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
     * @return
     */
    public String getShortUrl(){
        long id = getId();
        return format62radix(id);
    }


    /**
     * 10进制转62进制
     *  限制五位数，最多自增到916132832L。
     *  自己写算法，自己目前只能写出这个，如果可以用别人的算法，可以用推特的雪花算法来计算
     * @param id
     * @return
     */
    private static String format62radix(long id) {
        StringBuilder sb = new StringBuilder();
        long tmp = id;
        while (true) {
            // 求余
            int remainder = (int) (tmp % 62);
            sb.append(chars[remainder]);
            // 余数会重复，第二次进一位到时候不会重复
            tmp = tmp / 62;
            if (tmp == 0) {
                break;
            }
        }
        if (sb.length() < 5) {
            int differ = 5 - sb.length();
            for (int i = 0; i < differ; i++) {
                sb.insert(0, chars[new Random().nextInt(62)]);
            }
        }
        return sb.toString();
    }

    /**
     * 利用redis自增获取唯一id
     * @return
     */
    private long getId() {
        return redisUtil.incrBy("id",1);
    }

    /**
     * 第一种： 利用redis自增获取唯一id
     *
     * @return
     */
//    public String getLongUrl() {
//        return Long.toString(redisUtil.incrBy("id", 1));
//    }


}
