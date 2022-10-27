package com.eurlanda.edu.tp.service;

import com.eurlanda.edu.tp.dao.entity.SysLicense;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.webdomain.request.ReqImportOrUpdateLicense;

import java.util.Map;

/**
 * Created by test on 2018/6/6.
 */
public interface SysLicenseService {


    SysLicense selectLicenseData();

    int insertSelective(SysLicense sysLicense);

    int updateByPrimaryKeySelective(SysLicense sysLicense);


    String getSysKey() throws ApplicationErrorException;

    Map<String,Object> decodeLicense() throws ApplicationErrorException;

    Map<String,Object> decodeLicense(String string) throws ApplicationErrorException;

    Map<String,Object> importOrUpdateLicense(ReqImportOrUpdateLicense license) throws ApplicationErrorException;

}
