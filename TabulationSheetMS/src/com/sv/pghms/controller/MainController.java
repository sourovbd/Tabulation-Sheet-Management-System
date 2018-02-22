package com.sv.pghms.controller;

import java.util.ArrayList;

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

import com.sv.pghms.util.Constant;
import com.sv.pghms.util.Utilities;
import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.model.TResultForm;
import com.sv.pghms.model.TUser;
import com.sv.pghms.service.AdminService;

@Controller
@RequestMapping("/main")
public class MainController {
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value="/homePage", method=RequestMethod.GET)
	public String HomePage(Model model){
		
		TCourseDetails courseDetails = new TCourseDetails();
		List<String> courseDetailsListCourseNo = new ArrayList<String>();
		List<String> courseDetailsListBatchNo = new ArrayList<String>();
		
		try{
			courseDetailsListCourseNo = adminService.getcourseDetailsList_courseNo();
			courseDetailsListBatchNo = adminService.getcourseDetailsList_batchNo();
		}catch(Exception e){
			e.printStackTrace();
		}
		model.addAttribute("courseDetails",courseDetails);
		model.addAttribute("courseDetailsListCourseNo", courseDetailsListCourseNo);
		model.addAttribute("courseDetailsListBatchNo", courseDetailsListBatchNo);
					
		return "HomePage";
	}	
	
	// For password reset
	@RequestMapping(value="/resetPassword",method=RequestMethod.GET)
	public String PasswordResetPage() {
		
		return "PasswordResetPage";
	}
	@RequestMapping(value="/resetPassword",method=RequestMethod.POST)
	public String PasswordReset(Model model,@RequestParam("currentPassword") String currentPassword,@RequestParam("newPassword") String newPassword,@RequestParam("retypeNewPassword") String retypeNewPassword) {
		
		TUser user = adminService.getUserByLoginID(Utilities.getCurrentLoginID());
		String userPassword = user.getUserPassword();
		/*String mismatch = "Your previous password doesn't match.";
		String mismatchNew = "Your new password and retyped password doesn't match.";
		String match = "Congratulation! You have successfully changed your password.";*/
		if(!(userPassword.equals(currentPassword))) {
			
			model.addAttribute("mismatchedPassword",Constant.mismatchedPassword);
		}
		else {
			if(!(newPassword.equals(retypeNewPassword))){
				
				model.addAttribute("mismatchedNewPassword",Constant.mismatchedNewPassword);
			}
			else {
				user.setUserPassword(retypeNewPassword);
				adminService.insertUser(user);
				model.addAttribute("matchedNewPassword",Constant.matchedNewPassword);
			}
		}
				
		return "PasswordResetPage";
	}
	@RequestMapping(value="/createUser",method=RequestMethod.GET)
	public ModelAndView UserForm() {
		
		ModelAndView model = new ModelAndView("UserCreationPage");
		
		TUser user = new TUser();
		List<TUser> userList = new ArrayList<TUser>();
		try {
			
			userList = adminService.getAllUser();
		}catch(Exception e){
			
			e.printStackTrace();
		}
		model.addObject("user",user);
		model.addObject("userList", userList);
		
		return model;
	}
	
	@RequestMapping(value="/createUser",method=RequestMethod.POST)
	public ModelAndView CreateUser(@ModelAttribute("user")TUser user) {
		
		ModelAndView model = new ModelAndView("redirect:/main/createUser");
		try{
			
			adminService.insertUser(user);
			
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping(value="/editUser/{id}")
	public ModelAndView EditUser(@PathVariable("id") String id) {
		
		ModelAndView model = new ModelAndView("UserCreationPage");
		TUser user = new TUser();
		List<TUser> userList = new ArrayList<TUser>();
		
		try{
			user = adminService.getSingleUser(id);
			userList = adminService.getAllUser();
			//System.out.println("update completed.");
		}catch(Exception e){
			
			e.printStackTrace();
		}
		model.addObject("user",user);
		model.addObject("userList", userList);
		
		return model;
	}
	
	@RequestMapping(value="/deleteUser/{id}")
	public ModelAndView DeleteUser(@PathVariable("id") String id) {
		
		ModelAndView model = new ModelAndView("redirect:/main/createUser");
		try{
			adminService.deleteSingleUser(id);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
	@RequestMapping(value="/activateUser/{userLoginId}")
	public ModelAndView ActivateUser(@PathVariable("userLoginId") String userLoginId) {
	
		ModelAndView model = new ModelAndView("redirect:/main/createUser");
		try{
			adminService.activateUser(userLoginId);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping(value="/deactivateUser/{userLoginId}")
	public ModelAndView DeactivateUser(@PathVariable("userLoginId") String userLoginId) {
	
		ModelAndView model = new ModelAndView("redirect:/main/createUser");
		try{
			adminService.deactivateUser(userLoginId);
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return model;
	}
}