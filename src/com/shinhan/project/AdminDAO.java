package com.shinhan.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
	
	public int adminInsert(AdminDTO admin) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into admin(
				admin_id,
				admin_pw)
				values(?,?)
				""";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setString(1, admin.getAdmin_id());
			st.setString(2, admin.getAdmin_pw());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean isAdminIdExists(String adminID) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = "SELECT COUNT(*) FROM admin WHERE ADMIN_ID = ?";
		
        try {
        	st = conn.prepareStatement(sql);
    		st.setString(1, adminID);
    		
    		ResultSet rs = st.executeQuery();
    		
            if(rs.next()) {
            	return rs.getInt(1) > 0;  // 1개 이상 있으면 true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return true;
    }
	
	public String adminLogin(String adminID, String adminPW) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "SELECT admin_id FROM admin WHERE admin_id = ? AND admin_pw = ?";
		MemberDTO mem = null;
		try {
			st = conn.prepareStatement(sql); 
			st.setString(1, adminID); 
			st.setString(2, adminPW);
			
			rs = st.executeQuery(); 
			if(rs.next()) { 
				 // 로그인 성공: id 리턴
				return rs.getString("admin_id");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return null;	
	}

	private AdminDTO makeAdmin(ResultSet rs) throws SQLException {
		AdminDTO admin = AdminDTO.builder()
						.admin_id(rs.getString("admin_id"))
						.admin_pw(rs.getString("admin_pw"))
						.build();
		return admin;
	}
}
