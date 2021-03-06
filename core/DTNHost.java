/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import movement.MovementModel;
import movement.Path;
import routing.MessageRouter;
import routing.RoutingInfo;

/**
 * A DTN capable host.
 */
public class DTNHost implements Comparable<DTNHost> {
	private static int nextAddress = 0;
	private int address;

	private Coord location; 	// where is the host
	private Coord destination;	// where is it going

	private MessageRouter router;
	private MovementModel movement;
	private Path path;
	private double speed;
	private double nextTimeToMove;
	private String name;
	private List<MessageListener> msgListeners;
	private List<MovementListener> movListeners;
	private List<NetworkInterface> net;
	private ModuleCommunicationBus comBus;
	private int id;
	/*** add***/ 
	public List<HostMeet> meetList;
	public double[] pinterest;
	
    
	public double [][] totalpinterest={
			/*{0.75, 0.75, 1, 1, 0.75},//P0
            {1, 0.5, 0.5, 0, 0.5},
            {0.25, 1, 1, 0.75, 0.75},
            {0.5, 1, 0.75, 0.75, 0.75},
            {0.5, 1, 0, 1, 1},
            {0.5, 1, 0.25, 0, 0}
            */
            
            
            {0.75, 0.75, 1, 1,0.75}, //p0 ==>2
            {1, 0.5, 0.5, 0, 0.5},//p1
            {0.25, 1, 1, 0.75, 0.75},//p2
            {0.5, 1, 0.75, 0.75, 0.75},//p3
            {0.75, 0.5, 1, 0.5, 1}, //P4
            {0.5, 0.75, 0.5, 0.5, 0.5},//p5
            {0.75, 0.75, 0.5, 0.75, 0.5},//p6
            {0.75, 0.75, 0.75, 0.75, 0.75},//p7
            {0.5, 0.75, 0.25, 0.5, 0.25},//P8
            {0.75, 0.25, 0.25, 0.75, 0.75},//p9
            {0.75, 0.75, 0.5, 0.75, 0.5},//p10
            {0.75, 1, 0.75, 1, 0.75},//p11
            {1, 1, 0.25, 0.25, 0.5},//p12
            {0.75, 0.75, 1, 0.75, 0.75},//p13
            {0.75, 0.75, 0.75, 0.75, 0.25},//P14
            {1, 0.75, 1, 0.5, 1},//p15
            {0.5, 1, 1, 1, 0.75},//p16
            {0.5, 1, 0.5, 0.75, 0.75},//p17
            {0.5, 0.5, 0.75, 0.5, 0.5},//p18
            {0.25, 0.5, 1, 0.75, 0.5},//P19
            {0.5, 1, 1, 0.75, 0.75},//p20
            {0.75, 1, 1, 0.75, 0.5},//p21
            {0.5, 0.75, 1, 0.75, 0.75},//p22
            {0.75, 0.75, 0.75, 1, 0.75},//p23
            {0.75, 0.75, 1, 0.75, 0.5},//p24
            {1, 0.5, 1, 0.5, 0.75},//p25
            {1, 0.75, 0.75, 0.5, 0.75},//p26
            {0.25, 0.75, 0.75, 1, 0.5},//p27
            {1, 1, 1, 1, 1},//p28
            {0.75, 1, 0.75, 1, 1},//p29
            {0.75, 0.75, 1, 0.75, 0.75},//p30
            {0.5, 0.5, 0.75, 0.75, 0.5},//p31
            {0.5, 0.5, 0.75, 0.5, 0.5},//p32
            {0.75, 0.75, 0.75, 0.5, 0.75},//p33
            {1, 1, 1, 0.5, 0.5},//p34
            {1, 1, 0.75, 0.5, 0.5},//p35
            {1, 0.75, 1, 0.75, 0.5},//p36
            {0.75, 0.75, 0.5, 0.25, 0.5},//p37
            {0.5, 0.5, 0.75, 0.25, 0.25},//p38
            {1, 0.25, 0.75, 0.25, 0.5},//p39
            {1, 0.75, 0.75, 1, 0.75},//p40
            {0.25, 0.75, 0.5, 0.75, 0.5},//p41
            {0.75, 0.5, 0.75, 0.75, 0.5},//p42
            {0.75, 0.5, 1, 0.5, 1},//p43
            {1, 0.5, 0.75, 0.75, 0.5},//p44
            {0.75, 0.75, 0.75, 0.5, 0.75},//p45
            {0.5, 0.5, 0.75, 0.25, 0.25},//p46
            {1, 1, 0.75, 0.5, 0.5},//p47
            {0.75, 0.75, 0.5, 0.5, 0.75},//p48
            {1, 0.75, 0.5, 0.75, 0.5},//p49
            {0.75, 0.5, 1, 0.5, 0.25},//p50
            {0.5, 1, 0.75, 0.75, 0.75},//p51
            {1, 0.75, 0.5, 0.5, 0.5},//p52
            {1, 0.75, 0.5, 0.25, 0},//p53
            {1, 0.75, 0.75, 0.75, 0.75},//p54
            {0.75, 0.25, 0.75, 0.5, 0.75},//p55
            {0.75, 0.75, 0.75, 0.75, 0.75},//p56
            {1, 1, 0.75, 0.75, 0.25},//p57
            {0.5, 0.75, 1, 0.5, 0.75},//p58
            {1, 0.75, 0.75, 0.75, 1},//p59
            {0.75, 0.75, 0.75, 0.5, 0.5},//p60
            {0.25, 0.5, 0.5, 0.5, 0.25},//p61
            {0.75, 0.75, 0.5, 0.75, 0.5},//p62
            {0.75, 1, 0.25, 0, 0.75},//p63
            {0.75, 0.75, 0.75, 0.75, 0.75},//p64
            {0.75, 0.75, 0.25, 0.75, 0.5},//p65
            {0.75, 0.75, 0.75, 0.75, 0.75},//p66
            {0.5, 0.75, 0.75, 0.75, 0.5},//p67
            {0.75, 0.75, 0.75, 0.5,	0.75},//p68
            {0.5, 0.75, 0.75, 1, 0.75},//p69
            {0.25, 0.5, 0.75, 0.75, 0.25},//p70
            {0.75, 0.5, 1, 0.75, 0.5},//p71
            {0.75, 0.5, 0.75, 0.5, 0.5},//p72
            {0.75, 1, 0.75, 0.5, 0.25},//p73
            {0.75, 0.75, 0.75, 0.75, 0.75},//p74
            {0.75, 0.75, 0.5, 0.5, 0.5},//p75
            {0.5, 1, 0.5, 0.75, 0.75},//p76
            {1, 1, 1, 1, 0.75},//p77
            {0.5, 1, 0.75, 0.75, 0.75},//p78
            {0.25, 1, 0.5, 1, 0.75},//p79
            {0.5, 0.75, 0.75, 0.5, 0.5},//p80
            {0.75, 0.5, 0.75, 0.75, 0.75},//p81
            {0.75, 0.75, 0.5, 0.25, 0.5},//p82
            {0.75, 1, 0.5, 0.5, 0.5},//p83
            {0.75, 0.75, 0.5, 1, 0.75},//p84
            {0.75, 0.75, 1, 0.75, 0.5},//p85
            {0.5, 0.75, 0.75, 0.25, 0.75},//p86
            {0.5, 0.75, 0.75, 0.5, 0.75},//p87
            {0.75, 0.75, 1, 1, 0.75},//p88
            {0.75, 0.75, 0.75, 0.25, 0.75},//p89
            {0.75, 0.5, 0.75, 0.75, 0.5},//p90
            {0.75, 0.25, 0.5, 0.75, 0.25},//p91
            {0.25, 0.75, 0.5, 0.5, 0.5},//p92
            {0.75, 0.75, 0.75, 1, 0.5},//p93
            {1, 0.75, 1, 0.5, 0.5},//p94
            {0.75, 0.75, 0.5, 0.5, 0.5},//p95
            {1, 0.75, 1, 0.75, 0.5},//p96
            {0.75, 0.75, 0.5, 0.75, 0.5},//p97
            {0.75, 0.75, 1, 1, 1},//p98
            {0.5, 0.75, 0.75, 0.75, 0.75},//p99
            {1, 0.75, 0.75, 0.75, 0.75},//p100
            {0.75, 0.75, 1, 0.5, 0.75},//p101
            {0.25, 0.75, 0.75, 1, 0.5},//p102
            {0.75, 0.5, 0.75, 0.5, 0.5},//p103
            {0.75, 0.75, 0.75, 0.5, 0.75},//p104
            {1, 0.75, 1, 0.75, 0.75},//p105
            {0.75, 0.75, 0.75, 0.75, 0.75},//p106
            {0.75, 1, 1, 1, 0.75},//p107
            {0.5, 0.5, 0.75, 0.75, 0.5},//p108
            {0.75, 0.75, 0.75, 1, 0.5},//p109
            {0.5, 0.5, 1, 0.5, 0.25},//p110
            {1, 1, 0.75, 1, 0.5},//p111
            {0.5, 0.75, 1, 1, 0.5},//p112
            {1, 1, 0.75, 1, 0.75},//p113
            {0.5, 0.25, 0.5, 0.25, 0.5},//p114

                                    			
	};
		
	
	static {
		DTNSim.registerForReset(DTNHost.class.getCanonicalName());
		reset();
	}
	/**
	 * Creates a new DTNHost.
	 * @param msgLs Message listeners
	 * @param movLs Movement listeners
	 * @param groupId GroupID of this host
	 * @param interf List of NetworkInterfaces for the class
	 * @param comBus Module communication bus object
	 * @param mmProto Prototype of the movement model of this host
	 * @param mRouterProto Prototype of the message router of this host
	 */
	/****add
	 public DTNHost(List<MessageListener> msgLs,
			List<MovementListener> movLs,
			String groupId, List<NetworkInterface> interf,
			ModuleCommunicationBus comBus, 
			MovementModel mmProto, MessageRouter mRouterProto, double[] pinterest) {
	 
	 *******/
	
	
	
	public DTNHost(List<MessageListener> msgLs,
			List<MovementListener> movLs,
			String groupId, List<NetworkInterface> interf,
			ModuleCommunicationBus comBus, 
			MovementModel mmProto, MessageRouter mRouterProto, double[] pinterest) {
		this.comBus = comBus;
		this.location = new Coord(0,0);
		this.address = getNextAddress();
		this.name = groupId+address;
		this.net = new ArrayList<NetworkInterface>();
		
		//this.pinterest= pinte;
		
		//SimScenario SS = new SimScenario();
		//this.pinterest= SS.ranpinterest(1);
		
		//System.out.print("ID : "+id+" /n"+this.pinterest[0]);
		
		 
		this.meetList= new ArrayList<HostMeet>();
		/** add
		this.pinterest=pinterest;
		**/
		for (NetworkInterface i : interf) {
			NetworkInterface ni = i.replicate();
			ni.setHost(this);
			net.add(ni);
		}	

		// TODO - think about the names of the interfaces and the nodes
		//this.name = groupId + ((NetworkInterface)net.get(1)).getAddress();

		this.msgListeners = msgLs;
		this.movListeners = movLs;

		// create instances by replicating the prototypes
		this.movement = mmProto.replicate();
		this.movement.setComBus(comBus);
		setRouter(mRouterProto.replicate());

		this.location = movement.getInitialLocation();

		this.nextTimeToMove = movement.nextPathAvailable();
		this.path = null;

		if (movLs != null) { // inform movement listeners about the location
			for (MovementListener l : movLs) {
				l.initialLocation(this, this.location);
			}
		}
	}
	
	/***** add *****/
	
	public void addMeetList(HostMeet hm){
		if(this.meetList.size() >= 5)this.meetList.remove(0);
		this.meetList.add(hm);
		
		
		System.out.println("相遇清單"+hm);
	}
	
	public List<HostMeet> getMeetList(){
		System.out.print("DTNHOST___"+meetList);
		return this.meetList;
	}
	
	
	
	/**
	 * Returns a new network interface address and increments the address for
	 * subsequent calls.
	 * @return The next address.
	 */
	private synchronized static int getNextAddress() {
		return nextAddress++;	
	}

	/**
	 * Reset the host and its interfaces
	 */
	public static void reset() {
		nextAddress = 0;
	}

	/**
	 * Returns true if this node is active (false if not)
	 * @return true if this node is active (false if not)
	 */
	public boolean isActive() {
		return this.movement.isActive();
	}

	/**
	 * Set a router for this host
	 * @param router The router to set
	 */
	private void setRouter(MessageRouter router) {
		router.init(this, msgListeners);
		this.router = router;
	}

	/**
	 * Returns the router of this host
	 * @return the router of this host
	 */
	public MessageRouter getRouter() {
		return this.router;
	}

	/**
	 * Returns the network-layer address of this host.
	 */
	public int getAddress() {
		return this.address;
	}
	
	/**
	 * Returns this hosts's ModuleCommunicationBus
	 * @return this hosts's ModuleCommunicationBus
	 */
	public ModuleCommunicationBus getComBus() {
		return this.comBus;
	}
	
    /**
	 * Informs the router of this host about state change in a connection
	 * object.
	 * @param con  The connection object whose state changed
	 */
	public void connectionUp(Connection con) {
		this.router.changedConnection(con);
	}

	public void connectionDown(Connection con) {
		this.router.changedConnection(con);
	}

	/**
	 * Returns a copy of the list of connections this host has with other hosts
	 * @return a copy of the list of connections this host has with other hosts
	 */
	public List<Connection> getConnections() {
		List<Connection> lc = new ArrayList<Connection>();

		for (NetworkInterface i : net) {
			lc.addAll(i.getConnections());
		}

		return lc;
	}

	/**
	 * Returns the current location of this host. 
	 * @return The location
	 */
	public Coord getLocation() {
		return this.location;
	}

	/**
	 * Returns the Path this node is currently traveling or null if no
	 * path is in use at the moment.
	 * @return The path this node is traveling
	 */
	public Path getPath() {
		return this.path;
	}


	/**
	 * Sets the Node's location overriding any location set by movement model
	 * @param location The location to set
	 */
	public void setLocation(Coord location) {
		this.location = location.clone();
	}

	/**
	 * Sets the Node's name overriding the default name (groupId + netAddress)
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the messages in a collection.
	 * @return Messages in a collection
	 */
	public Collection<Message> getMessageCollection() {
		return this.router.getMessageCollection();
	}

	/**
	 * Returns the number of messages this node is carrying.
	 * @return How many messages the node is carrying currently.
	 */
	public int getNrofMessages() {
		return this.router.getNrofMessages();
	}
	
	/**add*/
	
	public double [] getminteresr(String id) {
		return this.router.getminteresr(id);
	}

	
	/***** add *******/ 
	public double[] get_P_interest(){
		//System.out.print("interest"+pinterest[0]);
		return this.pinterest;
		
	}
	
	
	/**
	 * Returns the buffer occupancy percentage. Occupancy is 0 for empty
	 * buffer but can be over 100 if a created message is bigger than buffer 
	 * space that could be freed.
	 * @return Buffer occupancy percentage
	 */
	public double getBufferOccupancy() {
		double bSize = router.getBufferSize();
		double freeBuffer = router.getFreeBufferSize();
		return 100*((bSize-freeBuffer)/bSize);
	}

	/**
	 * Returns routing info of this host's router.
	 * @return The routing info.
	 */
	public RoutingInfo getRoutingInfo() {
		return this.router.getRoutingInfo();
	}

	/**
	 * Returns the interface objects of the node
	 */
	public List<NetworkInterface> getInterfaces() {
		return net;
	}

	/**
	 * Find the network interface based on the index
	 */
	protected NetworkInterface getInterface(int interfaceNo) {
		NetworkInterface ni = null;
		try {
			ni = net.get(interfaceNo-1);
		} catch (IndexOutOfBoundsException ex) {
			System.out.println("No such interface: "+interfaceNo);
			System.exit(0);
		}
		return ni;
	}

	/**
	 * Find the network interface based on the interfacetype
	 */
	protected NetworkInterface getInterface(String interfacetype) {
		for (NetworkInterface ni : net) {
			if (ni.getInterfaceType().equals(interfacetype)) {
				return ni;
			}
		}
		return null;	
	}

	/**
	 * Force a connection event
	 */
	public void forceConnection(DTNHost anotherHost, String interfaceId, 
			boolean up) {
		NetworkInterface ni;
		NetworkInterface no;

		if (interfaceId != null) {
			ni = getInterface(interfaceId);
			no = anotherHost.getInterface(interfaceId);

			assert (ni != null) : "Tried to use a nonexisting interfacetype "+interfaceId;
			assert (no != null) : "Tried to use a nonexisting interfacetype "+interfaceId;
		} else {
			ni = getInterface(1);
			no = anotherHost.getInterface(1);
			
			assert (ni.getInterfaceType().equals(no.getInterfaceType())) : 
				"Interface types do not match.  Please specify interface type explicitly";
		}
		
		if (up) {
			ni.createConnection(no);
		} else {
			ni.destroyConnection(no);
		}
	}

	/**
	 * for tests only --- do not use!!!
	 */
	public void connect(DTNHost h) {
		System.err.println(
				"WARNING: using deprecated DTNHost.connect(DTNHost)" +
		"\n Use DTNHost.forceConnection(DTNHost,null,true) instead");
		forceConnection(h,null,true);
	}

	/**
	 * Updates node's network layer and router.
	 * @param simulateConnections Should network layer be updated too
	 */
	public void update(boolean simulateConnections) {
		if (!isActive()) {
			return;
		}
		
		if (simulateConnections) {
			for (NetworkInterface i : net) {
				i.update();
			}
		}
		this.router.update();
	}

	/**
	 * Moves the node towards the next waypoint or waits if it is
	 * not time to move yet
	 * @param timeIncrement How long time the node moves
	 */
	public void move(double timeIncrement) {		
		double possibleMovement;
		double distance;
		double dx, dy;

		if (!isActive() || SimClock.getTime() < this.nextTimeToMove) {
			return; 
		}
		if (this.destination == null) {
			if (!setNextWaypoint()) {
				return;
			}
		}

		possibleMovement = timeIncrement * speed;
		distance = this.location.distance(this.destination);

		while (possibleMovement >= distance) {
			// node can move past its next destination
			this.location.setLocation(this.destination); // snap to destination
			possibleMovement -= distance;
			if (!setNextWaypoint()) { // get a new waypoint
				return; // no more waypoints left
			}
			distance = this.location.distance(this.destination);
		}

		// move towards the point for possibleMovement amount
		dx = (possibleMovement/distance) * (this.destination.getX() -
				this.location.getX());
		dy = (possibleMovement/distance) * (this.destination.getY() -
				this.location.getY());
		this.location.translate(dx, dy);
	}	

	/**
	 * Sets the next destination and speed to correspond the next waypoint
	 * on the path.
	 * @return True if there was a next waypoint to set, false if node still
	 * should wait
	 */
	private boolean setNextWaypoint() {
		if (path == null) {
			path = movement.getPath();
		}

		if (path == null || !path.hasNext()) {
			this.nextTimeToMove = movement.nextPathAvailable();
			this.path = null;
			return false;
		}

		this.destination = path.getNextWaypoint();
		this.speed = path.getSpeed();

		if (this.movListeners != null) {
			for (MovementListener l : this.movListeners) {
				l.newDestination(this, this.destination, this.speed);
			}
		}

		return true;
	}

	/**
	 * Sends a message from this host to another host
	 * @param id Identifier of the message
	 * @param to Host the message should be sent to
	 */
	public void sendMessage(String id, DTNHost to) {
		this.router.sendMessage(id, to);
	}

	/**
	 * Start receiving a message from another host
	 * @param m The message
	 * @param from Who the message is from
	 * @return The value returned by 
	 * {@link MessageRouter#receiveMessage(Message, DTNHost)}
	 */
	public int receiveMessage(Message m, DTNHost from) {
		int retVal = this.router.receiveMessage(m, from); 

		if (retVal == MessageRouter.RCV_OK) {
			m.addNodeOnPath(this);	// add this node on the messages path
		}

		return retVal;	
	}
	/*
	public int parsetonode(String tmp){
		//tmp.substring(1,tmp.length()-1) ;
		return Integer.valueOf(tmp.substring(1,tmp.length()));
	}
*/
	/**
	 * Requests for deliverable message from this host to be sent trough a
	 * connection.
	 * @param con The connection to send the messages trough
	 * @return True if this host started a transfer, false if not
	 */
	public boolean requestDeliverableMessages(Connection con) {
		return this.router.requestDeliverableMessages(con);
	}

	/**
	 * Informs the host that a message was successfully transferred.
	 * @param id Identifier of the message
	 * @param from From who the message was from
	 */
	public void messageTransferred(String id, DTNHost from) {
		this.router.messageTransferred(id, from);
	}

	/**
	 * Informs the host that a message transfer was aborted.
	 * @param id Identifier of the message
	 * @param from From who the message was from
	 * @param bytesRemaining Nrof bytes that were left before the transfer
	 * would have been ready; or -1 if the number of bytes is not known
	 */
	public void messageAborted(String id, DTNHost from, int bytesRemaining) {
		this.router.messageAborted(id, from, bytesRemaining);
	}

	/**
	 * Creates a new message to this host's router
	 * @param m The message to create
	 */
	public void createNewMessage(Message m) {
		this.router.createNewMessage(m);
	}

	/**
	 * Deletes a message from this host
	 * @param id Identifier of the message
	 * @param drop True if the message is deleted because of "dropping"
	 * (e.g. buffer is full) or false if it was deleted for some other reason
	 * (e.g. the message got delivered to final destination). This effects the
	 * way the removing is reported to the message listeners.
	 */
	public void deleteMessage(String id, boolean drop) {
		this.router.deleteMessage(id, drop);
	}

	/**
	 * Returns a string presentation of the host.
	 * @return Host's name
	 */
	public String toString() {
		return name;
	}

	/**
	 * Checks if a host is the same as this host by comparing the object
	 * reference
	 * @param otherHost The other host
	 * @return True if the hosts objects are the same object
	 */
	public boolean equals(DTNHost otherHost) {
		return this == otherHost;
	}

	/**
	 * Compares two DTNHosts by their addresses.
	 * @see Comparable#compareTo(Object)
	 */
	public int compareTo(DTNHost h) {
		return this.getAddress() - h.getAddress();
	}
	
	public String getrouter(){
		return String.valueOf(this.router);
	}

}
