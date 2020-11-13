
package jiangqi.distributed.communication.consumer.netty;

import org.apache.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import jiangqi.distributed.communication.general.bean.ProtocolBean;
import jiangqi.distributed.communication.general.def.TestDef;
import jiangqi.distributed.communication.general.marshalling.MarshallingCodecFactory;


/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class NettyClient {
	private static Logger logger = Logger.getLogger(NettyClient.class); 
	
	private Channel channel=null;
	private int port=0;
	private String host;
	
	public  synchronized void sendMsg(ProtocolBean request){
		logger.info("NettyClient.sendMsg:  request = #"+request.getId());
		
		channel.writeAndFlush(request);
	}
	
	public NettyClient(String host,int port ){
		this.port=port;
		this.host=host;
	}

	public  void connect() throws Exception {
		// 配置客户端NIO线程组
		logger.info("NettyClient.connect #0");
				
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.TRACE))
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
							ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingEncoder());
							//ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(5));
							ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(TestDef.READER_IDLE_TIME, TestDef.WRITE_IDLE_TIME,TestDef.ALL_IDLE_TIME));
							ch.pipeline().addLast(new RequestClientHandler());
						}
					});

			// 发起异步连接操作
			ChannelFuture f = b.connect(host, port).sync();
			
			channel=f.channel();
		
			// 当代客户端链路关闭
			f.channel().closeFuture().sync();
			System.out.println("close");
		} finally {
			// 优雅退出，释放NIO线程组
			group.shutdownGracefully();
		}
	}

}
