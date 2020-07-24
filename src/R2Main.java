import java.net.UnknownHostException;
import java.util.ArrayList;

public class R2Main {  // Main to be called from R2

	public static void main(String[] args) throws InterfaceNotFoundException, UnknownHostException, InterruptedException {
		ArrayList<Thread> threads = new ArrayList<>();
		threads.add(new Thread(new Router(Node.S, Node.R2, Node.D, 50001)));//Routing from S through R2 to D
		threads.add(new Thread(new Router(Node.D, Node.R2, Node.S, 50002)));//Routing from D through R2 to S
		for (Thread t: threads) { //start threads
			t.start(); 
		}
		for (Thread t: threads) { //wait threads
			t.join();
		}

	}

}
