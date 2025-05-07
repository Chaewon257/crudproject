package com.shinhan.project;

import java.util.List;


public class TourView {
	// 여러건 출력
	public static <T> void display(List<T> datalist) {
		if(datalist.isEmpty()) {
			display("해당 건이 존재하지 않습니다.");
			return;
		}
		System.out.println("++++++여러건 조회++++++");
		datalist.stream().forEach(data->System.out.println(data));
		System.out.println("+++++++++조회  완료++++++++");
	}
	
	// 멤버 한 건 출력
	public static void display(MemberDTO data) {
		if(data == null) {
			display("해당 회원이 존재하지 않습니다.");
			return;
		}
		System.out.println("++++++++회원 조회++++++++");
		System.out.println("정보: " + data);
		System.out.println("++++++조회 완료++++++");
	}
	
	// 멤버 한 건 출력
	public static void display(ToursDTO data) {
		if(data == null) {
			display("해당 여행이 존재하지 않습니다.");
			return;
		}
		System.out.println("++++++++여행 조회++++++++");
		System.out.println("정보: " + data);
		System.out.println("++++++조회 완료++++++");
	}
	

	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	
}
