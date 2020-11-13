package jiangqi.distributed.communication.consumer.netty.pool;

import java.util.Iterator;
//import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.consumer.netty.PoolNotifier;
import jiangqi.distributed.communication.general.bean.ProtocolBean;
import jiangqi.distributed.communication.general.def.TestDef;

public class WaitPoolManager { 

	private static Logger logger = Logger.getLogger(WaitPoolManager.class);

	//private static HashMap<Long, PoolBean> pool = new HashMap<Long, PoolBean>(20000);
	private static ConcurrentHashMap<Long, PoolBean> pool = new ConcurrentHashMap<Long, PoolBean>(10240,32);
	
	private static final long TimeOut = TestDef.READER_TIME_OUT;
	private static PoolNotifier notifier = PoolNotifier.getNotifier();

	public static  void putBean(PoolBean bean) {
		//synchronized(pool){
			logger.info("WaitPoolManager.putReadBean = #" + bean.getThreadId());

			pool.put(bean.getThreadId(), bean);
		//}
	}

	public static void putRetMsg(ProtocolBean obj) {
		
		//synchronized(pool){
			logger.info("WaitPoolManager.putRetMsg = #" + obj.getId());

			PoolBean tmp = pool.get(obj.getId());
			if (tmp != null) {
				if (tmp.isEnd() == false) {
					tmp.setEnd(true);
					notifier.fireOnRetMsg(tmp.getThreadId(), obj);
				}
			}
		//}
	}

	public static void invalidBean() {
		
		//synchronized(pool){
			Iterator<ConcurrentMap.Entry<Long, PoolBean>> it = null;
			ConcurrentMap.Entry<Long, PoolBean> node = null;
			PoolBean tmp = null;
			long time = 0;

			if (pool.isEmpty() != true) {

				it = pool.entrySet().iterator();
				while (it.hasNext()) {
					node = it.next();
					tmp = node.getValue();

					if (tmp.isEnd() == false) {
						time = System.currentTimeMillis() - tmp.getTime();

						if (time > TimeOut) {
							logger.info("WaitPoolManager time out = " + tmp.getThreadId());
							// pool.remove(node.getKey());
							pool.remove(node.getKey());
							notifier.fireOnReaderTimeOut(tmp.getThreadId());
						}
					}
					else{
						pool.remove(node.getKey());
					}
				}
			}
		//}
	}

}
