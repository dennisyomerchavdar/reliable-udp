import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class STransmitter1 extends Thread {
	@Override
	public void run() {
		try (DatagramSocket transmitterSocket = new DatagramSocket()) {
			
			SMain.setSendTime1(System.currentTimeMillis());
			int lastBase = SMain.PACKET_COUNT1;
			String data = "-1";
			String message = MessageType.START.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			DatagramPacket datagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(),
			SMain.linkToR1.ip, SMain.linkToR1.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session1CanStart) {
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime1() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(),
					SMain.linkToR1.ip, SMain.linkToR1.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime1(System.currentTimeMillis());
				}
				
			}
			while (SMain.getBase1() < lastBase) {
				
				if (SMain.getNextSeqNum1() < SMain.getBase1() + SMain.WINDOW_SIZE && SMain.getNextSeqNum1() < lastBase) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] byteData= new byte[500];
					for (int i =0 ; i < 500 ; i++) {
						byteData[i] = SMain.bytes[(int) (SMain.getNextSeqNum1()*500 + i)] ; 
					}
					data = new String(byteData);
					message = MessageType.DATA.name() + " " + SMain.getNextSeqNum1() + " "+ Message.hash(data) + " " + data; // message
																												// which we
																												// will send
					datagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(),
							SMain.linkToR1.ip, SMain.linkToR1.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					//System.out.println("send1: " + data + " checksum: " + Message.hash(data));
					SMain.setNextSeqNum1(SMain.getNextSeqNum1()+1);
				}
				if (System.currentTimeMillis() > SMain.getSendTime1() + SMain.TIMEOUT) {
					System.out.println("Transmitter1 timeoutted , base: " + SMain.getBase1());
					SMain.setNextSeqNum1(SMain.getBase1());
					SMain.setSendTime1(System.currentTimeMillis());
				}
				
			}
			SMain.setSendTime1(System.currentTimeMillis());
			data = "END";
			message = MessageType.END.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			datagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(),
			SMain.linkToR1.ip, SMain.linkToR1.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session1CanEnd) {
					System.out.println("session1 end");
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime1() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes("UTF-8"), message.length(),
					SMain.linkToR1.ip, SMain.linkToR1.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime1(System.currentTimeMillis());
				}
				
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
