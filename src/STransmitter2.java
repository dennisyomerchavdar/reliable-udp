import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.zip.Adler32;

public class STransmitter2 extends Thread {
	@Override
	public void run() {
		try (DatagramSocket transmitterSocket = new DatagramSocket()) {
			SMain.setSendTime2(System.currentTimeMillis());
			int lastBase = SMain.PACKET_COUNT1 + SMain.PACKET_COUNT2;
			String data = Integer.toString(SMain.PACKET_COUNT1 - 1);
			String message = MessageType.START.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
			SMain.linkToR2.ip, SMain.linkToR2.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session2CanStart) {
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime2() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
					SMain.linkToR2.ip, SMain.linkToR2.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime2(System.currentTimeMillis());
				}
				
			}
			while(SMain.getBase2() < lastBase) {
				if (SMain.getNextSeqNum2() < SMain.getBase2() + SMain.WINDOW_SIZE && SMain.getNextSeqNum2() < lastBase) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] byteData= new byte[500];
					for (int i =0 ; i < 500 ; i++) {
						byteData[i] = SMain.bytes[(int) (SMain.getNextSeqNum2()*500 + i)] ; 
					}
					data = new String(byteData);
					message = MessageType.DATA.name() + " " + SMain.getNextSeqNum2() + " " + Message.hash(data) + " " + data; // message
																												// which we
																												// will send
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
							SMain.linkToR2.ip, SMain.linkToR2.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					//System.out.println("send2: " + data + " checksum: " + Message.hash(data));
					SMain.setNextSeqNum2(SMain.getNextSeqNum2()+1);
				}
				if (System.currentTimeMillis() > SMain.getSendTime2() +  SMain.TIMEOUT) {
					System.out.println("Transmitter2 timeoutted , base: " + SMain.getBase2());
					SMain.setNextSeqNum2(SMain.getBase2());
					SMain.setSendTime2(System.currentTimeMillis());
				}
			}
			SMain.setSendTime2(System.currentTimeMillis());
			data = "END";
			message = MessageType.END.name() + " " + -1 + " " + Message.hash(data) + " " + data; // message
			datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
			SMain.linkToR2.ip, SMain.linkToR2.port); // create a packet for destination carrying the message
			transmitterSocket.send(datagramPacket); // send the packet
			while (true) {
				if(SMain.session2CanEnd) {
					System.out.println("session2 end");
					break;
				}
				if(System.currentTimeMillis() > SMain.getSendTime2() + SMain.TIMEOUT) {
					datagramPacket = new DatagramPacket(message.getBytes(), message.length(),
					SMain.linkToR2.ip, SMain.linkToR2.port); // create a packet for destination carrying the message
					transmitterSocket.send(datagramPacket); // send the packet
					SMain.setSendTime2(System.currentTimeMillis());
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