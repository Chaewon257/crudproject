package com.shinhan.day15;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CRUDTest2 {
	public static void main(String[] args) throws SQLException {
		// 모두 성공하면 commit, 하나라도 실패하면 rollback
		// 이게 하나의 transaction
		// insert
		// update
		
		Connection conn = null;
		Statement st1 = null;
		Statement st2 = null;
		
		String sql1 = """
				insert into emp1(employee_id, first_name, last_name, hire_date, job_id, email) 
				        values(4, 'aa', 'bb', sysdate, 'IT', 'naver')
				""";
		String sql2 = """
				update emp1 set salary = 3000 
				where employee_id = 198
				""";
		
		conn = DBUtil.getConnection();
		conn.setAutoCommit(false); // auto commit 안 하도록 설정
		st1 = conn.createStatement();
		int result1 = st1.executeUpdate(sql1); // 이때 commit돼버림 <- 그래서 autoCommit안 하게 설정함
		
		st2 = conn.createStatement();
		int result2 = st2.executeUpdate(sql2);
		
		if(result1>=1 && result2 >=1) {
			conn.commit(); // 둘 다 성공했을 때만 db에 반영
		} else {
			conn.rollback(); // 한 쪽만 성공했을 때는 다시 원상복구 시켜야됨
		}
		
	}

	public static void main3(String[] args) throws SQLException {
		Connection conn = null;
		Statement st = null;
		int result = 0;

		String sql = """
				delete from emp1
				where employee_id < 100
				""";

		conn = DBUtil.getConnection();
		st = conn.createStatement();
		// insert 건수 나옴
		result = st.executeUpdate(sql); // DML을 업데이트함 <- 이때 자동으로 commit됨
		System.out.println(result >= 0 ? result + "건 delete success" : "delete fail");

	}

	public static void main2(String[] args) throws SQLException {
		Connection conn = null;
		Statement st = null;
		int result = 0; // insert 성공 여부만 알면 됨

//		String sql = """
//				insert into emp1 values(4, '박','채원','fejdf', 'phone', '25/04/23', 'job', 100,0.2,100,'20')
//				""";

		String sql = """
				update emp1
				set department_id = (
				            select department_id
				            from employees
				            where employee_id = 102),
				    salary = (
				            select salary
				            from employees
				            where employee_id = 103)
				where employee_id = 99
				""";

		conn = DBUtil.getConnection();
		st = conn.createStatement();
		// insert 건수 나옴
		result = st.executeUpdate(sql); // DML을 업데이트함 <- 이때 자동으로 commit됨
//		System.out.println(result>=1?"insert success":"insert fail");
		System.out.println(result >= 1 ? "update success" : "update fail");

	}

	public static void main1(String[] args) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		String sql = """
				select e.ename, e.sal
				from emp e join emp m on(e.mgr = m.empno)
				where e.ename in (
				            select ename
				            from emp
				            where m.ename = 'KING')
				""";
		conn = DBUtil.getConnection();
		st = conn.createStatement();
		rs = st.executeQuery(sql);

		while (rs.next()) {
			String a = rs.getString(1);
			int b = rs.getInt(2);

			System.out.println(a + "\t" + b);
		}

		DBUtil.dbDisconnect(conn, st, rs);
	}
}
