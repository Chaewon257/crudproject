package com.shinhan.project;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.shinhan.emp.DateUtil;
import com.shinhan.emp.EmpView;

public class TourController { //implements CommonControllerInterface{
	
	static Scanner sc = new Scanner(System.in);
	static TourService tourService = new TourService();
	
	static String currentPartID = "";
	
//	@Override
//	public void execute() {
	
	public static void main(String[] args) {
		boolean isStop = false;
		while(!isStop) {
			// 회원 / 관리자 선택
			start();
			int job = sc.nextInt();
			switch(job) {
				case 1->{f_login();} // 회원 로그인
				case 2->{f_adminlogin();} // 관리자 로그인
				case 3->{f_signup();} // 회원가입
				case 0->{isStop = true;}
			}
			// 회원 로그인 성공 -> 메뉴 띄우기
			
			// 패키지 여행 조회(tours select), 패키지 여행 에약(booking insert, booked), 
			// 예약 확인(booking select), 예약 취소(booking insert, canceled)
			// 여행 일정 확인(스케줄 select), 회원정보 관리(member update)
			
			// 관리자 로그인 성공 -> 메뉴 띄우기
			// 패키지 여행 조회, 패키지 여행 수정
			// 여행 일정 조회, 여행 일정 수정, 전체 예약 확인
			

		
		}
		sc.close();
		System.out.println("=======서비스를 종료합니다=======");
		
	}

	private static void start() {
		// 회원/관리자 선택 로그인
		System.out.println("=======================================================");
		System.out.println("1.회원 로그인 2.관리자 로그인 3.회원가입 0.서비스 종료");
		System.out.println("=======================================================");
		System.out.print("작업을 선택하세요>> ");
		
	}
	
	private static void f_signup() {
		System.out.print("회원가입, 관리자가입 중 선택해주세요>> ");
		String mode = sc.next();
		
		if(mode.equals("회원가입")) {
		
			String partID = "";
			System.out.println("회원가입 정보를 입력해주세요.");
			while(true) {
				System.out.print("회원 ID>> ");
				partID = sc.next();
				if (!tourService.isIdExists(partID)) {
		            break;
		        }
		        System.out.println("이미 존재하는 ID입니다. 다른 ID를 입력해주세요.");
			}
			int result = tourService.memberInsert(makeMember(partID));
			TourView.display("회원가입 완료되었습니다.");
			
		} else if(mode.equals("관리자가입")) {
			String adminID = "";
			System.out.println("회원가입 정보를 입력해주세요.");
			while(true) {
				System.out.print("관리자 ID>> ");
				adminID = sc.next();
				if (!tourService.isAdminIdExists(adminID)) {
		            break;
		        }
		        System.out.println("이미 존재하는 관리자 ID입니다. 다른 ID를 입력해주세요.");
			}
			int result = tourService.adminInsert(makeAdmin(adminID));
			TourView.display("회원가입 완료되었습니다.");
		}
		
	}

	private static AdminDTO makeAdmin(String adminID) {
		System.out.print("회원 PW>> ");
		String adminPW = sc.next();
		
		AdminDTO admin = AdminDTO.builder()
						.admin_id(adminID).admin_pw(adminPW)
						.build();
		return admin;
	}

	private static MemberDTO makeMember(String partID) { // 회원가입
		System.out.print("회원 PW>> ");
		String partPW = sc.next();
		System.out.print("회원 Name>> ");
		String partName = sc.next();
		
		String partEmail = "";
		while (true) {
			System.out.print("회원 Email(user@domain.com)>> ");
			partEmail = sc.next();
			if (partEmail.matches("[\\w._%+-]+@[\\w.-]+\\.com")) {
	            break;
	        }
	        System.out.println("잘못된 이메일 형식입니다. user@domain.com 형태로 입력해주세요.");
		}
		
		String partPhone = "";
		while(true) {
			System.out.print("회원 Phone(###-####-####)>> ");
			partPhone = sc.next();
			
			if (partPhone.matches("\\d{3}-\\d{4}-\\d{4}")) {
	            break;
	        }
	        System.out.println("잘못된 전화번호 형식입니다. ###-####-#### 형태로 입력해주세요.");
		}
		
		MemberDTO member = MemberDTO.builder()
				.part_id(partID)
				.part_pw(partPW)
				.email(partEmail)
				.part_name(partName)
				.phone(partPhone)
				.build();
		
		return member;
	}
	
	private static void f_adminlogin() {
		System.out.print("ID 입력>> ");
		String partID = sc.next();
		System.out.print("Password 입력>> ");
		String partPW = sc.next();
		
		String member = tourService.adminLogin(partID, partPW);
		if(member != null) {
			System.out.println(member + "님 환영합니다!");
			boolean back = false;
			while (!back) {
				adminMenuDisplay();
	            int sel = sc.nextInt();
	            switch (sel) {
	                case 1 -> f_selectAllTours();
	                case 2 -> f_updateTours();
	                case 3 -> f_insertTours();
	                case 4 -> f_checkSchedule();
	                case 5 -> f_selectAllBooking();
	                case 6 -> f_selectAllmember();
	                case 0 -> back = true;
	                default -> System.out.println("잘못된 선택");
	            }
	        }
		} else {
			System.out.println("관리자 로그인에 실패하였습니다.");
		}
			
	}
	
	private static void f_selectAllmember() {
		List<MemberDTO> memberList = tourService.selectAllMember();
		TourView.display(memberList);
	}

	private static void f_selectAllBooking() {
		List<BookingDTO> bookingList = tourService.selectAllBooking();
		TourView.display(bookingList);
	}

	private static void f_insertTours() {
		System.out.println("신규 패키지 정보를 입력하세요");
		System.out.print("투어 ID>> ");
		int tourID = sc.nextInt();
		
		int result = tourService.tourInsert(makeTour(tourID));
		EmpView.display(result + "건이 추가되었습니다.");
	}

	private static void f_updateTours() {
		f_selectAllTours();
		System.out.println("수정할 투어 ID를 입력하세요.");
		int tourID = 0;
		while(true) {
			System.out.print("tour_id>> ");
			tourID = sc.nextInt();
			if (tourService.isToursExists(tourID)) {
	            break;
	        }
	        System.out.println("존재하지 않는 패키지 여행입니다.");
		}
		
		System.out.println("수정할 정보를 입력하세요. 수정을 원치 않는 정보는 *을 입력해주세요.");
		int result = tourService.tourUpdate(makeTour(tourID));
		TourView.display(result + "건이 수정됨.");
	}

	private static ToursDTO makeTour(int tourID) {
		System.out.print("투어 이름>> ");
		String tourName = sc.next();
		
		Date startDate = null;
		System.out.print("투어 시작일>> ");
		String sDate = sc.next();
		if(!sDate.equals("*")) {
			startDate = DateUtil.convertToSQLDate(DateUtil.convertToDate(sDate));
		}
		Date endDate = null;
		System.out.print("투어 종료일>> ");
		String eDate = sc.next();
		if(!eDate.equals("*")) {
			endDate = DateUtil.convertToSQLDate(DateUtil.convertToDate(eDate));
		}
		
		System.out.print("투어 1인당 요금>> ");
		String price = sc.next();
		Integer tourPrice = null;
		if(!price.equals("*")) {
			tourPrice = Integer.parseInt(price);
		}
		System.out.print("투어 정원>> ");
		String part = sc.next();
		Integer maxPart = null;
		if(!part.equals("*")) {
			maxPart = Integer.parseInt(part);
		}
		sc.nextLine();
		System.out.print("패키지 정보>> ");
		String tourInfo = sc.nextLine();
		
		if(tourName.equals("*")) tourName = null;
		if(tourInfo.equals("*")) tourInfo = null;
		
		ToursDTO tour = ToursDTO.builder()
						.tour_id(tourID)
						.tour_name(tourName)
						.start_date(startDate)
						.end_date(endDate)
						.tour_price(tourPrice)
						.max_participants(maxPart)
						.tour_info(tourInfo)
						.build();
		
		return tour;
	}

	private static void f_login() {
		System.out.print("ID 입력>> ");
		String partId = sc.next();
		System.out.print("Password 입력>> ");
		String partPW = sc.next();
		
		String member = tourService.login(partId, partPW);
		if(member != null) {
			currentPartID = partId;
			System.out.println(member + "님 환영합니다!");
			boolean back = false;
			while (!back) {
				memberMenuDisplay();
	            int sel = sc.nextInt();
	            switch (sel) {
	                case 1 -> {f_selectAllTours();}
	                case 2 -> {f_BookingInsert();}
	                case 3 -> {f_checkBooking();}
	                case 4 -> {f_deleteBooking();}
	                case 5 -> {f_checkSchedule();}
	                case 6 -> {f_controlMember();}
	                case 0 -> {back = true;}
	                default -> System.out.println("잘못된 선택");
	            }
	        }
		} else {
			System.out.println("로그인에 실패하였습니다.");
		}		
	}


	private static void f_controlMember() {
		boolean back = false;
		while (!back) {
			System.out.println("=======================================");
			System.out.println("1.회원 정보 조회 2.회원 정보 수정 0.뒤로 가기");
			System.out.println("=======================================");
			System.out.print("작업을 선택하세요>> ");
            int sel = sc.nextInt();
            switch (sel) {
            case 1 -> {f_selectByID();}
            case 2 -> {f_updateMember();}
            case 0 -> {back = true;}
            default -> System.out.println("잘못된 선택");
            }
		}
		
	}
	

	private static void f_selectByID() {
		MemberDTO member = tourService.selectByPartID(currentPartID);
		TourView.display(member);
	}

	private static void f_updateMember() {
		f_selectByID();
		tourService.MemberUpdate(updateMember());
		System.out.println("회원 정보가 수정되었습니다.");
		f_selectByID();
	}

	private static MemberDTO updateMember() {
		System.out.println("수정을 원치 않는 정보는 *을 입력해주세요.");
		System.out.print("회원 PW>> ");
		String partPW = sc.next();
		System.out.print("회원 Name>> ");
		String partName = sc.next();
		
		String partEmail = "";
		while (true) {
			System.out.print("회원 Email(user@domain.com)>> ");
			partEmail = sc.next();
			if (partEmail.matches("[\\w._%+-]+@[\\w.-]+\\.com") || partEmail.equals("*")) {
	            break;
	        }
	        System.out.println("잘못된 이메일 형식입니다. user@domain.com 형태로 입력해주세요.");
		}
		
		String partPhone = "";
		while(true) {
			System.out.print("회원 Phone(###-####-####)>> ");
			partPhone = sc.next();
			
			if (partPhone.matches("\\d{3}-\\d{4}-\\d{4}") || partEmail.equals("*")) {
	            break;
	        }
	        System.out.println("잘못된 전화번호 형식입니다. ###-####-#### 형태로 입력해주세요.");
		}
		
		if(partPW.equals("*")) partPW = null;
		if(partName.equals("*")) partName = null;
		if(partEmail.equals("*")) partEmail = null;
		if(partPhone.equals("*")) partPhone = null;
		
		MemberDTO member = MemberDTO.builder()
				.part_id(currentPartID)
				.part_pw(partPW)
				.email(partEmail)
				.part_name(partName)
				.phone(partPhone)
				.build();
		
		return member;
	}

	private static void f_checkSchedule() {
		f_selectAllTours();
		System.out.print("일정 조회할 여행 ID>> ");
		int tourID = sc.nextInt();
		List<ScheduleDTO> schdList = tourService.checkSchedule(tourID);
		TourView.display(tourID + " 여행의 일정표입니다.");
		TourView.display(schdList);
	}

	private static void f_deleteBooking() {
		f_checkBooking();
		while(true) {
			System.out.print("예약 취소할 예약 번호>> ");
			int bookingNo = sc.nextInt();
			int result = tourService.deleteBooking(bookingNo);
			if(result >= 1) {
				TourView.display(result + "건의 예약이 삭제되었습니다. 취소된 예약 번호: " + bookingNo);
				break;
			}
			TourView.display("해당 번호의 예약이 없습니다.");
		}
		
	}

	private static void f_checkBooking() {
		List<BookingDTO> booking = tourService.checkBooking(currentPartID);
		TourView.display(booking);
	}

	private static void f_BookingInsert() {
		f_selectAllTours();
		int tourID = 0;
		
		while(true) {
			System.out.print("tour_id>> ");
			tourID = sc.nextInt();
			if (tourService.isToursExists(tourID)) {
	            break;
	        }
	        System.out.println("존재하지 않는 패키지 여행입니다.");
		}
		Date today = Date.valueOf(LocalDate.now());
		
		BookingDTO booking = BookingDTO.builder()
				.booking_no(0)
				.tour_id(tourID)
				.part_id(currentPartID)
				.booking_date(today)
				.booking_status("booked")
				.paid_amount(0)
				.build();

		try {
			int result = tourService.bookingInsert(booking);
			TourView.display(result + "건의 예약이 완료되었습니다.");
	    } catch (SQLException e) {
	        // 트리거에서 던진 사용자 에러인지 확인
	    	if(e.getErrorCode() == 1){
	    		TourView.display("이미 예약된 여행 패키지입니다.");
	    	} else if (e.getErrorCode() == 20001) {
	    		TourView.display("예약 불가: 정원(남은좌석) 초과");
	        } else {
	        	TourView.display("예약 중 오류 발생: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}


	private static void f_selectAllTours() {
		List<ToursDTO> toursList = tourService.selectAllTours();
		TourView.display(toursList);
		
	}

	private static void memberMenuDisplay() {	
		System.out.println("================================================");
		System.out.println("1.전체 패키지 조회 2.여행 예약 3.예약 확인 4.예약 취소");
		System.out.println("5.여행 일정 확인  6.회원정보 관리  0.뒤로 가기");
		System.out.println("================================================");
		
		System.out.print("작업을 선택하세요>> ");
		
	}
	
	private static void adminMenuDisplay() {
		System.out.println("=========================================================");
		System.out.println("1.전체 패키지 조회 2.패키지 여행 수정 3.패키지 여행 추가 ");
		System.out.println("4.여행 일정 조회  5.전체 예약 확인  6.전체 회원 조회  0.뒤로 가기 ");
		System.out.println("=========================================================");
		
		System.out.print("작업을 선택하세요>> ");
	}
}
