import java.net.UnknownHostException;
import java.util.ArrayList;

public class R3Main { // Main to be called from R3
	public static void main(String[] args) throws InterfaceNotFoundException, UnknownHostException, InterruptedException {
		ArrayList<Thread> threads = new ArrayList<>();
		threads.add(new Thread(new Router(Node.S, Node.R3, Node.D, 50001)));//Routing from S through R3 to D
		threads.add(new Thread(new Router(Node.D, Node.R3, Node.S, 50002)));//Routing from D through R3 to S
		for (Thread t: threads) { //Start threads
			t.start();
		}
		for (Thread t: threads) { // wait threads
			t.join();
		}

	}
}
