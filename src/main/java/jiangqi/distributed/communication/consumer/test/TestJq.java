package jiangqi.distributed.communication.consumer.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jiangqi.distributed.communication.consumer.server.ServerIdManager;
import jiangqi.distributed.communication.consumer.server.ServerManager;
import jiangqi.distributed.communication.consumer.test.TestServer;
import jiangqi.distributed.communication.general.def.TestDef;


public class TestJq implements  Runnable{
	private ApplicationContext context ;
	private CountDownLatch latch;
	private TestServer a1 ;
	
	private static Logger logger = Logger.getLogger(TestJq.class); 
	
	
	public TestJq(ApplicationContext context,CountDownLatch latch){
		logger.info("TestJq.TestJq() #0");

		this.context= context;
		this.latch=latch;
		
	}
	
	public void test(long aa,long cc){
		String out="Thread=%d item=%d ret=%d";
		
		Long bb=0L;
		try {
			long start=System.currentTimeMillis();
			
			logger.info("TestJq.test() #0");
			bb = a1.test(aa,cc);
			long end=System.currentTimeMillis();
			
			System.out.println("end="+end);
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
		CountDownLatch latch=new CountDownLatch(TestDef.TEST_THREAD_COUNT);
		long start=0;
		long end=0;
		try {
			ServerManager.init();
			ServerIdManager.init();
			
			ExecutorService service=Executors.newFixedThreadPool(4);
			start=System.currentTimeMillis();
			for(int i=0;i<TestDef.TEST_THREAD_COUNT;i++)
			{
				TestJq tmp=new TestJq(context,latch);
				service.execute(tmp);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			latch.await();
			end=System.currentTimeMillis();
			System.out.println("close time="+(end-start));
			//ServerManager.close();
			System.exit(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() {
		logger.info("create test start #"+Thread.currentThread().getId());
		a1 = this.context.getBean("testServer",TestServer.class);
		logger.info("create test start end #"+Thread.currentThread().getId());
		
		// TODO Auto-generated method stub
		for(long i=0;i<10;i++){
			logger.info("TestJq.run #"+Thread.currentThread().getId());
			test(Thread.currentThread().getId(),20);
			logger.info("TestJq.run.end #"+Thread.currentThread().getId());
		}
		
		this.latch.countDown();
	}
}
