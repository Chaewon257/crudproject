package com.shinhan.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	// DB 연결함수
	public static Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:oracle:thin:@192.168.0.135:1521:xe";
		String userid = "tour", userpass = "1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userid, userpass);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}
	// DB 연결시 사용한 자원 해제하는 함수
	public static void dbDisconnect(Connection conn, Statement st, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(st != null) st.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}