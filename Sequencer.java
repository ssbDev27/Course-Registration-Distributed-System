//package drcs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.ArrayList;

public class Sequencer {

	   public static void main(String args[]) throws SecurityException, IOException {
   	   	   
		   Runnable task = () -> {
				
			   recieve6();
		   };
//		   
	//
		   Thread thread = new Thread(task);
//		   Thread thread2 = new Thread(task2);
//		   Thread thread3 = new Thread(task3);
//		   Thread thread4 = new Thread(task4);
	//	
		   thread.start();
//		   thread2.start();
//		   thread3.start();
//		   thread4.start();
	//

		   
	   }
	
	static Integer iSeqNo = 000;
	
	public static final int SEQUENCER_LISTENING_PORT = 8889;
	public static final String rm1Name = "Replica Manager 1";
    public static final String rm2Name = "Replica Manager 2";
    public static final String rm3Name = "Replica Manager 3";
  
    public static final String rm1Address = "localhost";
    public static final String rm2Address = "232.12.14.8";
    public static final String rm3Address = "233.2.3.4";
    
    public static final int rm1Port = 8891;
    public static final int rm2Port = 8891;
    public static final int rm3Port = 4446;

	
    static String data = null;

	//ArrayList<Integer> portList = new ArrayList<Integer>();
	
	public static void recieve6() {

		System.out.println("Inside Sequencer Server");
		DatagramSocket aSocket = null;
		
		

		try {
			aSocket = new DatagramSocket(SEQUENCER_LISTENING_PORT);
			byte[] buffer = new byte[1000];
			System.out.println("Server SEQUENCER_LISTENING_PORT Started............");
			//logInfo.info("Server 40000 Started............");
			while (true) {
				data=null;
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);

				String msg_received = new String(buffer, 0, request.getLength()); // byte to string
				
				iSeqNo = iSeqNo + 1;
				data = iSeqNo + "," + msg_received;

				sendRequest();

				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			aSocket.close();
		}

	}
	
	
	 

	    /**
	     * Send request to each RM
	     */
	    public static void sendRequest(){
	        sendToRm(rm1Address, rm1Port, rm1Name);
	        sendToRm(rm2Address, rm2Port, rm2Name);
	        sendToRm(rm3Address, rm3Port, rm3Name);
	    }

	    /**
	     * Send to RMs, currently, no response is needed from the RMs. To be determined whether response is needed
	     * @param rmAddress rm address
	     * @param rmPort rm port
	     * @param serverName rm name
	     */
	   
	    
	    private static void sendToRm(String rmAddress, int rmPort, String serverName){
	        byte[] messageInByte = data.toString().getBytes();
	        int length = messageInByte.length;
	        DatagramSocket socket;
	        try{
	            System.out.println("Sequencer is sending message to "+ serverName + " at " + rmAddress +":"+rmPort);
	            socket = new DatagramSocket();
	            InetAddress address = InetAddress.getByName(rmAddress);
	            DatagramPacket request = new DatagramPacket(messageInByte, length, address, rmPort);
	            socket.send(request);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }


}