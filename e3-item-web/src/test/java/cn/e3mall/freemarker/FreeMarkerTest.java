package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTest {
	
	@Test
	public void testFreeMarker() throws IOException, TemplateException{
		//把freemarker的jar包添加到工程中
		//创建一个模板文件
		//创建一个Configuration对象，直接new
		Configuration configuration = new Configuration(Configuration.getVersion());
		//这种模板所在目录
		configuration.setDirectoryForTemplateLoading(new File("E:/New_beg/java%20上课资料5/workspace2/e3-item-web/src/main/webapp/WEB-INF/ftl/hello.ftl"));
		//设置模板文件使用的编码格式，一般就是utf-8
		configuration.setDefaultEncoding("utf-8");
		//加载模板得到一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集，可以是map页可以是pojo，通常使用map
		Map data = new HashMap<>();
		data.put("hello", "hello freemarker!!!");
		//创建一个writer对象，指定输出文件的路径及文件名
		Writer out = new FileWriter(new File("E:/New_beg/temp/freemarker/hello.txt"));
		//生成文件
		template.process(data, out);
		//关闭流
		out.close();
	}

}
