package com.eurlanda.edu.tp.controller;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ResponseMessage;
import com.eurlanda.edu.tp.service.CloudwareService;
import com.eurlanda.edu.tp.webdomain.response.ResExperimentInfo;
import com.eurlanda.edu.tp.webdomain.request.ReqStudentExperiment;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/10/19.
 */

@CrossOrigin
@RestController
@RequestMapping("cloudware")
public class CloudwareController {

    @Autowired
    private CloudwareService cloudwareService;

    @ApiOperation(value = "获取学生实验容器", notes = "根据学生id和实验id获取实验容器地址，如果没有就新产生一个")
    @GetMapping("/cloudware/experiment")
    public ResponseMessage<String> getStudentCloudware(ReqStudentExperiment reqStudentExperiment) throws
            ApplicationErrorException {
        return new ResponseMessage.Success<>(cloudwareService.getStudentExperiment(reqStudentExperiment));
    }

    @ApiOperation(value = "获取试验内容")
    @GetMapping("/cloudware/{id}/info")
    public ResponseMessage<ResExperimentInfo> getExperiment(@PathVariable("id") int id) throws ApplicationErrorException{
        return new ResponseMessage.Success<>(cloudwareService.getExperiment(id));
    }
}
