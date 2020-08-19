package com.convert.convert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.convert.convert.entity.Convert;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
public interface ConvertService extends IService<Convert> {

    /**
     * 转换
     * @param url
     */
    String convert(String url);

}
