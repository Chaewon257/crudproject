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


public class ToursDAO {
	
	public List<BookingDTO> selectAllBooking(){
		List<BookingDTO> bookingList = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from booking order by tour_id";
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				BookingDTO booking = makeBooking(rs);
				bookingList.add(booking);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return bookingList;
	}
	
	public int tourInsert(ToursDTO tour) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = """
				insert into tours(
				tour_id,
				tour_name,
				start_date,
				end_date,
				tour_price,
				max_participants,
				tour_info)
				values(?,?,?,?,?,?,?)
				""";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, tour.getTour_id());
			st.setString(2, tour.getTour_name());
			st.setDate(3, tour.getStart_date());
			st.setDate(4, tour.getEnd_date());
			st.setInt(5, tour.getTour_price());
			st.setInt(6, tour.getMax_participants());
			st.setString(7, tour.getTour_info());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int tourUpdate(ToursDTO tour) {
		int result = 0;
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		Map<String, Object> dynamicSQL = new HashMap<>();
		
		if(tour.getTour_name() != null) dynamicSQL.put("TOUR_NAME", tour.getTour_name());
		if(tour.getStart_date() != null) dynamicSQL.put("START_DATE", tour.getStart_date());
		if(tour.getEnd_date() != null) dynamicSQL.put("END_DATE", tour.getEnd_date());
		if(tour.getTour_price() != null) dynamicSQL.put("TOUR_PRICE", tour.getTour_price());
		if(tour.getMax_participants() != null) dynamicSQL.put("MAX_PARTICIPANTS", tour.getMax_participants());
		if(tour.getTour_info() != null) dynamicSQL.put("TOUR_INFO", tour.getTour_info());
		
		String sql = "update tours set ";
		String sql2 = " where tour_id = ?";
		
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
			st.setInt(i, tour.getTour_id());
			
			result = st.executeUpdate(); 
		
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return result;
	}
	
	public List<ScheduleDTO> checkSchedule(int tourID){
		List<ScheduleDTO> schdList = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null; 
		ResultSet rs = null;
		
		String sql = "select * from schedule where tour_id = ?"; 
		
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, tourID);
			
			rs = st.executeQuery(); 
			
			while(rs.next()) {
				ScheduleDTO schd = makeSchedule(rs);
				schdList.add(schd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return schdList;
	}
	
	private ScheduleDTO makeSchedule(ResultSet rs) throws SQLException {
		ScheduleDTO schd = ScheduleDTO.builder()
							.tour_day(rs.getDate("tour_day"))
							.tour_id(rs.getInt("tour_id"))
							.activity(rs.getString("activity"))
							.tour_time(rs.getString("tour_time"))
							.build();
		return schd;
	}

	public int deleteBooking(int bookingNo) {
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		String sql = "delete from booking where booking_no = ?";
		
		try {
			st = conn.prepareStatement(sql);
			
			st.setInt(1, bookingNo);
			result = st.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	public List<BookingDTO> checkBooking(String partID){ // 에약 조회
		List<BookingDTO> bookingList = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null; 
		ResultSet rs = null;
		
		String sql = "select * from booking where part_id = ?"; 
		
		try {
			st = conn.prepareStatement(sql);
			st.setString(1, partID);
			
			rs = st.executeQuery(); 
			
			while(rs.next()) {
				BookingDTO booking = makeBooking(rs);
				bookingList.add(booking);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookingList;
	}
	
	private BookingDTO makeBooking(ResultSet rs) throws SQLException {
		BookingDTO booking = BookingDTO.builder()
							.booking_no(rs.getInt("booking_no"))
							.tour_id(rs.getInt("tour_id"))
							.part_id(rs.getString("part_id"))
							.booking_date(rs.getDate("booking_date"))
							.booking_status(rs.getString("booking_status"))
							.paid_amount(rs.getInt("paid_amount"))
							.build();
		return booking;
	}

	public int bookingInsert(BookingDTO booking) throws SQLException { // 여행 에약
		int result = 0; // 건수만 리턴
		
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		
		String sql = """
				insert into booking(
				booking_no,
				tour_id,
				part_id,
				booking_date,
				booking_status,
				paid_amount)
				values(?,?,?,?,?,?)
				""";

		st = conn.prepareStatement(sql);
		
		st.setInt(1, booking.getBooking_no());
		st.setInt(2, booking.getTour_id());
		st.setString(3, booking.getPart_id());
		st.setDate(4, booking.getBooking_date());
		st.setString(5, booking.getBooking_status());
		st.setInt(6, booking.getPaid_amount());
			
			result = st.executeUpdate();
		return result;
	}
	
	public boolean isToursExists(int tourID) {
		Connection conn = DBUtil.getConnection();
		PreparedStatement st = null;
		String sql = "SELECT COUNT(*) FROM tours WHERE tour_id = ?";
		
        try {
        	st = conn.prepareStatement(sql);
    		st.setInt(1, tourID);
    		
    		ResultSet rs = st.executeQuery();
    		
            if(rs.next()) {
            	return rs.getInt(1) > 0;  // 1개 이상 있으면 true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return true;
    }
	
	public List<ToursDTO> selectAllTours(){
		List<ToursDTO> toursList = new ArrayList<>();
		
		Connection conn = DBUtil.getConnection();
		Statement st = null;
		ResultSet rs = null;
		
		String sql ="select * from tours";
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ToursDTO tours = maketours(rs);
				toursList.add(tours);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return toursList;
	}

	private ToursDTO maketours(ResultSet rs) throws SQLException {
		ToursDTO tours = ToursDTO.builder()
						.tour_id(rs.getInt("tour_id"))
						.tour_name(rs.getString("tour_name"))
						.start_date(rs.getDate("start_date"))
						.end_date(rs.getDate("end_date"))
						.tour_price(rs.getInt("tour_price"))
						.max_participants(rs.getInt("max_participants"))
						.tour_info(rs.getString("tour_info"))
						.build();
		return tours;
	}
}
