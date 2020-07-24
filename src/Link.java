
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Link { //this class has the IP table and PORT table. provides encapsulation
	public final Inet4Address ip;
	public final int capacity;
	public final int port;

	private Link(Inet4Address ip, int capacity, int port) { //get the link 
		this.ip = ip; //  ip for link
		this.capacity = capacity; //capacity of the link
		this.port = port;  //port number for sending a message through link
	}

	public static int getPort(Node fromNode, Node toNode) { // this is the port table function, for every incoming link, there is a different port number for a node
		if (fromNode == Node.S) {
			if (toNode == Node.R1) {
				return 60000; 
			} else if (toNode == Node.R2) {
				return 60000;
			} else if (toNode == Node.R3) {
				return 60000;
			} else
				throw new InterfaceNotFoundException();
		} else if (fromNode == Node.R1) {
			if (toNode == Node.S) {
				return 60000;
			} else if (toNode == Node.R2) {
				return 60001;
			} else if (toNode == Node.D) {
				return 60000;
			} else
				throw new InterfaceNotFoundException();

		} else if (fromNode == Node.R2) {
			if (toNode == Node.S) {
				return 60001;
			} else if (toNode == Node.R1) {
				return 60001;
			} else if (toNode == Node.R3) {
				return 60001;
			} else if (toNode == Node.D) {
				return 60001;
			} else
				throw new InterfaceNotFoundException();

		} else if (fromNode == Node.R3) {
			if (toNode == Node.S) {
				return 60002;
			} else if (toNode == Node.R2) {
				return 60002;
			} else if (toNode == Node.D) {
				return 60002;
			} else
				throw new InterfaceNotFoundException();

		} else { // FromNode ==D
			if (toNode == Node.R1) {
				return 60002;
			} else if (toNode == Node.R2) {
				return 60003;
			} else if (toNode == Node.R3) {
				return 60002;
			} else
				throw new InterfaceNotFoundException();

		}
	}

	public static Link getLink(Node fromNode, Node toNode) throws InterfaceNotFoundException, UnknownHostException {// this is IP table function, also initializes port as a bonus
		if (fromNode == Node.S) {
			if (toNode == Node.R1) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 1, 2 }), 1000, 60000);
			} else if (toNode == Node.R2) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 2, 1 }), 1000, 60000);
			} else if (toNode == Node.R3) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 3, 2 }), 2000, 60000);
			} else
				throw new InterfaceNotFoundException();
		} else if (fromNode == Node.R1) {
			if (toNode == Node.S) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 1, 1 }), 1000, 60000);
			} else if (toNode == Node.R2) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 8, 2 }), 100, 60001);
			} else if (toNode == Node.D) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 4, 2 }), 1000, 60000);
			} else
				throw new InterfaceNotFoundException();

		} else if (fromNode == Node.R2) {
			if (toNode == Node.S) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 2, 2 }), 1000, 60001);
			} else if (toNode == Node.R1) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 8, 1 }), 100, 60001);
			} else if (toNode == Node.R3) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 6, 2 }), 100, 60001);
			} else if (toNode == Node.D) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 5, 2 }), 1000, 60001);
			} else
				throw new InterfaceNotFoundException();

		} else if (fromNode == Node.R3) {
			if (toNode == Node.S) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 3, 1 }), 2000, 60002);
			} else if (toNode == Node.R2) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 6, 1 }), 100, 60002);
			} else if (toNode == Node.D) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 7, 1 }), 2000, 60002);
			} else
				throw new InterfaceNotFoundException();

		} else { // FromNode ==D
			if (toNode == Node.R1) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 4, 1 }), 1000, 60002);
			} else if (toNode == Node.R2) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 5, 1 }), 1000, 60003);
			} else if (toNode == Node.R3) {
				return new Link((Inet4Address) InetAddress.getByAddress(new byte[] { 10, 10, 7, 2 }), 2000, 60002);
			} else
				throw new InterfaceNotFoundException();

		}

	}
}
