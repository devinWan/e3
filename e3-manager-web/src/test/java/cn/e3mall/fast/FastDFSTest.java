package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.utils.FastDFSClient;

public class FastDFSTest {
	@Test
	public void testUpload() throws Exception{
		//创建一个配置文件,文件名任意,内容就是tracker服务器的地址
		//使用全局对象加载配置文件
		ClientGlobal.init("C:/eclipseluna/jx_workspace/e3-manager-web/src/main/resources/config/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackerClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StoreageServer的引用,可以是null
		StorageServer storageServer = null;
		//创建一个StoreClient,参数需要TrackerServer和StoreageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//使用StoreClient上传文件
		//第一个参数:文件的全路径,注意,复制过来的路径有隐藏字符
		String[] strings = storageClient.upload_file("C:/Users/dongyue/Pictures/Saved Pictures/1.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
			
		}
	}
	@Test
	public void testFastDfsClient() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("C:/eclipseluna/jx_workspace/e3-manager-web/src/main/resources/config/client.conf");
		String string = fastDFSClient.uploadFile("C:/Users/dongyue/Pictures/Saved Pictures/a246e4346d1eb4e41ea870573e2e1cce.jpg");
		System.out.println(string);
	}
	
	
}
