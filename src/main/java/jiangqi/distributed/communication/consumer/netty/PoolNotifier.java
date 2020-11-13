package jiangqi.distributed.communication.consumer.netty;

import java.util.HashMap;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.general.bean.ProtocolBean;

public class PoolNotifier {
	private static Logger logger = Logger.getLogger(PoolNotifier.class); 
	
	private static PoolNotifier me=null;
	private static HashMap<Long,IPoolListern> listern=new HashMap<Long,IPoolListern>(20000);
	
	public static PoolNotifier getNotifier(){
		logger.info("PoolNotifier.getNotifier#0");
		
		if(me==null)
			me=new PoolNotifier();
		
		return me;
	}
	
	public static synchronized void addListend(Long id,IPoolListern me){
		logger.info("PoolNotifier.addListend:  id=#"+id);
		
		listern.put(id, me);
	}
	
	public static synchronized void rmListend(Long id){
		logger.info("PoolNotifier.rmListend:  id=#"+id);
		
		listern.remove(id);
	}
	
	public synchronized void  fireOnWriteTimeOut(Long id){
		logger.info("PoolNotifier.fireOnWriteTimeOut:  id=#"+id);
		
		IPoolListern node=listern.get(id);
		if(node!=null)
			node.onWriteTimeOut();
		
	}
	public synchronized void  fireOnReaderTimeOut(Long id){
		logger.info("PoolNotifier.fireOnReaderTimeOut:  id=#"+id);
		
		IPoolListern node=listern.get(id);
		if(node!=null)
			node.onReaderTimeOut();
		
	}
	public synchronized void  fireOnRetMsg(Long id,ProtocolBean bean){
		logger.info("PoolNotifier.fireOnRetMsg:  id=#"+id);
		
		IPoolListern node=listern.get(id);
		if(node!=null)
			node.onRetMsg(bean);
	}
}
