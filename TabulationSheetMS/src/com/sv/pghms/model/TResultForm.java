package com.sv.pghms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_result")
public class TResultForm {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="t_rowNo")
	private int rowNo;
	@Column(name="t_regNo")
	private String regNo;
	@Column(name="t_name")
	private String name;
	@Column(name="t_tt1")
	private String tt1;
	@Column(name="t_tt2")
	private String tt2;
	@Column(name="t_ttMark")
	private String ttMark;
	@Column(name="t_attendance")
	private String attendance;
	@Column(name="t_finalMark")
	private String finalMark;
	@Column(name="t_totalMark")
	private String totalMark;
	@Column(name="t_cgpa")
	private String cgpa;
	@Column(name="t_gradeLetter")
	private String gradeLetter;
	@Column(name="t_courseNo")
	private String courseNo;
	@Column(name="t_examHeld")
	private String examHeld;
	@Column(name="t_batchNo")
	private String batchNo;
	
	public String getRegNo() {
		return regNo;
	}
	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTt1() {
		return tt1;
	}
	public void setTt1(String tt1) {
		this.tt1 = tt1;
	}
	public String getTt2() {
		return tt2;
	}
	public void setTt2(String tt2) {
		this.tt2 = tt2;
	}
	public String getTtMark() {
		return ttMark;
	}
	public void setTtMark(String ttMark) {
		this.ttMark = ttMark;
	}
	public String getAttendance() {
		return attendance;
	}
	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	public String getFinalMark() {
		return finalMark;
	}
	public void setFinalMark(String finalMark) {
		this.finalMark = finalMark;
	}
	public String getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(String totalMark) {
		this.totalMark = totalMark;
	}
	public String getCgpa() {
		return cgpa;
	}
	public void setCgpa(String cgpa) {
		this.cgpa = cgpa;
	}
	public String getGradeLetter() {
		return gradeLetter;
	}
	public void setGradeLetter(String gradeLetter) {
		this.gradeLetter = gradeLetter;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public String getExamHeld() {
		return examHeld;
	}
	public void setExamHeld(String examHeld) {
		this.examHeld = examHeld;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
}
	