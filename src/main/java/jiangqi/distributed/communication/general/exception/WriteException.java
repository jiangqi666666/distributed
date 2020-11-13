package jiangqi.distributed.communication.general.exception;

public class WriteException extends Exception {

	private static final long serialVersionUID = 4464419322123184321L;

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		
		System.out.println("write in pool time out");
	}
	
	
}
