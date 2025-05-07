package com.shinhan.common;

import java.util.Scanner;

// FrontController 패턴 : Controller가 여러개인 경우 사용자의 요청과 응답의 출구가 여러개이면 바람직하지 않기 떄문에 이걸 하나로 통합 하는거임
// front는 한개만 만드는게 조음
// Servlet: Dispatcherservlet이 있음 (Spring은 FrontController가 있음)

public class FrontController {
	
	public static void main(String[] args) {
		// 사용자가 emp, dept 작업할 것인지 결정
		Scanner sc = new Scanner(System.in);		
		boolean isStop = false;
		CommonControllerInterface controller = null;
		
		while(!isStop) {
			System.out.print("emp, dept, job>> ");
			String job = sc.next();
			
			switch(job) {
				case "emp"->{ controller = ControllerFactory.make("emp");}
				case "dept"->{controller = ControllerFactory.make("dept");}
				case "job"->{ controller = ControllerFactory.make("job");}
				case "end" ->{isStop = true; continue;}
				default ->{continue;}
			}
			
			// 전략 패턴
			controller.execute(); // 작업이 달라져도 사용법은 같음 (<- 전략패턴)
		}
		sc.close();
		System.out.println("=====MAIN END=====");
	}
}
