package redis.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import junit.framework.TestCase;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class ShardedJedisSentinelPoolTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testX() throws Exception {

		GenericObjectPoolConfig config = new GenericObjectPoolConfig();

		List<String> masters = new ArrayList<String>();
		masters.add("a7001");
		masters.add("a7002");
		masters.add("a7003");

		Set<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.3.101:27001");
		sentinels.add("192.168.3.101:27002");
		sentinels.add("192.168.3.101:27003");

		ShardedJedisSentinelPool pool = new ShardedJedisSentinelPool(masters, sentinels, config, 60000);

		/*ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			System.out.println("ret="+jedis.set("aa", "aa"));
			// do somethind...
			// ...
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
			pool.destroy();
		}*/

		ShardedJedis j = null;
		for (int i = 0; i < 10000; i++) {
			try {
				j = pool.getResource();
				j.set("KEY: " + i, "" + i);
				System.out.println(i);
				System.out.println(" ");
				Thread.sleep(500);
				pool.returnResource(j);
			} catch (JedisConnectionException e) {
				//e.printStackTrace();
				System.out.println("x");
				i--;
				Thread.sleep(1000);
			}
		}

		System.out.println("");

		for (int i = 0; i < 10000; i++) {
			try {
				j = pool.getResource();
				assertEquals(j.get("KEY: " + i), "" + i);
				System.out.println(".");
				Thread.sleep(500);
				pool.returnResource(j);
			} catch (JedisConnectionException e) {
				System.out.print("x");
				i--;
				Thread.sleep(1000);
			}
		}

		pool.destroy();
	}
}
