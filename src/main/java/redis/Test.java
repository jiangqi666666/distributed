package redis;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class Test implements Runnable {
	private CountDownLatch latch;
	private int val;
	public final static int COUNT=10000;
	private ShardedJedisPool pool;

	public Test(int val, CountDownLatch latch,ShardedJedisPool pool) {
		this.val = val;
		this.latch = latch;
		this.pool=pool;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CountDownLatch latch = new CountDownLatch(COUNT);
		ExecutorService service = Executors.newFixedThreadPool(3);
		
		ArrayList<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.3.101",6379));
		//shards.add(new JedisShardInfo("192.168.3.102",6379));
		//shards.add(new JedisShardInfo("192.168.3.103",6379));
		
		
		JedisPoolConfig conf=new JedisPoolConfig();
		conf.setTestOnBorrow(false);
		conf.setMaxIdle(10);
		conf.setMaxWaitMillis(100000);
		conf.setMaxTotal(50);
		
		ShardedJedisPool pool = new ShardedJedisPool(conf, shards); 
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < COUNT; i++) {
			service.execute(new Test(i, latch,pool));
		}

		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");
		
		pool.destroy();
		pool.close();
		System.exit(0);
	}

	public void run() {
		// TODO Auto-generated method stub
		String key="%d-%d";
		ShardedJedis jedis=null;
		//Jedis jedis=null;
		try{
			jedis=this.pool.getResource();
			String tmp;
			for(int i=0;i<100;i++){
				tmp=String.format(key, this.val,i);
				jedis.set(tmp, "aaaa");
				
				System.out.println(tmp);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(jedis!=null){
				this.pool.returnResource(jedis);
				//jedis.close();
			}
			
			this.latch.countDown();
		}
	}

}
