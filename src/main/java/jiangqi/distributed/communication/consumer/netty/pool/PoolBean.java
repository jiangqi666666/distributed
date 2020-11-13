package jiangqi.distributed.communication.consumer.netty.pool;

import jiangqi.distributed.communication.general.bean.ProtocolBean;

public class PoolBean {
	
	private ProtocolBean obj=null;
	private String srvName=null;
	private long time;
	private long threadId;
	private boolean isEnd=false;
	
	public PoolBean(long threadId,long time){
		this.time=time;
		this.threadId=threadId;
	}
	
	public ProtocolBean getObj() {
		return obj;
	}

	public void setObj(ProtocolBean obj) {
		this.obj = obj;
	}

	public long getTime() {
		return time;
	}
	
	public long getThreadId() {
		return threadId;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public String getSrvName() {
		return srvName;
	}

	public void setSrvName(String srvName) {
		this.srvName = srvName;
	}
	
}
