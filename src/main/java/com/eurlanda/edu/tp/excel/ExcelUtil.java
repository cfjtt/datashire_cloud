package com.eurlanda.edu.tp.excel;

import com.eurlanda.edu.tp.error.ApplicationErrorException;
import com.eurlanda.edu.tp.error.ErrorCode;
import com.eurlanda.edu.tp.excel.domain.ExcelStudent;
import com.eurlanda.edu.tp.excel.domain.ExcelTeacher;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/22.
 */

public class ExcelUtil {

    public static ExcelStudent studentExcelAnalysis(MultipartFile file) throws IOException,
            ApplicationErrorException {
        ExcelStudent excelStudent = new ExcelStudent();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelStudent.ExcelStudentElement> excelStudentElementList = new ArrayList<>();
            for (int rowNum=1; rowNum<=sheet.getLastRowNum(); rowNum++) {
                ExcelStudent.ExcelStudentElement element = new ExcelStudent().new ExcelStudentElement();
                Row row = sheet.getRow(rowNum);
                String num = "";
                for(int i=0;i<row.getLastCellNum();i++){
                    Cell cell = row.getCell(i);
                    if(cell != null){
                        cell.setCellType(CellType.STRING);
                    }
                }
                try {
                    if(row.getCell(0) != null) {
                        num = row.getCell(0).getStringCellValue();
                    }
                } catch (IllegalStateException e){
                    Double v = new Double(row.getCell(0).getNumericCellValue());
                    if(Math.floor(v)==v){
                        //如果是整数
                        num = v.intValue()+"";
                    } else {
                        //否则
                        num = v.toString();
                    }
                }

                element.setStudentNum(num);
                if(row.getCell(1) != null) {
                    element.setStudentName(row.getCell(1).getStringCellValue());
                } else {
                    element.setStudentName("");
                }

                String gender = "";
                try{
                    if(row.getCell(2) != null) {
                        gender = row.getCell(2).getStringCellValue();
                    }
                } catch (Exception e){
                   /* Double v = new Double(row.getCell(2).getNumericCellValue());
                    if(Math.floor(v) == v){
                        gender = v.intValue()+"";
                    } else {
                        gender = v.toString();
                    }*/
                   gender= row.getCell(2).getStringCellValue().toString();

                }
               /* //校验学生性别是否合法
                if(gender.matches("1|2")){
                    element.setGender(Integer.parseInt(gender));
                }*/
                element.setGenderStr(gender);
                //年级
                String grade="";
                try{
                    if(row.getCell(3) != null) {
                        grade = row.getCell(3).getStringCellValue();
                    }
                }catch (Exception e){
                    grade = row.getCell(3).getStringCellValue().toString();
                }
                element.setGrade(grade);
                //联系方式
                String phone="";
                try{
                    if(row.getCell(4) != null) {
                        phone = row.getCell(4).getStringCellValue();
                    }
                }catch (Exception e){
                    phone = row.getCell(4).getStringCellValue().toString();
                }
                element.setPhone(phone);
                if(row.getCell(5) != null && !"".equals(row.getCell(5).getStringCellValue())) {
                    throw new ApplicationErrorException(ErrorCode.InvalidExcelFileFormat);
                }

                //去除空行
                if(isEmpty(num) && isEmpty(element.getStudentName()) && isEmpty(gender)){
                    continue;
                }
                excelStudentElementList.add(element);
            }

            excelStudent.setExcelStudentElementList(excelStudentElementList);
            return excelStudent;

        } catch ( Exception e) {
            throw new ApplicationErrorException(ErrorCode.InvalidExcelFileFormat);
        }
    }

    public static boolean isEmpty(String value){
        if(value==null || value.trim().length()==0){
            return true;
        }
        return false;
    }

    public static ExcelTeacher teacherExcelAnalysis(MultipartFile file) throws ApplicationErrorException, IOException {
        ExcelTeacher excelTeacher = new ExcelTeacher();
        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelTeacher.ExcelTeacherElement> excelTeacherElementList = new ArrayList<>();
            for (int rowNum=1; rowNum<=sheet.getLastRowNum(); rowNum++) {
                ExcelTeacher.ExcelTeacherElement element = new ExcelTeacher().new ExcelTeacherElement();
                Row row = sheet.getRow(rowNum);
                if(row==null){
                    continue;
                }
                String num = "";
                for(int i=0;i<row.getLastCellNum();i++){
                    Cell cell = row.getCell(i);
                    if(cell != null){
                        cell.setCellType(CellType.STRING);
                    }
                }
                try {
                    Cell cell = row.getCell(0);
                    if(cell != null ) {
                        num = row.getCell(0).getStringCellValue();
                    }
                } catch (IllegalStateException e){
                    Double v = new Double(row.getCell(0).getNumericCellValue());
                    if(Math.floor(v)==v){
                        //如果是整数
                        num = v.intValue()+"";
                    } else {
                        //否则
                        num = v.toString();
                    }
                }
                element.setTeacherNum(num);
                Cell cell = row.getCell(1);
                if(cell != null) {
                    element.setTeacherName(row.getCell(1).getStringCellValue());
                } else {
                    element.setTeacherName("");
                }
                String gender = "";
                try{
                    cell = row.getCell(2);
                    if(cell != null ) {
                        gender = row.getCell(2).getStringCellValue();
                    }
                } catch (Exception e){
                 /*   Double v = new Double(row.getCell(2).getNumericCellValue());
                    if(Math.floor(v) == v){
                        gender = v.intValue()+"";
                    } else {
                        gender = v.toString();
                    }*/
                 gender=gender.toString();
                }
                element.setGenderStr(gender);
               /* if(gender.matches("1|2")){
                    element.setGender(Integer.parseInt(gender));
                }*/
                String title = "";
                try{
                    if(row.getCell(3) != null) {
                        title = row.getCell(3).getStringCellValue();
                    }
                } catch (Exception e){
                   /* Double v = new Double(row.getCell(3).getNumericCellValue());
                    if(Math.floor(v) == v){
                        title = v.intValue()+"";
                    } else {
                        title = v.toString();
                    }*/
                   title=title.toString();

                }
                element.setTeacherTitleStr(title);
              /*  if(title.matches("1|2|3")){
                    element.setTeacherTitle(Integer.parseInt(title));
                }*/
                String concat = "";
                DecimalFormat df = new DecimalFormat("0");
                try {
                    if(row.getCell(4) != null) {
                        concat = row.getCell(4).getStringCellValue();
                    }
                } catch (IllegalStateException e){
                    concat = df.format(row.getCell(4).getNumericCellValue());
                }
                if(row.getCell(5) != null && !"".equals(row.getCell(5).getStringCellValue())) {
                    throw new ApplicationErrorException(ErrorCode.InvalidExcelFileFormat);
                }

                element.setTeacherContact(concat);

                //去除到空行
                if(isEmpty(num) && isEmpty(element.getTeacherName()) && isEmpty(gender) && isEmpty(title) && isEmpty(concat)){
                    continue;
                }
                excelTeacherElementList.add(element);
            }

            excelTeacher.setExcelTeacherElementList(excelTeacherElementList);
            return excelTeacher;

        } catch ( Exception e) {
            throw new ApplicationErrorException(ErrorCode.InvalidExcelFileFormat);
        }
    }
}
