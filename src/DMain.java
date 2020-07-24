import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DMain {
	public static byte[] receivedBytes= new byte[5000000];
	
	public static void main(String[] args) throws InterruptedException {
		
		//take start
		
		
		
		double startTime = System.currentTimeMillis();
		ArrayList<Thread> threads = new ArrayList<>();
		threads.add(new Thread(new Receiver(Node.R1, Node.D))); //receives message from R1
		threads.add(new Thread(new Receiver(Node.R2, Node.D))); //receives message from R2
		threads.add(new Thread(new Receiver(Node.R3, Node.D))); //receives message from R3
		for (Thread t: threads) { //Start all threads
			t.start(); 
		}
		for (Thread t: threads) { //wait all threads
			t.join();
		}
		double duration = (System.currentTimeMillis() - startTime ) / 1000;
		System.out.println("DURATION OF EXPERIMENT: " + duration);
		
		try (FileOutputStream fos = new FileOutputStream("/users/e2171478/bin/output.txt")) {
			   fos.write(receivedBytes);
			   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
