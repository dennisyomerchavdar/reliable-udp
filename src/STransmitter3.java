import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.zip.Adler32;

public class STransmitter3 extends Thread {
	@Override
	public void run() {
		try (DatagramSocket transmitterSocket = new DatagramSocket()) {
			SMain.setSendTime3(System.currentTimeMillis());
			int lastBase = SMain.PACKET_COUNT1 + SMain.PACKET_COUNT2 + SMain.PACKET_COUNT3;
			String data = Integer.toString(SMain.PACKET_COUNT1 + SMain.PACKET_COUNT2 - 1);
			String message = MessageType.START.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
			SMain.linkToR3.ip, SMain.linkToR3.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session3CanStart) {
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime3() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
					SMain.linkToR3.ip, SMain.linkToR3.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime3(System.currentTimeMillis());
				}
				
			}
			while(SMain.getBase3() < lastBase) {
				if (SMain.getNextSeqNum3() < SMain.getBase3() + SMain.WINDOW_SIZE && SMain.getNextSeqNum3() < lastBase) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] byteData= new byte[500];
					for (int i =0 ; i < 500 ; i++) {
						byteData[i] = SMain.bytes[(int) (SMain.getNextSeqNum3()*500 + i)] ; 
					}
					data = new String(byteData);
					message = MessageType.DATA.name() + " " + SMain.getNextSeqNum3() + " "+ Message.hash(data) + " " + data; // message
																												// which we
																												// will send
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
							SMain.linkToR3.ip, SMain.linkToR3.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					//System.out.println("send3: " + data + " checksum: " + Message.hash(data));
					SMain.setNextSeqNum3(SMain.getNextSeqNum3()+1);
				}
				if (System.currentTimeMillis() > SMain.getSendTime3() +  SMain.TIMEOUT) {
					System.out.println("Transmitter3 timeoutted , base: " + SMain.getBase3());
					SMain.setNextSeqNum3(SMain.getBase3());
					SMain.setSendTime3(System.currentTimeMillis());
				}
			}
			SMain.setSendTime3(System.currentTimeMillis());
			data = "END";
			message = MessageType.END.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
			SMain.linkToR3.ip, SMain.linkToR3.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session3CanEnd) {
					System.out.println("session3 end");
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime3() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
					SMain.linkToR3.ip, SMain.linkToR3.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime3(System.currentTimeMillis());
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
