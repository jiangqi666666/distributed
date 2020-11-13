package redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class Test1 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args)  {
		// TODO Auto-generated method stub

		Set sentinels = new HashSet();
        sentinels.add(new HostAndPort("192.168.3.102", 26379).toString());
        sentinels.add(new HostAndPort("192.168.3.103", 26379).toString());
        
		JedisSentinelPool  pool=new JedisSentinelPool("master1",sentinels);
		
		Jedis jds=pool.getResource();
		
		String ret;
		
		while(true){
			try {
				ret=jds.get("a");
				System.out.println("get="+ret);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//jds.close();
		//pool.destroy();
		//pool.close();
		
	}

}
