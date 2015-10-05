package input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.Coord;
import core.SettingsError;
import core.Tuple;


public class virtualcontactreader{
	
	//public static final String COMMENT_PREFIX = "#";
	private Scanner scanner;
	private double lastTimeStamp = -1;
	public double VirtualContactTime;
	//public double [] VirtualSocial;
	
	public double VirtualSocial;
	
	private boolean normalize;
	private String lastLine;
	
	public virtualcontactreader() {
		this.normalize=true;
		
		String inFilePath="data/NCCU/virtual.csv";
		
		File inFile = new File(inFilePath);
		try{
			scanner = new Scanner(inFile);
			
		}catch (FileNotFoundException e){
			throw new SettingsError("Couldn't find VirtualContact input" + "file"+ inFile);
		}
		
		String offsets = scanner.nextLine();
		
		try{
			Scanner lineScan = new Scanner(offsets);
			VirtualContactTime = lineScan.nextDouble();
			VirtualSocial = lineScan.nextDouble();
			
		} catch (Exception e) {
			throw new SettingsError("Invalid offset line '" + offsets + "'");
		}
		
		lastLine = scanner.nextLine();
		
	}
	
	//public List<Tuple<String, core.VirtualContactTime>> readNextVirtualContactTime()
	public List<Tuple<String, core.VirtualContactTime>> readNextVirtualContactTime(){
		ArrayList<Tuple<String, core.VirtualContactTime>> VirtualContactToServer =
				new ArrayList<Tuple<String, core.VirtualContactTime>>();
		
		
		//Scanner lineScan = new Scanner();
		
		Scanner lineScan = new Scanner(lastLine);
		double time = lineScan.nextDouble();
		String id = lineScan.next();
		double VCT = lineScan.nextDouble();
		double VS = lineScan.nextDouble();
		
		return VirtualContactToServer;
	}
	
	
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}
	
	
	/*Reads all new id-VirtualContactTime*/
	
	public double getVirtualTime(){
		
		return VirtualContactTime;
	}
	
	public double getVirtualSocial(){
		
		return VirtualSocial;
	}
	
	
	
}