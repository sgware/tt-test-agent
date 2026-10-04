package edu.uky.cs.nil.tt;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import edu.uky.cs.nil.tt.io.Connect;
import edu.uky.cs.nil.tt.world.State;
import edu.uky.cs.nil.tt.world.Status;
import edu.uky.cs.nil.tt.world.Turn;
import edu.uky.cs.nil.tt.world.World;

/**
 * A Tandem Tales agent that can play any world as either role, which makes fast
 * random decisions, and which always chooses to let proposed actions succeed.
 * <p>
 * When it is this agent's turn:
 * <ul>
 * <li>If this is a normal turn, the agent chooses to pass based on {@link
 * #PASS_PROBABILITY}. If the agent does not choose to pass, it chooses a
 * {@link Turn.Type#SUCCEED} or {@link Turn.Type#PROPOSE} turn uniformly at
 * random.</li>
 * <li>If the previous turn was a proposal, the agent always chooses the
 * first {@link Turn.Type#SUCCEED} action.</li>
 * </ul>
 * 
 * @author Stephen G. Ware
 */
public class TestAgent extends Client {
	
	/** The probability (between 0 and 1) that an agent will choose to pass */
	public static final double PASS_PROBABILITY = 0.5;
	
	/** A predicate for filtering only {@link Turn.Type#PASS} turns */
	private static final Predicate<Turn> PASS = turn -> turn.type == Turn.Type.PASS;
	
	/** A predicate for filtering only {@link Turn.Type#SUCCEED} turns */
	private static final Predicate<Turn> SUCCEED = turn -> turn.type == Turn.Type.SUCCEED;
	
	/**
	 * A predicate for filtering {@link Turn.Type#SUCCEED} and {@link
	 * Turn.Type#PROPOSE} turns
	 */
	private static final Predicate<Turn> SUCCEED_OR_PROPOSE = turn -> turn.type == Turn.Type.SUCCEED || turn.type == Turn.Type.PROPOSE;
	
	/** The ID number to assign to the next agent */
	private static int nextID = 0;
	
	/** A unique ID number that identifies this agent */
	public final int id = nextID++;
	
	/** A random number generator used by the agent to make decisions */
	private final Random random = new Random(0);
	
	/**
	 * Creates a test agent that will connect to the given URL and network port.
	 * 
	 * @param url the URL to which agents will connect
	 * @param port the network port to which the agents will connect
	 */
	public TestAgent(String url, int port) {
		super("test", null, null, null, url, port);
	}
	
	@Override
	public String toString() {
		return "Test Agent " + id;
	}
	
	@Override
	protected void onConnect(Connect connect) throws Exception {
		System.out.println(this + " connected.");
	}
	
	@Override
	protected void onStart(World world, Role role, State initial) throws Exception {
		System.out.println(this + " started in world \"" + world.name + "\" as " + role + ".");
	}
	
	@Override
	protected final int onChoice(Status status) throws Exception {
		// Choose between these turns.
		List<Turn> choices = status.getChoices();
		Turn choice = null;
		// If this is a normal turn...
		if(choices.stream().anyMatch(PASS)) {
			// Decide if the agent will pass.
			if(random.nextDouble() < PASS_PROBABILITY)
				choice = choices.stream().filter(PASS).findFirst().orElse(null);
			// Choose a non-fail action uniformly at random.
			else {
				List<Turn> options = choices.stream().filter(SUCCEED_OR_PROPOSE).collect(Collectors.toList());
				if(options.size() > 0)
					choice = options.get(random.nextInt(options.size()));
			}
		}
		// Otherwise, choose the first success.
		else
			choice = choices.stream().filter(SUCCEED).findFirst().orElse(null);
		// Print the turn.
		if(choice == null)
			System.err.println(this + " failed to choose a turn.");
		else
			System.out.println(this + " chose: " + choice.getDescription());
		// Return the index of the choice.
		for(int index = 0; index < choices.size(); index++)
			if(choices.get(index) == choice)
				return index;
		return 0;
	}
	
	@Override
	protected void onStop(String message) throws Exception {
		System.out.println(this + " ended" + (message == null ? "." : ": " + message));
	}
	
	@Override
	protected void onClose() throws Exception {
		System.out.println(this + " closed.");
	}
	
	@Override
	protected void onDisconnect() throws Exception {
		System.out.println(this + " disconnected.");
	}
}