import java.util.HashMap;

public class ServerConnectionRepository{
	
	HashMap<String,Integer> CorbaPorts = new HashMap<String,Integer>();
	
	HashMap<String,Integer> UdpPorts = new HashMap<String,Integer>();
	
	HashMap<String,Integer> UdpPorts_new_Components = new HashMap<String,Integer>();
	
	ServerConnectionRepository(){
		UdpPorts.put("COMP", 9999);
		UdpPorts.put("SOEN", 8888);
		UdpPorts.put("INSE", 7777);

		UdpPorts_new_Components.put("FE", 6666);  //remove this from UDP ports listcourse will have an issue if I dont remove this
		UdpPorts_new_Components.put("SEQUENCER", 8889); //remove this from UDP ports listcourse will have an issue if I dont remove this
		
		CorbaPorts.put("COMP", 7890);
		CorbaPorts.put("SOEN", 3456);
		CorbaPorts.put("INSE", 1234);
		CorbaPorts.put("FE",4231);

	}

}
