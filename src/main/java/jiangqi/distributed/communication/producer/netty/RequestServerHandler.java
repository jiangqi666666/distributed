/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package jiangqi.distributed.communication.producer.netty;

import io.netty.channel.ChannelHandler.Sharable;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import jiangqi.distributed.communication.general.bean.ProtocolBean;
import jiangqi.distributed.communication.producer.InvocationService;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
@Sharable
public class RequestServerHandler extends ChannelHandlerAdapter {
	private static Logger logger = Logger.getLogger(RequestServerHandler.class); 
	private  ApplicationContext context;
	
	public RequestServerHandler(ApplicationContext context){
		this.context=context;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("RequestServerHandler.channelRead");
		
		InvocationService server= new InvocationService(this.context,(ProtocolBean)msg);
		ctx.writeAndFlush(server.Invocation());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.info("RequestServerHandler.exceptionCaught");
		System.out.println("SSSSSSSSSSSSSSS");
		cause.printStackTrace();
		ctx.close();// 发生异常，关闭链路
	}
}
