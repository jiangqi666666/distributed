package jiangqi.distributed.communication.general.exception;

public class ReaderException extends Exception {
	
	private static final long serialVersionUID = 2976644414164613026L;

	public ReaderException(){
		
	}

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
		
		System.out.println("reader out pool time out");
			
	}
	
}
