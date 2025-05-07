package com.shinhan.emp;

import java.util.List;

// Service: 비즈니스 로직을 수행 (Controller와 DAO 사이의 다리 역할)
// 1) 이체업무 : (인출, 입금) <- 안에 잇는 건 DAO에서 구현
// 2) 비밀번호 암호화 (로그인시)
public class EmpService {
	
	EmpDAO empDAO = new EmpDAO();
	
	public EmpDTO execute_sp(int empid) {
		return empDAO.execute_sp(empid);
	}
	
	public int EmpInsert(EmpDTO emp) {
		return empDAO.EmpInsert(emp);
	}
	
	public int EmpUpdate(EmpDTO emp) {
		return empDAO.EmpUpdate(emp);
	}
	
	public int EmpDeleteByID(int empid) {
		return empDAO.EmpDeleteByID(empid);
	}
	
	public List<EmpDTO> selectAll() {
		return empDAO.selectAll();
	}
	
	public EmpDTO selectByID(int empid) {
		return empDAO.selectByID(empid);
	}
	
	public List<EmpDTO> selectByDept(int deptID) { 
		return empDAO.selectByDept(deptID);
	}
	
	public List<EmpDTO> selectByJob(String job) {
		return empDAO.selectByJob(job);
	}
	
	public List<EmpDTO> selectByJobAndDept(String job, int dept) { 
		return empDAO.selectByJobAndDept(job, dept);
	}

	public List<EmpDTO> selectByCondition(Integer[] deptid, String jobid, int salary, String hiredate) {
		return empDAO.selectByCondition(deptid, jobid, salary, hiredate);
	}
	
}
