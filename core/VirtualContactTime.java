package core;

// public class VirtualContactTime implements Cloneable, Comparable<VirtualContactTime>
public class VirtualContactTime implements Cloneable, Comparable<VirtualContactTime>{
	private double VCT; 
	private double [] VS;
	/*Constructor
	 * 
	 * */
	public VirtualContactTime (double VCT, double[] VS){
		setVCTVS(VCT,VS);
		
	}

	public void setVCTVS(double VCT, double[] VS){
		this.VCT=VCT;
		this.VS=VS;
		
	}
	
	public double getVCT(){
		return this.VCT;
	}
	
	public double[] getVS(){
		return this.VS;
	}

	@Override
	public int compareTo(VirtualContactTime o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}