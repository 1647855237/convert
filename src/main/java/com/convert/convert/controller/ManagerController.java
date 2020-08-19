package com.convert.convert.controller;

import com.convert.convert.entity.Convert;
import com.convert.convert.service.ConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
@Controller
public class ManagerController {

    @Autowired
    private ConvertService convertService;


    @RequestMapping("/manager")
    public String manager(){



        return "manager";
    }

    @RequestMapping("/convert/list")
    @ResponseBody
    public Object convertList(){
        List<Convert> list = convertService.list();
        return list;
    }

    @RequestMapping("/del")
    @ResponseBody
    public Object delConvert(Integer id){
        boolean b = convertService.removeById(id);
        if (b) {
            return "{code: 0,msg:操作成功}";
        } else {
            return "{code: 1,msg:操作失败}";
        }
    }

}
