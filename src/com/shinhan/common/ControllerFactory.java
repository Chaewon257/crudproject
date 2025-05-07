package com.shinhan.common;

import com.shinhan.dept.DeptController;
import com.shinhan.emp.EmpController;
import com.shinhan.job.JobController;

// Factory Controller
public class ControllerFactory {

	public static CommonControllerInterface make(String business) {
		CommonControllerInterface controller = null;
		
		switch(business) {
			// new하는 방법이 달라졌다면 여기만 수정하면 됨
			case "emp" ->{controller = new EmpController();}
			case "dept" -> {controller = new DeptController();}
			case "job" ->{controller = new JobController();}
			
		}
		
		return controller;
	}
	
}
