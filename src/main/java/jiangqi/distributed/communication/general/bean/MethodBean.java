package jiangqi.distributed.communication.general.bean;

import java.io.Serializable;

public class MethodBean implements Serializable{

	private static final long serialVersionUID = -6290277983800573664L;
	
	private String name="";
	private Object[] args=null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
}
