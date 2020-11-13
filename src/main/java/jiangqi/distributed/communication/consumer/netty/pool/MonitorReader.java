package jiangqi.distributed.communication.consumer.netty.pool;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.general.def.TestDef;

public class MonitorReader extends Thread{
	private static Logger logger = Logger.getLogger(MonitorReader.class); 

	@Override
	public void run() {
		while(true){
			try {
				synchronized(this){
					logger.info("MonitorReader run  #0");
					WaitPoolManager.invalidBean();
					this.wait(TestDef.READER_TIME_OUT/2);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
