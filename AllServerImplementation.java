import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.omg.CORBA.ORB;

import StudentAdvisorInterfaceModule.StudentAdvisorInterfacePOA;


/**
 * This class implements the remote interface SomeInterface.
 */


public class AllServerImplementation 
   {
  
	  /**
	 * 
	 */
	
	static Map<String, Integer> UDPRepository = new HashMap<String, Integer>();
	
	private static final long serialVersionUID = 1L;

	static HashMap<String, Map<String, Integer>> mastermap = new HashMap<String, Map<String, Integer>>();
	   
	static HashMap<String, Map<String, HashSet<String>>> studentcourse = new HashMap<String, Map<String, HashSet<String>>>(); 
	
	static HashMap<String, Map<String, Integer>> maxcourseLimit = new HashMap<String, Map<String, Integer>>();
	
	static HashMap<String, Integer> CrossDeptCourses = new HashMap<String, Integer>();
	    
	Logger logger;

	public String Department;
	
	static String check_swap_course="";
	
	static {
		
		UDPRepository.putAll(new ServerConnectionRepository().UdpPorts);
		
  	//  UDPRepository.put("COMP", 9999);
	//	UDPRepository.put("SOEN", 8888);
	//	UDPRepository.put("INSE", 7777);

	}
	
	public AllServerImplementation(String Department, Logger logger) throws SecurityException, IOException, RemoteException	 {
      super( );
		this.Department=Department;
		this.logger=logger;
		
      //mastermap.get("Fall").put("Soen6441", 50);
      //mastermap.get("Fall").put("Comp6231", 50);
      //mastermap.get("Fall").put("Comp6721", 30);
      //mastermap.get("Winter").put("Comp6721", 30);
      //mastermap.get("Winter").put("Comp6441", 50);
      //mastermap.get("Winter").put("Comp6741", 10);
      //mastermap.get("Summer").put("Comp6321", 30);
      //mastermap.get("Summer").put("Comp6551",20);
      if (Department=="COMP") {
    	  mastermap.put("FALL"	,new HashMap(){{put("COMP6231",49);}});
    	  mastermap.put("WINTER",new HashMap(){{put("COMP6741",49);}});
    	  mastermap.put("SUMMER",new HashMap(){{put("COMP6231",48);}});
    	  //mastermap.get("FALL").put("COMP6231", 50);
  	      mastermap.get("WINTER").put("COMP6441", 49);
  	      //mastermap.get("WINTER").put("COMP6741", 10);
  	      mastermap.get("SUMMER").put("COMP6551", 22);
  	      
  	      studentcourse.put("FALL",new HashMap(){{put("COMP6231",new HashSet<String>(){{add("COMPS1234");}});}});
  	      studentcourse.put("WINTER",new HashMap(){{put("COMP6741",new HashSet<String>(){{add("COMPS4432");}});}});
  	      studentcourse.put("SUMMER",new HashMap(){{put("COMP6551",new HashSet<String>(){{add("COMPS9786");}});}});
  	      
  	      studentcourse.get("WINTER").put("COMP6441",new HashSet<String>(){{add("COMPS3452");}});
  	      studentcourse.get("SUMMER").put("COMP6231",new HashSet<String>(){{add("COMPS8743");}});
  	      studentcourse.get("SUMMER").get("COMP6551").add("COMPS1234");
  	      
  	      maxcourseLimit.put("FALL"	,new HashMap(){{put("COMPS1234",3);}});
  	      maxcourseLimit.put("WINTER",new HashMap(){{put("COMPS1234",0);}});
  	      maxcourseLimit.put("SUMMER",new HashMap(){{put("COMPS1234",1);}});
  	      
  	      maxcourseLimit.get("WINTER").put("COMPS4432",1);
  	      maxcourseLimit.get("SUMMER").put("COMPS9786",1);
  	      maxcourseLimit.get("WINTER").put("COMPS3452",1);
  	      maxcourseLimit.get("SUMMER").put("COMPS8743",1);
  	      
  	      CrossDeptCourses.put("COMPS1234",2);
      }
      else if(Department=="SOEN") {
	    mastermap.put("FALL",new HashMap(){{put("SOEN7441",48);}});
	    mastermap.put("WINTER",new HashMap(){{put("SOEN7823",49);}});
  	  	mastermap.put("SUMMER",new HashMap(){{put("SOEN7231",49);}});    
	    //mastermap.put("Fall",new HashMap(){{put("Comp6721", 30);}});
	    //mastermap.put("Winter",testcases);
	    //testcases.remove("Soen6441");
	   
	  //  System.out.println(mastermap);
	    //.put("Summer",testcases);
	    
	    studentcourse.put("FALL",new HashMap(){{put("SOEN7441",new HashSet<String>(){{add("SOENS9818");}});}});
	    studentcourse.put("WINTER",new HashMap(){{put("SOEN7823",new HashSet<String>(){{add("SOENS7787");}});}});
	    studentcourse.put("SUMMER",new HashMap(){{put("SOEN7231",new HashSet<String>(){{add("SOENS5525");}});}});
	    
	    studentcourse.get("FALL").get("SOEN7441").add("COMPS1234");
	    //studentcourse.put("FALL",new HashMap(){{put("SOEN6441","SOENS1234");}});
	    //studentcourse.put("WINTER",new HashMap(){{put("SOEN6441","SOENS1357");}});
	    
	    maxcourseLimit.put("FALL",new HashMap(){{put("SOENS9818",1);}});
	    maxcourseLimit.put("WINTER",new HashMap(){{put("SOENS7787",1);}});
	    maxcourseLimit.put("SUMMER",new HashMap(){{put("SOENS5525",1);}});

	   
	    //studentcourse.get("FALL").put("SOEN6441",new HashSet<String>(){{add("SOENS1234");}});
	    //studentcourse.get("WINTER").put("SOEN6441",new HashSet<String>(){{add("SOEN1357");}});
	    //studentcourse.get("SUMMER").put("COMP6321",new HashSet<String>(){{add("SOEN1357");}});
	    }
      else if(Department=="INSE") {
	    mastermap.put("FALL",new HashMap(){{put("INSE9441",48);}});
	    mastermap.put("WINTER",new HashMap(){{put("INSE9823",49);}});
  	  	mastermap.put("SUMMER",new HashMap(){{put("INSE9231",29);}});    
  	  	
	    studentcourse.put("FALL",new HashMap(){{put("INSE9441",new HashSet<String>(){{add("INSES7011");}});}});
	    studentcourse.put("WINTER",new HashMap(){{put("INSE9823",new HashSet<String>(){{add("INSES8525");}});}});
	    studentcourse.put("SUMMER",new HashMap(){{put("INSE9231",new HashSet<String>(){{add("INSES6585");}});}});

	    studentcourse.get("FALL").get("INSE9441").add("COMPS1234");
	    maxcourseLimit.put("FALL",new HashMap(){{put("INSES7011",1);}});
	    maxcourseLimit.put("WINTER",new HashMap(){{put("INSES8525",1);}});
	    maxcourseLimit.put("SUMMER",new HashMap(){{put("INSES6585",1);}});

      }
      logger.info("Initial data added");
      System.out.println("Initial data added");
   }
   
	private ORB orb;

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}


public String SendAndGetReply(String msg, int UDPPort) {
		DatagramSocket aSocket = null;
		String Server = "";
		
		for (String key:UDPRepository.keySet()) {
			if (UDPRepository.get(key)==UDPPort) {
				Server=key;
			}
		}
		try {
			aSocket = new DatagramSocket();
			byte[] message = msg.getBytes();//message to be sent
			InetAddress aHost = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket(message, msg.length(), aHost, UDPPort);//changeport to sending server
			aSocket.send(request);
			logger.info("Request message sent to "+ Server +" Server with port number " + UDPPort + " is: "+ new String(request.getData()));
			System.out.println("Request message sent to "+ Server +" Server with port number " + UDPPort + " is: "+ new String(request.getData()));
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			aSocket.receive(reply);
			logger.info("Reply received from "+ Server +" Server with port number " + UDPPort + " is: "+ new String(reply.getData()));
			System.out.println("Reply received from "+ Server +" Server with port number " + UDPPort + " is: "+ new String(reply.getData()));
			return new String(reply.getData());
		}
		catch (SocketException e) {
		System.out.println("Socket: " + e.getMessage());
		logger.info("Socket error: " + e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("IO: " + e.getMessage());
		logger.info("IO error: " + e.getMessage());
	} finally {
		if (aSocket != null)
			aSocket.close();
	}
	    return "";
   }


public synchronized String addCourse(String CourseID, String Semester, int capacity){
	
	logger.info("Request type - Add a Course");
	
	logger.info("Request parameters - CourseID = "+CourseID+ ", Semester = "+ Semester +", Capacity = "+capacity);
	try
	{
    if(mastermap.get(Semester).containsKey(CourseID)) {
	   System.out.println("Operation Failed! The Course "+CourseID+" is already added in "+Semester);
	   logger.info("Operation Failed! The Course \"+CourseID+\" is already added in \"+Semester");
	   return "Operation Failed! The Course "+CourseID+" is already added in "+Semester;
	   
    }
    else {
	  mastermap.get(Semester).put(CourseID,capacity); //Check about capacity
	  System.out.println("Operation successfull! The Course "+CourseID+" is added in "+Semester);
	  logger.info("Operation successfull! The Course "+CourseID+" is added in "+Semester);
	  
	   return "Operation successfull! The Course "+CourseID+" is added in "+Semester;
 		}
	}
	
	catch(NullPointerException e) {
		
		mastermap.put(Semester,new HashMap(){{put(CourseID,capacity);}});
		System.out.println("Operation successfull! The Course "+CourseID+" is added in "+Semester);
		logger.info("Operation successfull! The Course "+CourseID+" is added in "+Semester);
		  
		return "Operation successfull! The Course "+CourseID+" is added in "+Semester;
	}

	catch(Exception e) {
		logger.info("Operation failed! Some other exception in removing course \n"+e);
		System.out.println("Operation Failed! check the exception");
		e.printStackTrace();
	    return "Operation Failed! Error in adding course";
	}
}

public synchronized String removeCourse(String CourseID, String Semester)  {
	
	logger.info("Request type - Remove a Course"); 	
	logger.info("Request parameters - CourseID = "+CourseID+ ", Semester = "+ Semester );
	
	String Course_dept=CourseID.substring(0, 4);
	
	ArrayList<String> students_Removed = new ArrayList<String>();
	

	if (mastermap.get(Semester).containsKey(CourseID)) {
		//do nothing
	}
	else {
		logger.info("Operation failed! The Course "+ CourseID  +" is not existing in this "+Semester);
		System.out.println("Operation failed! The Course "+ CourseID  +" is not existing in this "+Semester);
		return "Operation failed! The Course "+ CourseID  +" is not existing in this "+Semester;

	}
	try {	
		if(studentcourse.get(Semester).containsKey(CourseID)) {
			
		logger.info("Removing Students who were enrolled in this course");
		System.out.println("Removing Students who were enrolled in this course");
		
		Iterator<String> i = studentcourse.get(Semester).get(CourseID).iterator(); 
		while (i.hasNext()) 
		{
			
			String stu = i.next(); 
			
			if(Course_dept.equalsIgnoreCase(stu.substring(0, 4))) {
				maxcourseLimit.get(Semester).put(stu,maxcourseLimit.get(Semester).get(stu)-1);
			}
			else {
				//call interserver method to decrement maxcourseLimit and CrossDeptCourses
				String msg_from_other_server=SendAndGetReply("RestoreCourseLimit,"+Semester+","+stu,UDPRepository.get(stu.substring(0, 4)));			
				if (!msg_from_other_server.trim().equalsIgnoreCase("success")) {
					return "error in removing courses of other depts";
				}
			}
			
			logger.info("The Course "+ CourseID +" is removed for the student" +stu);
			System.out.println("The Course "+ CourseID +" is removed for the student" +stu);
			students_Removed.add(stu);

		}
		studentcourse.get(Semester).remove(CourseID);
		}
		
		mastermap.get(Semester).remove(CourseID);
		
		if (students_Removed.isEmpty()) {
			
			logger.info("Operation successfull! The Course "+ CourseID +" is removed for " + Semester);
			System.out.println("Operation successfull! The Course "+ CourseID +" is removed for " + Semester);
			return "Operation successfull! The Course "+ CourseID +" is removed for " + Semester +" and nobody was enrolled in this course";
		}
		else {
			logger.info("Operation successfull! The Course "+ CourseID +" is removed for " + Semester +" with these students removed - \n "+ students_Removed);
			System.out.println("Operation successfull! The Course "+ CourseID +" is removed for " + Semester+" with these students removed - \n "+ students_Removed);
			return "Operation successfull! The Course "+ CourseID +" is removed for " + Semester+ " please inform these students who were enrolled in this course - \n "+ students_Removed;
	
		}
		
		}
	
	catch(Exception e)
	{
		logger.info("Operation failed! Some other exception in removing course \n"+e);
		System.out.println("Operation failed! Error in removing course");
		e.printStackTrace();
		return "Operation failed! Error in removing course";

	}
	

}

public String RestoreCourseLimit(String Semester, String studentID){
	
	logger.info("Request type (Interserver) - Remove a Course - Restore enrolling limits for the Student"); 	
	logger.info("Request parameters (Interserver) - StudentID = "+studentID+ ", Semester = "+ Semester );

	
	try {
		logger.info("A course is deleted in other server, Attempting to increase the Cross Department Capacity and Max Course Limit for "+studentID+ " in "+ Semester);
		System.out.println("A course is deleted in other server, Attempting to increase the Cross Department Capacity and Max Course Limit for "+studentID+ " in "+ Semester);
		maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)-1);
		CrossDeptCourses.put(studentID,CrossDeptCourses.get(studentID)-1);
		logger.info("Operation Successful! Cross Department Capacity is now "+ CrossDeptCourses.get(studentID) +" and Max Course Limit is now "+ maxcourseLimit.get(Semester).get(studentID) +" for "+studentID+ " in "+ Semester);
		System.out.println("Operation Successful! Cross Department Capacity is now "+ CrossDeptCourses.get(studentID) +" and Max Course Limit is now "+ maxcourseLimit.get(Semester).get(studentID) +" for "+studentID+ " in "+ Semester);
		return "success";
	}
	catch (Exception e){
		logger.info("error in reducing the capacity");
		e.printStackTrace();
		return "error in reducing the capacity";
		
	}
}

public synchronized String listCourseAvailability(String Semester) { //reimplement with inter server communication
	
	logger.info("Request type - Check Course Availibility");
	
	logger.info("Request parameters - Semester = "+ Semester );

	Map<String, Integer> UDPPorts = new HashMap<String, Integer>();
	
	UDPPorts.putAll(UDPRepository);

	/*UDPPorts.put("COMP", 9999);
	UDPPorts.put("SOEN", 8888);
	UDPPorts.put("INSE", 7777);*/
	
	String reply = "The availaible courses in "+Semester+" are: \n";
	//System.out.println("The availaible courses in "+Semester+" are: \n");
	String dept ="";
	
	for (String key : mastermap.get(Semester).keySet()) {
		 dept = key.substring(0,4); 
		 break;
    }
	
	
	UDPPorts.remove(dept);
	
	
	String restAvailability="";

	String sendFinal="";
	for (Integer port: UDPPorts.values()) {
		restAvailability="";
		restAvailability = SendAndGetReply("ServerCourseAvailability,"+Semester,port);
		if (restAvailability.trim()!=null)
		{
			sendFinal = sendFinal+restAvailability.trim();
		}	

	}
	//String restAvailability=getCourseDetailsfromOtherServers(Semester, UDPPorts);
		
	logger.info("Operation Successfull");
	logger.info(reply+mastermap.get(Semester).toString()+sendFinal);
	System.out.println(reply+mastermap.get(Semester).toString()+sendFinal);
	return reply+mastermap.get(Semester).toString()+sendFinal;
	
}

/*
private String getCourseDetailsfromOtherServers(String semester, Map<String, Integer> UDPPorts) {
	// TODO Auto-generated method stub
	
	//ArrayList<Integer> ports= new ArrayList<Integer>();
	String resultfromOthers="",resultfromOne="";
	
	for (Integer port : UDPPorts.values()) {
		resultfromOne="";
		
		resultfromOne=SendAndGetReply("ServerCourseAvailability,"+semester,port);
		
		if (resultfromOne!=null)
		{
			resultfromOthers=resultfromOthers+resultfromOne;
		}
	}
	
	return resultfromOthers;
	
	
	//attempted multithreading
	
	String s1;
	Runnable task = () -> {
		String s1 = null;

		s1 = SendAndGetReply("ServerCourseAvailability,"+semester,ports.get(0));
		System.out.println("s1=="+s1);
		
	};
	
	
	Runnable task2 = () -> {
		String s2 = SendAndGetReply("ServerCourseAvailability,"+semester,ports.get(1));
		System.out.println("s2=="+s2);
	};
	Thread thread = new Thread(task);
	Thread thread2 = new Thread(task2);

	thread.start();
	thread2.start();
	
	try {
	thread.join();
	thread2.join();
	
	}
	catch(InterruptedException ie)
	{
		System.out.println("Thread interrupted" +ie);
	}
    
	
	
}*/



/*public int getCoursecountfromothers(String Semester,String studentID,Map<String, Integer> UDPPorts){
	
	int Course_counts_from_others=0;
	
	for (Integer port : UDPPorts.values()) {
		
		Course_counts_from_others=Course_counts_from_others+Integer.parseInt(SendAndGetReply("CourseCountofaStudent,"+Semester+","+studentID,port));
		
	}

	return Course_counts_from_others;
}*/

public String ServerCourseAvailability(String semester) {
	
	logger.info("Request type (Interserver) - Check Course Availibility");
	
	logger.info("Request parameters (Intersever) - Semester = "+ semester );

	
	logger.info("Course Availabity in "+semester+ " is = "+mastermap.get(semester).toString());
	System.out.println("Course Availabity in "+semester+ " is = "+mastermap.get(semester).toString());
	return mastermap.get(semester).toString();
	
}


public synchronized String enrolCourse(String studentID, String CourseID, String Semester) {
	
	logger.info("Request type - EnrollCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", CourseID = "+ CourseID +", Semester = "+ Semester);

	boolean RemoteRequest=false;
	boolean swap_cross_course=false;
	
	if (Thread.currentThread().getStackTrace()[3].getClassName().contains("CorbaServerRequestDispatcherImpl"))
	{
		RemoteRequest=true;
	}
	
	if (check_swap_course.equals("cross_course") && !RemoteRequest) {
		swap_cross_course=true;
	}
	
	
	String dept ="";
	dept = studentID.substring(0,4); 

	String Course_dept="";
	Course_dept=CourseID.substring(0,4); 
	
	/*
	 * In this DCRS, there are different advisors for 3 different servers. They create availability of courses 
	 * along with the semester the course is available in. There are three semesters in a year, Fall, Winter and Summer.
	 * A student can enroll into a course offered by any department, in any semester, if it is still available 
	 * (if the course capacity is not yet full). A server (which receives the request) maintains a max-course count for every 
	 * student. If this count reaches 3, then the student cannot enroll into more courses in that semester. 
	 * In other words, a student can enroll only up to 3 courses in a semester. You should ensure that if the capacity 
	 * of a course is full, more students cannot book that course. Also, a student can take as many courses in his/her 
	 * own department, but only at most 2 from other department courses overall.
	 */
	try {	
	if (dept.equalsIgnoreCase(Course_dept)&& !mastermap.get(Semester).containsKey(CourseID) ){ 
		
		logger.info("Operation failed! The course does not exist in "+Semester);
		System.out.println("Operation failed! The course does not exist in "+Semester);
		return "Operation failed! The course does not exist in "+Semester;
		//return error
    }	
	}
	catch(Exception e) {
		//Do nothing
	}
	
	if (dept.equalsIgnoreCase(Course_dept)) {
	for (String Sem : studentcourse.keySet()) {
		try {
		if (studentcourse.get(Sem).get(CourseID).contains(studentID)) {
			
			logger.info("Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem);
			System.out.println("Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem);
			return "Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem;
		}
		}
		catch(Exception e) {
			//Do nothing
		}
	}
	}
		
	try {
	if  (maxcourseLimit.get(Semester).get(studentID)==3 && RemoteRequest){
		logger.info("Operation failed! The student "+studentID+" is already enrolled in 3 courses in "+Semester);
		System.out.println("Operation failed! The student "+studentID+" is already enrolled in 3 courses in "+Semester);
		return ("Operation failed! The student "+studentID+" is already enrolled in 3 courses in "+Semester);
		//return error
	}
	}
	catch(NullPointerException np) {
		maxcourseLimit.get(Semester).put(studentID,0);
	}
	if (!dept.equalsIgnoreCase(Course_dept)){ //check for at most 2 courses in other depts
		try {
		if (CrossDeptCourses.get(studentID)==2 && !swap_cross_course) {
			
			logger.info("Operation failed! The student "+studentID+" is already enrolled in 2 courses in outside the department");
			System.out.println("Operation failed! The student "+studentID+" is already enrolled in 2 courses in outside the department");
			return "Operation failed! The student "+studentID+" is already enrolled in 2 courses in outside the department";
			//return error
		}
		}
		catch(NullPointerException np) {
			CrossDeptCourses.put(studentID,0);
		}

		//callinterserve comm  - to enroll
		
		String msg_from_other_server=SendAndGetReply("EnrollCrossDept,"+Semester+","+CourseID+","+studentID,UDPRepository.get(Course_dept));
		
		// if success
		if (msg_from_other_server.trim().equalsIgnoreCase("success")) {
			
			CrossDeptCourses.put(studentID,CrossDeptCourses.get(studentID)+1);
			maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)+1);
		
			logger.info("Operation Successfull! The student "+studentID+ " is successfully enrolled in the course "+ CourseID);
			System.out.println("Operation Successfull! The student "+studentID+ " is successfully enrolled in the course "+ CourseID);
			return "Operation Successfull! The student "+studentID+ " is successfully enrolled in the course "+ CourseID;
		}
		else {
			logger.info(msg_from_other_server);
			System.out.println(msg_from_other_server);
			return msg_from_other_server;
		}
			//return sucess msg
		// else
		   // return appropriate msg from that server e.g course does not exist in INSE dept ORRR capacity is full in INSE dept ORRR you are already enrolled in this course in fall/winter/summer
	}
 else {
	 	try {
		if(mastermap.get(Semester).get(CourseID)==0) { 
			   logger.info("Operation failed! The course capacity is full");
			   System.out.println("Operation failed! The course capacity is full");
			   return "Operation failed! The course capacity is full";
			}
	   //studentcourse.get(Semester).put(CourseID,new HashSet<String>(){{add(studentID);}}); //hashset has overrided the rest
	   studentcourse.get(Semester).get(CourseID).add(studentID);
	   mastermap.get(Semester).put(CourseID,mastermap.get(Semester).get(CourseID)-1); //Capacity reduced by one
	   maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)+1);
	   logger.info("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
	   System.out.println("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
	   return "Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester;
	   
	   }
	   catch(NullPointerException e) {
		   
		   mastermap.get(Semester).put(CourseID,mastermap.get(Semester).get(CourseID)-1);
		   studentcourse.get(Semester).put(CourseID,new HashSet<String>(){{add(studentID);}});
		   maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)+1);
		   //maxcourseLimit.put(Semester,new HashMap(){{put(studentID,maxcourseLimit.get(Semester).get(studentID)+1);}});
		   logger.info("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
		   System.out.println("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
		   return "Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in"+ Semester;

	   }
	 	//return sucess
 }
// System.out.println(mastermap);
// System.out.println(studentcourse);
}

public String EnrollCrossDept(String Semester,String CourseID, String studentID) {

	logger.info("Request type (interserver) - EnrollCourse");
	
	logger.info("Request parameters (intersever) - StudentID = "+studentID+", CourseID = "+ CourseID +", Semester = "+ Semester );

	
	if (!mastermap.get(Semester).containsKey(CourseID)){ 
		logger.info("Operation failed! The course does not exist in "+Semester);
		System.out.println("Operation failed! The course does not exist in "+Semester);
		return "Operation failed! The course does not exist in "+Semester;
		//return error
		
    }	
	
	for (String Sem : studentcourse.keySet()) {
		try {
		if (studentcourse.get(Sem).get(CourseID).contains(studentID)) {
			logger.info("Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem);
			System.out.println("Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem);
			return "Operation failed! The Student "+ studentID +" is already enrolled into this course in "+Sem;
		}
		}
		catch(Exception e) {
			//Do nothing
		}
	}
	
	if(mastermap.get(Semester).get(CourseID)==0) { 
		
	   logger.info("Operation failed! The course capacity is full");
	   System.out.println("Operation failed! The course capacity is full");
	   return "Operation failed! The course capacity is full";
	}
	
	mastermap.get(Semester).put(CourseID,mastermap.get(Semester).get(CourseID)-1);
	try {
	studentcourse.get(Semester).get(CourseID).add(studentID);
	}
	catch(NullPointerException e) {
		studentcourse.get(Semester).put(CourseID,new HashSet<String>(){{add(studentID);}});
	}
	logger.info("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
	System.out.println("Operation Successfull! The student "+studentID+" is successfully enrolled in the course "+CourseID+" in "+ Semester);
	return "success";
}

public String getClassSchedule(String studentID) {

	logger.info("Request type - GetClassSchedule");
	
	logger.info("Request parameters - StudentID = "+studentID);

	
	String Summer="";
	String Winter="";
	String Fall="";
	String finaldisplay = "The class schedule of student "+studentID+" are: \n";
	
	for (String Semester : studentcourse.keySet()) {
		for (String Course : studentcourse.get(Semester).keySet()) {
		 if (studentcourse.get(Semester).get(Course).contains(studentID))
		 {
			if (Semester.equalsIgnoreCase("SUMMER"))
			{
				Summer=Summer+Course+",";
			}
			else if (Semester.equalsIgnoreCase("WINTER"))
			{
				Winter=Winter+Course+",";
			}
			else {
				Fall=Fall+Course+",";
			}
			
		 }
	}
	}
	
	try {
	if (CrossDeptCourses.get(studentID)!=0) {
		
		Map<String, Integer> UDPPorts = new HashMap<String, Integer>();
		
		UDPPorts.putAll(UDPRepository);
		
		//System.out.println("The availaible courses in "+Semester+" are: \n");
		String dept = studentID.substring(0,4);
		
		UDPPorts.remove(dept);
		
		for (String key : UDPPorts.keySet()) {
			
			String reply=SendAndGetReply("ServerClassSchedule,"+studentID,UDPPorts.get(key));
			
			String [] breakReply = reply.split("#");
			
			
			if (!breakReply[0].trim().isEmpty()){
				if (Fall!="") {
					Fall=Fall+breakReply[0].trim()+",";
				}
				else {
					Fall=breakReply[0].trim()+",";
				}
			}
			if (!breakReply[1].trim().isEmpty()){
				if (Winter!="") {
					Winter=Winter+breakReply[1].trim()+",";
				}
				else {
					Winter=breakReply[1].trim()+",";
				}
			}
			if (!breakReply[2].trim().isEmpty()){
				if (Summer!="") {
					Summer=Summer+breakReply[2].trim()+",";
				}
				else {
					Summer=breakReply[2].trim()+",";
				}
			}

		}
	}
	}
	catch(NullPointerException e) {
		//Do nothing
	}
	
	if (!Fall.isEmpty()) {
		Fall=Fall.substring(0, Fall.length()-1);
	}
	if (!Winter.isEmpty()) {
		Winter=Winter.substring(0, Winter.length()-1);
	}
	if (!Summer.isEmpty()) {
		Summer=Summer.substring(0, Summer.length()-1);
	}

	logger.info("Operation Successful");
	logger.info(finaldisplay+", Fall: = " +Fall+", "+"Winter: = " +Winter+", "+"Summer: = " +Summer);
	System.out.println(finaldisplay+"Fall: = " +Fall+"\n"+"Winter: = " +Winter+"\n"+"Summer: = " +Summer+"\n");
	return finaldisplay+"Fall: = " +Fall+"\n"+"Winter: = " +Winter+"\n"+"Summer: = " +Summer+"\n";
	//interserver
}

public String ServerClassSchedule(String studentID) {
	
	logger.info("Request type (InterServer)- GetClassSchedule");
	
	logger.info("Request parameters (Interserver) - StudentID = "+studentID);

	String Summer="";
	String Winter="";
	String Fall="";	
	String response="";
	
	for (String Semester : studentcourse.keySet()) {
		for (String Course : studentcourse.get(Semester).keySet()) {
		 if (studentcourse.get(Semester).get(Course).contains(studentID))
		 {
			if (Semester.equalsIgnoreCase("SUMMER"))
			{
				Summer=Summer+Course+",";
			}
			else if (Semester.equalsIgnoreCase("WINTER"))
			{
				Winter=Winter+Course+",";
			}
			else {
				Fall=Fall+Course+",";
			}
			
		 }
	}
	}
	
	
	if (!Fall.equalsIgnoreCase("")) {
		
		logger.info("Courses Available in Fall = "+ Fall.substring(0, Fall.length()-1));
		System.out.println("Courses Available in Fall = "+ Fall.substring(0, Fall.length()-1));
		response=response+Fall.substring(0, Fall.length()-1)+"#";
	}
	else {
		response=response+"#";
	}
	
	if (!Winter.equalsIgnoreCase("")) {
		
		logger.info("Courses Available in Winter = "+ Winter.substring(0, Winter.length()-1));
		System.out.println("Courses Available in Winter = "+ Winter.substring(0, Winter.length()-1));
		response=response+Winter.substring(0, Winter.length()-1)+"#";
	}
	else {
		response=response+"#";
	}

	if (!Summer.equalsIgnoreCase("")) {
		
		logger.info("Courses Available in Summer = "+ Summer.substring(0, Summer.length()-1));
		System.out.println("Courses Available in Summer = "+ Summer.substring(0, Summer.length()-1));
		response=response+Summer.substring(0, Summer.length()-1)+"#";
	}
	else {
		response=response+"#";
	}	
	
	return response;
	
}

public synchronized String dropCourse(String studentID, String CourseID) {

	logger.info("Request type - DropCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", "+"CourseID = "+CourseID);

	
	String dept ="";
	dept = studentID.substring(0,4); 

	String Course_dept="";
	Course_dept=CourseID.substring(0,4); 

	
	boolean found_course = false,enrolled_student_found=false;
	
	String returnmsg="";
	
	if (dept.equalsIgnoreCase(Course_dept)) {
	for (String Semester : mastermap.keySet()) {
		if (mastermap.get(Semester).containsKey(CourseID)) {
			
			found_course=true;
			try {
			if (studentcourse.get(Semester).get(CourseID).contains(studentID)) {
				enrolled_student_found=true;
				
				logger.info("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
				System.out.println("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
				returnmsg="Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester;
				mastermap.get(Semester).put(CourseID,mastermap.get(Semester).get(CourseID)+1);
				studentcourse.get(Semester).get(CourseID).remove(studentID);
				maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)-1);
				
			}
		}
			catch(NullPointerException e) {
				//do Nothing
			}
		}
	}
	}

	else {
		
		String Dropresponse=SendAndGetReply("DropInterServerCourse,"+studentID+","+CourseID,UDPRepository.get(Course_dept));
		String [] breakReply = Dropresponse.split(",");
		
		String Drop_msg=breakReply[0];
		found_course=Boolean.valueOf(breakReply[1].trim());
		enrolled_student_found=Boolean.valueOf(breakReply[2].trim());
		
		
		if (breakReply[0].contains("success")){
			String [] split_drop_msg = Drop_msg.split("#");
			
			String Semester=split_drop_msg[1];
			
			maxcourseLimit.get(Semester).put(studentID,maxcourseLimit.get(Semester).get(studentID)-1);
			CrossDeptCourses.put(studentID,CrossDeptCourses.get(studentID)-1);
			
			logger.info("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
			System.out.println("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
			returnmsg="Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester;

		}
		
		
	}

	if (found_course==false) {
		logger.info("Operation failed! The Course does not exist");
		System.out.println("Operation failed! The Course does not exist");
		returnmsg="Operation failed! The Course does not exist";
	}
	else if(found_course==true && enrolled_student_found==false) {
		logger.info("Operation failed! The student "+studentID+" is not enrolled in this class "+CourseID);
		System.out.println("Operation failed! The student "+studentID+" is not enrolled in this class "+CourseID);
		returnmsg="Operation failed! The student "+studentID+" is not enrolled in this class "+CourseID;
	}
	
	return returnmsg;
}

public String DropInterServerCourse(String studentID, String CourseID) {

	logger.info("Request type (Interserver) - DropCourse ");
	
	logger.info("Request parameters (Interserver) - StudentID = "+studentID+", "+"CourseID = "+CourseID);

	
	boolean found_course = false,enrolled_student_found=false;
	String finalmsg="";
	
	for (String Semester : mastermap.keySet()) {
		if (mastermap.get(Semester).containsKey(CourseID)) {
			
			found_course=true;
			try {
			if (studentcourse.get(Semester).get(CourseID).contains(studentID)) {
				enrolled_student_found=true;
				
				mastermap.get(Semester).put(CourseID,mastermap.get(Semester).get(CourseID)+1);
				studentcourse.get(Semester).get(CourseID).remove(studentID);
				
				logger.info("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
				System.out.println("Operation Successful! The student "+ studentID +" has removed from the course "+ CourseID+ " in "+Semester);
				finalmsg="success#"+Semester;
				
			}
			}
			catch(NullPointerException e) {
				//Do Nothing
			}
		}
	}
	
	if (found_course==false) {
		logger.info("Operation failed! The Course does not exist");
		System.out.println("Operation failed! The Course does not exist");
	}
	else if(found_course==true && enrolled_student_found==false) {
		logger.info("Operation failed! The student "+studentID+" is not enrolled in this class "+CourseID);
		System.out.println("Operation failed! The student "+studentID+" is not enrolled in this class "+CourseID);
	}

	
	return finalmsg+","+found_course+","+enrolled_student_found;
	
	
}

public String CheckInterServerCourse(String studentID, String CourseID, boolean newCourse, String old_found_Semester) {

	logger.info("Request type (Interserver to check the courses for swap Course) - CheckInterServerCourse ");
	
	logger.info("Request parameters (Interserver to check the courses for swap Course) - StudentID = "+studentID+", "+"CourseID = "+CourseID+", "+"Is This a new Course = "+newCourse+", "+"Old Course is found in Semester = "+old_found_Semester);

	
	boolean found_course = false,enrolled_student_found=false,zero_capacity=false;
	String finalmsg="";
	String found_Semester="";

	if (newCourse) {
		
		try {
			if(mastermap.get(old_found_Semester).containsKey(CourseID)) {
				logger.info("The Course "+CourseID+" is found successfully");
				found_course=true;
			}
			
			if (mastermap.get(old_found_Semester).get(CourseID)==0) {
				zero_capacity=true;
			}
		}
		catch(Exception e){
				//Do nothing
		}
		
		try {
			if (studentcourse.get(old_found_Semester).get(CourseID).contains(studentID)) {
				logger.info("The student "+studentID+" is already enrolled into the Course "+CourseID+" has no capacity left");
				enrolled_student_found=true;
			}
		}
		catch(Exception e) {
			//Do nothing
		}

	}
	
	else {
	
	for (String Semester : mastermap.keySet()) {
		if (mastermap.get(Semester).containsKey(CourseID)) {
			
			
			if (mastermap.get(Semester).get(CourseID)==0) {
				logger.info("The Course "+CourseID+" has no capacity left");
				zero_capacity=true;
			}
			
			found_course=true;
			logger.info("The Course "+CourseID+" is found successfully");
			
			try {
			if (studentcourse.get(Semester).get(CourseID).contains(studentID)) {
				logger.info("The student "+studentID+" is already enrolled into the Course "+CourseID+" has no capacity left");
				enrolled_student_found=true;
				found_Semester=Semester;
								
			}
			}
			catch(NullPointerException e) {
				//Do Nothing
			}
		}
	}
	}	
	
	return found_Semester+","+found_course+","+enrolled_student_found+","+zero_capacity;
}


public synchronized String swapCourse(String studentID, String oldCourseID,String newCourseID) {

	logger.info("Request type - SwapCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", "+"New-CourseID = "+newCourseID+", "+"Old-CourseID = "+oldCourseID);

	if (newCourseID.equalsIgnoreCase(oldCourseID)) {
		logger.info("Swapping Failed! Cannot Swap same courses");
		System.out.println("Swapping Failed! Cannot Swap same courses");
		return "Swapping Failed! Cannot Swap same courses";
	}
	
	
	String newcoursedept ="";
	newcoursedept = newCourseID.substring(0,4); 

	String oldcoursedept="";
	oldcoursedept=oldCourseID.substring(0,4); 
	
	String Studentdept="";
	Studentdept=studentID.substring(0,4); 

	
	boolean old_found_course = false,old_enrolled_student_found=false;
	String old_found_Semester=null;
	
	
	if (oldcoursedept.equalsIgnoreCase(Studentdept)) {
	
	for (String Semester : mastermap.keySet()) {
		if (mastermap.get(Semester).containsKey(oldCourseID)) {
			
			old_found_course=true;
			try {
			if (studentcourse.get(Semester).get(oldCourseID).contains(studentID)) {
				old_enrolled_student_found=true;				
				old_found_Semester=Semester;
			}
			}
			catch(NullPointerException e) {
				//Do Nothing
			}
		}
	}
	}
	
	else {
		String Checkresponse=SendAndGetReply("CheckInterServerCourse,"+studentID+","+oldCourseID+","+"false"+","+old_found_Semester,UDPRepository.get(oldcoursedept));
		String [] breakReply = Checkresponse.split(",");
		
		old_found_Semester=breakReply[0].trim();
		old_found_course=Boolean.valueOf(breakReply[1].trim());
		old_enrolled_student_found=Boolean.valueOf(breakReply[2].trim());
		
	}
	
	if (old_found_course==false) {
		logger.info("Operation failed! The Old Course does not exist");
		System.out.println("Operation failed! The old Course does not exist");
		return "Operation failed! The old Course does not exist";
	}
	else if(old_found_course==true && old_enrolled_student_found==false) {
		logger.info("Operation failed! The student "+studentID+" is not enrolled in this course "+oldCourseID);
		System.out.println("Operation failed! The student "+studentID+" is not enrolled in this course "+oldCourseID);
		return "Operation failed! The student "+studentID+" is not enrolled in this course "+oldCourseID;
	}

	
	boolean new_found_course = false,new_enrolled_student_found=false,zero_capacity=false;
	if (newcoursedept.equalsIgnoreCase(Studentdept)) {
		
		try {
			if(mastermap.get(old_found_Semester).containsKey(newCourseID)) {
				new_found_course=true;
			}
			
			if (mastermap.get(old_found_Semester).get(newCourseID)==0) {
				zero_capacity=true;
			}
		}
		catch(Exception e){
				//Do nothing
		}
		
		try {
			if (studentcourse.get(old_found_Semester).get(newCourseID).contains(studentID)) {
				new_enrolled_student_found=true;
			}
		}
		catch(Exception e) {
			//Do nothing
		}
	
	}
	else {
		String Checkresponse=SendAndGetReply("CheckInterServerCourse,"+studentID+","+newCourseID+","+"true"+","+old_found_Semester,UDPRepository.get(newcoursedept));
		String [] breakReply = Checkresponse.split(",");
		
		new_found_course=Boolean.valueOf(breakReply[1].trim());
		new_enrolled_student_found=Boolean.valueOf(breakReply[2].trim());
		zero_capacity=Boolean.valueOf(breakReply[3].trim());
	}
	
	if (new_found_course==false) {
		logger.info("Operation failed! The new Course does not exist");
		System.out.println("Operation failed! The new Course does not exist in "+old_found_Semester);
		return "Operation failed! The new Course does not exist in "+old_found_Semester;
	}
	else if(new_found_course==true && new_enrolled_student_found==true) {
		logger.info("Operation failed! The student "+studentID+" is already enrolled in this course "+newCourseID+" in "+old_found_Semester);
		System.out.println("Operation failed! The student "+studentID+" is already enrolled in this course "+newCourseID+" in "+old_found_Semester);
		return "Operation failed! The student "+studentID+" is already enrolled in this course "+newCourseID;
	}
	else if (zero_capacity==true) {
		logger.info("Operation failed! The capacity of the new course "+newCourseID+" is full");
		System.out.println("Operation failed! The capacity of the new course "+newCourseID+" is full");
		return "Operation failed! The capacity of the new course "+newCourseID+" is full";		
	}
		
	
	if (newcoursedept!=Studentdept && oldcoursedept!=Studentdept) {
		check_swap_course="cross_course";
	}
	
	String enroll_reply=enrolCourse(studentID,newCourseID,old_found_Semester);
	
	check_swap_course="";
	
	String drop_reply="";
	
	
//	try {
//		Thread.sleep(10000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
	if (enroll_reply.contains("Operation Success")){
		
		logger.info("Enrolling success now trying to drop");
		drop_reply=dropCourse(studentID,oldCourseID);
	}
	else {
		logger.info("Swapping failed! Error in enrolling the new course ==> "+enroll_reply);
		System.out.println("Swapping failed! Error in enrolling the new course ==> "+enroll_reply);
		return "Swapping failed! Error in enrolling the new course ==> "+enroll_reply;
	}
	
	
	if (drop_reply.contains("Operation Successful")) {
		logger.info("Swapping successful! The courses have been swapped successfully");
		System.out.println("Swapping successful! The courses have been swapped successfully");
		return "Swapping successful! The courses have been swapped successfully";
	}
	else {
		logger.info("Swapping failed! Error in dropping the old course ==> "+drop_reply);
		System.out.println("Swapping successful! The courses have been swapped successfully");
		return "Swapping failed! Error in dropping the old course ==> "+drop_reply;
	}
	
	//handle further
	
}
} //end class
