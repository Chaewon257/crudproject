package com.shinhan.emp;

import java.util.List;

// EMP data를 display하려는 목적, 나중에 웹으로 변경되면 jsp로 할거라 view는 필요없음
public class EmpView {
	// Overloading
	
	// 여러건 출력
	public static void display(List<EmpDTO> emplist) {
		if(emplist.size() == 0) {
			display("해당하는 직원이 존재하지 않습니다.");
			return;
		}
		System.out.println("++++++직원 여러건 조회++++++");
		emplist.stream().forEach(emp->System.out.println(emp));
		System.out.println("+++++++++조회  완료++++++++");
	}
	
	// 한 건 출력
	public static void display(EmpDTO emp) {
		if(emp == null) {
			display("해당하는 직원이 존재하지 않습니다.");
			return;
		}
		System.out.println("++++++직원 조회++++++");
		System.out.println("직원정보: " + emp);
		System.out.println("++++++조회 완료++++++");
	}

	public static void display(String message) {
		System.out.println("알림: " + message);
	}
	
}
