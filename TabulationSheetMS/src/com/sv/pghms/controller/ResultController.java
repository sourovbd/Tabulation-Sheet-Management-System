package com.sv.pghms.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
public class ResultController {
	
	@Autowired
	private AdminService adminService;
	String courseNoGlobal,examHeldGlobal,batchNoGlobal;
	String regNoFromPathVariable,courseNoFromPathVariable, examHeldFromPathVariable, batchNoFromPathVariable;
	
	//For showing ShowResultPage.html
	@RequestMapping(value="/showResultWithParam", method=RequestMethod.GET)
	public String ShowResultWithParam(Model model,@RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo){
		
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		
		try{
			resultFormList = adminService.getresultListQuery(courseNo, batchNo);
			
		}catch(Exception e){
			e.printStackTrace();
		}

		model.addAttribute("resultForm",resultForm);
		model.addAttribute("resultFormList", resultFormList);
			
		return "ShowResultPage";
	}
	@RequestMapping(value="/showResultWithGlobalParam", method=RequestMethod.GET)
	public String ShowResult(Model model){
		
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		
		try{
			//resultFormList = adminService.getresultFormList();
			resultFormList = adminService.getresultListFromQuery(courseNoGlobal,batchNoGlobal);
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("resultForm",resultForm);
		model.addAttribute("resultFormList", resultFormList);
			
		return "ShowResultPage";
	}
	//For edit 
	@RequestMapping(value="/editSingleResultShowpage/{regNo}/{courseNo}/{examHeld}/{batchNo}")
	public String EditSingleResultShowpage(Model model, @PathVariable("regNo") String regNo, @PathVariable("batchNo") String batchNo,
			@PathVariable("courseNo") String courseNo, @PathVariable("examHeld") String examHeld) { 
		
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultList = new ArrayList<TResultForm>();
		
		//Path variables declared globally to send "/saveEditedResult" mapped method for deletePreviousRow(...) method.
		regNoFromPathVariable = regNo;
		courseNoFromPathVariable = courseNo;
		examHeldFromPathVariable = examHeld;
		batchNoFromPathVariable = batchNo;
		System.out.println(regNo+" "+courseNo+" "+examHeld+" "+batchNo);
		try{
			resultList = adminService.getSingleResultForm(regNo, courseNo, examHeld, batchNo);
			Iterator it = resultList.iterator();
			while(it.hasNext()){
				resultForm = (TResultForm) it.next();
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		model.addAttribute("resultForm",resultForm);
		model.addAttribute("star",Constant.star);
		model.addAttribute("starMarkedfieldsAreRequired",Constant.starMarkedfieldsAreRequired);
		
		return "EditResult";
	}  
	//For save after editing 
	@RequestMapping(value="/saveEditedResult", method=RequestMethod.POST)
	public String EditedResEntry(@ModelAttribute("resultForm") TResultForm resultForm, @RequestParam("courseNo") String courseNo, @RequestParam("examHeld") String examHeld, @RequestParam("batchNo") String batchNo, HttpServletRequest req){ //, 
		
		courseNoGlobal = courseNo;
		examHeldGlobal = examHeld;
		batchNoGlobal  = batchNo;
		//batchNoGlobal = req.getParameter("batchNo");
		System.out.println("FromPathVariable: "+regNoFromPathVariable+" "+courseNoFromPathVariable+" "+examHeldFromPathVariable);
		try{
			if(resultForm.getTt1()!="" && resultForm.getTt2()!="" && 
			   resultForm.getAttendance()!="" && resultForm.getFinalMark()!=""){
				System.out.println("Deleting...");
				adminService.deletePreviousRow(regNoFromPathVariable,courseNoFromPathVariable, examHeldFromPathVariable);
				System.out.println("Deleted.");
				System.out.println("Inserting...");
				adminService.insertResult(resultForm); //This insert new row. So previous row remains in db. So at first we will delete prev. row. Then save new row.
				System.out.println("Inserted.");
			}
		}catch(Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "redirect:/main/showResultWithGlobalParam";
	}
}
