/*
 * Copyright 2013-2018 Lilinfeng.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jiangqi.distributed.communication.producer.netty;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jiangqi.distributed.communication.general.marshalling.MarshallingCodecFactory;

public class NettyServer {
	private static Logger logger = Logger.getLogger(NettyServer.class); 
	//private static Log logger = LogFactory.getLog(NettyServer.class); 
	private ApplicationContext context ;

	public NettyServer(){
		this.context = new ClassPathXmlApplicationContext("srv.xml");
	}
	
	public void bind(int port) throws Exception {
		logger.info("NettyServer bind="+port);
		//String tmp="[%s][%d] %s%d";
		
		//System.out.println(toString().format(tmp, Thread.currentThread().getName(),System.currentTimeMillis(),"NettyServer bind=",port));
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup(2);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.handler(new LoggingHandler(LogLevel.TRACE))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// TODO Auto-generated method stub
							ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
							ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingEncoder());
							ch.pipeline().addLast(new RequestServerHandler(context));
						}
					});

			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		//PropertyConfigurator.configure("./config/log4j.properties");
		//PropertyConfigurator.configure( "E:/test/test/lib/log4j.properties" );
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {

			}
		}
		new NettyServer().bind(port);
	}
}
