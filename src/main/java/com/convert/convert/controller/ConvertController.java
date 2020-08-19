package com.convert.convert.controller;

import com.convert.convert.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xiongwei
 * @Date: 2020/8/18
 * @why：
 */
@Controller
public class ConvertController {

    @Autowired
    private ConvertService convertService;

    @RequestMapping("/send")
    public String String(String url, Model model, HttpServletRequest request){
        if (!StringUtils.isEmpty(url)) {
            String shortUrl = convertService.convert(url);
            System.out.println(shortUrl);
            StringBuffer reUrl = request.getRequestURL();
            String tempContextUrl = reUrl.delete(reUrl.length() - request.getRequestURI().length(), reUrl.length()).append("/").toString();
            model.addAttribute("url",tempContextUrl+shortUrl);
        }
       return "index";
    }





}
