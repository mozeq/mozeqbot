package mozeq.irc.bot.plugins;

import java.net.MalformedURLException;
import java.util.ArrayList;

import mozeq.irc.bot.ConfigLoader;
import mozeq.irc.bot.Configuration;
import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

import org.apache.xmlrpc.XmlRpcException;
import org.mozeq.bugzilla.BugzillaProxy;
import org.mozeq.bugzilla.BugzillaTicket;

public class BugzillaPlugin extends IrcBotPlugin {
	private String USERNAME = null;
	private String PASSWORD = null;
	private String BZ_URL = null;

	@Override
	public void init() {
		this.commands = new ArrayList<String>();

		commands.add(".rhbz#");

		Configuration conf = ConfigLoader.getConfiguration("bzplugin.conf");
		if (conf != null) {
			USERNAME = conf.get("username");
			PASSWORD = conf.get("password");
			BZ_URL = conf.get("bz_url");
		}
	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		clearResponses();
		String[] params = message.body.split("#");
		if (params.length < 2) {
			System.err.println("Can't parse the ticket id from the message");
			return responses;
		}

		BugzillaProxy bz = new BugzillaProxy(BZ_URL);
		try {
			bz.connect(USERNAME, PASSWORD);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BugzillaTicket bzTicket = null;
		try {
			bzTicket = bz.getTicket(Integer.parseInt(params[1]));
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (bzTicket != null) {
			String ticketURL = bz.getURL() + "/show_bug.cgi?id=" + bzTicket.getID();
			addResponse("bz#" + bzTicket.getID() + ": ["+ bzTicket.getComponent() +"] " + bzTicket.getSummary() + " <"+ ticketURL +">");
		}

		return responses;
	}

}
