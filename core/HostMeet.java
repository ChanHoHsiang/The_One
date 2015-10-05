package core;

public class HostMeet {
	private String hostid;
	private Coord location;
	private double meetTime;
	private double [] pinterest ;//憛�閎��
	
	public HostMeet(String hostid, Coord location, double time){
		this.hostid = hostid;
		this.location = location;
		this.meetTime = time;
		//this.pinterest=pinterestvalue;
	}
	
	public void setHostID(String hostid){
		this.hostid = hostid;
	}
	
	public String getHostID(){
		return this.hostid;
	}
	
	public void setLocation(Coord location){
		this.location = location;
	}
	
	public Coord getLocation(){
		return this.location;
	}
	
	public void setMeetTime(double time){
		this.meetTime = time;
	}
	
	public double getMeetTime(){
		return this.meetTime;
	}
	
	public double [] getpinterest(){
		return this.pinterest;
	}
	
}
