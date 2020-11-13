package jiangqi.distributed.communication.consumer.server;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import jiangqi.distributed.communication.consumer.netty.NettyClient;
import jiangqi.distributed.communication.consumer.netty.pool.MonitorReader;

public class ServerManager {
	private static HashMap<String,NettyClient> serverMap=new HashMap<String,NettyClient>();
	private static ExecutorService service;
	private static NettyClient client;
	private static ServerConnect serverConnect;
	private static MonitorReader monR;
	
	private static Logger logger = Logger.getLogger(ServerManager.class); 
		
	public static synchronized NettyClient getNettyClient(String srvName){
		logger.info("ServerManager.getNettyClient #0");
		
		return serverMap.get(srvName);
	}
	
	public static void close(){
		logger.info("ServerManager.close #0");
		service.shutdownNow();
	}
	
	public static void init() throws InterruptedException{
		
		logger.info("ServerManager.init #0");
		
		client= new NettyClient("192.168.3.103",8080);
		serverConnect=new ServerConnect(client);
		
		serverMap.put("node1", client);
		
		service = Executors.newFixedThreadPool(2);

		monR =new MonitorReader();	
		
		service.execute(serverConnect);
		Thread.sleep(3000);

		service.execute(monR);

	}
}
