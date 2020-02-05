import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.net.*;
import java.io.*;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import StudentAdvisorInterfaceModule.StudentAdvisorInterface;
import StudentAdvisorInterfaceModule.StudentAdvisorInterfaceHelper;


public class CompServer  {
   
   final static String ServerName = "COMP";
   static int RMPort_UDP=0;
   static int UDPPort=0;
   static String orbsetlocation[]= {};
   
   static{
	   ServerConnectionRepository sc = new ServerConnectionRepository();
	   
	   RMPort_UDP=sc.CorbaPorts.get(ServerName);
	   UDPPort=sc.UdpPorts.get(ServerName);
	   //orbsetlocation= new String[]{"-ORBInitialPort",String.valueOf(CorbaPort),"-ORBInitialHost","localhost"};

   }
      
   static Logger logger;
   public static void main(String args[]) throws SecurityException, IOException {
	   	   	   
	   logger = Logger.getLogger(ServerName+ " server" + " - log");
	   
	   LogFormatter logFormatter = new LogFormatter();
	   
	   LogFormatter.loggingfor = ServerName ;
	   
	   FileHandler logFileHandler; 
		   	    
	   logger.setUseParentHandlers(false);
			
	   new File(System.getProperty("user.home") + "/Desktop/Server" + ServerName + "DCRSlogs").mkdir();
			
	   logFileHandler = new FileHandler(System.getProperty("user.home") + "/Desktop/Server" + ServerName + "DCRSlogs/" + ServerName + "server" + "-log.log",true );  
			  
	   logger.addHandler(logFileHandler);
	      
	   logFileHandler.setFormatter(logFormatter);
			
//	   try {
//	   Process p= Runtime.getRuntime().exec(new String[]{"orbd","-ORBInitialPort", String.valueOf(CorbaPort)});
//	   }
//	   catch(Exception ex) {
//		   ex.printStackTrace();
//	   }
//	   	   
//	   logger.info(ServerName+" in now connected with the ORB at port "+CorbaPort);
	   
	   AllServerImplementation exportedObj = new AllServerImplementation(ServerName,logger);
	   
	   Runnable task = () -> {
		
		   UDPreceiveAndReply_RM(RMPort_UDP, exportedObj);
	   };
	   
	   Runnable task2 = () -> {
		
		   UDPreceiveAndReply(UDPPort, exportedObj);
	   };
	   Thread thread = new Thread(task);
	   Thread thread2 = new Thread(task2);
	
	   thread.start();
	   thread2.start();

  } // end main
  

    	private static void UDPreceiveAndReply_RM(int Port ,AllServerImplementation exportedObj) {
    		DatagramSocket aSocket = null;
    		try {
    			aSocket = new DatagramSocket(Port);
    			byte[] buffer = new byte[1000];
    			logger.info(ServerName+" Server is now ready with UDP port "+Port+" for interserver Communication");
    			System.out.println(ServerName+" Server is now ready with UDP port "+Port+" for interserver Communication");
    			while (true) {
    				
    				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
    				aSocket.receive(request);
    				
    				//Convert DatagramPacket object request to String
    				
    				String msg_received=new String(buffer, 0, request.getLength());
    				
    				logger.info("Message received from port "+request.getPort()+" is = "+ msg_received);
    				System.out.println("Message received from port "+request.getPort()+" is = "+ msg_received);
    				
    				String [] breakMsg = msg_received.split(",");
    				//call method from message recieved
    				
    				String Sequence_Num=breakMsg[0];
    				
    				String methodCheck=breakMsg[1];
    				
    				String Department=breakMsg[2];
    			    				
    				String replyStr="";
    				if (methodCheck.equalsIgnoreCase("addCourse")) {
    					 
    					 String CourseId=breakMsg[3].trim();
    					 String Semester=breakMsg[4].trim();
    					 int Capacity=Integer.valueOf(breakMsg[5].trim()) ;
    					 replyStr = exportedObj.addCourse(CourseId,Semester,Capacity);
    					 //replyStr = "COMP SERVER :: "+replyStr;
    				}
    				else if(methodCheck.equalsIgnoreCase("removeCourse")) {
    					
    					String CourseID=breakMsg[3].trim();
    					String Semester=breakMsg[4].trim();
    					
    					replyStr = exportedObj.removeCourse(CourseID,Semester);
    				}
    				else if(methodCheck.equalsIgnoreCase("listCourseAvailability")) {
    					String Semester=breakMsg[3].trim();    					
    					
    					replyStr = exportedObj.listCourseAvailability(Semester);
    				}
    				else if(methodCheck.equalsIgnoreCase("enrolCourse")) {
   					 String studentID=breakMsg[3].trim();
   					 String CourseID=breakMsg[4].trim();
   					 String Semester=breakMsg[5].trim();
   					 replyStr = exportedObj.enrolCourse(studentID,CourseID,Semester);
    				}

    				else if(methodCheck.equalsIgnoreCase("getClassSchedule")) {
    					String studentID=breakMsg[3];
     					
    					replyStr = exportedObj.getClassSchedule(studentID);
    				}
    				
    				else if(methodCheck.equalsIgnoreCase("dropCourse")) {
    					String studentID=breakMsg[3];
    					String CourseID=breakMsg[4];
    					
    					replyStr = exportedObj.dropCourse(studentID,CourseID);
    				}
    				else if(methodCheck.equalsIgnoreCase("swapCourse")) {
    					String studentID=breakMsg[3];
    					String oldCourseID=breakMsg[4];
    					String newCourseID=breakMsg[5];
    					
    					replyStr = exportedObj.swapCourse(studentID,oldCourseID,newCourseID);
    				}


    				
    				logger.info("Message send to port "+request.getPort()+" is ="+ replyStr);
    				System.out.println("Message send to port "+request.getPort()+" is ="+ replyStr);
    				byte[] replyByte = replyStr.getBytes();
    				
    				//breakMsg[0](breakMsg[0]);
    				
    				DatagramPacket reply = new DatagramPacket(replyByte, replyStr.length(), request.getAddress(), 8895);
    				aSocket.send(reply);
    				
    			}
    		} catch (SocketException e) {
    			System.out.println("Socket: " + e.getMessage());
    			logger.info("Socket error: " + e.getMessage());
    		} catch (IOException e) {
    			logger.info("IO error: " + e.getMessage());
    			System.out.println("IO: " + e.getMessage());
    		} finally {
    			if (aSocket != null)
    				aSocket.close();
    		}
    	}   
   
   
    
	private static void UDPreceiveAndReply(int Port,AllServerImplementation exportedObj) {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(Port);
			byte[] buffer = new byte[1000];
			logger.info(ServerName+" Server is now ready with UDP port "+Port);
			System.out.println(ServerName+" Server is now ready with UDP port "+Port);
			while (true) {
				
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				
				//Convert DatagramPacket object request to String
				
				String msg_received=new String(buffer, 0, request.getLength());
				
				logger.info("Message received from port "+request.getPort()+" is = "+ msg_received);
				System.out.println("Message received from port "+request.getPort()+" is = "+ msg_received);
				
				String [] breakMsg = msg_received.split(",");
				//call method from message recieved
				
				String methodCheck=breakMsg[0];
				
				String replyStr="";
				if (methodCheck.equalsIgnoreCase("ServerCourseAvailability")) {
					 
					 String Argument=breakMsg[1];
					 replyStr = exportedObj.ServerCourseAvailability(Argument);
					 //replyStr = "COMP SERVER :: "+replyStr;
				}
				else if(methodCheck.equalsIgnoreCase("EnrollCrossDept")) {
					
					String Semester=breakMsg[1];
					String CourseID=breakMsg[2];
					String studentID=breakMsg[3];
					
					replyStr = exportedObj.EnrollCrossDept(Semester,CourseID,studentID);
				}
				else if(methodCheck.equalsIgnoreCase("RestoreCourseLimit")) {
					String Semester=breakMsg[1];
					String studentID=breakMsg[2];
					
					replyStr = exportedObj.RestoreCourseLimit(Semester,studentID);
				}
				else if(methodCheck.equalsIgnoreCase("ServerClassSchedule")) {
					String studentID=breakMsg[1];
					
					replyStr = exportedObj.ServerClassSchedule(studentID);
				}

				else if(methodCheck.equalsIgnoreCase("DropInterServerCourse")) {
					String studentID=breakMsg[1];
					String CourseID=breakMsg[2];
					
					replyStr = exportedObj.DropInterServerCourse(studentID,CourseID);
				}
				
				else if(methodCheck.equalsIgnoreCase("CheckInterServerCourse")) {
					String studentID=breakMsg[1];
					String CourseID=breakMsg[2];
					boolean newCourse=Boolean.valueOf(breakMsg[3]);
					String newCourseSem=breakMsg[4];
					
					replyStr = exportedObj.CheckInterServerCourse(studentID,CourseID,newCourse,newCourseSem);
				}

				
				logger.info("Message send to port "+request.getPort()+" is ="+ replyStr);
				System.out.println("Message send to port "+request.getPort()+" is ="+ replyStr);
				byte[] replyByte = replyStr.getBytes();
				
				//breakMsg[0](breakMsg[0]);
				
				DatagramPacket reply = new DatagramPacket(replyByte, replyStr.length(), request.getAddress(), request.getPort());
				aSocket.send(reply);
				
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
			logger.info("Socket error: " + e.getMessage());
		} catch (IOException e) {
			logger.info("IO error: " + e.getMessage());
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
} // end class
