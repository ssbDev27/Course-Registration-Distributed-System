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
import java.net.MulticastSocket;
import java.net.SocketException;

import org.omg.CORBA.ORB;

import StudentAdvisorInterfaceModule.StudentAdvisorInterfacePOA;


/**
 * This class implements the remote interface SomeInterface.
 */


public class FEServerImplementation extends StudentAdvisorInterfacePOA
   {
  
	  /**
	 * 
	 */
	
	static Map<String, Integer> UDPRepository = new HashMap<String, Integer>();
	
	static Map<String, Integer> UDPRepository_new_Components = new HashMap<String, Integer>();
	
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
		UDPRepository_new_Components.putAll(new ServerConnectionRepository().UdpPorts_new_Components);

	}
	
	public FEServerImplementation(String Department, Logger logger) throws SecurityException, IOException, RemoteException	 {
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
		//final DatagramSocket aSocket = null;
		String Server = "";
		
		for (String key:UDPRepository_new_Components.keySet()) {
			if (UDPRepository_new_Components.get(key)==UDPPort) {
				Server=key;
			}
		}
		try {
			final MulticastSocket aSocket = new MulticastSocket(6666);
			byte[] message = msg.getBytes();//message to be sent
			InetAddress aHost = InetAddress.getByName("localhost");
			aSocket.joinGroup(InetAddress.getByName("230.1.2.5"));
			DatagramPacket request = new DatagramPacket(message, msg.length(), aHost, UDPPort);//changeport to sending server
			aSocket.send(request);
			logger.info("Request message sent to "+ Server +" Server with port number " + UDPPort + " is: "+ new String(request.getData()));
			System.out.println("Request message sent to "+ Server +" Server with port number " + UDPPort + " is: "+ new String(request.getData()));
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			
			byte[] buffer2 = new byte[1000];
			DatagramPacket reply2 = new DatagramPacket(buffer2, buffer2.length);
			
			byte[] buffer3 = new byte[1000];
			DatagramPacket reply3 = new DatagramPacket(buffer3, buffer3.length);
			
			final MulticastSocket aSocket2 = new MulticastSocket(6666);
			aSocket2.joinGroup(InetAddress.getByName("230.1.1.6"));

			final MulticastSocket aSocket3 = new MulticastSocket(6666);
			aSocket3.joinGroup(InetAddress.getByName("230.1.1.7"));
			
			
			aSocket.receive(reply);
			String msg_received=new String(buffer, 0, reply.getLength());
			System.out.println(msg_received);

//			Ashmeet	
			aSocket3.receive(reply3);
			String msg_received3=new String(buffer3, 0, reply3.getLength());
			System.out.println(msg_received3);
					
			aSocket2.receive(reply2);
			String msg_received2=new String(buffer2, 0, reply2.getLength());
			System.out.println(msg_received2);



//	        Runnable task1 = new Runnable(){
//	        	msg_received3="";
//	            @Override
//	            public void run(){
//	              System.out.println(Thread.currentThread().getName() + " is running");
//	            }
//	          };

			
//			Runnable task = () -> {
//					
//				try {
////					aSocket.setSoTimeout(10000);
//					aSocket.receive(reply);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};		
//			
//			Runnable task2 = () -> {
//				
//				try {
////					aSocket2.setSoTimeout(10000);
//					aSocket2.receive(reply2);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};			
//
//			Runnable task3 = () -> {
//				
//				try {
////					aSocket3.setSoTimeout(10000);
//					aSocket3.receive(reply3);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			};		
//			
//			Thread thread = new Thread(task);
//			Thread thread2 = new Thread(task2);
//			Thread thread3 = new Thread(task3);
//			
//			thread.start();
//			thread2.start();
//			thread3.start();
//			
//			try {
//				thread.join();
//				thread2.join();
//				thread3.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			String msg_received="";
//			String msg_received2="";
//			String msg_received3="";
//			
			msg_received=new String(buffer, 0, reply.getLength());
			System.out.println(msg_received);

			msg_received2=new String(buffer2, 0, reply2.getLength());
			System.out.println(msg_received2);

			msg_received3=new String(buffer3, 0, reply3.getLength());
			System.out.println(msg_received3);

			
//			aSocket.receive(reply);
//
//			aSocket.receive(reply2);
//			
//			aSocket.receive(reply3);
			
		//	logger.info("Reply received from "+ Server +" Server with port number " + UDPPort + " is: "+ new String(reply.getData()));
		//	System.out.println("Reply received from "+ Server +" Server with port number " + UDPPort + " is: "+ new String(reply.getData()));
//			aSocket.close();
			if (msg_received.trim()=="") {	
				return msg_received2+"#"+msg_received3;	
			}
			else if (msg_received2.trim()=="") {
				return msg_received+"#"+msg_received3;	
			}
			else if (msg_received3.trim()=="") {
				return msg_received+"#"+msg_received2;	
			}

			
			return msg_received+"#"+msg_received2+"#"+msg_received3;
		}
		catch (SocketException e) {
		System.out.println("Socket: " + e.getMessage());
		logger.info("Socket error: " + e.getMessage());
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("IO: " + e.getMessage());
		logger.info("IO error: " + e.getMessage());
	} 
	    return "";
   }

public String buildReturnMsg(String getallreplies) {
	
	
	//System.out.println(getallreplies);
	HashMap<String, String> Replyid_value = new HashMap<String, String>();
	System.out.println(getallreplies);
	String [] Serverreplies = getallreplies.split("#");
	System.out.println(Serverreplies.length);
	String []RM_number= new String[Serverreplies.length];
	String []Seq_Num= new String[Serverreplies.length];
	String []Msg=new String[Serverreplies.length];
	
	for (int i=0;i<=Serverreplies.length-1;i++) {
		
//		try {
		String [] breakReply=Serverreplies[i].split(",");

		RM_number[i]=breakReply[0].trim();
		Seq_Num[i]=breakReply[1].trim();
		Msg[i]=breakReply[2].trim();
		//Replyid_value.put("Seq_Number"+RM_number,breakReply[1].trim());
		//Replyid_value.put("Message"+RM_number,breakReply[2].trim());
		System.out.println(Msg[i]+"###"+Seq_Num[i]+"###"+RM_number[i]);
		Replyid_value.put(Seq_Num[i]+RM_number[i],Msg[i]);
						//SequenceNo          +Replica_No, Msg
//		}
//		catch(Exception e) {
//			if (i!=2) {
//				i=i-1;
//			}
//		}
		
	}
	
	int Fetch_Size=Replyid_value.size();
	System.out.println("size = "+Fetch_Size);
	
	if (Fetch_Size==3) {
		if (Seq_Num[0].equalsIgnoreCase(Seq_Num[1]) && Seq_Num[1].equalsIgnoreCase(Seq_Num[2])) {
			System.out.println("Sequence Numbers of all 3 Servers are same");
		}
		else {
			System.out.println("Diagnose Sequence numbers");
			System.out.println("RM1 = "+Seq_Num[0]+RM_number[0]);
			System.out.println("RM2 = "+Seq_Num[1]+RM_number[1]);
			System.out.println("RM3 = "+Seq_Num[2]+RM_number[2]);
			return "Sequenceerror";
		}
		
		if (Replyid_value.get(Seq_Num[0]+RM_number[0]).equalsIgnoreCase(Replyid_value.get(Seq_Num[1]+RM_number[1])) && Replyid_value.get(Seq_Num[1]+RM_number[1]).equalsIgnoreCase(Replyid_value.get(Seq_Num[2]+RM_number[2]))) {
			System.out.println("Messages of all 3 Servers are same");
			return Replyid_value.get(Seq_Num[0]+RM_number[0]);
		}
		else {
			System.out.println("Diagnose Messages");
			System.out.println("RM1 = "+Replyid_value.get(Seq_Num[0]+RM_number[0]));
			System.out.println("RM2 = "+Replyid_value.get(Seq_Num[1]+RM_number[1]));
			System.out.println("RM3 = "+Replyid_value.get(Seq_Num[2]+RM_number[2]));
			return "Messageerror";
		}
				
	}
	
	if (Fetch_Size==2) {
						
			if (Seq_Num[0].equalsIgnoreCase(Seq_Num[1])){
				System.out.println("Sequence Numbers of both Servers are same");
			}
			else {
				System.out.println("Diagnose Sequence numbers");
				System.out.println("RM_one = "+Seq_Num[0]);
				System.out.println("RM_another = "+Seq_Num[1]);
				return "Sequenceerror";
			}
		

			if (Msg[0].equalsIgnoreCase(Msg[0])){
				
			if (Msg[0].equalsIgnoreCase(Msg[1])){
				System.out.println("Same Messages found of both servers");
				return Msg[0];
			}
			else {
				System.out.println("Diagnose Messages");
				System.out.println("RM_one = "+Msg[0]);
				System.out.println("RM_another = "+Msg[1]);
				return "Messageerror";
			}
		}
			
	
		}
			


	
//		String Seq_Number=breakReply[1].trim();
//		String Message=breakReply[2].trim();

	
//	String RM_number=breakReply[0].trim();
//	String Seq_Number=breakReply[1].trim();
//	String Message=breakReply[2].trim();

	return "Fetch Size incorrect => "+Fetch_Size+"  <===>  "+Replyid_value;

}

public String addCourse(String CourseID, String Semester, int capacity){
	
	logger.info("Request type - Add a Course");
	
	logger.info("Request parameters - CourseID = "+CourseID+ ", Semester = "+ Semester +", Capacity = "+capacity);
	
	String Department=CourseID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("addCourse,"+Department+","+CourseID+","+Semester+","+capacity, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;
}
public synchronized String removeCourse(String CourseID, String Semester)  {
	
	logger.info("Request type - Remove a Course"); 	
	logger.info("Request parameters - CourseID = "+CourseID+ ", Semester = "+ Semester );
		
	String Department=CourseID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("removeCourse,"+Department+","+CourseID+","+Semester, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;
	

}


public synchronized String listCourseAvailability(String Semester,String Department) { //reimplement with inter server communication
	
	logger.info("Request type - Check Course Availibility");
	
	logger.info("Request parameters - Semester = "+ Semester );

	
	String getallreplies=SendAndGetReply("listCourseAvailability,"+Department+","+Semester, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;
	
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


public synchronized String enrolCourse(String studentID, String CourseID, String Semester) {
	
	logger.info("Request type - EnrollCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", CourseID = "+ CourseID +", Semester = "+ Semester);
	
	String Department=studentID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("enrolCourse,"+Department+","+studentID+","+CourseID+","+Semester, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;

	 	//return sucess
	
// System.out.println(mastermap);
// System.out.println(studentcourse);
}


public String getClassSchedule(String studentID) {

	logger.info("Request type - GetClassSchedule");
	
	logger.info("Request parameters - StudentID = "+studentID);
	
	String Department=studentID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("getClassSchedule,"+Department+","+studentID, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;

}


public synchronized String dropCourse(String studentID, String CourseID) {

	logger.info("Request type - DropCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", "+"CourseID = "+CourseID);

	String Department=studentID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("dropCourse,"+Department+","+studentID+","+CourseID, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;

}

public synchronized String swapCourse(String studentID, String oldCourseID,String newCourseID) {

	logger.info("Request type - SwapCourse");
	
	logger.info("Request parameters - StudentID = "+studentID+", "+"New-CourseID = "+newCourseID+", "+"Old-CourseID = "+oldCourseID);

	
	String Department=studentID.substring(0, 4);
	
	String getallreplies=SendAndGetReply("dropCourse,"+Department+","+studentID+","+oldCourseID+","+newCourseID, UDPRepository_new_Components.get("SEQUENCER"));
	
	String returnMsg=buildReturnMsg(getallreplies);
	
	return returnMsg;

}
} //end class
