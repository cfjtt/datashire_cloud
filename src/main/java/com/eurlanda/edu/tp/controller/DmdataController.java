package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.authentication.security.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("dmdata")
public class DmdataController {

    @Autowired
    private EduApi eduApi;

    @ApiOperation(value = "获取所有行业", notes = "")
    @PostMapping(value = "industry/all")
    public String getIndustry(String key,String isTop){
        String result = eduApi.getAllIndustry(key,isTop);
        return result;
    }

    @ApiOperation(value = "获取行业数量，数据数量，数据总量，下载次数", notes = "")
    @GetMapping(value = "/getMsg")
    public String getPartCountMsg(){
        String result = eduApi.getDataIndustryDownloadCount();
        return result;
    }

    @ApiOperation(value = "获取数据集信息", notes = "")
    @PostMapping(value = "dm/getDmData")
    public String getDmData(Integer industryId,Integer page,Integer offset,String dmName){
        String result = eduApi.getDmData(industryId,page,offset,dmName);
        return result;
    }

    @ApiOperation(value = "获取服务器加密时间", notes = "")
    @GetMapping(value="/getencrypt")
    public String getEncryptTime(Integer userId){
        return JwtTokenUtil.generateTimeToken(userId);
    }

    @ApiOperation(value = "获取服务器当前时间", notes = "")
    @GetMapping(value="/getcurrenttime")
    public String currentTime(){
        return System.currentTimeMillis()+"";
    }
}
