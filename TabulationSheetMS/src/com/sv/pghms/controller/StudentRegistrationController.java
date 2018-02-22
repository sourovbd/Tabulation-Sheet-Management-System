package com.sv.pghms.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.model.TResultForm;
import com.sv.pghms.service.AdminService;
import com.sv.pghms.util.Constant;

@Controller
@RequestMapping("/main")
public class StudentRegistrationController {
	
	@Autowired
	private AdminService adminService;
		
	String editRegNoGlobal, courseNoGlobal, batchNoGlobal;
	
	@RequestMapping(value="/studentEntry", method=RequestMethod.GET)
	public String getBatchDetails(Model model){
		
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		
		String courseDetails = new String(); // No need
		List<String> courseDetailsList_courseNo = new ArrayList<String>();
		List<String> courseDetailsList_batchNo = new ArrayList<String>();
				
		try{
			
			courseDetailsList_courseNo = adminService.getcourseDetailsList_courseNo();
			courseDetailsList_batchNo = adminService.getcourseDetailsList_batchNo();		
			resultFormList = adminService.getresultListFromQuery(courseNoGlobal,batchNoGlobal);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("resultForm",resultForm);
		model.addAttribute("resultFormList", resultFormList);
		
		model.addAttribute("courseDetails",courseDetails); // No need
		model.addAttribute("courseDetailsList_courseNo", courseDetailsList_courseNo);
		model.addAttribute("courseDetailsList_batchNo", courseDetailsList_batchNo);
		model.addAttribute("star",Constant.star);
		model.addAttribute("starMarkedfieldsAreRequired",Constant.starMarkedfieldsAreRequired);
		
		return "StudentEntry";
	}
	//For 'Add More' option, InputForm page save
	@RequestMapping(value="/studentEntry", method=RequestMethod.POST)
	public String saveBatchDetails(@ModelAttribute("resultForm") TResultForm resultForm, @RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo, HttpServletRequest req){
		
		courseNoGlobal = courseNo;
		batchNoGlobal = req.getParameter("batchNo");//batchNo;
		
		try{
			if(resultForm.getName()!="" && resultForm.getRegNo()!="" && 
			   resultForm.getCourseNo()!="" && resultForm.getBatchNo()!=""){
			adminService.deletePreviousROW(editRegNoGlobal, courseNo, batchNo);
			adminService.insertBatch(resultForm);
			}
		}catch(Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "redirect:/main/studentEntry";
	}
	//For course search page
	@RequestMapping(value="/studentViewPage", method=RequestMethod.GET)
	public String CourseViewPage(Model model){
				
		TResultForm resultForm = new TResultForm();
		List<String> courseDetailsListCourseNo = new ArrayList<String>();
		List<String> courseDetailsListBatchNo = new ArrayList<String>();
				
		try{
			courseDetailsListCourseNo = adminService.getcourseDetailsList_courseNo();
			courseDetailsListBatchNo = adminService.getcourseDetailsList_batchNo();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("resultForm",resultForm);
		model.addAttribute("courseDetailsListCourseNo", courseDetailsListCourseNo);
		model.addAttribute("courseDetailsListBatchNo", courseDetailsListBatchNo);
				
		return "SearchStudent";
	}
	//For course view page
	@RequestMapping(value="/viewStudents", method=RequestMethod.GET)
	public String ViewCourses(Model model,@RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo){
		
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		
		try{
			resultFormList = adminService.getresultListQuery(courseNo, batchNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("resultForm",resultForm);
		model.addAttribute("resultFormList", resultFormList);
			
		return "StudentView";
	}
	// For edit   
	@RequestMapping(value="/editSingleStudent/{regNo}/{courseNo}/{batchNo}")
	public ModelAndView EditUser(@PathVariable("regNo") String regNo, @PathVariable("courseNo") String courseNo, @PathVariable("batchNo")  String batchNo) {
		
		ModelAndView model = new ModelAndView("StudentEntry");
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		
		TCourseDetails courseDetails = new TCourseDetails();
		List<TCourseDetails> courseDetailsList = new ArrayList<TCourseDetails>();
		List<String> courseDetailsList_courseNo = new ArrayList<String>();
		List<String> courseDetailsList_batchNo = new ArrayList<String>();

		editRegNoGlobal = regNo;
		System.out.println("editRegNoGlobal: "+editRegNoGlobal);
		
		try{
			courseDetailsList_courseNo = adminService.getcourseDetailsList_courseNo();
			courseDetailsList_batchNo = adminService.getcourseDetailsList_batchNo();
			resultFormList = adminService.getresultListFor3Query(regNo, courseNo, batchNo);
			System.out.println("resultFormList: "+resultFormList);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		Iterator it = resultFormList.iterator();
		while(it.hasNext()){
			resultForm = (TResultForm) it.next();
		}
		
		model.addObject("resultForm",resultForm);
		model.addObject("resultFormList", resultFormList);
		model.addObject("courseDetails",courseDetails);
		model.addObject("courseDetailsList", courseDetailsList);
		model.addObject("courseDetailsList_courseNo", courseDetailsList_courseNo);
		model.addObject("courseDetailsList_batchNo", courseDetailsList_batchNo);
		model.addObject("star",Constant.star);
		model.addObject("starMarkedfieldsAreRequired",Constant.starMarkedfieldsAreRequired);
		
		return model;
	}
	// For delete 
	@RequestMapping(value="/deleteSingleStudent/{regNo}/{courseNo}/{batchNo}")
	public ModelAndView DeleteSingleCourse(@PathVariable("regNo") String regNo, @PathVariable("courseNo") String courseNo, @PathVariable("batchNo") String batchNo) {
		
		ModelAndView model = new ModelAndView("redirect:/main/studentEntry");
		try{
			adminService.deleteSingleStudent(regNo, courseNo, batchNo);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
}
