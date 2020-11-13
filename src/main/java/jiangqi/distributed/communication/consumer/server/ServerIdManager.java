package jiangqi.distributed.communication.consumer.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.general.bean.DefClass;
import jiangqi.distributed.communication.general.bean.IdBean;

public class ServerIdManager {
	private static Logger logger = Logger.getLogger(ServerIdManager.class); 
	
	private static IdBean idBean;
	
	private static boolean saveIdBean(IdBean bean) {
		
		logger.info("ServerIdManager.saveIdBean bean.count="+bean.count+" #0");

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(DefClass.SRVID_FILE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(bean);
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static boolean init() {
		logger.info("ServerIdManager.init #0");
		
		FileInputStream fis=null;
		ObjectInputStream ois=null;

		try {
			fis = new FileInputStream(DefClass.SRVID_FILE);
			ois = new ObjectInputStream(fis);
			idBean = (IdBean) ois.readObject();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.info("文件不存在，重新创建，file="+DefClass.SRVID_FILE);
			
			idBean = new IdBean();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
			idBean.date = df.format(new Date());
			idBean.count = DefClass.SRVID_PWRINCSS;

			if(saveIdBean(idBean)==false)
				return false;
			else{
				idBean.count=0L;
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static synchronized long getServerId() {
		logger.info("ServerIdManager.getServerId #0");
		
		String id="%d%s";
		
		if((idBean.count%DefClass.SRVID_PWRINCSS)==0)
			allocaId();
		else
			idBean.count++;
		
		return  Long.valueOf(String.format(id,idBean.count,idBean.date));
	}

	private static void allocaId(){
		logger.info("ServerIdManager.allocaId #0");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String date=df.format(new Date());
		if(date.equals(idBean.date)==true){
			IdBean bean=new IdBean();
			bean.date=idBean.date;
			bean.count=(((idBean.count+1)/DefClass.SRVID_PWRINCSS)+1)*DefClass.SRVID_PWRINCSS;
			saveIdBean(bean);
			
			idBean.count++;
		}
		else{
			idBean.date=date;
			idBean.count=DefClass.SRVID_PWRINCSS;
			saveIdBean(idBean);
			idBean.count=0L;
		}
	}

}
