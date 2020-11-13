package jiangqi.distributed.communication.producer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class ProducerProxy {
	private static Logger logger = Logger.getLogger(ProducerProxy.class); 
	
	public static Object getProducer(ApplicationContext context,String serverName){
		logger.info("ProducerProxy.getProducer");
		
		return context.getBean(serverName);
	}
}
