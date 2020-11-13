package jiangqi.distributed.tools.log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class InfoManager {
	private static final String file = "log4jtest.log";
	private static DbManager db = new DbManager();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InfoManager aa=new InfoManager();
		
		try {
			db.init();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ;
		}
		
		try {
			aa.go();
			db.commit();
			db.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.rollback();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.rollback();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.rollback();
		}
		
	}

	public static void go() throws ClassNotFoundException, SQLException, NumberFormatException, IOException {

		FileReader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);

		String str = null;

		while ((str = br.readLine()) != null) {
			String[] aa = str.split("#");
			InfoBean bean = new InfoBean();
			bean.setHaomiao(Integer.valueOf(aa[0]));
			bean.setXiancheng(aa[1]);
			bean.setLeiming(aa[2]);
			bean.setWeizhi(Integer.valueOf(aa[3]));
			bean.setTxt(aa[4]);
			bean.setThreadid(Long.valueOf(aa[5]));
			
			db.insert(bean);
		}

		br.close();
		reader.close();
	}
}
