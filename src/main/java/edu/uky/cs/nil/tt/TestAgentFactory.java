package edu.uky.cs.nil.tt;

/**
 * A {@link ClientFactory factory} that creates new {@link TestAgent}s as
 * needed.
 * 
 * @author Stephen G. Ware
 */
public class TestAgentFactory extends ClientFactory {
	
	/** The URL to which agents will connect */
	private final String url;
	
	/** The network port to which agents will connect */
	private final int port;
	
	/**
	 * Creates a test agent factory whose agents will connect to a given URL and
	 * port.
	 * 
	 * @param url the URL to which agents will connect
	 * @param port the network port to which the agents will connect
	 */
	public TestAgentFactory(String url, int port) {
		super();
		this.url = url;
		this.port = port;
	}
	
	@Override
	public String toString() {
		return "Test Agent Factory";
	}
	
	@Override
	protected Client create() throws Exception {
		return new TestAgent(url, port);
	}
	
	@Override
	protected void onStart() throws Exception {
		System.out.println(this + " started.");
	}
	
	@Override
	protected void onClose() throws Exception {
		System.out.println(this + " closed.");
	}
	
	@Override
	protected void onStop() throws Exception {
		System.out.println(this + " stopped.");
	}
}