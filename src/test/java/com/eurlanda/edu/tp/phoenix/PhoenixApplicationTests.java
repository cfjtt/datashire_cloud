package com.eurlanda.edu.tp.phoenix;

import com.eurlanda.edu.tp.api.EduApi;
import com.eurlanda.edu.tp.service.imp.ImportAndExportServiceImpl;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.apache.hadoop.conf.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhoenixApplicationTests {

	@Autowired
	EduApi eduApi;

	@Autowired
	ImportAndExportServiceImpl importAndExportService;

	@Test
	public void contextLoads() {
		String courseName = "测试demo数猎场";
		String result = eduApi.getHdfsPathByCourse(courseName);
		System.out.println(result);
	}

	@Test
	public void importAndExportServiceTest() throws Exception{
		String dbName = "c203";
		importAndExportService.getDataBaseData(dbName);
	}

	@Test
	public void uploadFile() throws Exception{
		String srcPath = "C:\\Users\\test\\Downloads\\testUpload\\cdata\\c211";
		String toPath = "/cdata/c98";
		importAndExportService.setFileSystemConfig(new Configuration());
		importAndExportService.upload(srcPath,toPath);
	}

	@Test
	public void test01()throws  Exception{
		String json = "[{\"columnData\":[{\"columnName\":\"id\",\"columnType\":\"INT\",\"length\":11},{\"columnName\":\"title\",\"columnType\":\"VARCHAR\",\"length\":60}],\"columnValue\":{\"0\":[\"1\",\"java\"],\"1\":[\"2\",\"c\"],\"2\":[\"3\",\"c++\"],\"3\":[\"4\",\"node.js\"]},\"tableName\":\"course\"},{\"columnData\":[{\"columnName\":\"id\",\"columnType\":\"INT\",\"length\":11},{\"columnName\":\"name\",\"columnType\":\"VARCHAR\",\"length\":35}],\"columnValue\":{\"0\":[\"1\",\"carl\"],\"1\":[\"2\",\"rose\"],\"2\":[\"3\",\"jack\"],\"3\":[\"4\",\"bules\"]},\"tableName\":\"user\"}]";
		String dbName = "c201";
		importAndExportService.parseDbJsonAndInsertData(json,dbName);
	}

	@Test
	public void ppt2pdf(){
		System.out.println("启动PPT");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Powerpoint.Application");
			Dispatch presentations = app.getProperty("Presentations").toDispatch();
			Dispatch presentation = Dispatch.call(presentations,//
					"Open",
					"D:\\test.ppt",// FileName
					true,// ReadOnly
					true,// Untitled 指定文件是否有标题。
					false // WithWindow 指定文件是否可见。
			).toDispatch();

			File tofile = new File("D:\\test2.pdf");
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(presentation,//
					"SaveAs", //
					"D:\\test2.pdf", // FileName
				32);

			Dispatch.call(presentation, "Close");
			long end = System.currentTimeMillis();
			System.out.println("转换完成..用时：" + (end - start) + "ms.");
		} catch (Exception e) {
			System.out.println("========Error:文档转换失败：" + e.getMessage());
		} finally {
			if (app != null) app.invoke("Quit");
		}
	}


}
