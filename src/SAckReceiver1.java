import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SAckReceiver1 extends Thread{
	@Override
	public void run(){
		try(DatagramSocket receiverSocket1 = new DatagramSocket(Link.getPort(Node.R1, Node.S))){// Ackreceiver for r1
			int lastBase = SMain.PACKET_COUNT1;
			while(!SMain.session1CanEnd) {
				byte[] buffer = new byte[1024];
				receiverSocket1.setSoTimeout(20000);
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length); // create the receiver packet
				receiverSocket1.receive(datagramPacket);// receive the message
				Message message= new Message(new String(datagramPacket.getData())); // stringify the message
				if(message.valid) { 
					if(message.messageType == MessageType.START) {
						SMain.session1CanStart = true;
					}
					else if(message.messageType == MessageType.END) {
						SMain.session1CanEnd = true;
					}
					else {
						if(Long.compare(message.sequenceNum, SMain.getBase1()) == 0) {
							System.out.println("ACK1: " + message.sequenceNum + " ackedBASE: "+(SMain.getBase1()));
							SMain.setBase1(SMain.getBase1()+1);
							SMain.setSendTime1(System.currentTimeMillis());
						}
						else {
							System.out.println("invalid seqNum1: " + message.sequenceNum + " base1: " + SMain.getBase1());
							SMain.setBase1(message.sequenceNum+1);
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}