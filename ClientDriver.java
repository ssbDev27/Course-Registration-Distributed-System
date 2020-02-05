import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import StudentAdvisorInterfaceModule.StudentAdvisorInterface;
import StudentAdvisorInterfaceModule.StudentAdvisorInterfaceHelper;



public class ClientDriver {

    public static String validCourses []= {"COMP","SOEN","INSE"};
    public static String validRoles []= {"A","S"};
    public static String validSemesters [] = {"FALL","WINTER","SUMMER"};
	private Scanner iDInput;
	static Logger logger;
	static Map<String, Integer> ConnectionRepository = new HashMap<String, Integer>();
	static {
	
	ConnectionRepository.putAll(new ServerConnectionRepository().CorbaPorts);
	}
    
	public String getUniqueID() {
		//#1. Enter ID	

		System.out.println("Please enter your ID: ");
		iDInput = new Scanner(System.in);
		String uniqueID = iDInput.nextLine().toUpperCase().trim();
		
		//#2. Check if format is correct  //TO----DO This check Should have been in Server
		if(uniqueID.isEmpty() || uniqueID.length() < 8 ||(!Arrays.asList(validCourses).contains(uniqueID.substring(0,4)) )|| (!Arrays.asList(validRoles).contains(Character.toString(uniqueID.charAt(4)))) || (!isInteger(uniqueID.substring(5)))){
			System.out.println("Oops! Invalid ID entered, please try again.");
			return getUniqueID(); 
			//continue;
		}
		
		return uniqueID;
		
	}
	
	public String DetermineAdvisorOrStudent(String uniqueID) {  //TO----DO This method Should have been in Server
		
		if (String.valueOf(uniqueID.charAt(4)).equals(validRoles[0])) {
			System.out.println("Advisor is Connected");	
			logger.info("The Advisor "+uniqueID+" of "+uniqueID.substring(0,4)+" Department is logged in");
			return "Advisor";
		}
		else if (String.valueOf(uniqueID.charAt(4)).equals(validRoles[1])) {
			logger.info("The Student "+ uniqueID +" of "+uniqueID.substring(0,4)+" Department is logged in");
			System.out.println("Student is Connected");	
			return "Student";
		}
		return "error";
	}
	
	public String checkDepartment(String uniqueID) {
		if (uniqueID.substring(0,4).equals(validCourses[0])) {
			System.out.println("Department is Computer Science");
			return "COMP";
		}
		if (uniqueID.substring(0,4).equals(validCourses[1])) {
			System.out.println("Department is Software Engineering");
			return "SOEN";
		}
		if (uniqueID.substring(0,4).equals(validCourses[2])) {
			System.out.println("Department is Information Security");
			return "INSE";
		}
		return "";
	}
	
	public boolean isInteger( String input ) {
	    try {
	        Integer.parseInt( input );
	        return true;
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	
	public String getandValidateCourseID(String Department,boolean IsAdvisor,boolean StudentOption)
	{
    	System.out.println("Please enter the Course ID:");
		iDInput = new Scanner(System.in);
		String CourseID = iDInput.nextLine().toUpperCase().trim();
		if(CourseID.isEmpty() || CourseID.length() < 7 || (!Arrays.asList(validCourses).contains(CourseID.substring(0,4)) || (!isInteger(CourseID.substring(4)))))
		{
			System.out.println("Oops! Invalid Course ID entered, please try again.");
			return getandValidateCourseID(Department,IsAdvisor,StudentOption); 
			//continue;
		}
		else if ((!CourseID.substring(0,4).equals(Department)) && IsAdvisor && !StudentOption) {
			System.out.println("Oops! You are a advisor of "+Department+ " Department. The course of "+CourseID.substring(0,4)+ "Department Cannot be added. Please try again");
			return getandValidateCourseID(Department,IsAdvisor,StudentOption);
		}

		return CourseID;
	}
	public String getandValidateStudentID(String Department) {
		
    	System.out.println("Please enter the Student ID:");
		iDInput = new Scanner(System.in);
		String StudentID = iDInput.nextLine().toUpperCase().trim();

		if(StudentID.isEmpty() || StudentID.length() < 8 || !StudentID.substring(0,4).equalsIgnoreCase(Department) || StudentID.charAt(4)!='S' || (!isInteger(StudentID.substring(5)))){
			System.out.println("Oops! Invalid Student ID entered for "+Department+" department, please try again.");
			return getandValidateStudentID(Department); 
			//continue;
		}
		
		return StudentID;

	}
	public String getandValidateSemester()
	{
    	System.out.println("Please enter the Semester:");
		iDInput = new Scanner(System.in);
		String Semester = iDInput.nextLine().toUpperCase().trim();
		if(Semester.isEmpty() || (!Arrays.asList(validSemesters).contains(Semester)))
		{
			System.out.println("Oops! Invalid Semester entered, please try again.");
			return getandValidateSemester(); 
			//continue;
		}

		return Semester;
	}
	
	public int getandValidateCapacity()
	{
    	System.out.println("Please enter the Capacity of this course:");
		iDInput = new Scanner(System.in);
		String capacity = iDInput.nextLine().toUpperCase().trim();
		if(capacity.isEmpty() || (!isInteger(capacity)))
		{
			System.out.println("Oops! Invalid Capacity entered, please try again.");
			return getandValidateCapacity(); 
			//continue;
		}

		return Integer.parseInt(capacity);
	}

	
	public String showAdvisorMenu(String Department, boolean IsAdvisor,StudentAdvisorInterface h){

		
		logger.info("The Advisor is required to choose an action");
		System.out.println("Hello, Advisor of "+Department+" Department, Please choose an action from below\n");
		
		HashMap<String,String> advisorActionOptions = new HashMap<String,String>();
		advisorActionOptions.put("1","Add Course");
		advisorActionOptions.put("2","Remove Course");
		advisorActionOptions.put("3","List Course Availibity");
		advisorActionOptions.put("4","Enroll into course for a student");
		advisorActionOptions.put("5","See the class Schedule for a student");
		advisorActionOptions.put("6","Drop Course for a student");
		advisorActionOptions.put("7","Swap a Course for a student");
		advisorActionOptions.put("8","Logout");
		
		System.out.println("1: "+advisorActionOptions.get("1"));
		System.out.println("2: "+advisorActionOptions.get("2"));
		System.out.println("3: "+advisorActionOptions.get("3"));
		System.out.println("4: "+advisorActionOptions.get("4"));
		System.out.println("5: "+advisorActionOptions.get("5"));
		System.out.println("6: "+advisorActionOptions.get("6"));
		System.out.println("7: "+advisorActionOptions.get("7"));
		System.out.println("8: "+advisorActionOptions.get("8")+"\n");
		
		System.out.println("Your selection is:");
		
		iDInput = new Scanner(System.in);
		String action = iDInput.nextLine().trim();
		
		try {
			logger.info("The Advisor has chosen to "+advisorActionOptions.get(action));
		}
		catch(NullPointerException e) {
			logger.info("The Advisor has chosen to an invalid option will try again to select");
		}
		
		String CourseID,Semester,studentID = "";
		int Capacity=0;
		String result="";
		switch(action) {
        case "1" :
        	
    		CourseID = getandValidateCourseID(Department, true , false);
    		
    	
    		//System.out.println(CourseID);
    		Semester = getandValidateSemester();
    		//System.out.println(Semester);
    		Capacity = getandValidateCapacity();
    		//System.out.println(Capacity);
    		logger.info("The Parameters send to "+Department+" Server for adding course are: = "+ "CourseID = "+CourseID+ ", Semester = "+Semester+ ", Capacity=" +Capacity);
    		try {
    			result = h.addCourse(CourseID, Semester, Capacity);
    			System.out.println(result);
    			
    			logger.info("Message from "+Department +" Server for adding this course is: - "+result);
    			
				} 
    		catch (Exception e) {	
    			logger.info("Some problem error occured while adding Course "+e);
    			e.printStackTrace(); // handle better and logs too.
				}
           break;
           
        case "2" :
        	
        	CourseID = getandValidateCourseID(Department, true , false);
    		//System.out.println(CourseID);
    		Semester = getandValidateSemester();
    		//System.out.println(Semester);
    		
    		logger.info("The Parameters send to "+Department+" Server for Removing course are: = "+ " CourseID = "+CourseID+ ", Semester = "+Semester);

            try {
            	result = h.removeCourse(CourseID, Semester);
            	logger.info("Message from "+Department +" Server for removing this course is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {
            	logger.info("Some problem error occured while removing Course "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
    		break;
    		
        case "3" :
    		//System.out.println(CourseID);
    		Semester = getandValidateSemester();
    		//System.out.println(Semester);
       		logger.info("The Parameters send to "+Department+" Server for Viewing course Availaibilty are: = "+ "Semester = "+Semester);

            try {
    		    result= h.listCourseAvailability(Semester,Department);
    		    logger.info("Message from "+Department +" Server for Viewing the course Availibity is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while Viewing course Availaibilty Course "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
            break;
            
        case "4" :
        	
        	CourseID = getandValidateCourseID(Department, true , true);
    		//System.out.println(CourseID);
    		Semester = getandValidateSemester();
    		
    		studentID = getandValidateStudentID(Department);
    		
       		logger.info("The Parameters send to "+Department+" Server for enrolling a student into a Course are: = "+ "CourseID = "+CourseID+ ", Semester = "+Semester+ ", studentID=" +studentID);
    		
            try {
    		    result= h.enrolCourse(studentID, CourseID, Semester);
    		    logger.info("Message from "+Department +" Server for enrolling a student is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while enrolling a Course "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
            break;
            
        case "5" :
        	studentID = getandValidateStudentID(Department);
        	
       		logger.info("The Parameters send to "+Department+" Server for viewing a class Schedule of a student are: = "+"studentID=" +studentID);
      	
            try {
    		    result= h.getClassSchedule(studentID);
    		    logger.info("Message from "+Department +" Server for viewing a class Schedule of a student is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while viewing a class Schedule of a student "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
       	
           break;
           
        case "6" :	
        	CourseID = getandValidateCourseID(Department, true , true);
    		
    		studentID = getandValidateStudentID(Department);

       		logger.info("The Parameters send to "+Department+" Server for Dropping a Course for a student are: = "+ "CourseID = "+CourseID+ ", studentID=" +studentID);
    		
            try {
    		    result= h.dropCourse(studentID, CourseID);
    		    logger.info("Message from "+Department +" Server for Dropping a Course for a student is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while Dropping a Course for a student "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
        	
           break;
        case "7":
        	
    		studentID = getandValidateStudentID(Department);
    		
        	System.out.println("Please select the old course which the student wants to swap");
        	String oldCourseID = getandValidateCourseID(Department, true , true);
        	
        	System.out.println("Please select the new course which the student want to enroll");
        	String newCourseID = getandValidateCourseID(Department, true , true);        	
        	
            try {
    		    result= h.swapCourse(studentID, oldCourseID, newCourseID);
    		    logger.info("Message from "+Department +" Server for Swapping a Course for a student is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while Swapping a Course for a student "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
        	
    		        	
        	break;
        case "8" :
        	logger.info("The Advisor of "+Department+" department has logged out");
        	return "The Advisor of "+Department+" department has logged out";
        	
        default :
           System.out.println("Invalid Action Selected. Please try again.");
           return showAdvisorMenu(Department, IsAdvisor, h);
           
     }
		System.out.println("Please press * to Logout or any other key to perform other operations.......");
		
		iDInput = new Scanner(System.in);
		action = iDInput.nextLine().trim();
		
		if (action.equalsIgnoreCase("*")){
			logger.info("The Advisor of "+Department+" department has logged out");
			return "The Advisor of "+Department+" department has logged out";
		}
		else {
			logger.info("The Advisor of "+Department+" department has chosen to perform another action");
			return showAdvisorMenu(Department, IsAdvisor, h);
		}
	
	}
	
	public String showStudentMenu(String StudentID, boolean IsAdvisor,StudentAdvisorInterface h) {
		
		logger.info("The Student is required to choose an action");
		
		System.out.println("Hello, "+StudentID+". Please choose an action from below\n");
		
		HashMap<String,String> StudentActionOptions = new HashMap<String,String>();
		
		StudentActionOptions.put("1","Enroll into a course");
		StudentActionOptions.put("2","See your the class Schedule");
		StudentActionOptions.put("3","Drop a Course");
		StudentActionOptions.put("4","Swap a Course");
		StudentActionOptions.put("5","Logout");
	
		System.out.println("1: "+StudentActionOptions.get("1"));
		System.out.println("2: "+StudentActionOptions.get("2"));
		System.out.println("3: "+StudentActionOptions.get("3"));
		System.out.println("4: "+StudentActionOptions.get("4"));
		System.out.println("5: "+StudentActionOptions.get("5")+"\n");
		System.out.println("Your selection is:");

		
		String CourseID = "",Semester= "";
		String result="";

		iDInput = new Scanner(System.in);
		String action = iDInput.nextLine().trim();
		
		try {
			logger.info("The Student has chosen to "+StudentActionOptions.get(action));
		}
		catch(NullPointerException e) {
			logger.info("The Student has chosen to an invalid option will try again to select");
		}

		String Department = StudentID.substring(0, 4);
		
		switch(action) {
        case "1" :
        	CourseID = getandValidateCourseID(Department, false , true);
    		//System.out.println(CourseID);
    		Semester = getandValidateSemester();
    		
      		logger.info("The Parameters send to "+Department+" Server for enrolling into a Course are: = "+ "CourseID = "+CourseID+ ", Semester = "+Semester+ ", studentID=" +StudentID);
      		     		
            try {
    		    result= h.enrolCourse(StudentID, CourseID, Semester);
      		    logger.info("Message from "+Department +" Server for enrolling into a course is: - "+result);
    		    System.out.println(result);
    			} 
            catch (Exception e) {	
            	logger.info("Some problem error occured while enrolling a Course "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
            
           break;
        case "2" :
        	
       		logger.info("The Parameters send to "+Department+" Server for viewing class Schedule are: = "+"studentID=" +StudentID);
       	 
            try {
    		    result= h.getClassSchedule(StudentID);
    		    
       		    logger.info("Message from "+Department +" Server for viewing class Schedule is: - "+result);
    		    System.out.println(result);
    			} 
            catch (Exception e) {	
            	logger.info("Some problem error occured while viewing class Schedule "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
           break;
        	
        case "3" :
        	
        	CourseID = getandValidateCourseID(Department, false , true);
        	
      		logger.info("The Parameters send to "+Department+" Server for Dropping a Course are: = "+ "CourseID = "+CourseID+ ", studentID=" +StudentID);
      		 
            try {
    		    result= h.dropCourse(StudentID, CourseID);
    		    logger.info("Message from "+Department +" Server for Dropping a Course is: - "+result);
    		    System.out.println(result);
    			} 
            catch (Exception e) {	
            	logger.info("Some problem error occured while Dropping a Course "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
           break;
           
        case "4":
        	
        	//start
//     	   Runnable task = () -> {
//     			
//               try {
//       		    String result4= h.swapCourse("COMPS1234", "COMP6231", "COMP8872");
//       		    logger.info("Message from "+Department +" Server for Swapping a Course for a student is: - "+result4);
//       		    System.out.println(result4);
//       			} 
//               catch (StudentAdvisorInterfaceModule.RemoteException e) {	
//               	logger.info("Some problem error occured while Swapping a Course for a student "+e);
//       			e.printStackTrace(); // handle better and logs too.
//       			}
//    
//    	   };
//    	   Runnable task2 = () -> {
//    		
//               try {
//       		    String result2= h.swapCourse("COMPS0022", "COMP6231", "COMP8872");
//       		    logger.info("Message from "+Department +" Server for Swapping a Course for a student is: - "+result2);
//       		    System.out.println(result2);
//       			} 
//               catch (StudentAdvisorInterfaceModule.RemoteException e) {	
//               	logger.info("Some problem error occured while Swapping a Course for a student "+e);
//       			e.printStackTrace(); // handle better and logs too.
//       			}
//    
//    	   };
//    	   
//    	   Runnable task3 = () -> {
//       		
//               try {
//       		    String result3= h.swapCourse("COMPS1122", "COMP6231", "COMP8872");
//       		    logger.info("Message from "+Department +" Server for Swapping a Course for a student is: - "+result3);
//       		    System.out.println(result3);
//       			} 
//               catch (StudentAdvisorInterfaceModule.RemoteException e) {	
//               	logger.info("Some problem error occured while Swapping a Course for a student "+e);
//       			e.printStackTrace(); // handle better and logs too.
//       			}
//    
//    	   };
// 
//    	   Thread thread = new Thread(task);
//    	   Thread thread2 = new Thread(task2);
//    	   Thread thread3 = new Thread(task3);
//    	
//    	   thread.start();
//    	   thread2.start();
//    	   thread3.start();

        	//stop
        	        	
        	System.out.println("Please select the old course which you want to drop");
        	String oldCourseID = getandValidateCourseID(Department, true , true);
        	
           	System.out.println("Please select the new course which you want to enroll");
        	String newCourseID = getandValidateCourseID(Department, true , true);
        	
        	
            try {
    		    result= h.swapCourse(StudentID, oldCourseID, newCourseID);
    		    logger.info("Message from "+Department +" Server for Swapping a Course for a student is: - "+result);
    		    System.out.println(result);
    			} 
            catch (StudentAdvisorInterfaceModule.RemoteException e) {	
            	logger.info("Some problem error occured while Swapping a Course for a student "+e);
    			e.printStackTrace(); // handle better and logs too.
    			}
        	
    		        	
        	break;
   
        case "5" :
           logger.info("The Student "+StudentID+" has logged out");
           return "The Student "+StudentID+" has logged out";


        default :
           System.out.println("Invalid Action Selected. Please try again.");
           return showStudentMenu(StudentID,IsAdvisor,h);
           
     }
		
		System.out.println("Please press * to Logout or any other key to perform other operations.......");
		iDInput = new Scanner(System.in);
		action = iDInput.nextLine().trim();
		if (action.equalsIgnoreCase("*")){
			logger.info("The Student "+StudentID+" has logged out");
			return "The Student "+StudentID+" has logged out";
		}
		else {
			logger.info("The Student "+StudentID+" has chosen to perform another action");
			return showStudentMenu(StudentID, IsAdvisor, h);
		}
		
	
	}
	
	public static void main(String[] args) throws Exception{
		
		logger = Logger.getLogger("Client - logs");
		
		LogFormatter logFormatter = new LogFormatter();
		 
		logger.setUseParentHandlers(false);
		
		new File(System.getProperty("user.home") + "/Desktop/DCRSClientlogs").mkdir();
		
		while (true) {
			
		
		System.out.println("*******************************");
		System.out.println("*****   Welcome to DCRS   *****");
		System.out.println("*******************************\n\n");
		
		ClientDriver cd = new ClientDriver();
		//#1. Enter ID
		//#2. Check if format is correct
		String uniqueID = cd.getUniqueID();
		System.out.println(uniqueID);
		
		LogFormatter.loggingfor = uniqueID ;
		
		FileHandler logFileHandler = new FileHandler(System.getProperty("user.home") + "/Desktop/DCRSClientlogs/Client-" +uniqueID+ "-log.log",true);
		logger.addHandler(logFileHandler);
		
		logFileHandler.setFormatter(logFormatter);
		//#3. Call Advisor or Student
		String AdvisorOrStudent = cd.DetermineAdvisorOrStudent(uniqueID);
		//UniversalClient client = new UniversalClient("a","");
		
		
		//#4. Determine Department
		String Department = cd.checkDepartment(uniqueID);
		//String registryURL = "rmi://localhost:" + ConnectionRepository.get(Department) + "/"+Department; 		
		//System.out.println(registryURL);
		String orbsetlocation[] = new String[]{"-ORBInitialPort",String.valueOf(ConnectionRepository.get("FE")),"-ORBInitialHost","localhost"};


		
		
		logger.info("The "+AdvisorOrStudent+", "+uniqueID+" is trying to connect to "+Department+" server");
		
		//StudentAdvisorInterface h = (StudentAdvisorInterface)Naming.lookup(registryURL);
		ORB orb = ORB.init(orbsetlocation, null);
		//-ORBInitialPort 1050 -ORBInitialHost localhost
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
		StudentAdvisorInterface h = (StudentAdvisorInterface) StudentAdvisorInterfaceHelper.narrow(ncRef.resolve_str("FE"));

		
		logger.info("The "+AdvisorOrStudent+", "+uniqueID+" is now successfully connected with the "+"FE"+" server");
		
		if (AdvisorOrStudent=="Advisor") {
			cd.showAdvisorMenu(Department,true,h);
		}
		else if (AdvisorOrStudent=="Student"){
			cd.showStudentMenu(uniqueID,false,h);
		}
		
		for (int i=0;i<=1000;i++) {
			System.out.println();
			
		}
		}
	}

//#5. Setup Advisor or Student Menus

//#6. Connect to appropriate Server
	
//#7. Give menu for Advisor or Student
	
//#8. Interconnect servers with lookup
}
