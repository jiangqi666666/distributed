package jiangqi.distributed.communication.general.def;

public class TestDef {
	//public static final int CLIENT_COUNT=10000;
	public static final long WRITE_TIME_OUT=10000;
	public static final long READER_TIME_OUT=10000;
	
	public static final int POOL_MESSAGE_TYPE_WRITE_TIME_OUT=0;
	public static final int POOL_MESSAGE_TYPE_READER_TIME_OUT=1;
	public static final int POOL_MESSAGE_TYPE_RET=2;
	
	public static final long SRV_WORK_TIME=100;
	
	public static final int TEST_THREAD_COUNT=10000;
	
	public static final int READER_IDLE_TIME=60;
	public static final int WRITE_IDLE_TIME=60;
	public static final int ALL_IDLE_TIME=120;
}
