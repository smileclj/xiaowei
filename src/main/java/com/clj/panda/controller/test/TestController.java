package com.clj.panda.controller.test;

import com.clj.panda.common.enums.PandaCode;
import com.clj.panda.common.exceptions.PandaException;
import com.clj.panda.common.resp.Result;
import com.clj.panda.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lao on 2015/9/28.
 */
@RestController
@RequestMapping(value="test")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private TestService userService;

    @RequestMapping(value="/testView.htm")
    public ModelAndView testView(@RequestParam("id") String id){
        if(id == null || id.equals("")){
            throw new PandaException(PandaCode.ERROR_PARAM);
        }
        ModelAndView view = new ModelAndView("page/test/TestJsp");
        view.addObject("user","panda");
        return view;
    }

    /**
     * 返回json数据
     * @return
     */
    @RequestMapping(value="/testJson.htm")
    public Result testJson(@RequestParam("id") String id){
        if(id == null || id.equals("")){
            throw new PandaException(PandaCode.ERROR_PARAM);
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user","clj");
        return new Result(PandaCode.SUCCESS,map);
    }

    @RequestMapping(value="/getUser.htm")
    public String getUser(){
//        return userService.findUserById("1").getName();
        return null;
    }

    @RequestMapping(value="/singleUpload.htm")
    public String singleUpload(MultipartFile file,String comment,HttpServletRequest request,HttpServletResponse response){
        logger.debug("单文件上传");
        logger.debug("comment:"+comment);
        logger.debug(file.getOriginalFilename());
        return "success";
    }

    @RequestMapping(value="/multiUpload.htm")
    public String multiUpload(@RequestParam(value="file")CommonsMultipartFile[] files,String comment,HttpServletRequest request,HttpServletResponse response){
        logger.debug("多文件上传");
        logger.debug("comment:"+comment);
        for(int i = 0;i<files.length;i++){
            logger.debug(files[i].getOriginalFilename());
        }
        return "success";
    }

    @RequestMapping(value="/downloadOne")
    public void downloadOne(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("text/plain; charset=utf-8");
        boolean isExist = true;
        FileInputStream fis = null;
        try{
            File file = new File("D:\\test.txt");
            fis = new FileInputStream(file);
        }catch(Exception e){
            isExist = false;
        }

        ServletOutputStream ss = null;
        try {
            ss = response.getOutputStream();
            if(isExist){
                while(fis.available()>0){
                    ss.write(fis.read());
                }
            }else{
                response.setContentLength(0);
                ss.write("".getBytes());
            }
            ss.flush();
            ss.close();
        } catch (IOException e) {
            logger.error("",e);
        }
    }
}
