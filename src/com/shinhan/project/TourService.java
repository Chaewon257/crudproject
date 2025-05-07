package com.shinhan.project;

import java.sql.SQLException;
import java.util.List;

public class TourService {
	
	MemberDAO memberDAO = new MemberDAO();
	ToursDAO toursDAO = new ToursDAO();
	AdminDAO adminDAO = new AdminDAO();
	
	public int adminInsert(AdminDTO admin) {
		return adminDAO.adminInsert(admin);
	}
	
	public boolean isAdminIdExists(String adminID) {
		return adminDAO.isAdminIdExists(adminID);
	}
	
	public String adminLogin(String adminID, String adminPW) {
		return adminDAO.adminLogin(adminID, adminPW);
	}
	
	public List<MemberDTO> selectAllMember(){
		return memberDAO.selectAllMember();
	}
	
	public List<BookingDTO> selectAllBooking(){
		return toursDAO.selectAllBooking();
	}
	
	public int tourInsert(ToursDTO tour) {
		return toursDAO.tourInsert(tour);
	}
	
	public int tourUpdate(ToursDTO tour) {
		return toursDAO.tourUpdate(tour);
	}
	
	public MemberDTO selectByPartID(String partID) {
		return memberDAO.selectByPartID(partID);
	}
	
	public int MemberUpdate(MemberDTO member) {
		return memberDAO.MemberUpdate(member);
	}
	
	public List<ScheduleDTO> checkSchedule(int tourID){
		return toursDAO.checkSchedule(tourID);
	}
	
	public int deleteBooking(int bookingNo) {
		return toursDAO.deleteBooking(bookingNo);
	}
	
	public List<BookingDTO> checkBooking(String partID){
		return toursDAO.checkBooking(partID);
	}
	
	public int bookingInsert(BookingDTO booking) throws SQLException {
		return toursDAO.bookingInsert(booking);
	}
	
	public boolean isToursExists(int tourID) {
		return toursDAO.isToursExists(tourID);
	}
	
	public List<ToursDTO> selectAllTours(){
		return toursDAO.selectAllTours();
	}
	
	public int memberInsert(MemberDTO member) {
		return memberDAO.memberInsert(member);
	}
	
	public boolean isIdExists(String partID) {
		return memberDAO.isIdExists(partID);
	}
	
	public String login(String partID, String partPW) {
		return memberDAO.login(partID, partPW);
	}
}
