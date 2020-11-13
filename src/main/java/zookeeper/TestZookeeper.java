package zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class TestZookeeper implements  Runnable{
	 public void run() {
	        CuratorFramework client = CuratorFrameworkFactory.builder()
	            .connectString("192.168.3.101:2181,192.168.3.102:2182,192.168.3.103:2183")
	            .sessionTimeoutMs(5000)
	            .connectionTimeoutMs(3000)
	            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
	            .build();
	        client.start();

	        /**
	         * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
	         */
	        ExecutorService pool = Executors.newFixedThreadPool(1);

	        /**
	         * 监听子节点的变化情况
	         */
	        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-huey", true);
	        try {
				childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
				childrenCache.getListenable().addListener(
			            new PathChildrenCacheListener() {
			                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
			                        throws Exception {
			                        switch (event.getType()) {
			                        case CHILD_ADDED:
			                            System.out.println("jq CHILD_ADDED: " + event.getData().getPath());
			                            break;
			                        case CHILD_REMOVED:
			                            System.out.println("jq CHILD_REMOVED: " + event.getData().getPath());
			                            break;
			                        case CHILD_UPDATED:
			                            System.out.println("jq CHILD_UPDATED: " + event.getData().getPath());
			                            break;
			                        default:
			                            break;
			                    }
			                }
			            },
			            pool
			        );

			        Thread.sleep(5000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
	        
	    }
	}


