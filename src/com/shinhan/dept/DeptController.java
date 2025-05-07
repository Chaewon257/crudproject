package com.shinhan.dept;

import java.util.Scanner;

import com.shinhan.common.CommonControllerInterface;

//Controller(사용자가 요청하면  요청을 처리하고 응답을 보낸다. (Servlet으로 변경할 예정이다)
//Web아니고 Console로 동작하는 프로그램이므로 키보드입력을 이용(System.in)
public class DeptController implements CommonControllerInterface{
	static Scanner sc = new Scanner(System.in);
	static DeptService deptService = new DeptService();
	
	@Override
	public void execute( ) {
	      boolean isStop = false;
	      while(!isStop) {
	    	  DeptView.menuDisplay();
	    	  String job = sc.nextLine(); 
	    	  switch(job) {
	    	  case "all"->{ f_all();}
	    	  case "detail"->{f_detail();}
	    	  case "i"->{f_insert();}
	    	  case "u"->{f_update();}
	    	  case "d"->{f_delete();}
	    	  case "exit"->{isStop = true;}
	    	  }
	      }
	      DeptView.display("프로그램 종료");
	}
	private static void f_all() {
		DeptView.display( deptService.selectAll() );
	}
	private static void f_detail() {
		int deptid = parseInt(dataInsert("부서번호>>"));
		DeptView.display(deptService.selectById(deptid));		
	}
	private static void f_insert() {		
		DeptDTO dept = makeDept("입력할 부서번호>>");
		DeptView.display(deptService.insertDept(dept)+"건 입력");
	}
	private static DeptDTO makeDept(String title) {
		int deptid = parseInt(dataInsert(title));
		String deptname = dataInsert("부서이름>>");
		int mgrid = parseInt(dataInsert("메니저번호>>"));
		int locid = parseInt(dataInsert("지역번호>>"));	
		DeptDTO dept = DeptDTO.builder()
				.department_id(deptid)
				.department_name(deptname)
				.location_id(locid)
				.manager_id(mgrid)
				.build();
        return dept;		
	}

	private static int parseInt(String data) {
		return Integer.parseInt(data);
	}

	private static String dataInsert(String column) {
		System.out.print(column);
		return sc.nextLine();
	}

	private static void f_update() {
		DeptDTO dept = makeDept("수정할 부서번호>>");
		DeptView.display(deptService.updateDept(dept)+"건 수정");
		
	}

	private static void f_delete() {
		int deptid = parseInt(dataInsert("삭제할 부서번호>>"));
		DeptView.display(deptService.deleteDept(deptid)+"건 삭제");
	}
}










