package inage.test;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class Image_Client {
	@Test
	public void imageClient() throws Exception{
		// 1、添加对客户端jar包的依赖
		// 2、创建一个配置文件，配置文件中需要配置Tracker服务器的地址。（）
		// 3、加载配置文件。
		ClientGlobal.init("E:/New_beg/workspaces/workspace-e3mall/git-reps/e3-manager-web/src/main/resources/conf/client.conf");
		// 4、创建一个TrackerClient对象，直接new。
		TrackerClient client = new TrackerClient();
		// 5、通过Trackerclient对象获得TrackerServer对象。
		TrackerServer trackerServer = client.getConnection();
		// 6、创建一个StorageClient对象，构造方法中需要两个参数，一个TrackerServer，一个是StorageServer（null）
		StorageClient storageClient = new StorageClient(trackerServer, null);
		// 7、上传文件。返回文件的路径和文件名。
		//参数1：文件路径 参数2：文件扩展名 参数3：元数据
		String[] file = storageClient.upload_appender_file("D:/照片/Camera/2012-08-07 21.28.43.jpg","jpg", null);
		// 8、打印结果
		for (String string : file) {
			System.out.println(string);
			
		}
		
	}

}
