package routing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.soap.Node;

import org.omg.CORBA.PUBLIC_MEMBER;

import core.Connection;
import core.Coord;
import core.DTNHost;
import core.Message;
import core.Settings;
import core.Tuple;
//import movement.ExternalMovement;
//import movement.ExtendedMovementModel;
//import input.ExternalMovementReader;

/*
 * chan test
 * 
 */

public class InterestBaseRouter extends ActiveRouter{
	
	public static final String INTEREST_NS="InterestBaseRouter";
	
	
	//System.out.println("d");
	public InterestBaseRouter (Settings s)	{
		
		super(s);
		
		//System.out.println("#Int");
	}
	
	public InterestBaseRouter(InterestBaseRouter ibr){
		super(ibr);
		
		//System.out.println("##Int");
	}
	
	@Override
	public void update() {
		
		super.update();
		if (isTransferring() || !canStartTransfer()) {
			return; // transferring, don't try other connections yet
		}
		
		// Try first the messages that can be delivered to final recipient
				//最終的接受者，在social的話前面可以加一個判斷為跟其他connetion的node比較數值，若沒有比較好的傳輸者，則自己為最後的接收者
		if (exchangeDeliverableMessages() != null) {
			return; // started a transfer, don't try others (yet)
		}
		
		// then try any/all message to any/all connection
		// this.tryOtherMessages();
		//System.out.println("進入發訊息");
		this.trySendMessageswithInterest();
		
	}

	

	
	
	//這邊是要寫routing規則like interest
	
	
	
	private Tuple<Message, Connection> trySendMessageswithInterest() {
		
		List<Tuple<Message, Connection>> messages = new ArrayList<Tuple<Message, Connection>>();
		List<Message> copyLeft = new ArrayList<Message>();
		List<Message> forwardings = new ArrayList<Message>();
		
		Collection<Message> msgCollection = getMessageCollection();
		
		
		//System.out.print("getMessageCollection :"+getMessageCollection()+"\n");
		
		DTNHost fromNode = getHost();
		
		DTNHost toNode = null;
		
		
		
		//System.out.print("fromNode :"+getHost()+"\n");
		
		
		
		//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		
		//AllValuesList [] globalvalue =new AllValuesList();
		//AllValuesList [] globalvalue =new AllValuesList[fromNode.meetList.size()];
		/*
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
		//DTNHost ReceivedHost = null;
		
		
		
		for (Connection con : getConnections()){
			
			toNode = con.getOtherNode(getHost());
			
						
			InterestBaseRouter ReceivedHostRouter = (InterestBaseRouter)toNode.getRouter();
			
			//System.out.println("IB___meeting__list__"+toNode.meetList.size()+"\n");
			
		/*	if(toNode.meetList.size()>0){
			
			System.out.println("IB___meeting__list__"+toNode.meetList.get(0).getHostID()+"\n");
			}
			*/
			
			if (ReceivedHostRouter.isTransferring()) {
												
				continue; // skip hosts that are transferring
			}
			
			boolean t=true;
			for (Message fm : msgCollection){
				
				if(t)
					System.out.print("initial...."+"\n");
				//System.out.print("fm :"+fm+"____"+fm.minterest[0]+"\n");
				//System.out.print("fm0 :"+fm+"\n");
				
				//System.out.print("fm1 :"+fromNode.getMessageCollection()+"\n");
				//System.out.print("fm2 :"+fromNode.getminteresr(fm.getId())[0]+"\n");
				
				
				//System.out.print("fm3 :"+parsetonode(String.valueOf(toNode))+"\n");
				//System.out.print("fm4 :"+String.valueOf(toNode)+"\n");
				//System.out.print("fm5 :"+toNode.totalpinterest[parsetonode(String.valueOf(toNode))][0]+"\n");
				
				
				double[] a= new double[5];
				double[] b= new double[5];
				double[] c= new double[5];
				double x=0.0;
				double y=0.0;
			//	double xx=0.0;
				
				double suma=0.0;
				double sumb=0.0;
				
				double sumaa=0.0;
				double sumbb=0.0;
				
				double similarity=0.0;
				
				double fsimilarity=0.0;
				
				for(int i=0;i<5;i++)
				{
					a[i]=fromNode.getminteresr(fm.getId())[i]; //訊息的興趣
					b[i]=toNode.totalpinterest[parsetonode(String.valueOf(toNode))][i]; //要傳的人興趣
					
					
				//	System.out.println("***************"+toNode.totalpinterest[parsetonode(String.valueOf(toNode))][i]+"\n");
					
					//c[i]=toNode.meetList.
					x+=a[i]*b[i];
					//y*=Math.sqrt(Math.pow(a[i], 2))*Math.sqrt(Math.pow(b[i], 2));
					suma+=Math.pow(a[i], 2);
					sumb+=Math.pow(b[i], 2);
				}
				similarity= x /(Math.sqrt(suma)*Math.sqrt(sumb));
				//System.out.println("sim"+similarity+"\n");
				
				t=false;
		/*		
		 * 
				System.out.print("fm :"+fm.minterest[0]+"\n");
				System.out.print("fm :"+toNode.pinterest[0]+"\n");
				//toNode.pinterest
				Double similarity =0.00;
				Double sum =0.00;
				
				For a vector A = (a1, a2), ||A|| is defined as sqrt(a1^2 + a2^2)

				For vector A = (a1, a2) and B = (b1, b2), A . B is defined as a1 b1 + a2 b2;

				So for vector A = (a1, a2) and B = (b1, b2), the cosine similarity is given as:

				(a1 b1 + a2 b2) / sqrt(a1^2 + a2^2) sqrt(b1^2 + b2^2)
								
								
			*/	
				
			if (similarity > 0.75){
					
					
					messages.add(new Tuple<Message, Connection>(fm,con));
				}
				//在寫其他相遇的人的感興趣狀況
			else {
				
				//System.out.println("sim*******"+similarity+"\n");
				
				for(int k=0 ; k<toNode.meetList.size();k++){
					
					for (int j=0 ; j<5;j++){
						
					a[j]=fromNode.getminteresr(fm.getId())[j];
					
					c[j]=toNode.totalpinterest[parsetonode(String.valueOf(toNode.meetList.get(k).getHostID()))][j];
					
					y+=a[j]*c[j];
					
					sumaa+=Math.pow(a[j], 2);
					sumbb+=Math.pow(c[j], 2);
					
			//		System.out.print("123");
					
			//		System.out.print("******************"+toNode.totalpinterest[parsetonode(String.valueOf(toNode.meetList.get(k).getHostID()))][j]+"\n");
			//		System.out.print("******************"+toNode.totalpinterest[parsetonode(String.valueOf(toNode.meetList.get(k).getHostID()))][j]+"\n");
					
					}
					//fsimilarity = y/ (Math.sqrt(sumaa)*Math.sqrt(sumbb));
						
					if (fsimilarity > 0.75)
						{
							messages.add(new Tuple<Message, Connection>(fm,con));
						}
						//System.out.println("fsim...."+fsimilarity+"\n");
				}
				
			}
			
			}
					
		}
		
		
	
		
		return tryMessagesForConnected(messages);
	}
	

	@Override
	public int receiveMessage(Message m, DTNHost from) {
		
		return super.receiveMessage(m, from);
	}

	/*public List<Connection> NodeFuction() {
		
		return NodeFuction();
	}*/
	
	@Override
	public InterestBaseRouter  replicate() {
		// TODO Auto-generated method stub
		return new InterestBaseRouter(this);
	}
	
	public int parsetonode(String tmp){
		//tmp.substring(1,tmp.length()-1) ;
		return Integer.valueOf(tmp.substring(1,tmp.length()));
	}
	
} 