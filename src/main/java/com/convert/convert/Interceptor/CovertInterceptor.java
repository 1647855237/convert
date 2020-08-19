package com.convert.convert.Interceptor;

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

    private static final int DEFUAULTCONUT = 1;

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
            Integer count = selectOne.getCount();
            // 调用次数过多，放入缓存
            if (count == DEFUAULTCONUT) {
                redisUtil.set(shortUrl, selectOne.getLongUrl());
            }
            selectOne.setCount(+1);
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
