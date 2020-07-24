import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.Adler32;

public class Receiver implements Runnable { //receives messages and stores them for data
	private final Node otherNode; // the node that sends the packets
	private final Node myNode; // this  node 
	private final int receivePort; // port to check for new messages
	private Link ackLink;
	
	public Receiver(Node otherNode, Node myNode) {
		this.otherNode = otherNode; 
		this.myNode = myNode;
		this.receivePort = Link.getPort(otherNode, myNode); //get the link info for the link between sender node and my node
		this.ackLink = null;
		try {
			this.ackLink= Link.getLink(myNode, otherNode);
		} catch (InterfaceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try (DatagramSocket receiverSocket = new DatagramSocket(this.receivePort)) { //create the receiver socket
			long lastCorrectSeqNum = -1;
			while (true) {
				byte[] buffer = new byte[SMain.BUFFER_SIZE];
				receiverSocket.setSoTimeout(20000);
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length); // create the receiver packet
				receiverSocket.receive(datagramPacket); // receive the message
				String receivedMessage = new String(datagramPacket.getData()); // stringify the message
				Message message = new Message(receivedMessage);
				//System.out.println(new String(message.data));
				if(message.valid && message.messageType == MessageType.END) {
					String data = "ACK";
					String ackMessage = MessageType.END.name() + " " + lastCorrectSeqNum + " "+ Message.hash(data) + " " + data; // message
					datagramPacket = new DatagramPacket(ackMessage.getBytes(), ackMessage.length(),
							this.ackLink.ip, this.ackLink.port); // create a packet for destination carrying the message
					try (DatagramSocket ackSocket = new DatagramSocket()) { //create the transmission socket
						ackSocket.send(datagramPacket); // send the packet
					}
					break;
				}
				
				if(message.valid && message.messageType == MessageType.START) {
					System.out.println( "$" +new String(message.data) + "$");
					lastCorrectSeqNum = Integer.valueOf(new String(message.data).trim());
					String data = "ACK";
					String ackMessage = MessageType.START.name() + " " + lastCorrectSeqNum + " "+ Message.hash(data) + " " + data; // message
					datagramPacket = new DatagramPacket(ackMessage.getBytes(), ackMessage.length(),
							this.ackLink.ip, this.ackLink.port); // create a packet for destination carrying the message
					try (DatagramSocket ackSocket = new DatagramSocket()) { //create the transmission socket
						ackSocket.send(datagramPacket); // send the packet
					}
					continue;
				}
				
				if (message.valid && Long.compare(message.sequenceNum, lastCorrectSeqNum+1) == 0) {
					lastCorrectSeqNum++;
					long start = message.sequenceNum * 500;
					for (long i= 0 ; i < 500 ; i++  ) {
						DMain.receivedBytes[(int) (start + i)]= message.data[(int) i];
					}
		
				}
				String data = "ACK";
				String ackMessage = MessageType.DATA.name() + " " + lastCorrectSeqNum + " "+ Message.hash(data) + " " + data; // message
				datagramPacket = new DatagramPacket(ackMessage.getBytes(), ackMessage.length(),
						this.ackLink.ip, this.ackLink.port); // create a packet for destination carrying the message
				
				
				try (DatagramSocket ackSocket = new DatagramSocket()) { //create the transmission socket
					ackSocket.send(datagramPacket); // send the packet
				}
				
				
				System.out.println("from: " + this.myNode + " to: " + this.otherNode + " message: " + ackMessage); // print for informative purposes
				System.out.println("from: " + this.otherNode + " to: " + this.myNode + " message: " + receivedMessage); // print for informative purposes

			}
			
			
			

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
