package zookeeper;

import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent; 
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode;
import org.apache.curator.framework.recipes.nodes.PersistentEphemeralNode.Mode; 

/**
 * Curator事件监听
 * @author  huey
 * @version 1.0 
 * @created 2015-3-2
 */
public class CarutorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
            //.connectString("192.168.3.101:2181,192.168.3.102:2182,192.168.3.103:2183")
        	.connectString("192.168.3.101:2181")
            .sessionTimeoutMs(5000)
            .connectionTimeoutMs(3000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();

      /*  client.create()
            .creatingParentsIfNeeded()
            .forPath("/zk-huey/cnode", "hello".getBytes());

        *//**
         * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
         *//*
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        pool.execute(new TestZookeeper());

        *//**
         * 监听数据节点的变化情况
         *//*
        final NodeCache nodeCache = new NodeCache(client, "/zk-huey/cnode", false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(
            new NodeCacheListener() {
                public void nodeChanged() throws Exception {
                    System.out.println("Node data is changed, new data: " + 
                        new String(nodeCache.getCurrentData().getData()));
                }
            }, 
            pool
        );

        *//**
         * 监听子节点的变化情况
         *//*
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-huey", true);
        childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
            new PathChildrenCacheListener() {
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                        throws Exception {
                        switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                }
            },
            pool
        );

        System.out.println("client.setData().forPath(/zk-huey/cnode, world.getBytes());");
        client.setData().forPath("/zk-huey/cnode", "world".getBytes());

        System.out.println(" client.create().forPath(/zk-huey/a1, hello.getBytes());");
        client.create().forPath("/zk-huey/a1", "hello".getBytes());

        Thread.sleep(1 * 1000);

        System.out.println(" create EPHEMERAL;");
        PersistentEphemeralNode node = new PersistentEphemeralNode(client, Mode.EPHEMERAL, "/zk-huey/jq", "临时节点".getBytes());
        node.start();

        Thread.sleep(1 * 1000);

        System.out.println(" delete zk-huey/a1;");
        client.delete().deletingChildrenIfNeeded().forPath("/zk-huey/a1");

        System.out.println(" delete /zk-huey/cnode;");
        client.delete().deletingChildrenIfNeeded().forPath("/zk-huey/cnode");

        Thread.sleep(1 * 1000);
        System.out.println("node.close()");
      //  node.close();
        System.out.println("node.1111111()");
        */
        
       /* client.create()
        .creatingParentsIfNeeded()
        .forPath("/zk-huey/cnode", "hello".getBytes());*/
        
        byte[] aa =client.getData().forPath("/zk-huey/cnode");
        System.out.println(new String(aa));
        
        String path="/dubbo/com.alibaba.dubbo.demo.user.UserService/providers";
       /* String path="/dubbo";
        byte[] bs =client.getData().forPath(path);
        System.out.println(new String(bs));*/
        
        List<String> sss=client.getChildren().forPath(path);
        for(String bb:sss){
        	;
        	System.out.println(URLDecoder.decode(bb, "UTF-8"));
        }
        
       // pool.shutdown();
        client.close();
    }
}

