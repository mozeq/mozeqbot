package mozeq.irc.bot.plugins;

import java.net.MalformedURLException;
import java.util.ArrayList;

import mozeq.irc.bot.ConfigLoader;
import mozeq.irc.bot.Configuration;
import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.apache.xmlrpc.XmlRpcException;
import org.mozeq.Trac.Ticket;
import org.mozeq.Trac.TracProxy;

public class TracPlugin extends IrcBotPlugin {

	private String USERNAME = null;
	private String PASSWORD = null;
	private String PROJECT = null;
	private String TRAC_URL = null;

	@Override
	public void init() {
		this.commands = new ArrayList<String>();

		commands.add(".trac#\\d+");

		Configuration conf = ConfigLoader.getConfiguration("tracplugin.conf");
		USERNAME = conf.get("username");
		PASSWORD = conf.get("password");
		PROJECT = conf.get("project");
		TRAC_URL = conf.get("trac_url");
	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();
		String[] params = message.body.split("#");
		if (params.length < 2) {
			System.err.println("Can't parse the ticket id from the message");
			return responses;
		}

		TracProxy trac = new TracProxy(TRAC_URL, PROJECT);
		try {
			trac.connect(USERNAME, PASSWORD);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int ticketID = 0;
		try {
			ticketID = parseNumberFromMsg(params[1]);
		} catch (NumberFormatException e) {
			//write this message to the irc?
			System.err.println("Can't parse number from: " + params[1]);
			//there is no reason to continue, so return..
		}

		Ticket t = null;
		try {
			t = trac.getTicket(ticketID);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

		if(t != null)
			addResponse("trac#"+t.getID() +": ["+ t.getComponent() + "] " + t.getSummary() + " <" + trac.getProjectURL() + "/ticket/" + t.getID() + ">");

		return responses;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
