import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SAckReceiver2 extends Thread{
	@Override
	public void run(){
		try(DatagramSocket receiverSocket2 = new DatagramSocket(Link.getPort(Node.R2, Node.S))){// Ackreceiver for r2
			int lastBase = SMain.PACKET_COUNT1 + SMain.PACKET_COUNT2;
			while(!SMain.session2CanEnd) {
				byte[] buffer = new byte[1024];
				receiverSocket2.setSoTimeout(20000);
				DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length); // create the receiver packet
				receiverSocket2.receive(datagramPacket);// receive the message
				Message message= new Message(new String(datagramPacket.getData())); // stringify the message
				if(message.valid) { 
					if(message.messageType == MessageType.START) {
						SMain.session2CanStart = true;
					}
					else if(message.messageType == MessageType.END) {
						SMain.session2CanEnd = true;
					}
					else {
						if(Long.compare(message.sequenceNum, SMain.getBase2()) == 0) {
							System.out.println("ACK2: " + message.sequenceNum+ " ackedBASE: "+SMain.getBase2());
							SMain.setBase2(SMain.getBase2()+1);
							SMain.setSendTime2(System.currentTimeMillis());
						}
						else {
							System.out.println("invalid seqNum2: " + message.sequenceNum + " base2: " + SMain.getBase2());
							SMain.setBase2(message.sequenceNum+1);
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