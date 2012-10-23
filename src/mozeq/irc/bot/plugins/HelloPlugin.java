package mozeq.irc.bot.plugins;

import java.util.ArrayList;

import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

public class HelloPlugin extends IrcBotPlugin {

	public HelloPlugin() {
		this.commands = new ArrayList<String>();
		commands.add("hello");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		// TODO Auto-generated method stub

		addResponse("Hello world");
		return responses;
	}

}
