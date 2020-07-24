import java.net.UnknownHostException;
import java.util.ArrayList;

public class R1Main {  // Main to be called from R1
	public static void main(String[] args) throws InterfaceNotFoundException, UnknownHostException, InterruptedException {
		ArrayList<Thread> threads = new ArrayList<>();
		threads.add(new Thread(new Router(Node.S, Node.R1, Node.D, 50001)));//Routing from S through R1 to D
		threads.add(new Thread(new Router(Node.D, Node.R1, Node.S, 50002)));//Routing from D through R1 to S
		for (Thread t: threads) { //Start threads
			t.start();
		}
		for (Thread t: threads) { //wait threads
			t.join();
		}

	}
}
