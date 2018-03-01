package com.sv.pghms.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sv.pghms.model.TCourseDetails;
import com.sv.pghms.model.TResultForm;
import com.sv.pghms.model.TUser;

@Repository
public class AdminDaoImpl implements AdminDao{

	private SessionFactory sessionFactory;
	public Session session;
	public Transaction tx;
	
	@Required
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public boolean deletePreviousRow(String regNoFromPathVariable, String courseNoFromPathVariable, String examHeldFromPathVariable) {
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();
			
			session.createSQLQuery("DELETE FROM tsms.t_result WHERE t_regNo = '"+regNoFromPathVariable+"' AND t_courseNo = '"+courseNoFromPathVariable+"' AND t_examHeld = '"+examHeldFromPathVariable+"'").addEntity(TResultForm.class).executeUpdate();
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	@Override
	public boolean deletePreviousROW(String regNo, String courseNo, String batchNo) {
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();
			
			session.createSQLQuery("DELETE FROM tsms.t_result WHERE t_regNo = '"+regNo+"' AND t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"'").addEntity(TResultForm.class).executeUpdate();
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	@Override
	public boolean insertResult(TResultForm resultForm) {
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();
			System.out.println("regNo: "+resultForm.getRegNo());
			
			calculateCGPALetterGrade(resultForm);
			
			session.saveOrUpdate(resultForm);
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	
	public boolean insertUser(TUser user){
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();	
			session.saveOrUpdate(user);
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}

	public void calculateCGPALetterGrade(TResultForm resultForm){
		String cgpa, gradeLetter;
		int attendance;
		double tt1, tt2, ttMark, finalMark, totalMark;
		tt1 = Double.parseDouble(resultForm.getTt1());
		tt2 = Double.parseDouble(resultForm.getTt2());
		ttMark = (tt1 + tt2)/2;
		attendance = Integer.parseInt(resultForm.getAttendance());
		finalMark = Double.parseDouble(resultForm.getFinalMark());
		totalMark = (ttMark + attendance + finalMark);
		
		resultForm.setTtMark(String.valueOf(ttMark));
		resultForm.setTotalMark(String.valueOf(totalMark));
		
		if(totalMark >= 40 && totalMark < 45){
			cgpa = String.valueOf(2.0);
			gradeLetter = "C-";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 45 && totalMark < 50){
			cgpa = String.valueOf(2.25);
			gradeLetter = "C";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 50 && totalMark < 55){
			cgpa = String.valueOf(2.5);
			gradeLetter = "C+";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 55 && totalMark < 60){
			cgpa = String.valueOf(2.75);
			gradeLetter = "B-";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 60 && totalMark < 65){
			cgpa = String.valueOf(3.0);
			gradeLetter = "B";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 65 && totalMark < 70){
			cgpa = String.valueOf(3.25);
			gradeLetter = "B+";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 70 && totalMark < 75){
			cgpa = String.valueOf(3.5);
			gradeLetter = "A-";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 75 && totalMark < 80){
			cgpa = String.valueOf(3.75);
			gradeLetter = "A";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}else if(totalMark >= 80 && totalMark <= 100){
			cgpa = String.valueOf(4.0);
			gradeLetter = "A+";
			resultForm.setCgpa(cgpa);
			resultForm.setGradeLetter(gradeLetter);
		}
	}
	@Override
	public List<TResultForm> getresultFormList() {
		
		session = sessionFactory.openSession(); 
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		List<TCourseDetails> courseDetailsList = new ArrayList<TCourseDetails>();
		try {
			
			tx = session.beginTransaction();
			resultFormList =  session.createSQLQuery("SELECT * FROM tsms.t_result WHERE t_courseNo = 'CSE 375' AND t_examHeld = 'May 2017' order by t_regNo").addEntity(TResultForm.class).list();		
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return resultFormList;
	}

	@Override
	public List<TResultForm> getSingleResultForm(String regNo, String courseNo, String examHeld, String batchNo) {
		session = sessionFactory.openSession(); 
		TResultForm resultForm = new TResultForm();
		List<TResultForm> resultList = new ArrayList<TResultForm>();
		try {
			tx = session.beginTransaction();
			resultList = session.createSQLQuery("SELECT * FROM tsms.t_result WHERE t_regNo = '"+regNo+"' AND t_courseNo = '"+courseNo+"' AND t_examHeld = '"+examHeld+"' AND t_batchNo = '"+batchNo+"' order by t_regNo").addEntity(TResultForm.class).list(); 
			tx.commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			tx.rollback();
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();
		}
		return resultList;
	}
	
	@Override
	public boolean deleteSingleResult(String id) {
		
		session = sessionFactory.openSession(); 
		boolean retVal = false;
		try {
			tx = session.beginTransaction();
			TResultForm resultForm = (TResultForm) session.get(TResultForm.class, id);
			//System.out.println("deleted obj: "+user);
			session.delete(resultForm);
			retVal = true;
			tx.commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			tx.rollback();
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();
		}
		return retVal;
	}

	@Override
	public TUser getUserByLoginId(String id) {
		session = sessionFactory.openSession(); 
		TUser user = new TUser();
		try {
			tx = session.beginTransaction();
			user = (TUser) session.get(TUser.class, id);
			tx.commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			tx.rollback();
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();
		}
		return user;
	}

	@Override
	public boolean insertCourseDetails(TCourseDetails courseDetails) {
		
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();	
			session.saveOrUpdate(courseDetails);
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}

	@Override
	public List<TCourseDetails> getCourseDetailsList() {
		
		session = sessionFactory.openSession(); 
		List<TCourseDetails> courseDetailsList = new ArrayList<TCourseDetails>();
		try {
			
			tx = session.beginTransaction();
			courseDetailsList =  session.createQuery("from TCourseDetails order by courseNo").list();
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList;
	}
	//********************************* Batch Entry ***************************************\\
	
	
	public boolean insertBatch(TResultForm resultForm){
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();	
			session.saveOrUpdate(resultForm);
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	
	@Override
	public List<TCourseDetails> getCourseListFormQuery(String courseNo,String examHeld, String batchNo) {
		
		session = sessionFactory.openSession(); 
		List<TCourseDetails> courseDetailsList = new ArrayList<TCourseDetails>();
		try {
			
			tx = session.beginTransaction();			
			courseDetailsList = session.createSQLQuery("SELECT * FROM tsms.t_coursedetails WHERE t_courseNo = '"+courseNo+"' AND t_examHeld = '"+examHeld+"' AND t_batchNo = '"+batchNo+"'").addEntity(TCourseDetails.class).list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList;
	}
	@Override
	public List<TResultForm>  getresultListFromQuery(String courseNo, String batchNo) {
	
		session = sessionFactory.openSession(); 
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		try {
			
			tx = session.beginTransaction();			
			resultFormList = session.createSQLQuery("SELECT * FROM tsms.t_result WHERE t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"' order by t_regNo").addEntity(TResultForm.class).list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return resultFormList;
	}

	@Override
	public boolean deleteSingleCourse(String courseNo, String batchNo) {
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();
			
			session.createSQLQuery("DELETE FROM tsms.t_coursedetails WHERE t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"'").addEntity(TResultForm.class).executeUpdate();
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	@Override
	public boolean deleteSingleStudent(String regNo, String courseNo, String batchNo) {
		boolean retVal = false;
		session = sessionFactory.openSession(); 
		try {
			tx = session.beginTransaction();
			
			session.createSQLQuery("DELETE FROM tsms.t_result WHERE t_regNo = '"+regNo+"' AND t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"'").addEntity(TResultForm.class).executeUpdate();
			retVal = true;
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			session.getTransaction().rollback();
		}
		finally {
			
			session.flush();
			session.clear();
			session.close();
		}
		return retVal;
	}
	@Override
	public List<TCourseDetails> getSingleCourse(String courseNo,String batchNo) {
		
		session = sessionFactory.openSession(); 
		List<TCourseDetails> courseDetailsList = new ArrayList<TCourseDetails>();
		try {
			
			tx = session.beginTransaction();			
			courseDetailsList = session.createSQLQuery("SELECT * FROM tsms.t_coursedetails WHERE t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"'").addEntity(TCourseDetails.class).list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList;
	}
	@Override
	public List<TUser> getAllUser() {
		
		session = sessionFactory.openSession(); 
		TUser user = new TUser();
		List<TUser> userList = new ArrayList<TUser>();
		try {
			
			tx = session.beginTransaction();			
			userList = session.createQuery("From TUser").list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return userList;
	}
	/*public boolean insertUser1(TUser user){
		
		sessionFactory.getCurrentSession().saveOrUpdate(user);
		return true;
	}*/

	@Override
	public TUser getSingleUser(String id) {
		
		session = sessionFactory.openSession();
		TUser user = new TUser();
		
		try{
			tx = session.beginTransaction();
			user = (TUser) session.createSQLQuery("SELECT * from tsms.t_user WHERE user_LoginID = '"+id+"'").addEntity(TUser.class).uniqueResult();
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.clear();
			session.flush();
			session.close();
		}
		return user;
	}
	@Override
	public boolean deleteSingleUser(String id) {
		
		boolean retVal = false;
		session = sessionFactory.openSession();
		TUser user = new TUser();
		
		try{
			tx = session.beginTransaction();
			session.createSQLQuery("DELETE from tsms.t_user WHERE user_LoginID = '"+id+"'").addEntity(TUser.class).executeUpdate();
			retVal = true; 
			tx.commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			session.clear();
			session.flush();
			session.close();
		}
		return retVal;
	}
	@Override
	public boolean activateUser(String userLoginId) {
		
		session = sessionFactory.openSession(); 
		boolean retVal = false;
		try {
			tx = session.beginTransaction();
			TUser user = (TUser) session.get(TUser.class, userLoginId);
			user.setUserStatus(true);
			session.update(user);
			retVal = true;
			tx.commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			tx.rollback();
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();
		}
		return retVal;
	}

	@Override
	public boolean deactivateUser(String userLoginId) {
		
		session = sessionFactory.openSession(); 
		boolean retVal = false;
		try {
			tx = session.beginTransaction();
			TUser user = (TUser) session.get(TUser.class, userLoginId);
			user.setUserStatus(false);
			session.update(user);
			retVal = true;
			tx.commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			retVal = false;
			tx.rollback();
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();
		}
		return retVal;
	}

	@Override
	public List<TResultForm>  getresultListQuery(String courseNo, String batchNo) {
	
		session = sessionFactory.openSession(); 
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		try {
			
			tx = session.beginTransaction();			
			resultFormList = session.createSQLQuery("SELECT * FROM tsms.t_result WHERE t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"' order by t_regNo").addEntity(TResultForm.class).list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return resultFormList;
	}
	@Override
	public List<TResultForm>  getresultListFor3Query(String regNo, String courseNo, String batchNo) {
	
		session = sessionFactory.openSession(); 
		List<TResultForm> resultFormList = new ArrayList<TResultForm>();
		try {
			
			tx = session.beginTransaction();			
			resultFormList = session.createSQLQuery("SELECT * FROM tsms.t_result WHERE t_regNo = '"+regNo+"' AND t_courseNo = '"+courseNo+"' AND t_batchNo = '"+batchNo+"' order by t_regNo").addEntity(TResultForm.class).list(); 			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return resultFormList;
	}

	@Override
	public List<String> getcourseDetailsList_courseNo() {
		session = sessionFactory.openSession(); 
		List<String> courseDetailsList_courseNo = new ArrayList<String>();
		//Query courseDetailsList_courseNo = null;
		try {
			
			tx = session.beginTransaction();
			courseDetailsList_courseNo =  session.createQuery("select distinct courseNo from TCourseDetails order by courseNo").list();
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList_courseNo;
	}

	@Override
	public List<String> getcourseDetailsList_examHeld() {
		session = sessionFactory.openSession(); 
		List<String> courseDetailsList_examHeld = new ArrayList<String>();
		//Query courseDetailsList_examHeld = null;
		try {
			
			tx = session.beginTransaction();
			courseDetailsList_examHeld =  session.createQuery("select distinct examHeld from TCourseDetails order by examHeld").list();
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList_examHeld;
	}

	@Override
	public List<String> getcourseDetailsList_batchNo() {
		session = sessionFactory.openSession(); 
		List<String> courseDetailsList_batchNo = new ArrayList<String>();
		//String[] courseDetailsList_batchNo;
		try {
			
			tx = session.beginTransaction();
			courseDetailsList_batchNo =  session.createQuery("select distinct batchNo from TCourseDetails order by batchNo").list();
			session.getTransaction().commit();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			session.getTransaction().rollback();			
		}
		finally {
			
			session.clear();
			session.flush();
			session.close();			
		}
		return courseDetailsList_batchNo;
	}

}
