package virtualcontact;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import core.SettingsError;
import core.Tuple;
import core.DTNSim;
import core.Settings;
import core.SimClock;


public class externalvirtualcontact{
	
	/* Namespace for settings */
	public static final String EXTERNALVIRTUALCONTACT_NS = "virtualcontact";
	
	/*
	static {
		DTNSim.registerForReset(externalvirtualcontact.class.getCanonicalName());				
		reset();
	}
	 */
	public static void reset() {
		Settings s = new Settings(EXTERNALVIRTUALCONTACT_NS);
		/*if (s.contains(RNG_SEED)) {
			int seed = s.getInt(RNG_SEED);
			rng = new Random(seed);
		}
		else {
			rng = new Random(0);
		}
		*/
	}
}