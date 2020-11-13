package jiangqi.distributed.tools.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import jiangqi.distributed.communication.producer.ProducerProxy;

public class ShowMsg {
	private static DbManager db = new DbManager();
	private static final String file = "log.txt";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		
		String ss="id[%d] 毫秒[%d] 信息[%s] 线程[%s] 类[%s] 代码行[%d]";
		
		FileWriter write = new FileWriter(file);
		BufferedWriter wr = new BufferedWriter(write);
		
		//InfoManager aa=new InfoManager();
		db.init();
		
		String tmp;
		InfoBean bean;
		Long id=0L;
		Iterator<Long> ite=db.getId().iterator();
		while(ite.hasNext()){
			id=ite.next();
			Iterator<InfoBean> itt=db.getBean(id).iterator();
			
			while(itt.hasNext()){
				bean=itt.next();
				tmp=String.format(ss, bean.getThreadid(),bean.getHaomiao(),bean.getTxt(),bean.getXiancheng(),bean.getLeiming(),bean.getWeizhi());
				
				wr.write(tmp);
				wr.newLine();
			}
			wr.write("-----------------------");
			wr.newLine();
			wr.newLine();
		}
		
		wr.close();
		write.close();
		db.close();
	}
}
