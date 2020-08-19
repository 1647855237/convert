package com.convert.convert.Interceptor;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.convert.convert.config.RedisUtil;
import com.convert.convert.entity.Convert;
import com.convert.convert.mapper.ConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
@Component
public class CovertInterceptor implements HandlerInterceptor {

    @Autowired
    private ConvertMapper convertMapper;
    @Autowired
    private RedisUtil redisUtil;

    private static final int DEFUAULTCONUT = 10;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String shortUrl = request.getRequestURI().substring(1);
        // 长链接
        String longUrl;
        Object cacheUrl = redisUtil.get(shortUrl);
        if (!StringUtils.isEmpty(cacheUrl)) {
            longUrl = cacheUrl.toString();
            response.sendRedirect(longUrl);
            return false;
        }

        Convert convert = new Convert();
        convert.setShortUrl(shortUrl);
        convert.setType(0);
        QueryWrapper<Convert> queryWrapper = new QueryWrapper<>(convert);

        Convert selectOne = convertMapper.selectOne(queryWrapper);
        if (selectOne != null) {
            // 时间1分钟失效
            Date oldDate = selectOne.getCreateDate();
            Date newDate = DateUtil.offset(oldDate, DateField.MINUTE, 1);
            Date date = new Date();
            long betweenDay = DateUtil.between(date, newDate, DateUnit.MINUTE);
            if (betweenDay > 1) {
                response.setStatus(404);
                return false;
            }

            Integer count = selectOne.getCount();
            // 调用次数过多，放入缓存
            if (count == DEFUAULTCONUT) {
                redisUtil.set(shortUrl, selectOne.getLongUrl());
            }
            selectOne.setCount(selectOne.getCount()+1);
            convertMapper.updateById(selectOne);
            longUrl = selectOne.getLongUrl();
            response.sendRedirect(longUrl);
        } else {
            response.setStatus(404);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
