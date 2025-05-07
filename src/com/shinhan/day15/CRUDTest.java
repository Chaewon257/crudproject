package com.shinhan.day15;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// CRUD(Create, Read, Update, Delete)
// Read(select)
public class CRUDTest {
	public static void main(String[] args) { // util 사용 버전
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = """
				select department_id, max(salary), min(salary)
				from employees
				group by department_id
				having max(salary) <> min(salary)
				""";
		
		conn = DBUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				int deptid = rs.getInt(1);
				int maxsal = rs.getInt(2);
				int minsal = rs.getInt(3);
				System.out.println(deptid + "\t" + maxsal + "\t" + minsal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}	
	}
	
	public static void main2(String[] args) { // exception 안 던지고 try-catch 버전
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userid = "hr", userpass = "hr";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = """
				select department_id, count(*)
				from employees
				group by department_id
				having count(*) >= 5
				order by 2 desc
				""";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userid, userpass);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				// 대소문자 구별 X
				int deptid = rs.getInt(1); // 1번째 컬럼
				int cnt = rs.getInt(2); // 1번째 컬럼
				System.out.println("부서코드: " + deptid  + "\t인원수: " + cnt);
			}
		} catch (ClassNotFoundException e) {
			// class 존재하지 않을 떄
			e.printStackTrace();
		} catch (SQLException e) {
			// conn이 잘못 됐을 떄
			e.printStackTrace();
		} finally {
			try {
				if(rs!= null)rs.close();
				if(st!= null)st.close();
				if(conn!= null)conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main1(String[] args) throws ClassNotFoundException, SQLException {
		// 1. JDBC Driver 준비 (class path추가)
		// 2. JDBC Driver가 메모리에 load돼야함
		Class.forName("oracle.jdbc.driver.OracleDriver"); // driver를 올리는 거임
		System.out.println("JDBC Driver load 성공");
		
		// 3. Connection
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userid = "hr", userpass = "hr";
		Connection conn = DriverManager.getConnection(url, userid, userpass);
		System.out.println("Connection 성공");
		
		// 4. SQL문 보낼 통로 얻기
		Statement st = conn.createStatement();
		System.out.println("Statement 통로 얻기 성공");
		
		// 5. SQL문 생성, 실행
		String sql = """
				select * 
				from employees
				where department_id = 80 
				"""; //(안에 ; 넣으면 안댐)
		ResultSet rs =  st.executeQuery(sql); // rs는 표와 비슷함
		while(rs.next()) {
			// 대소문자 구별 X
			int empid = rs.getInt("employee_id");
			String fname = rs.getString("first_name");
			Date hdate = rs.getDate("hire_date");
			double comm = rs.getDouble("commission_pct");
			
			System.out.printf("직원번호: %d, 직원이름: %s, 입사일: %s, 커미션: %3.1f\n", empid, fname, hdate, comm);
		}
		
		rs.close();
		st.close();
		conn.close();
	}
}
