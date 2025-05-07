package com.shinhan.dept;

import java.util.List;

//View(Web에서 JSP로 변경될 예정이다)
public class DeptView {

	public static void display(List<DeptDTO> deptlist) {
		if(deptlist.size() == 0) {
			display("존재하는 data없음");
		}
		System.out.println("=======부서목록========");
		deptlist.stream().forEach(dept->display(dept));
	}
	public static void display(DeptDTO dept) {
		if(dept==null) {
			display("해당하는 data가 존재하지않습니다.");
			return;
		}
		System.out.println("부서번호:" + dept.getDepartment_id());
		System.out.println("부서이름:" + dept.getDepartment_name());
		System.out.println("메니저번호:" + dept.getManager_id());
		System.out.println("지역코드:" + dept.getLocation_id());
		System.out.println("----------------------------------");
	}
	
	public static void display(String message) {
		System.out.println("알림:" + message);
		System.out.println("=========    ====    =========");
		System.out.println("========  ===    ===  ========");
		System.out.println("=======   Good Bye!!!  =======");
		System.out.println("========  ==========  ========");
		System.out.println("==========  ======  ==========");
		System.out.println("============  ==  ============");
		System.out.println("==============   =============");
	}
	public static void menuDisplay() {
		System.out.println("1.모든 부서조회(all)");
		System.out.println("2.부서번호로 상세보기(detail)");
		System.out.println("3.부서 입력(i)");
		System.out.println("4.부서 수정(u)");
		System.out.println("5.부서 삭제(d)");
		System.out.println("6.부서 삭제(exit)");
		System.out.print("작업>>");
	}
	
	
}
