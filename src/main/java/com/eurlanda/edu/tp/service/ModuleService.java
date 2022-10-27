package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.dao.entity.Module;
import com.eurlanda.edu.tp.webdomain.request.ReqAddModuleResource;
import com.eurlanda.edu.tp.webdomain.request.ReqAdjustModuleOrderClass;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteModule;
import com.eurlanda.edu.tp.webdomain.request.ReqDeleteModuleResource;
import com.eurlanda.edu.tp.webdomain.response.ResModuleId;
import com.eurlanda.edu.tp.webdomain.response.ResModuleImages;

import java.util.Map;

public interface ModuleService {
    ResModuleId createModule(Module module) throws ApplicationErrorException;

    void deleteModule(ReqDeleteModule reqDeleteModule) throws ApplicationErrorException;

    ResModuleImages getModuleImageUrls(int moduleId);

    Integer addModuleResource(ReqAddModuleResource reqAddModuleResource) throws ApplicationErrorException;

    void deleteModuleResource(ReqDeleteModuleResource reqDeleteModuleResource) throws ApplicationErrorException;

    boolean moduleIsExist(Module module) throws ApplicationErrorException;

    Map<String, Object> adjustModuleOrder(ReqAdjustModuleOrderClass entity) throws ApplicationErrorException;

    void updateModule(Module module) throws ApplicationErrorException;

    Module getModuleInfo(Module module) throws ApplicationErrorException;
}
