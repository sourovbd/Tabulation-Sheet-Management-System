package com.sv.pghms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_courseDetails")
public class TCourseDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="t_rowNo")
	private String rowNo;
	@Column(name="t_courseNo")
	private String courseNo;
	@Column(name="t_courseTitle")
	private String courseTitle;
	@Column(name="t_courseCredit")
	private String courseCredit;
	@Column(name="t_batchNo")
	private String batchNo;
	@Column(name="t_semester")
	private String semester;
	@Column(name="t_examYear")
	private String examYear;
	@Column(name="t_examHeld")
	private String examHeld;
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getCourseCredit() {
		return courseCredit;
	}
	public void setCourseCredit(String courseCredit) {
		this.courseCredit = courseCredit;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getExamYear() {
		return examYear;
	}
	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}
	public String getExamHeld() {
		return examHeld;
	}
	public void setExamHeld(String examHeld) {
		this.examHeld = examHeld;
	}
	
}
