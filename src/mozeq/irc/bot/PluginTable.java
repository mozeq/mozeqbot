package mozeq.irc.bot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

public class PluginTable {
	private Hashtable<Pattern, List<IrcBotPlugin>> pluginsTable;

	PluginTable() {
		pluginsTable = new Hashtable<Pattern, List<IrcBotPlugin>>();
	}

	public void registerPlugin(IrcBotPlugin plugin) {

		List<IrcBotPlugin> plugins = null;
		for(String s: plugin.commands){

			plugins = this.pluginsTable.get(s);
			if (plugins == null)
				plugins = new ArrayList<IrcBotPlugin>();

			plugins.add(plugin);

			this.pluginsTable.put(Pattern.compile(s), plugins);
		}

	}

	public List<Pattern> getCommands() {
		ArrayList<Pattern> commands = new ArrayList<Pattern>();

		for(Pattern p : pluginsTable.keySet()) {
			commands.add(p);
		}

		return commands;
	}

	public List<IrcBotPlugin> getActions(Pattern command) {
		List<IrcBotPlugin> actionList = pluginsTable.get(command);
		return actionList;
	}

}

