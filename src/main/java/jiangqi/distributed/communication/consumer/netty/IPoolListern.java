package jiangqi.distributed.communication.consumer.netty;

import jiangqi.distributed.communication.general.bean.ProtocolBean;

public interface IPoolListern {
	public void onWriteTimeOut();
	public void onReaderTimeOut();
	public void onRetMsg(ProtocolBean bean);
}
