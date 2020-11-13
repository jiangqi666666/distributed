package jiangqi.distributed.communication.general.bean;

import java.io.Serializable;

public class ProtocolBean implements Serializable{
	
	private static final long serialVersionUID = -3091771604298291191L;
	
	private long id=0;
	private String serverName="";
	private String group="";
	private String version="";
	private MethodBean method=new MethodBean();
	private Boolean isException=new Boolean(false);
	private Object ret=null;
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public MethodBean getMethod() {
		return method;
	}
	public void setMethod(MethodBean method) {
		this.method = method;
	}
	public Boolean getIsException() {
		return isException;
	}
	public void setIsException(Boolean isException) {
		this.isException = isException;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Object getRet() {
		return ret;
	}
	public void setRet(Object ret) {
		this.ret = ret;
	}
	
}
