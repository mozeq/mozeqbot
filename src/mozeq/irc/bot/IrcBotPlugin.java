package mozeq.irc.bot;

import java.util.ArrayList;
import java.util.List;

public abstract class IrcBotPlugin {

	protected ArrayList<String> responses = null;
	IrcMessage lastMessage= null;

	public IrcBotPlugin() {
	}

	protected void clearResponses(){
		responses = new ArrayList<String>();
	}

	protected void addResponse(String response){
		if (responses == null)
			clearResponses();

		responses.add(response);
	}

	protected List<String> commands = null;

	/* method called when the plugin is loaded */
	abstract public void init();

	abstract public ArrayList<String> run(IrcMessage message, String command);

	public boolean test() {
		return true;
	}

	public int parseNumberFromMsg(String strToParse) throws NumberFormatException {
		int intEnd = 0;

		while (intEnd < strToParse.length() && Character.isDigit(strToParse.charAt(intEnd)))
			intEnd++;

		String strNum = strToParse.substring(0, intEnd);
		int ticketID = 0;

		System.out.println(">>>" + strNum);

		ticketID = Integer.parseInt(strNum);

		return ticketID;
	}

}
