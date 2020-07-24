import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SAckReceiver3 extends Thread{
	@Override
	public void run(){
		try(DatagramSocket receiverSocket3 = new DatagramSocket(Link.getPort(Node.R3, Node.S))){// Ackreceiver for r3
			int lastBase = SMain.PACKET_COUNT1 + SMain.PACKET_COUNT2 + SMain.PACKET_COUNT3;
			while(!SMain.session3CanEnd) {
				byte[] buffer = new byte[1024];
				receiverSocket3.setSoTimeout(20000);
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length); // create the receiver packet
				receiverSocket3.receive(datagramPacket);// receive the message
				Message message= new Message(new String(datagramPacket.getData())); // stringify the message
				if(message.valid) { 
					if(message.messageType == MessageType.START) {
						SMain.session3CanStart = true;
					}
					else if(message.messageType == MessageType.END) {
						SMain.session3CanEnd = true;
					}
					else {
						if(Long.compare(message.sequenceNum, SMain.getBase3()) == 0) {
							System.out.println("ACK3: " + message.sequenceNum +  " ackedBASE: "+SMain.getBase3());
							SMain.setBase3(SMain.getBase3()+1);
							SMain.setSendTime3(System.currentTimeMillis());
						}
						else {
							System.out.println("invalid seqNum3: " + message.sequenceNum + " base3: " + SMain.getBase3());
							SMain.setBase3(message.sequenceNum+1);
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
