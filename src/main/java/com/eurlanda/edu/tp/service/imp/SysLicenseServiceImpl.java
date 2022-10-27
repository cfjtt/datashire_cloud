package com.eurlanda.edu.tp.service.imp;

import com.eurlanda.edu.tp.authentication.auth.AuthServiceImpl;
import com.eurlanda.edu.tp.dao.entity.SysLicense;
import com.eurlanda.edu.tp.dao.mapper.SysLicenseMapper;
import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.service.SysLicenseService;
import com.eurlanda.edu.tp.webdomain.request.ReqImportOrUpdateLicense;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by test on 2018/6/6.
 */
@Service
public class SysLicenseServiceImpl implements SysLicenseService {


    static Logger log = Logger.getLogger(SysLicenseServiceImpl.class.getName());

    @Autowired
    SysLicenseMapper sysLicenseMapper;

    @Autowired
    AuthServiceImpl authService;

    @Override
    public SysLicense selectLicenseData() {
        return sysLicenseMapper.selectLicenseData();
    }

    @Override
    public int insertSelective(SysLicense sysLicense) {
        return sysLicenseMapper.insertSelective(sysLicense);
    }

    @Override
    public int updateByPrimaryKeySelective(SysLicense sysLicense) {
        return sysLicenseMapper.updateByPrimaryKeySelective(sysLicense);
    }

    @Override
    public String getSysKey() throws ApplicationErrorException {
        SysLicense sysLicense = selectLicenseData();

        String key = null;
        try {
            byte[] bytes = com.eurlanda.deep.enc.KeyGen.gen();
            key = new String(bytes);
            if(sysLicense == null){
                SysLicense paraLicense = new SysLicense();
                paraLicense.setKey(key);
                sysLicenseMapper.insertSelective(paraLicense);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getSysKey" + e.getMessage());
            throw new ApplicationErrorException(ErrorCode.GetSysKeyError);
        }
        return key;
    }

    @Override
    public Map<String, Object> decodeLicense() throws ApplicationErrorException {
        SysLicense sysLicense = selectLicenseData();
        String license = null;
        if (sysLicense != null) {
            license = sysLicense.getLicense();
        }
        Map<String, Object> map = new HashMap<>();
        if (license == null) {
            SysLicense paraSys = new SysLicense();
            paraSys.setStatus(0);
            sysLicenseMapper.updateLicenseData(paraSys);
            throw new ApplicationErrorException(ErrorCode.NoImportLicense);
        } else {
            try {
                String result = com.eurlanda.deep.enc.LicenseDecode.decode(license);
                if ("".equals(result)) {
                    log.error("解析license错误。。。");
                    SysLicense paraSys = new SysLicense();
                    paraSys.setStatus(-1);
                    sysLicenseMapper.updateLicenseData(paraSys);
                    throw new ApplicationErrorException(ErrorCode.LicenseIsUpdated);
                } else {
                    String[] strArr = result.split("@@");
                    //不限时长，则只有开始时间没有结束时间
                    map.put("maxOnlineNum", strArr[0]);
                    map.put("startTime", strArr[1]);
                    if (strArr.length == 3) {
                        map.put("endTime", strArr[2]);
                    }
                    SysLicense paraSys = new SysLicense();
                    paraSys.setStatus(1);
                    sysLicenseMapper.updateLicenseData(paraSys);
                }


            } catch (Exception e) {
                e.printStackTrace();
                log.error("导入license错误" + e.getMessage());
                throw new ApplicationErrorException(ErrorCode.ImportLicenseError);
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> decodeLicense(String string) throws ApplicationErrorException {
        Map<String, Object> map = new HashMap<>();
        try {
            String result = com.eurlanda.deep.enc.LicenseDecode.decode(string);
            if ("".equals(result)) {
                SysLicense paraSys = new SysLicense();
                paraSys.setStatus(-1);
                sysLicenseMapper.updateByPrimaryKeySelective(paraSys);
                throw new ApplicationErrorException(ErrorCode.LicenseIsUpdated);
            } else {
                String[] strArr = result.split("@@");
                //不限时长，则只有开始时间没有结束时间
                map.put("maxOnlineNum", strArr[0]);
                map.put("startTime", strArr[1]);
                if (strArr.length == 3) {
                    map.put("endTime", strArr[2]);
                }
                SysLicense paraSys = new SysLicense();
                paraSys.setStatus(1);
                sysLicenseMapper.updateLicenseData(paraSys);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("decodeLicense错误" + e.getMessage());
            throw new ApplicationErrorException(ErrorCode.ImportLicenseError);
        }

        return map;
    }

    /**
     * 导入或者更新license
     *
     * @throws ApplicationErrorException
     */
    @Override
    public Map<String, Object> importOrUpdateLicense(ReqImportOrUpdateLicense license) throws ApplicationErrorException {
        String licenseStr = license.getLicense();

        SysLicense existLicense = sysLicenseMapper.selectLicenseData();
        if (existLicense != null) {
            String existLic = existLicense.getLicense();

            if (existLic != null && licenseStr.replaceAll("\\s*|\\t|\\r|\\n","").equals(existLic.replaceAll("\\s*|\\t|\\r|\\n",""))) {
                throw new ApplicationErrorException(ErrorCode.RepeatLicense);
            }
        }

        //1.导入  2.更新
        Integer type = license.getType();
        try {
            String result = com.eurlanda.deep.enc.LicenseDecode.decode(licenseStr);
            if ("".equals(result)) {
                throw new ApplicationErrorException(ErrorCode.InvalidLicense);
            } else {
                SysLicense paraLicense = new SysLicense();
                paraLicense.setLicense(licenseStr);
                paraLicense.setStatus(1);
                sysLicenseMapper.updateLicenseData(paraLicense);
                authService.licenseValidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("importOrUpdateLicense" + e.getMessage());
            throw new ApplicationErrorException(ErrorCode.InvalidLicense);
        }
        Map<String, Object> result = decodeLicense(licenseStr);
        return result;

    }

    public static void main(String[] args) {
        String str = "dsdasdadadsa   dsdsds     dsds        \nfdsfsfsfsfsfd         \n\n\n\n\n\n\n\n\n\n\n\n\nfdfsfs    ";
        System.out.println(str);
        str = str.replaceAll("\\s*|\\t|\\r|\\n","");
        System.out.println(str);
    }



}
