package com.sv.pghms.service;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sv.pghms.dao.AdminDao;
import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.model.TResultForm;
import com.sv.pghms.model.TUser;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminDao adminDao;
	
	@Override
	public boolean insertResult(TResultForm resultForm) {
		
		return adminDao.insertResult(resultForm);
	}

	@Override
	public boolean insertUser(TUser user) {
		
		return adminDao.insertUser(user);
	}
	
	@Override
	public List<TResultForm> getresultFormList() {
	
		return adminDao.getresultFormList();
	}

	@Override
	public List<TResultForm> getSingleResultForm(String regNo, String courseNo, String examHeld, String batchNo) {
		
		return adminDao.getSingleResultForm(regNo, courseNo, examHeld, batchNo);
	}

	@Override
	public boolean deleteSingleResult(String id) {
		
		return adminDao.deleteSingleResult(id);
	}

	@Override
	public TUser getUserByLoginID(String id) {
		
		return adminDao.getUserByLoginId(id);
	}

	@Override
	public boolean insertCourseDetails(TCourseDetails courseDetails) {
		
		return adminDao.insertCourseDetails(courseDetails);
	}

	@Override
	public List<TCourseDetails> getCourseDetailsList() {
		
		return adminDao.getCourseDetailsList();
	}

	@Override
	public boolean insertBatch(TResultForm resultForm) {
		
		return adminDao.insertBatch(resultForm);
	}

	@Override
	public List<TCourseDetails> getCourseListFormQuery(String courseNo,String examHeld, String batchNo) {
		
		return adminDao.getCourseListFormQuery(courseNo, examHeld, batchNo);
	}


	@Override
	public List<TResultForm> getresultListFromQuery(String courseNo, String batchNo) {
		
		return adminDao.getresultListFromQuery(courseNo, batchNo);
	}

	@Override
	public boolean deletePreviousRow(String regNoFromPathVariable, String courseNoFromPathVariable, String examHeldFromPathVariable) {
		
		return adminDao.deletePreviousRow(regNoFromPathVariable,courseNoFromPathVariable, examHeldFromPathVariable);
	}

	@Override
	public boolean deleteSingleCourse(String courseNo, String batchNo) {
		
		return adminDao.deleteSingleCourse(courseNo, batchNo);
	}

	@Override
	public List<TCourseDetails> getSingleCourse(String courseNo, String batchNo) {
		
		return adminDao.getSingleCourse(courseNo, batchNo);
	}

	@Override
	public List<TUser> getAllUser() {
		
		return adminDao.getAllUser();
	}

	@Override
	public TUser getSingleUser(String id) {
		
		return adminDao.getSingleUser(id);
	}

	@Override
	public boolean deleteSingleUser(String id) {
		
		return adminDao.deleteSingleUser(id);
	}

	@Override
	public boolean activateUser(String userLoginId) {
		
		return adminDao.activateUser(userLoginId);
	}

	@Override
	public boolean deactivateUser(String userLoginId) {
		
		return adminDao.deactivateUser(userLoginId);
	}

	@Override
	public List<TResultForm> getresultListQuery(String courseNo, String batchNo) {
		
		return adminDao.getresultListQuery(courseNo, batchNo);
	}

	@Override
	public boolean deletePreviousROW(String regNo, String courseNo, String batchNo) {
		
		return adminDao.deletePreviousROW(regNo, courseNo, batchNo);
	}

	@Override
	public boolean deleteSingleStudent(String regNo, String courseNo, String batchNo) {
		
		return adminDao.deleteSingleStudent(regNo, courseNo, batchNo);
	}

	@Override
	public List<TResultForm> getresultListFor3Query(String regNo, String courseNo, String batchNo) {
		
		return adminDao.getresultListFor3Query(regNo, courseNo, batchNo);
	}

	@Override
	public List<String> getcourseDetailsList_courseNo() {
		
		return adminDao.getcourseDetailsList_courseNo();
	}

	@Override
	public List<String> getcourseDetailsList_examHeld() {
		
		return adminDao.getcourseDetailsList_examHeld();
	}

	@Override
	public List<String> getcourseDetailsList_batchNo() {
		
		return adminDao.getcourseDetailsList_batchNo();
	}

}
