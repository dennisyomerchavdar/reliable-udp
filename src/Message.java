import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Message {
	public boolean valid = false;
	public MessageType messageType ;
	public long sequenceNum;
	public long checksum;
	public byte[] data;
	public Message(String s) {
		try (Scanner sc = new Scanner(s)) { // Open up the message
			
			this.messageType = MessageType.valueOf(sc.next());
			this.sequenceNum = sc.nextLong();
			this.checksum = sc.nextLong();
			
				
			//System.out.println(s);
			sc.useDelimiter("\\z");
			String stringData = sc.next();
			stringData = stringData.substring(1);
			this.data = stringData.getBytes();
			
			//System.out.println("Data is: $" + stringData +"$ hash is: $" + hash(stringData));
			if( Long.compare(hash(stringData), this.checksum) ==0) {
				this.valid = false;
			}
			else {
				this.valid= true;
			}
			
				
			
		}
		
	}
	
	@Override
	public String toString() {
		return this.messageType.name() + " " + this.sequenceNum + " " + this.checksum + " " + this.data;   // message which we will send
	}
	
	public static long hash(String string) {
		  long h = 1125899906842597L; // prime
		  int len = string.length();

		  for (int i = 0; i < len; i++) {
		    h = 31*h + string.charAt(i);
		  }
		  return h;
	}
}
