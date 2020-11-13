package jiangqi.distributed.communication.general.bean;

public class DefClass {
	public static final int CLIENT_COUNT=10000;
	public static final long WRITE_TIME_OUT=3000L;
	public static final long READER_TIME_OUT=3000L;
	
	public static final int POOL_MESSAGE_TYPE_WRITE_TIME_OUT=0;
	public static final int POOL_MESSAGE_TYPE_READER_TIME_OUT=1;
	public static final int POOL_MESSAGE_TYPE_RET=2;
	
	public static final long SRV_WORK_TIME=100;
	
	public static final long SRVID_PWRINCSS=10000L;
	public static final String SRVID_FILE="srv.dat";

}
