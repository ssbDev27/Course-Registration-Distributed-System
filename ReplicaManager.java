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


public class ReplicaManager  {
   
   final static String CompServer = "COMP";
   final static String SoenServer = "SOEN";
   final static String InseServer = "INSE";
   static ServerConnectionRepository sc;
   static AllServerImplementation Comp_Server_obj;
   static AllServerImplementation Soen_Server_obj;
   static AllServerImplementation Inse_Server_obj;
   static int CorbaPort=0;
   static int UDPPort=0;
   static String orbsetlocation[]= {};
   static{
	  
//	   CorbaPort=sc.CorbaPorts.get(ServerName);
//	   UDPPort=sc.UdpPorts.get(ServerName);

   }
      
   static Logger logger_Comp;
   static Logger logger_Soen;
   static Logger logger_Inse;
   
   public static void main(String args[]) throws SecurityException, IOException {
	   
	   sc = new ServerConnectionRepository();
	   	   	   
	   logger_Comp = Logger.getLogger(CompServer+ " server" + " - log");
	   LogFormatter logFormatter_comp = new LogFormatter();
	   LogFormatter.loggingfor = CompServer ;
	   FileHandler logFileHandler_comp;    	    
	   logger_Comp.setUseParentHandlers(false);	
	   new File(System.getProperty("user.home") + "/Desktop/Server" + CompServer + "DCRSlogs").mkdir();	
	   logFileHandler_comp = new FileHandler(System.getProperty("user.home") + "/Desktop/Server" + CompServer + "DCRSlogs/" + CompServer + "server" + "-log.log",true );  			  
	   logger_Comp.addHandler(logFileHandler_comp);	      
	   logFileHandler_comp.setFormatter(logFormatter_comp);
				   	   
	   logger_Comp.info(CompServer+" Server in now receiving UDP requests at port"+sc.UdpPorts.get(CompServer));
	   
	   Comp_Server_obj = new AllServerImplementation(CompServer,logger_Comp);
	   
	   logger_Soen = Logger.getLogger(SoenServer+ " server" + " - log");
	   LogFormatter logFormatter_soen = new LogFormatter();
	   LogFormatter.loggingfor = SoenServer ;
	   FileHandler logFileHandler_soen;    	    
	   logger_Soen.setUseParentHandlers(false);	
	   new File(System.getProperty("user.home") + "/Desktop/Server" + SoenServer + "DCRSlogs").mkdir();	
	   logFileHandler_soen = new FileHandler(System.getProperty("user.home") + "/Desktop/Server" + SoenServer + "DCRSlogs/" + SoenServer + "server" + "-log.log",true );  			  
	   logger_Soen.addHandler(logFileHandler_soen);	      
	   logFileHandler_soen.setFormatter(logFormatter_soen);
				   	   
	   logger_Soen.info(SoenServer+" Server in now receiving UDP requests at port"+sc.UdpPorts.get(SoenServer));
	   
	   Soen_Server_obj = new AllServerImplementation(SoenServer,logger_Soen);

	   logger_Inse = Logger.getLogger(InseServer+ " server" + " - log");
	   LogFormatter logFormatter_Inse = new LogFormatter();
	   LogFormatter.loggingfor = InseServer ;
	   FileHandler logFileHandler_Inse;    	    
	   logger_Inse.setUseParentHandlers(false);	
	   new File(System.getProperty("user.home") + "/Desktop/Server" + InseServer + "DCRSlogs").mkdir();	
	   logFileHandler_Inse = new FileHandler(System.getProperty("user.home") + "/Desktop/Server" + InseServer + "DCRSlogs/" + InseServer + "server" + "-log.log",true );  			  
	   logger_Inse.addHandler(logFileHandler_Inse);	      
	   logFileHandler_Inse.setFormatter(logFormatter_Inse);
				   	   
	   logger_Inse.info(SoenServer+" Server in now receiving UDP requests at port"+sc.UdpPorts.get(InseServer));
	   
	   Inse_Server_obj = new AllServerImplementation(InseServer,logger_Inse);
	   
	   Runnable task = () -> {
		
		   startCorbaserver(orbsetlocation, exportedObj);
	   };
	   
	   Runnable task2 = () -> {
		
		   UDPreceiveAndReply_interserver(CompServer,sc.UdpPorts.get(CompServer), Comp_Server_obj,logger_Comp);
	   };
	   Runnable task3 = () -> {
			
		   UDPreceiveAndReply_interserver(SoenServer,sc.UdpPorts.get(SoenServer), Soen_Server_obj,logger_Soen);
	   };
	   Runnable task4 = () -> {
			
		   UDPreceiveAndReply_interserver(InseServer,sc.UdpPorts.get(InseServer), Inse_Server_obj,logger_Inse);
	   };

	   Thread thread = new Thread(task);
	   Thread thread2 = new Thread(task2);
	   Thread thread3 = new Thread(task3);
	   Thread thread4 = new Thread(task4);
	
	   thread.start();
	   thread2.start();
	   thread3.start();
	   thread4.start();

  } // end main
  
	private synchronized static void UDPreceiveAndReply_RM(String ServerName,int Port, Logger logger) {
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
				
				AllServerImplementation Server_selected;
				
				if (Department.equals(CompServer)) {
					Server_selected=Comp_Server_obj;
				}
				else if (Department.equals(SoenServer)) {
					Server_selected=Soen_Server_obj;
				}
				else {
					Server_selected=Inse_Server_obj;
				}
				
				String replyStr="";
				if (methodCheck.equalsIgnoreCase("addCourse")) {
					 
					 String CourseId=breakMsg[3].trim();
					 String Semester=breakMsg[4].trim();
					 int Capacity=breakMsg[5].trim();
					 replyStr = Server_selected.addCourse(CourseId,Semester,Capacity);
					 //replyStr = "COMP SERVER :: "+replyStr;
				}
				else if(methodCheck.equalsIgnoreCase("EnrollCrossDept")) {
					
					String Semester=breakMsg[1];
					String CourseID=breakMsg[2];
					String studentID=breakMsg[3];
					
					replyStr = Server_selected.EnrollCrossDept(Semester,CourseID,studentID);
				}
				else if(methodCheck.equalsIgnoreCase("RestoreCourseLimit")) {
					String Semester=breakMsg[1];
					String studentID=breakMsg[2];
					
					replyStr = Server_selected.RestoreCourseLimit(Semester,studentID);
				}
				else if(methodCheck.equalsIgnoreCase("ServerClassSchedule")) {
					String studentID=breakMsg[1];
					
					replyStr = Server_selected.ServerClassSchedule(studentID);
				}

				else if(methodCheck.equalsIgnoreCase("DropInterServerCourse")) {
					String studentID=breakMsg[1];
					String CourseID=breakMsg[2];
					
					replyStr = Server_selected.DropInterServerCourse(studentID,CourseID);
				}
				
				else if(methodCheck.equalsIgnoreCase("CheckInterServerCourse")) {
					String studentID=breakMsg[1];
					String CourseID=breakMsg[2];
					boolean newCourse=Boolean.valueOf(breakMsg[3]);
					String newCourseSem=breakMsg[4];
					
					replyStr = Server_selected.CheckInterServerCourse(studentID,CourseID,newCourse,newCourseSem);
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

//    private static void startCorbaserver(String orbsetlocation[],AllServerImplementation exportedObj) {
//	   String registryURL;
//		try {
//			//System.out.println(args[0]+args[1]);
//			// create and initialize the ORB //// get reference to rootpoa &amp; activate
//			// the POAManager
//
//			ORB orb = ORB.init(orbsetlocation, null);
//			//-ORBInitialPort 1050 -ORBInitialHost localhost
//			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//			rootpoa.the_POAManager().activate();
//
//			// create servant and register it with the ORB
//			//AdditionObj addobj = new AdditionObj();
//			exportedObj.setORB(orb);
//
//			// get object reference from the servant
//			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(exportedObj);
//			StudentAdvisorInterface href = StudentAdvisorInterfaceHelper.narrow(ref);
//
//			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//
//			NameComponent path[] = ncRef.to_name(ServerName);
//			ncRef.rebind(path, href);
//
//			logger.info(ServerName+" Server ready and waiting ...");
//			
//			System.out.println(ServerName+" Server ready and waiting ...");
//
//			// wait for invocations from clients
//			for (;;) {
//				orb.run();
//			}
//		}
//
//		catch (Exception e) {
//			System.err.println("ERROR: " + e);
//			e.printStackTrace(System.out);
//		}


//	}
   
   
    
	private synchronized static void UDPreceiveAndReply_interserver(String ServerName,int Port,AllServerImplementation exportedObj, Logger logger) {
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
