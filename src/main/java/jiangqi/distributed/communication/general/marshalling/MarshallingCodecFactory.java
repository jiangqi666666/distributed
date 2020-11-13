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
package jiangqi.distributed.communication.general.marshalling;

import org.apache.log4j.Logger;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * @author Administrator
 * @date 2014年3月15日
 * @version 1.0
 */
public final class MarshallingCodecFactory {
	private static Logger logger = Logger.getLogger(MarshallingCodecFactory.class); 

	public static MarshallingDecoder buildMarshallingDecoder(){
		logger.info("MarshallingCodecFactory.buildMarshallingDecoder #0");
		
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		UnmarshallerProvider provider=new DefaultUnmarshallerProvider(marshallerFactory,configuration);
		MarshallingDecoder deco=new MarshallingDecoder(provider,1024);
		return deco;
	}
	
	public static MarshallingEncoder buildMarshallingEncoder(){
		logger.info("MarshallingCodecFactory.buildMarshallingEncoder  #0");
		
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		MarshallerProvider provider=new DefaultMarshallerProvider(marshallerFactory,configuration);
		MarshallingEncoder enc=new MarshallingEncoder(provider);
		return enc;
	}
}
