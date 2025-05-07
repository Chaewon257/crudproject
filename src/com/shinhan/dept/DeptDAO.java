package com.shinhan.dept;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.shinhan.day15.DBUtil;

//비지니스 로직(업무로직)중에서 데이터베잇 관련업무는 DAO에서 작성한다. 
//CRUD작업 
//DAO(Data Access Object)
public class DeptDAO {
	//DB연결, 해제시 사용
	Connection conn;
	//SQL문을 DB에 전송
	Statement st;
	PreparedStatement pst;
	//Select결과
	ResultSet rs;
	//insert,delete,update결과는 영향받은 건수 
	int resultCount;
	
	static final String SELECT_ALL = "select * from departments";
	static final String SELECT_DETAIL = "select * from departments where department_id=?";
	static final String INSERT = "insert into departments values(?,?,?,?)";
	static final String UPDATE = "update departments set "
								+ " department_name=?,manager_id=?,location_id=? "
								+ " where department_id=?";
	static final String DELETE = "delete from departments where department_id=?";
	
	//1.Select(Read)..모두보기 
	public List<DeptDTO> selectAll() {
		List<DeptDTO> deptlist = new ArrayList<DeptDTO>();
		conn = DBUtil.getConnection();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(SELECT_ALL);
			while(rs.next()) {
				DeptDTO dept = makeDept(rs);
				deptlist.add(dept);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return deptlist;
	}
	//2.Select(Read)..상세보기
	public DeptDTO selectById(int deptid) {
		DeptDTO dept = null;
		conn = DBUtil.getConnection();
		try {
			pst = conn.prepareStatement(SELECT_DETAIL);
			pst.setInt(1, deptid);
			rs = pst.executeQuery();
			while(rs.next()) {
			 dept = makeDept(rs);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.dbDisconnect(conn, pst, rs);
		}
		
		return dept;
	}
	
	//3.Inert
	public int insertDept(DeptDTO dept) {
		conn = DBUtil.getConnection();
		try {
			pst = conn.prepareStatement(INSERT);
			pst.setInt(1, dept.getDepartment_id());
			pst.setString(2, dept.getDepartment_name());
			pst.setInt(3, dept.getManager_id());
			pst.setInt(4, dept.getLocation_id());
			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, null);
		}
		return resultCount;
	}
	//4.Update
	public int updateDept(DeptDTO dept) {
		conn = DBUtil.getConnection();
		try {
			pst = conn.prepareStatement(UPDATE);
			pst.setInt(4, dept.getDepartment_id());
			pst.setString(1, dept.getDepartment_name());
			pst.setInt(2, dept.getManager_id());
			pst.setInt(3, dept.getLocation_id());
			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, null);
		}
		return resultCount;
	}
	
	//5.Delete
	public int deleteDept(int deptid) {
		conn = DBUtil.getConnection();
		try {
			pst = conn.prepareStatement(DELETE);
			pst.setInt(1, deptid);
			resultCount = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, pst, null);
		}
		return resultCount;
	}
	private DeptDTO makeDept(ResultSet rs) throws SQLException {
		DeptDTO dept = DeptDTO.builder()
			.department_id(rs.getInt(1))
			.department_name(rs.getString(2))
			.manager_id(rs.getInt(3))
			.location_id(rs.getInt(4))
			.build();
		return dept;
	}
	
}






