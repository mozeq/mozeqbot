package mozeq.irc.bot.plugins;

import java.util.ArrayList;

import mozeq.irc.bot.IrcBotPlugin;
import mozeq.irc.bot.IrcMessage;

public class ControlPlugin extends IrcBotPlugin {
	String[] controlCommands = {"quit", "reload"};

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.commands = new ArrayList<String>();

		for (String s: controlCommands) {
			commands.add(s);
		}

	}

	@Override
	public ArrayList<String> run(IrcMessage message, String command) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
