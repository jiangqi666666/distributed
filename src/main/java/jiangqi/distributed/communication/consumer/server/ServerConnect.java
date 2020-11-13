package jiangqi.distributed.communication.consumer.server;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.consumer.netty.NettyClient;

public class ServerConnect implements  Runnable{

	private NettyClient client=null;
	private static Logger logger = Logger.getLogger(ServerConnect.class); 
	
	public ServerConnect(NettyClient client){
		
		this.client=client;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.info("this.client.connect() #0");
			this.client.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
