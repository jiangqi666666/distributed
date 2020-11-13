package jiangqi.distributed.communication.consumer;

import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;

public  class ConsumerProxy {
	private static Logger logger = Logger.getLogger(ConsumerProxy.class); 
	
	public static synchronized Object getServerInterface(String serverName,String interfaceName,String group,String version) throws IllegalArgumentException, ClassNotFoundException{
		logger.info("ConsumerProxy.getServerInterface  interfaceName="+interfaceName+" #0");
		
		return Proxy.newProxyInstance(
				Class.forName(interfaceName).getClassLoader(),
				new Class[]{Class.forName(interfaceName)},
				new ProxyHandler(serverName,group,version));
	}
}
