package jiangqi.distributed.communication.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import jiangqi.distributed.communication.consumer.netty.IPoolListern;
import jiangqi.distributed.communication.consumer.netty.NettyClient;
import jiangqi.distributed.communication.consumer.netty.PoolNotifier;
import jiangqi.distributed.communication.consumer.netty.pool.PoolBean;
import jiangqi.distributed.communication.consumer.netty.pool.WaitPoolManager;
import jiangqi.distributed.communication.consumer.server.ServerIdManager;
import jiangqi.distributed.communication.consumer.server.ServerManager;
import jiangqi.distributed.communication.general.bean.MethodBean;
import jiangqi.distributed.communication.general.bean.ProtocolBean;
import jiangqi.distributed.communication.general.def.TestDef;
import jiangqi.distributed.communication.general.exception.ReaderException;
import jiangqi.distributed.communication.general.exception.WriteException;

public class ProxyHandler implements IPoolListern, InvocationHandler{
	private static Logger logger = Logger.getLogger(ProxyHandler.class); 
	
	private String serverName;
	private String group;
	private String version; 
	
	private  Integer lock=0;
	private boolean retOk=false;
	private int retType=-1;
	private ProtocolBean obj=null;

	public ProxyHandler(String serverName,String group,String version){
		this.serverName=serverName;
		this.group=group;
		this.version=version;
	}
	
	private PoolBean createPoolBean(Method method, Object[] args,long id){
		logger.info("ProxyHandler.createPoolBean  id=#"+id);
		
		MethodBean methodBean=new MethodBean();
		methodBean.setName(method.getName());
		methodBean.setArgs(args);
		
		ProtocolBean protocolBean=new ProtocolBean();
		protocolBean.setId(id);
		protocolBean.setServerName(serverName);
		protocolBean.setVersion(version);
		protocolBean.setGroup(group);
		protocolBean.setMethod(methodBean);
		
		PoolBean poolBean=new PoolBean(id,System.currentTimeMillis());
		poolBean.setObj(protocolBean);
		poolBean.setSrvName("node1");
		
		return poolBean;
	}
	
	
	
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		retOk=false;
		retType=-1;
		obj=null;
		
		long id=ServerIdManager.getServerId();
		logger.info("ProxyHandler.invoke id=#"+id);
		PoolNotifier.addListend(id,this);
		
		PoolBean bean= createPoolBean( method,  args,id);
		WaitPoolManager.putBean(bean);
		NettyClient tmp=ServerManager.getNettyClient(bean.getSrvName());
		if(tmp!=null)
			tmp.sendMsg(bean.getObj());
		
		while(true){
			synchronized(this.lock){
				if(this.retOk==false)
					try {
						this.lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				else{
					switch(this.retType){
					case TestDef.POOL_MESSAGE_TYPE_WRITE_TIME_OUT:
						PoolNotifier.rmListend(id);
						throw new WriteException();
					case TestDef.POOL_MESSAGE_TYPE_READER_TIME_OUT:
						PoolNotifier.rmListend(id);
						throw new ReaderException();
					case TestDef.POOL_MESSAGE_TYPE_RET:
						PoolNotifier.rmListend(id);
						if(this.obj.getIsException()==true){
							Exception e=new Exception((String)this.obj.getRet());
							throw e;
						}
					default:
						PoolNotifier.rmListend(id);
					}
					break;
				}
			}
		}
		
		logger.info("ProxyHandler.invoke end id=#"+id);
		return this.obj.getRet();
	}

	public void onWriteTimeOut() {
		// TODO Auto-generated method stub
		logger.info("ProxyHandler.onWriteTimeOut ");
		
		this.retOk=true;
		this.retType=TestDef.POOL_MESSAGE_TYPE_WRITE_TIME_OUT;
		
		synchronized(this.lock){
			this.lock.notifyAll();
		}
		
	}

	public void onReaderTimeOut() {
		// TODO Auto-generated method stub
		logger.info("ProxyHandler.onReaderTimeOut ");
		
		this.retOk=true;
		this.retType=TestDef.POOL_MESSAGE_TYPE_READER_TIME_OUT;
		
		synchronized(this.lock){
			this.lock.notifyAll();
		}
	}

	public void onRetMsg(ProtocolBean bean) {
		// TODO Auto-generated method stub
		logger.info("ProxyHandler.onRetMsg id=#"+bean.getId());
		
		this.retOk=true;
		this.retType=TestDef.POOL_MESSAGE_TYPE_RET;
		
		this.obj=bean;
		synchronized(this.lock){
			this.lock.notifyAll();
		}
	}

}
