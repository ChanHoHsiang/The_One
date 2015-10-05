/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

import java.util.Random;

import core.Settings;
import core.SettingsError;

/**
 * Message creation -external events generator. Creates uniformly distributed
 * message creation patterns whose message size and inter-message intervals can
 * be configured.
 */
public class MessageEventGenerator implements EventQueue {
	/** Message size range -setting id ({@value}). Can be either a single
	 * value or a range (min, max) of uniformly distributed random values.
	 * Defines the message size (bytes). */
	public static final String MESSAGE_SIZE_S = "size";
	/** Message creation interval range -setting id ({@value}). Can be either a 
	 * single value or a range (min, max) of uniformly distributed 
	 * random values. Defines the inter-message creation interval (seconds). */
	public static final String MESSAGE_INTERVAL_S = "interval";
	/** Sender/receiver address range -setting id ({@value}). 
	 * The lower bound is inclusive and upper bound exclusive. */
	public static final String HOST_RANGE_S = "hosts";
	/** (Optional) receiver address range -setting id ({@value}). 
	 * If a value for this setting is defined, the destination hosts are 
	 * selected from this range and the source hosts from the 
	 * {@link #HOST_RANGE_S} setting's range.   
	 * The lower bound is inclusive and upper bound exclusive. */
	public static final String TO_HOST_RANGE_S = "tohosts";

	/** Message ID prefix -setting id ({@value}). The value must be unique 
	 * for all message sources, so if you have more than one message generator,
	 * use different prefix for all of them. The random number generator's
	 * seed is derived from the prefix, so by changing the prefix, you'll get
	 * also a new message sequence. */
	public static final String MESSAGE_ID_PREFIX_S = "prefix";
	/** Message creation time range -setting id ({@value}). Defines the time
	 * range when messages are created. No messages are created before the first
	 * and after the second value. By default, messages are created for the 
	 * whole simulation time. */
	public static final String MESSAGE_TIME_S = "time";
	
	/** Time of the next event (simulated seconds) */
	protected double nextEventsTime = 0;
	/** Range of host addresses that can be senders or receivers */
	protected int[] hostRange = {0, 0};
	/** Range of host addresses that can be receivers */
	protected int[] toHostRange = null;
	/** Next identifier for a message */
	private int id = 0;
	/** Prefix for the messages */
	protected String idPrefix;
	/** Size range of the messages (min, max) */
	private int[] sizeRange;
	/** Interval between messages (min, max) */
	private int[] msgInterval;
	/** Time range for message creation (min, max) */
	protected double[] msgTime;

	/** Random number generator for this Class */
	protected Random rng;
	
	/**
	 * Constructor, initializes the interval between events, 
	 * and the size of messages generated, as well as number 
	 * of hosts in the network.
	 * @param s Settings for this generator.
	 */
	public MessageEventGenerator(Settings s){
		this.sizeRange = s.getCsvInts(MESSAGE_SIZE_S);
		this.msgInterval = s.getCsvInts(MESSAGE_INTERVAL_S);
		this.hostRange = s.getCsvInts(HOST_RANGE_S, 2);
		this.idPrefix = s.getSetting(MESSAGE_ID_PREFIX_S);
		
		if (s.contains(MESSAGE_TIME_S)) {
			this.msgTime = s.getCsvDoubles(MESSAGE_TIME_S, 2);
		}
		else {
			this.msgTime = null;
		}
		if (s.contains(TO_HOST_RANGE_S)) {
			this.toHostRange = s.getCsvInts(TO_HOST_RANGE_S, 2);
		}
		else {
			this.toHostRange = null;
		}
		
		/* if prefix is unique, so will be the rng's sequence */
		this.rng = new Random(idPrefix.hashCode());
		
		if (this.sizeRange.length == 1) {
			/* convert single value to range with 0 length */
			this.sizeRange = new int[] {this.sizeRange[0], this.sizeRange[0]};
		}
		else {
			s.assertValidRange(this.sizeRange, MESSAGE_SIZE_S);
		}
		if (this.msgInterval.length == 1) {
			this.msgInterval = new int[] {this.msgInterval[0], 
					this.msgInterval[0]};
		}
		else {
			s.assertValidRange(this.msgInterval, MESSAGE_INTERVAL_S);
		}
		s.assertValidRange(this.hostRange, HOST_RANGE_S);
		
		if (this.hostRange[1] - this.hostRange[0] < 2) {
			if (this.toHostRange == null) {
				throw new SettingsError("Host range must contain at least two " 
						+ "nodes unless toHostRange is defined");
			}
			else if (toHostRange[0] == this.hostRange[0] && 
					toHostRange[1] == this.hostRange[1]) {
				// XXX: teemuk: Since (X,X) == (X,X+1) in drawHostAddress()
				// there's still a boundary condition that can cause an
				// infinite loop.
				throw new SettingsError("If to and from host ranges contain" + 
						" only one host, they can't be the equal");
			}
		}
		
		/* calculate the first event's time */
		this.nextEventsTime = (this.msgTime != null ? this.msgTime[0] : 0) 
			+ msgInterval[0] + 
			(msgInterval[0] == msgInterval[1] ? 0 : 
			rng.nextInt(msgInterval[1] - msgInterval[0]));
	}
	
	
	/**
	 * Draws a random host address from the configured address range
	 * @param hostRange The range of hosts
	 * @return A random host address
	 * 
	 * chan**
	 */
	protected int drawHostAddress(int hostRange[]) {
		
		//System.out.print("MessageEvent____"+hostRange[1]+"\n"+"_____eeeee"+hostRange[0]+"\n");
		if (hostRange[1] == hostRange[0]) {
			return hostRange[0];
		}
		return hostRange[0] + rng.nextInt(hostRange[1] - hostRange[0]);
	}
	
	/**
	 * Generates a (random) message size
	 * @return message size
	 */
	protected int drawMessageSize() {
		int sizeDiff = sizeRange[0] == sizeRange[1] ? 0 : 
			rng.nextInt(sizeRange[1] - sizeRange[0]);
		return sizeRange[0] + sizeDiff;
	}
	
	/**
	 * Generates a (random) time difference between two events
	 * @return the time difference
	 */
	protected int drawNextEventTimeDiff() {
		int timeDiff = msgInterval[0] == msgInterval[1] ? 0 : 
			rng.nextInt(msgInterval[1] - msgInterval[0]);
		return msgInterval[0] + timeDiff;
	}
	
	/**
	 * Draws a destination host address that is different from the "from"
	 * address
	 * @param hostRange The range of hosts
	 * @param from the "from" address
	 * @return a destination address from the range, but different from "from"
	 */
	protected int drawToAddress(int hostRange[], int from) {
		int to;
		do {
			to = this.toHostRange != null ? drawHostAddress(this.toHostRange):
				drawHostAddress(this.hostRange); 
		} while (from==to);
		
		return to;
	}
	
	/** 
	 * Returns the next message creation event
	 * @see input.EventQueue#nextEvent()
	 */
	public ExternalEvent nextEvent() {
		int responseSize = 0; /* zero stands for one way messages */
		int msgSize;
		int interval;
		int from;
		int to;
		
		int [][] msgtimeinterval= {
				
				{60,3600},//p0==>2
				{60,5760},//p1
				{60,2400},//p2
				{60,4500},//p3
				{60,4500},//p4
				{60,4500},//p5
				{60,2400},//p6
				{60,2400},//p7
				{60,9000},//p8
				{60,9000},//p9
				{60,2400},//p10
				{60,2400},//p11
				{60,9000},//p12
				{60,4500},//p13
				{60,5760},//p14
				{60,5760},//p15
				{60,3600},//p16
				{60,4500},//p17
				{60,3600},//p18
				{60,5760},//p19
				{60,3600},//p20
				{60,4500},//p21
				{60,5760},//p22
				{60,9000},//p23
				{60,5760},//p24
				{60,9000},//p25
				{60,3600},//p26
				{60,4500},//p27
				{60,9000},//p28
				{60,4500},//p29
				{60,3600},//p30
				{60,9000},//p31
				{60,5760},//p32
				{60,5760},//p33
				{60,2400},//p34
				{60,2400},//p35
				{60,2400},//p36
				{60,9000},//p37
				{60,9000},//p38
				{60,9000},//p39
				{60,3600},//p40
				{60,2400},//p41
				{60,9000},//p42
				{60,9000},//p43
				{60,2880},//p44
				{60,5760},//p45
				{60,5760},//p46
				{60,2880},//p47
				{60,9000},//p48
				{60,2880},//p49
				{60,2400},//p50
				{60,2400},//p51
				{60,2400},//p52
				{60,2400},//p53
				{60,2400},//p54
				{60,5760},//p55
				{60,2400},//p56
				{60,3600},//p57
				{60,2880},//p58
				{60,5760},//p59
				{60,4500},//p60
				{60,2400},//p61
				{60,9000},//p62
				{60,5760},//p63
				{60,5760},//p64
				{60,2400},//p65
				{60,5760},//p66
				{60,5760},//p67
				{60,9000},//p68
				{60,18000},//p69
				{60,5760},//p70
				{60,5760},//p71
				{60,3600},//p72
				{60,3600},//p73
				{60,4500},//p74
				{60,9000},//p75
				{60,9000},//p76
				{60,9000},//p77
				{60,2880},//p78
				{60,18000},//p79
				{60,4500},//p80
				{60,18000},//p81
				{60,9000},//p82
				{60,9000},//p83
				{60,4500},//p84
				{60,9000},//p85
				{60,9000},//p86
				{60,2400},//p87
				{60,5760},//p88
				{60,2400},//p89
				{60,2400},//p90
				{60,18000},//p91
				{60,5760},//p92
				{60,4500},//p93
				{60,3600},//p94
				{60,2880},//p95
				{60,3600},//p96
				{60,5760},//p97
				{60,5760},//p98
				{60,9000},//p99
				{60,9000},//p100
				{60,2400},//p101
				{60,2400},//p102
				{60,9000},//p103
				{60,5760},//p104
				{60,5760},//p105
				{60,9000},//p106
				{60,2400},//p107
				{60,2400},//p108
				{60,4500},//p109
				{60,5760},//p110
				{60,3600},//p111
				{60,2880},//p112
				{60,9000},//p113
				{60,2400},//p114

	      }; 
		
		/*int [][] msgtimeinterval= {
				
				{60,120},//p0==>2
				{60,120},//p1
				{60,120},//p2
				{60,120},//p3
				{60,120},//p4
				{60,120},//p5
				{60,120},//p6
				{60,120},//p7
				{60,120},//p8
				{60,120},//p9
				{60,120},//p10
				{60,120},//p11
				{60,120},//p12
				{60,120},//p13
				{60,120},//p14
				{60,120},//p15
				{60,120},//p16
				{60,120},//p17
				{60,120},//p18
				{60,120},//p19
				{60,120},//p20
				{60,120},//p21
				{60,120},//p22
				{60,120},//p23
				{60,120},//p24
				{60,120},//p25
				{60,120},//p26
				{60,120},//p27
				{60,120},//p28
				{60,120},//p29
				{60,120},//p30
				{60,120},//p31
				{60,120},//p32
				{60,120},//p33
				{60,120},//p34
				{60,120},//p35
				{60,120},//p36
				{60,120},//p37
				{60,120},//p38
				{60,120},//p39
				{60,120},//p40
				{60,120},//p41
				{60,120},//p42
				{60,120},//p43
				{60,120},//p44
				{60,120},//p45
				{60,120},//p46
				{60,120},//p47
				{60,120},//p48
				{60,120},//p49
				{60,120},//p50
				{60,120},//p51
				{60,120},//p52
				{60,120},//p53
				{60,120},//p54
				{60,120},//p55
				{60,120},//p56
				{60,120},//p57
				{60,120},//p58
				{60,120},//p59
				{60,120},//p60
				{60,120},//p61
				{60,120},//p62
				{60,120},//p63
				{60,120},//p64
				{60,120},//p65
				{60,120},//p66
				{60,120},//p67
				{60,120},//p68
				{60,120},//p69
				{60,120},//p70
				{60,120},//p71
				{60,120},//p72
				{60,120},//p73
				{60,120},//p74
				{60,120},//p75
				{60,120},//p76
				{60,120},//p77
				{60,120},//p78
				{60,120},//p79
				{60,120},//p80
				{60,120},//p81
				{60,120},//p82
				{60,120},//p83
				{60,120},//p84
				{60,120},//p85
				{60,120},//p86
				{60,120},//p87
				{60,120},//p88
				{60,120},//p89
				{60,120},//p90
				{60,120},//p91
				{60,120},//p92
				{60,120},//p93
				{60,120},//p94
				{60,120},//p95
				{60,120},//p96
				{60,120},//p97
				{60,120},//p98
				{60,120},//p99
				{60,120},//p100
				{60,120},//p101
				{60,120},//p102
				{60,120},//p103
				{60,120},//p104
				{60,120},//p105
				{60,120},//p106
				{60,120},//p107
				{60,120},//p108
				{60,120},//p109
				{60,120},//p110
				{60,120},//p111
				{60,120},//p112
				{60,120},//p113
				{60,120},//p114

	      };*/
		
		
		
		/* Get two *different* nodes randomly from the host ranges */
		from = drawHostAddress(this.hostRange);	
		to = drawToAddress(hostRange, from);
		
		msgSize = drawMessageSize();
		//interval = drawNextEventTimeDiff();
	//	try{
		interval=msgtimeinterval[from][0] +rng.nextInt(msgtimeinterval[from][1] - msgtimeinterval[from][0]);
	//	}
	//	catch (IndexOutOfBoundsException ex)
	//	{
			
	//	}
		
		/*
		 	int timeDiff = msgInterval[0] == msgInterval[1] ? 0 : 
			rng.nextInt(msgInterval[1] - msgInterval[0]);
			return msgInterval[0] + timeDiff;*/
		
		/* Create event and advance to next event */
		MessageCreateEvent mce = new MessageCreateEvent(from, to, this.getID(), 
				msgSize, responseSize, this.nextEventsTime);
		this.nextEventsTime += interval;	
		
		if (this.msgTime != null && this.nextEventsTime > this.msgTime[1]) {
			/* next event would be later than the end time */
			this.nextEventsTime = Double.MAX_VALUE;
		}
		
		return mce;
	}

	/**
	 * Returns next message creation event's time
	 * @see input.EventQueue#nextEventsTime()
	 */
	public double nextEventsTime() {
		return this.nextEventsTime;
	}
	
	/**
	 * Returns a next free message ID
	 * @return next globally unique message ID
	 */
	protected String getID(){
		this.id++;
		return idPrefix + this.id;
	}	
}
