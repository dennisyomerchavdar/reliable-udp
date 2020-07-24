import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Router implements Runnable { // Takes the packet coming from sendernode, and forwards it to receiverNode
	private final Node myNode;   // This node, the router node.
	private final Node receiverNode; // destination for our packet
	private final int sendPort;   // destination port to send the packet
	private final int receivePort;  // this node's port to check for new messages

	public Router(Node senderNode, Node myNode, Node receiverNode,  int sendPort) {
		this.myNode = myNode;
		this.receiverNode = receiverNode;
		this.sendPort = sendPort;
		this.receivePort = Link.getPort(senderNode, myNode);

	}

	@Override
	public void run() {
		try (DatagramSocket receiverSocket = new DatagramSocket(this.receivePort)) {
			byte[] buffer = new byte[SMain.BUFFER_SIZE];
			receiverSocket.setSoTimeout(20000);
			while (true) {
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length); //create a packet
				receiverSocket.receive(datagramPacket);  // receive the message
				String receivedMessage = new String(datagramPacket.getData()); // stringify the message

				try (DatagramSocket transmitterSocket = new DatagramSocket(this.sendPort)) {// reroute the packet to the destination

					String message = receivedMessage;  //create the message to send
					Link link = Link.getLink(this.myNode, this.receiverNode); // get info about the link between this node and destination
					DatagramPacket responsePacket = new DatagramPacket(message.getBytes(), message.length(), link.ip,
							link.port);
					transmitterSocket.send(responsePacket); //send the packet

				} catch (InterfaceNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

	}
}
