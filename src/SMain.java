import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.zip.Adler32;

public class SMain {
	
	private static int TOTAL_PACKETS = 10000; //Total packets in file
	public static int PACKET_COUNT1 = 3333;
	public static int PACKET_COUNT2 = 3333;
	public static int PACKET_COUNT3 = 3334;
	public static double TIMEOUT = 300;
	public static int WINDOW_SIZE = 100;
	public static int BUFFER_SIZE = 1024;
	public static byte[] bytes;
	public static File myFile = new File("/users/e2171478/bin/input.txt"); //Unfortunately we dont support UTF-8
	public static Link linkToR1; //for ip adresses etc
	public static Link linkToR2;//for ip adresses etc
	public static Link linkToR3;//for ip adresses etc
	private static long nextSeqNum1 = 0; //seqnum for go backn of route r1
	private static long nextSeqNum2 = PACKET_COUNT1 ; //seqnum for go backn of route r2
	private static long nextSeqNum3 = PACKET_COUNT1 + PACKET_COUNT2; //seqnum for go backn of route r3
	private static long base1 = 0; //base for go backn of route r1
	private static long base2 = PACKET_COUNT1 ; //base for go backn of route r2
	private static long base3 = PACKET_COUNT1 + PACKET_COUNT2; //base for go backn of route r3
	private static double sendTime1; // this is for timeout of r1
	private static double sendTime2; // this is for timeout of r2
	private static double sendTime3; // this is for timeout of r3
	public static boolean session1CanStart = false; // managing start
	public static boolean session2CanStart = false; // managing start
	public static boolean session3CanStart = false;// managing start
	public static boolean session1CanEnd = false; // managing end
	public static boolean session2CanEnd = false;// managing end
	public static boolean session3CanEnd = false;// managing end

	static {
		 
		try {
			bytes = Files.readAllBytes(myFile.toPath());
			
			System.out.println(myFile.length());
			
			
			linkToR1 = Link.getLink(Node.S, Node.R1);
			linkToR2 = Link.getLink(Node.S, Node.R2);
			linkToR3 = Link.getLink(Node.S, Node.R3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized long getBase1() {
		return base1;
	}
	public static synchronized long getBase2() {
		return base2;
	}
	public static synchronized long getBase3() {
		return base3;
	}
	public static synchronized long getNextSeqNum1() {
		return nextSeqNum1;
	}
	public static synchronized long getNextSeqNum2() {
		return nextSeqNum2;
	}
	public static synchronized long getNextSeqNum3() {
		return nextSeqNum3;
	}
	public static synchronized double getSendTime1() {
		return sendTime1;
	}
	public static synchronized double getSendTime2() {
		return sendTime2;
	}
	public static synchronized double getSendTime3() {
		return sendTime3;
	}
	
	
	public static synchronized void setBase1(long value) {
		base1 = value;
	}
	public static synchronized void setBase2(long value) {
		base2 = value;
	}
	public static synchronized void setBase3(long value) {
		base3 = value;
	}
	public static synchronized void setNextSeqNum1(long value) {
		nextSeqNum1 = value;
	}
	public static synchronized void setNextSeqNum2(long value) {
		nextSeqNum2 = value;
	}
	public static synchronized void setNextSeqNum3(long value) {
		nextSeqNum3 = value;
	}
	public static synchronized void setSendTime1(double value) {
		sendTime1 = value;
	}
	public static synchronized void setSendTime2(double value) {
		sendTime2 = value;
	}
	public static synchronized void setSendTime3(double value) {
		sendTime3 = value;
	}
	
	
	public static void main(String[] args) {
		System.out.println("Starting transmitters");
		SAckReceiver1 r1 = new SAckReceiver1();
		SAckReceiver2 r2 = new SAckReceiver2();
		SAckReceiver3 r3 = new SAckReceiver3();
		STransmitter1 t1 = new STransmitter1();
		STransmitter2 t2 = new STransmitter2();
		STransmitter3 t3 = new STransmitter3();
		r1.start();
		r2.start();
		r3.start();
		t1.start();
		t2.start();
		t3.start();

	}
}