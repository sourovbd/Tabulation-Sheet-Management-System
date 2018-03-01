package com.sv.pghms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.service.AdminService;
import com.sv.pghms.util.Constant;

import BirtIntegration.BirtViewer.ReportProcessor;
import BirtIntegration.factory.BirtEngineFactory;

@Controller
@RequestMapping("/main")
public class ReportController {
	@Autowired
	private AdminService adminService;
	private ReportProcessor processor;
	private String reportFormat = "pdf";
	private String reportName = "tabulation_sheet.rptdesign";
	@RequestMapping(value="/printReportPage",method=RequestMethod.GET)
	public String PrintReportPage(Model model) {
		
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
		
		model.addAttribute("star",Constant.star);
		model.addAttribute("starMarkedfieldsAreRequired",Constant.starMarkedfieldsAreRequired);
		
		return "PrintReportPage";
	}
	
	@RequestMapping(value="/printReport", method=RequestMethod.GET)
	public String PrintReport(Model model,@RequestParam("courseNo") String courseNo, @RequestParam("batchNo") String batchNo, HttpServletRequest request, HttpServletResponse response){
		processor = ReportProcessor.getReportProcessor();
		processor.initilizeBirtEngine();
		reportName="new_report.rptdesign";

		request.setAttribute("ReportName", reportName);
		request.setAttribute("ReportFormat", reportFormat);
		processor.processReport(request, response);
		return "redirect:/main/printReportPage";
	}
}
