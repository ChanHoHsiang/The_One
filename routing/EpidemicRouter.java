/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package routing;

import core.Settings;
import core.DTNHost;
import core.SimClock;

/**
 * Epidemic message router with drop-oldest buffer and only single transferring
 * connections at a time.
 */
public class EpidemicRouter extends ActiveRouter {
	
	/**
	 * Constructor. Creates a new message router based on the settings in
	 * the given Settings object.
	 * @param s The settings object
	 */
	public EpidemicRouter(Settings s) {
		super(s);
		//TODO: read&use epidemic router specific settings (if any)
	}
	
	/**
	 * Copy constructor.
	 * @param r The router prototype where setting values are copied from
	 */
	protected EpidemicRouter(EpidemicRouter r) {
		super(r);
		//TODO: copy epidemic settings here (if any)
	}
			
	@Override
	public void update() {
		super.update();
		if (isTransferring() || !canStartTransfer()) {
			return; // transferring, don't try other connections yet
		}
		
		// Try first the messages that can be delivered to final recipient
		if (exchangeDeliverableMessages() != null) {
			return; // started a transfer, don't try others (yet)
		}
		//DTNHost fromNode = getHost();
		//DTNHost toNode = getHost();
		
		
		//System.out.print("**meetTime :"+fromNode.+"\n");
		//System.out.print("**fromNode :"+fromNode+"\n");
		/*AllValuesList [] globalvalue =new AllValuesList[fromNode.meetList.size()];
		for(int i=0;i<fromNode.meetList.size();i++){
			globalvalue[i] =new AllValuesList();
			globalvalue[i].fromNodeID=fromNode.meetList.get(i).getHostID();
			globalvalue[i].meetLocation =fromNode.meetList.get(i).getLocation();
			globalvalue[i].meetTime = fromNode.meetList.get(i).getMeetTime();
			
			
			System.out.print("*globalvalue.fromNodeID :"+globalvalue[i].fromNodeID+"\n");
			System.out.print("**meetTime :"+globalvalue[i].meetTime+"\n");
			System.out.print("***meetLocation :"+globalvalue[i].meetLocation+"\n");
			
		}
		*/
		
		// then try any/all message to any/all connection
		//System.out.print("System::::::::::"+SimClock.getTime());
		
		this.tryAllMessagesToAllConnections();
	}
	
	
	@Override
	public EpidemicRouter replicate() {
		return new EpidemicRouter(this);
	}

}