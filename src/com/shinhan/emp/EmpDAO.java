package com.shinhan.emp;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.shinhan.day15.DBUtil;

// DAO(Data Access Object): DB의 CRUD작업(select, insert, update, delete)
// 실제 작업이 있음
// Statement는 sql을 문을 보내는 통로인데...바인딩변수 지원하지 않음
// PreparedStatement: Statement를 상속받음, 바인딩 변수 지원함, sp호출 지원 안 함
// CallableStatement: sp 호출을 지원함
public class EmpDAO {
	
	// Stored Procedure를 실행하기
	public EmpDTO execute_sp(int empid) {
		// 직원 번호를 받아서 email과 salary 리턴하기
		EmpDTO emp = null;
		
		Connection conn = DBUtil.getConnection();
		CallableStatement st = null;
		
		String sql = "{call sp_empone (?,?,?)}";
		
		try {
			st = conn.prepareCall(sql);
			
			st.setInt(1, empid);
			st.registerOutParameter(2, Types.VARCHAR); // varchar 타입을 주는거임
			st.registerOutParameter(3, Types.DECIMAL);
			
			st.execute();
			emp = new EmpDTO();
			String email = st.getString(2);
			double sal = st.getDouble(3);
			emp.setEmail(email);
			emp.setSalary(sal);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return emp;		
	}
	
	// 모든 값 수정
	public int EmpUpdate(EmpDTO emp) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		Map<String, Object> dynamicSQL = new HashMap<>();
		
		if(emp.getFirst_name() != null) dynamicSQL.put("FIRST_NAME", emp.getFirst_name());
		if(emp.getLast_name()!=null) dynamicSQL.put("LAST_NAME", emp.getLast_name());
		if(emp.getEmail()!=null) dynamicSQL.put("EMAIL", emp.getEmail());
		if(emp.getPhone_number()!=null) dynamicSQL.put("PHONE_NUMBER", emp.getPhone_number());
		if(emp.getHire_date()!=null) dynamicSQL.put("HIRE_DATE", emp.getHire_date());
		if(emp.getJob_id()!=null) dynamicSQL.put("JOB_ID", emp.getJob_id());
		if(emp.getSalary()!=null) dynamicSQL.put("SALARY", emp.getSalary());
		if(emp.getCommission_pct()!=null) dynamicSQL.put(" COMMISSION_PCT", emp.getCommission_pct());
		if(emp.getManager_id()!=null) dynamicSQL.put("MANAGER_ID", emp.getManager_id());
		if(emp.getDepartment_id()!=null) dynamicSQL.put("DEPARTMENT_ID", emp.getDepartment_id());
		
		String sql = "update employees set ";
		String sql2 = " where EMPLOYEE_ID = ?";

		for(String key:dynamicSQL.keySet()) {
			sql += key + "=" + "?," ;
		}
		sql = sql.substring(0, sql.length()-1); // 마지막 , 삭제
		sql += sql2;
//		System.out.println(sql);
		
		try {
			st = conn.prepareStatement(sql);
			
			int i=1;
			for(String key:dynamicSQL.keySet()) {
		 		st.setObject(i++, dynamicSQL.get(key));
		 	}
			st.setInt(i, emp.getEmployee_id());
			
			result = st.executeUpdate(); // auto-commit
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	// 모든 값 수정
	public int EmpUpdate2(EmpDTO emp) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql_fname = "";
		if(emp.getFirst_name() != null) {
			sql_fname = "FIRST_NAME=?" + emp.getFirst_name();
		}
		String sql = """
				update employees set
					 FIRST_NAME=?,
					 LAST_NAME=?,
					 EMAIL=?,
					 PHONE_NUMBER=?,
					 HIRE_DATE=?,
					 JOB_ID=?,
					 SALARY=?,
					 COMMISSION_PCT=?,
					 MANAGER_ID=?,
					 DEPARTMENT_ID=? 
				 where employee_id = ?
				""";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(11, emp.getEmployee_id());
			
			st.setString(1, emp.getFirst_name());
			st.setString(2, emp.getLast_name());
			st.setString(3, emp.getEmail());
			st.setString(4, emp.getPhone_number());
			st.setDate(5, emp.getHire_date());
			st.setString(6, emp.getJob_id());
			st.setDouble(7, emp.getSalary());
			st.setDouble(8, emp.getCommission_pct());
			st.setInt(9, emp.getManager_id());
			st.setInt(10, emp.getDepartment_id());
			
			result = st.executeUpdate(); // auto-commit
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	// 입력
	public int EmpInsert(EmpDTO emp) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		String sql = """
				insert into employees(
				 EMPLOYEE_ID,
				 FIRST_NAME,
				 LAST_NAME,
				 EMAIL,
				 PHONE_NUMBER,
				 HIRE_DATE,
				 JOB_ID,
				 SALARY,
				 COMMISSION_PCT,
				 MANAGER_ID,
				 DEPARTMENT_ID)
				 values(?,?,?,?,?,?,?,?,?,?,?)
				""";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, emp.getEmployee_id());
			st.setString(2, emp.getFirst_name());
			st.setString(3, emp.getLast_name());
			st.setString(4, emp.getEmail());
			st.setString(5, emp.getPhone_number());
			st.setDate(6, emp.getHire_date());
			st.setString(7, emp.getJob_id());
			st.setDouble(8, emp.getSalary());
			st.setDouble(9, emp.getCommission_pct());
			st.setInt(10, emp.getManager_id());
			st.setInt(11, emp.getDepartment_id());
			
			result = st.executeUpdate(); // auto-commit
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	// 특정 직원 삭제
	public int EmpDeleteByID(int empid) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		String sql = "delete from employees where employee_id = ?";
		
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, empid);
			result = st.executeUpdate(); // auto-commit
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<EmpDTO> selectByCondition(Integer[] deptid, String jobid, int salary, String hiredate) {
		List<EmpDTO> emplist = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null; 
		ResultSet rs = null;
		
		// 가변 문자열
		String deptStr = Arrays.stream(deptid)
							   .map(s->"?")
							   // 배열을 다시 문자열로 바꿈
							   .collect(Collectors.joining(",")); //?,?,?
		
		String sql = """
					select * from employees 
					where department_id in ( """ + deptStr + """  
					) and job_id like ? 
					and salary >= ?
					and hire_date >= ?
				"""; 
		
		int idx = 1;
		try {
			st = conn.prepareStatement(sql); // sql문을 준비한다.
			for(Integer dept:deptid) {
				st.setInt(idx++, dept); 
			}
			st.setString(idx++, "%"+jobid+"%"); 
			st.setInt(idx++, salary);
			
			Date d = DateUtil.convertToSQLDate(DateUtil.convertToDate(hiredate));
			st.setDate(idx++, d);
			
			rs = st.executeQuery(); 
			
			while(rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emplist;
		
	}
	
	// job&부서로 직원 조회
	public List<EmpDTO> selectByJobAndDept(String job, int dept) { 
		List<EmpDTO> emplist = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null; 
		ResultSet rs = null;
		
		String sql = "select * from employees where job_id = ? and department_id = ?"; 

		try {
			st = conn.prepareStatement(sql); // sql문을 준비한다.
			st.setString(1, job); // 1번쨰 물음표에 값을 세팅
			st.setInt(2, dept); // 2번쨰 물음표에 값을 세팅
			
			rs = st.executeQuery(); 
			
			while(rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emplist;
	}
	// job별 직원 조회
	public List<EmpDTO> selectByJob(String job) { 
		List<EmpDTO> emplist = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null; 
		ResultSet rs = null;
		
//		String sql = "select * from employees where job_id = '" + job + "'";  <- 이거 안 좋음 
		String sql = "select * from employees where job_id = ?"; // ?은 바인딩 변수인데(가변변수) Statement이 이걸 지원 안 함
		
		try {
			st = conn.prepareStatement(sql); // sql문을 준비한다.
			st.setString(1, job); // 1번쨰 물음표에 값을 세팅
			
			rs = st.executeQuery(); // 여기에 sql주면 안 됨. ?가 그대로 넘어가버림
			
			while(rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emplist;
	}
	
	// 부서의 직원 조회
	public List<EmpDTO> selectByDept(int deptID) { 
		List<EmpDTO> emplist = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from employees where department_id = " + deptID;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emplist;
	}
	
	// 직원 번호로 직원 정보를 상세보기
	public EmpDTO selectByID(int empid) { // 한 건임
				
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from employees where employee_id = " + empid;
		
		EmpDTO emp = null;
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if(rs.next()) { // 어짜피 한 건이라 while 써도 댐
				emp = makeEmp(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emp;
	}
	
	// 모든 직원 조회
	public List<EmpDTO> selectAll() { 
		List<EmpDTO> emplist = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from employees";
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				EmpDTO emp = makeEmp(rs);
				emplist.add(emp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return emplist;
	}

	private EmpDTO makeEmp(ResultSet rs) throws SQLException {
		EmpDTO emp = EmpDTO.builder()
					.commission_pct(rs.getDouble("commission_pct"))
					.department_id(rs.getInt("department_id"))
					.email(rs.getString("email"))
					.employee_id(rs.getInt("employee_id"))
					.first_name(rs.getString("first_name"))
					.hire_date(rs.getDate("hire_date"))
					.job_id(rs.getString("job_id"))
					.last_name(rs.getString("last_name"))
					.manager_id(rs.getInt("manager_id"))
					.phone_number(rs.getString("phone_number"))
					.salary(rs.getDouble("salary"))
					.build();
		
		return emp;
	}
	
	
}
