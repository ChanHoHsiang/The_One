package virtualcontact;

import java.util.Random;

import input.virtualcontactreader;
import core.DTNSim;
import core.ModuleCommunicationBus;
import core.Settings;
import core.SimClock;
import core.SimError;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public abstract class virtualcontactmodel{
	
	public static final String VIRTUALCONTACT_MODEL_NS="virtualcontactmodel";
	
	public static final String VIRTUAL_FILE_S = "file";
	
	private static virtualcontactreader reader;
	private static String inputFileName;
	
	public static double initVirtualContactTime;
	
	
	public static void main(String[] args) throws IOException {            
        
		FileReader fr = new FileReader("data/NCCU/virtual.csv");
		        
		BufferedReader br = new BufferedReader(fr);
		
	
	
	}
	
	// static initialization of all movement models' random number generator
	/*static {
		DTNSim.registerForReset(virtualcontactmodel.class.getCanonicalName());				
		reset();
	}
	*/
	/*	public static void reset() {
		//	Settings s = new Settings(VIRTUALCONTACT_MODEL_NS);
		
		if (s.contains(RNG_SEED)) {
			int seed = s.getInt(RNG_SEED);
			rng = new Random(seed);
		}
		else {
			rng = new Random(0);
		}
	}
	*/
	public virtualcontactmodel(Settings settings){
		//super(settings);
		//Settings s = new Settings(VIRTUALCONTACT_MODEL_NS);
		
		//inputFileName = s.getSetting(VIRTUAL_FILE_S); 
		
		//reader = new virtualcontactreader(inputFileName);
		
		//initVirtualContactTime = reader.readN
		
		
		
	}
	
	
	
	
	
	
}