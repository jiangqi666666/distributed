package jiangqi.distributed.communication.producer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import jiangqi.distributed.communication.general.bean.ProtocolBean;

public class InvocationService {
	private static Logger logger = Logger.getLogger(InvocationService.class); 
	
	private ProtocolBean bean;
	private  ApplicationContext context;
	
	public InvocationService(ApplicationContext context,ProtocolBean bean){
		this.bean=bean;
		this.context=context;
	}

	public ProtocolBean Invocation(){
		logger.info("ProtocolBean.Invocation");
		
		ProtocolBean retBean=new ProtocolBean();
		retBean.setIsException(false);
		retBean.setId(this.bean.getId());
		
		Object obj=null;
		
		try {
			obj=call(ProducerProxy.getProducer(this.context, this.bean.getServerName()));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("ClassNotFoundException: "+e.getMessage());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("NoSuchMethodException: "+e.getMessage());
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("SecurityException: "+e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("IllegalArgumentException: "+e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("InvocationTargetException: "+e.getMessage());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retBean.setIsException(true);
			retBean.setRet("IllegalAccessException: "+e.getMessage());
		}
		
		if(retBean.getIsException().booleanValue()==false)
			retBean.setRet(obj);
		
		return retBean;
	}
	
	private Object call(Object obj) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IllegalAccessException{
		logger.info("ProtocolBean.call");
		
		Class<?>[] paraClass;
		if(this.bean.getMethod().getArgs()==null)
			paraClass=new Class<?>[0];
		else
			paraClass=new Class<?>[this.bean.getMethod().getArgs().length] ;
		
		for(int i=0;i<paraClass.length;i++)
			paraClass[i]=Class.forName(this.bean.getMethod().getArgs()[i].getClass().getName());
		
		
		Method method=obj.getClass().getMethod(this.bean.getMethod().getName(), paraClass);
		return method.invoke(obj, this.bean.getMethod().getArgs());
	}
}
