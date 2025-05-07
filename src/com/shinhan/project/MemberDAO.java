package com.shinhan.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MemberDAO {
	
	public List<MemberDTO> selectAllMember(){
		List<MemberDTO> memberList = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from member";
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				MemberDTO member = makemem(rs);
				memberList.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return memberList;
	}
	
	public MemberDTO selectByPartID(String partID) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql ="select * from member where part_id = ?";
		
		MemberDTO member = null;
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, partID);
			
			rs = st.executeQuery();
			
			if(rs.next()) { 
				member = makemem(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		
		return member;
	}
	
	public int MemberUpdate(MemberDTO member) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		Map<String, Object> dynamicSQL = new HashMap<>();
		
		if(member.getPart_pw() != null) dynamicSQL.put("PART_PW", member.getPart_pw());
		if(member.getPart_name() != null) dynamicSQL.put("PART_NAME", member.getPart_name());
		if(member.getEmail() != null) dynamicSQL.put("EMAIL", member.getEmail());
		if(member.getPhone() != null) dynamicSQL.put("PHONE", member.getPhone());
		
		String sql = "update member set ";
		String sql2 = " where part_id = ?";
		
		for(String key:dynamicSQL.keySet()) {
			sql += key + "=" + "?," ;
		}
		sql = sql.substring(0, sql.length()-1); // 마지막 , 삭제
		sql += sql2;
		System.out.println(sql);
		
		try {
			st = conn.prepareStatement(sql);
			
			int i=1;
			for(String key:dynamicSQL.keySet()) {
		 		st.setObject(i++, dynamicSQL.get(key));
		 	}
			st.setString(i, member.getPart_id());
			
			result = st.executeUpdate(); 
		
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return result;
	}
	
	public int memberInsert(MemberDTO member) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into member(
				part_id,
				part_pw,
				part_name,
				email,
				phone)
				values(?,?,?,?,?)
				""";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setString(1, member.getPart_id());
			st.setString(2, member.getPart_pw());
			st.setString(3, member.getPart_name());
			st.setString(4, member.getEmail());
			st.setString(5, member.getPhone());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean isIdExists(String partID) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = "SELECT COUNT(*) FROM MEMBER WHERE PART_ID = ?";
		
        try {
        	st = conn.prepareStatement(sql);
    		st.setString(1, partID);
    		
    		ResultSet rs = st.executeQuery();
    		
            if(rs.next()) {
            	return rs.getInt(1) > 0;  // 1개 이상 있으면 true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return true;
    }
	
	public String login(String partID, String partPW) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "SELECT part_id FROM member WHERE part_id = ? AND part_pw = ?";
		MemberDTO mem = null;
		try {
			st = conn.prepareStatement(sql); 
			st.setString(1, partID); 
			st.setString(2, partPW);
			
			rs = st.executeQuery(); 
			if(rs.next()) { 
				 // 로그인 성공: id 리턴
				return rs.getString("part_id");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return null;	
	}

	private MemberDTO makemem(ResultSet rs) throws SQLException {
		MemberDTO member = MemberDTO.builder()
							.part_id(rs.getString("part_id"))
							.part_pw(rs.getString("part_pw"))
							.email(rs.getString("email"))
							.part_name(rs.getString("part_name"))
							.phone(rs.getString("phone"))
							.build();
		return member;
	}
}
