package edu.uky.cs.nil.tt;

/**
 * The main entry point for the Tandem Tales Test Agent.
 * 
 * @author Stephen G. Ware
 */
public class TestAgentMain {
	
	/** A message explaining how to use this tool. */
	private static final String USAGE = "Tandem Tales Test Agent by Stephen G. Ware\n" +
		"Usage: java -jar tt-test-agent.jar [OPTIONS]\n" +
		"Options:\n" +
		"  -help           Print this message and halt.\n" +
		"  -url <string>   The URL to which agents will connect (default: localhost).\n" +
		"  -port <number>  The network port to which agents will connect (default: " + Settings.DEFAULT_PORT + ").";
	
	/**
	 * A private constructor to prevent creation of instances.
	 */
	private TestAgentMain() {
		// default constructor
	}
	
	/**
	 * The main entry point for the Tandem Tales Test Agent which parses the
	 * arguments passed from the terminal.
	 * 
	 * @param args the arguments passed from the terminal
	 * @throws Exception if a problem occurs while the test agents are running
	 */
	public static void main(String[] args) throws Exception {
		Arguments arguments = new Arguments(args);
		if(arguments.contains("help")) {
			System.out.println(USAGE);
			return;
		}
		String url = "localhost";
		if(arguments.contains("url"))
			url = arguments.getValue("url");
		int port = Settings.DEFAULT_PORT;
		if(arguments.contains("port"))
			port = Integer.parseInt(arguments.getValue("port"));
		arguments.checkUnused();
		try(TestAgentFactory factory = new TestAgentFactory(url, port)) {
			factory.call();
		}
	}
}