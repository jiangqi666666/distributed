package jiangqi.distributed.tools.log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class DbManager {
	private Connection conn = null;

	public void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.conn = DriverManager.getConnection("jdbc:mysql://192.168.3.103:3306/test", "test", "111111");

		this.conn.setAutoCommit(false);
	}
	
	public void close(){
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void commit() {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insert(InfoBean bean) throws SQLException {
		String sql = "insert into netty(haomiao,xiancheng,leiming,weizhi,txt,threadid) values(?,?,?,?,?,?)";

		PreparedStatement ps = null;

		ps = this.conn.prepareStatement(sql);
		ps.setInt(1, bean.getHaomiao());
		ps.setString(2, bean.getXiancheng());
		ps.setString(3, bean.getLeiming());
		ps.setInt(4, bean.getWeizhi());
		ps.setString(5, bean.getTxt());
		ps.setLong(6, bean.getThreadid());

		ps.execute();

	}
	
	public ArrayList<InfoBean> getBean(Long id) throws SQLException{
		String sql="select * from netty where threadid=?";
		PreparedStatement ps = null;

		ps = this.conn.prepareStatement(sql);
		ps.setLong(1, id);

		ResultSet rst=ps.executeQuery();
		
		ArrayList<InfoBean> ret=new ArrayList<InfoBean>();
		
		while(rst.next()){
			InfoBean bean=new InfoBean();
			bean.setHaomiao(rst.getInt("haomiao"));
			bean.setLeiming(rst.getString("leiming"));
			bean.setThreadid(id);
			bean.setTxt(rst.getString("txt"));
			bean.setWeizhi(rst.getInt("weizhi"));
			bean.setXiancheng(rst.getString("xiancheng"));
			
			ret.add(bean);
		}
		
		return ret;
	}
	
	public ArrayList<Long> getId() throws SQLException{
		String sql="select distinct threadid from netty where threadid<>0";
		PreparedStatement ps = null;

		ps = this.conn.prepareStatement(sql);

		ResultSet rst=ps.executeQuery();
		
		ArrayList<Long> ret=new ArrayList<Long>();
		
		while(rst.next())
			ret.add(rst.getLong("threadid"));
		
		return ret;
	}

}
