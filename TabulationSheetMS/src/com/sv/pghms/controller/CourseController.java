package com.sv.pghms.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import groovy.ui.SystemOutputInterceptor;

@Controller
@RequestMapping("/main")
public class CourseController {
	
	@Autowired
	private AdminService adminService;
	String globalCourseNo, globalBatchNo;
	
	//For showing CourseDetailsEntry.html page
	@RequestMapping(value="/saveCourseDetails", method=RequestMethod.GET)
	public String SaveCourseDetails(Model model){
		
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		
		try{
			courseDetailsFormList = adminService.getCourseDetailsList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
		model.addAttribute("star",Constant.star);
		model.addAttribute("starMarkedfieldsAreRequired",Constant.starMarkedfieldsAreRequired);
			
		return "CourseDetailsEntry";
	}
	// For saving entry from CourseDetailsEntry.html page
	@RequestMapping(value="/saveCourseDetails", method=RequestMethod.POST)
	public String SaveCourseDetails(@ModelAttribute("courseDetailsForm") TCourseDetails courseDetailsForm){
		
		try{
			if(courseDetailsForm.getCourseNo()!="" && courseDetailsForm.getBatchNo()!="" && courseDetailsForm.getCourseTitle()!=""
			   && courseDetailsForm.getCourseCredit()!="" && courseDetailsForm.getExamHeld()!="" && courseDetailsForm.getExamYear()!=""
			   && courseDetailsForm.getSemester()!=""){
				adminService.insertCourseDetails(courseDetailsForm);
			}		
		}catch(Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "redirect:/main/saveCourseDetails";
	}
	//For course search page
	@RequestMapping(value="/courseViewPage", method=RequestMethod.GET)
	public String CourseViewPage(Model model){

		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<String> courseDetailsFormListCourseNo = new ArrayList<String>();
		List<String> courseDetailsFormListBatchNo = new ArrayList<String>();
		
		try{
			courseDetailsFormListCourseNo = adminService.getcourseDetailsList_courseNo();
			courseDetailsFormListBatchNo = adminService.getcourseDetailsList_batchNo();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormListCourseNo", courseDetailsFormListCourseNo);
		model.addAttribute("courseDetailsFormListBatchNo", courseDetailsFormListBatchNo);
		
		return "SearchCourse";
	}
	//For course view page
	@RequestMapping(value="/viewCourses", method=RequestMethod.GET)
	public String ViewCourses(Model model,@RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo){
		
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		System.out.println("courseNo: "+courseNo);
		System.out.println("batchNo: "+batchNo);
		try{
			courseDetailsFormList = adminService.getSingleCourse(courseNo, batchNo);
			System.out.println("courseDetailsFormList: "+courseDetailsFormList);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
			
		return "CourseView";
	}
	// For edit   
	@RequestMapping(value="/editSingleCourse/{courseNo}/{batchNo}")
	public ModelAndView EditUser(@PathVariable("courseNo") String courseNo, @PathVariable("batchNo")  String batchNo) {
		
		ModelAndView model = new ModelAndView("EditCourseDetails");
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		globalCourseNo = courseNo;
		globalBatchNo = batchNo;
		
		try{
			courseDetailsFormList = adminService.getSingleCourse(courseNo, batchNo);
			System.out.println("courseDetailsFormList: "+courseDetailsFormList);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		Iterator it = courseDetailsFormList.iterator();
		while(it.hasNext()){
			courseDetailsForm = (TCourseDetails) it.next();
		}
		
		model.addObject("courseDetailsForm",courseDetailsForm);
		model.addObject("courseDetailsFormList", courseDetailsFormList);
		return model;
	}
	@RequestMapping(value="/editCourseDetails", method=RequestMethod.GET)
	public String EditCourseDetails(Model model){
		
		TCourseDetails courseDetailsForm = new TCourseDetails();
		List<TCourseDetails> courseDetailsFormList = new ArrayList<TCourseDetails>();
		
		try{
			courseDetailsFormList = adminService.getCourseDetailsList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		model.addAttribute("courseDetailsForm",courseDetailsForm);
		model.addAttribute("courseDetailsFormList", courseDetailsFormList);
			
		return "EditCourseDetails";
	}
	@RequestMapping(value="/editCourseDetails", method=RequestMethod.POST)
	public String EditCourseDetails(@ModelAttribute("courseDetailsForm") TCourseDetails courseDetailsForm){
		
		try{
			System.out.println("test: "+globalCourseNo+" "+globalBatchNo );
			adminService.deleteSingleCourse(globalCourseNo, globalBatchNo);
			adminService.insertCourseDetails(courseDetailsForm);
			
		}catch(Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "redirect:/main/editCourseDetails";
	}
	// For delete 
	@RequestMapping(value="/deleteSingleCourse/{courseNo}/{batchNo}")
	public ModelAndView DeleteSingleCourse(@PathVariable("courseNo") String courseNo, @PathVariable("batchNo") String batchNo) {
		
		ModelAndView model = new ModelAndView("redirect:/main/saveCourseDetails");
		try{
			adminService.deleteSingleCourse(courseNo, batchNo);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
}
