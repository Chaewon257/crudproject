package com.shinhan.emp;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.shinhan.common.CommonControllerInterface;

// user와 대화
// MVC2 모델
// FrontController -> Controller 선택-> Service->DAO->DB
//					<-				<-			<-	 <-
public class EmpController implements CommonControllerInterface{
	
	static Scanner sc = new Scanner(System.in);
	static EmpService empService = new EmpService();
	
	@Override
	public void execute() {
		boolean isStop = false;
		while(!isStop) {
			menuDisplay();
			int job = sc.nextInt();
			switch(job) {
			case 1->{ f_selectAll();}
			case 2->{ f_selectByID();}
			case 3->{ f_selectByDept();}
			case 4->{ f_selectByJob();}
			case 5->{ f_selectByJobAndDept();}
			case 6->{ f_selectByCondition();}
			case 7->{ f_deleteByEmpID();}
			case 8->{ f_insertEmp();}
			case 9->{ f_updateEmp();}
			case 10->{ f_spCall();}
			case 99->{ isStop = true;}
			}
		}
		System.out.println("=======작업을 종료합니다=======");
	}


	private static void f_spCall() {
		System.out.print("조회할 ID>>");
		int empid = sc.nextInt();
		
		EmpDTO emp =  empService.execute_sp(empid);
		String message = "해당 직원이 존재하지 않습니다.";
		if(emp != null) {
			message = emp.getEmail() + "---" + emp.getSalary();
		}
		EmpView.display(message);
		
	}


	private static void f_updateEmp() {
		System.out.println("수정할 직원 정보를 입력하세요");
		System.out.print("직원employee_id(PK)>> ");
		int employee_id = sc.nextInt();
		
		EmpDTO existEmp = empService.selectByID(employee_id);
		if(existEmp == null) {
			EmpView.display("존재하지 않는 직원입니다.");
			return;
		}
		EmpView.display("====존재하는 직원정보입니다.====");
		EmpView.display(existEmp);
		
		int result = empService.EmpUpdate(makeEMP(employee_id));
		EmpView.display(result + "건이 수정됨.");
	}

	private static void f_insertEmp() {
		System.out.println("신규 직원 정보를 입력하세요");
		System.out.print("직원employee_id(PK)>> ");
		int employee_id = sc.nextInt();
		
		int result = empService.EmpInsert(makeEMP2(employee_id));
		EmpView.display(result + "건이 입력됨.");
		
	}
	
	static EmpDTO makeEMP(int employee_id) {
		
		System.out.print("직원first_name>> ");
		String first_name = sc.next();
		System.out.print("직원last_name>> ");
		String last_name = sc.next();
		
		System.out.print("직원email>> ");
		String email = sc.next();
		System.out.print("직원phone_number>> ");
		String phone_number = sc.next();
		
		Date hire_date = null;
		System.out.print("직원hire_date(yyyy-MM-dd)>> ");
		String hdate = sc.next();
		if(!hdate.equals("0")) {
			hire_date = DateUtil.convertToSQLDate(DateUtil.convertToDate(hdate));
		}
		
		System.out.print("직원job_id(FK:IT_PROG)>> ");
		String job_id = sc.next();
		System.out.print("직원salary>> ");
		Double salary = sc.nextDouble();
		
		System.out.print("직원commission_pct(0.2)>> ");
		Double commission_pct = sc.nextDouble();
		System.out.print("직원manager_id(FK:100)>> ");
		Integer manager_id = sc.nextInt();
		System.out.print("직원department_id(FK:60,90)>> ");
		Integer department_id = sc.nextInt();
		
		if(first_name.equals("0")) first_name = null;
		if(last_name.equals("0")) last_name = null;
		if(email.equals("0")) email = null;
		if(phone_number.equals("0")) phone_number = null;
		if(job_id.equals("0")) job_id = null;
		if(salary==0) salary = null;
		if(commission_pct==0) commission_pct = null;
		if(manager_id==0) manager_id = null;
		if(department_id==0) department_id = null;
		
		
		EmpDTO emp = EmpDTO.builder()
				.commission_pct(commission_pct)
				.department_id(department_id)
				.email(email)
				.employee_id(employee_id)
				.first_name(first_name)
				.hire_date(hire_date)
				.job_id(job_id)
				.last_name(last_name)
				.manager_id(manager_id)
				.phone_number(phone_number)
				.salary(salary)
				.build();		
		return emp;
	}
	static EmpDTO makeEMP2(int employee_id) {
		
		System.out.print("직원first_name>> ");
		String first_name = sc.next();
		System.out.print("직원last_name>> ");
		String last_name = sc.next();
		
		System.out.print("직원email>> ");
		String email = sc.next();
		System.out.print("직원phone_number>> ");
		String phone_number = sc.next();
		
		System.out.print("직원hire_date(yyyy-MM-dd)>> ");
		String hdate = sc.next();
		Date hire_date = DateUtil.convertToSQLDate(DateUtil.convertToDate(hdate));;
		System.out.print("직원job_id(FK:IT_PROG)>> ");
		String job_id = sc.next();
		System.out.print("직원salary>> ");
		double salary = sc.nextDouble();
		
		System.out.print("직원commission_pct(0.2)>> ");
		double commission_pct = sc.nextDouble();
		System.out.print("직원manager_id(FK:100)>> ");
		int manager_id = sc.nextInt();
		System.out.print("직원department_id(FK:60,90)>> ");
		int department_id = sc.nextInt();
		
		EmpDTO emp = EmpDTO.builder()
				.commission_pct(commission_pct)
				.department_id(department_id)
				.email(email)
				.employee_id(employee_id)
				.first_name(first_name)
				.hire_date(hire_date)
				.job_id(job_id)
				.last_name(last_name)
				.manager_id(manager_id)
				.phone_number(phone_number)
				.salary(salary)
				.build();		
		return emp;
	}

	private static void f_deleteByEmpID() {
		// 해당 직원을 참조하고 있는 직원이 있을 경우 삭제 못 함
		System.out.print("삭제할 직원ID>> ");
		int empid = sc.nextInt();
		int result = empService.EmpDeleteByID(empid);
		EmpView.display(result + "건이 삭제됨.");
		
	}

	private static void f_selectByCondition() {
		// = 부서, like 직책, >=급여, >=입사일
		System.out.print("조회할 부서(10,20,30)>> ");
		String[] str_deptid = sc.next().split(",");
		
		Integer[] deptArr = Arrays.stream(str_deptid)
								.map(Integer::parseInt)
								.toArray(Integer[]::new);
				
		System.out.print("조회할 직책ID>> ");
		String jobid = sc.next();
		
		System.out.print("조회할 Salary(이상)>> ");
		int salary = sc.nextInt();
		System.out.print("조회할 입사일(yyy-MM-DD, 이상)>> ");
		String hiredate = sc.next();
		
		List<EmpDTO> emp = empService.selectByCondition(deptArr, jobid, salary, hiredate);
		EmpView.display(emp);
		
	}

	private static void f_selectByJobAndDept() {
		System.out.print("조회할 직책ID, 부서ID>> "); // IT_PROG,60
		String data = sc.next();
		
		String[] arr = data.split(",");
		String job = arr[0].trim();
		int deptid= Integer.parseInt(arr[1].trim());
		List<EmpDTO> emp = empService.selectByJobAndDept(job, deptid);
		EmpView.display(emp);
				
	}

	private static void f_selectByJob() {
		System.out.print("조회할 직책ID>> ");
		String job = sc.next();
		List<EmpDTO> emp = empService.selectByJob(job);
		EmpView.display(emp);
		
	}

	private static void f_selectByDept() {
		System.out.print("조회할 부서ID>> ");
		int deptid = sc.nextInt();
		List<EmpDTO> emp = empService.selectByDept(deptid);
		EmpView.display(emp);
		
	}

	private static void f_selectByID() {
		System.out.print("조회할 ID>>");
		int empid = sc.nextInt();
		EmpDTO emp = empService.selectByID(empid);
		EmpView.display(emp);
	}

	private static void f_selectAll() {
		List<EmpDTO> emplist = empService.selectAll();
		EmpView.display(emplist);
	}

	private static void menuDisplay() {
		System.out.println("=======================================================");
		System.out.println("1.모두 조회 2.직원번호로 조회 3.부서로 조회 4.직책으로 조회 ");
		System.out.println("5.직책/부서로 조회 6.기타 7.직원 삭제 8.직원 입력 9.직원수정");
		System.out.println("10.SP 호출 99.끝 ");
		System.out.println("=======================================================");
		
		System.out.print("작업을 선택하세요>> ");
		
	}
}
