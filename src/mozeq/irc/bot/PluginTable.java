package mozeq.irc.bot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class PluginTable {
	private Hashtable<String, List<IrcBotPlugin>> pluginsTable;

	PluginTable() {
		pluginsTable = new Hashtable<String, List<IrcBotPlugin>>();
	}

	public void registerPlugin(IrcBotPlugin plugin) {

		List<IrcBotPlugin> plugins = null;
		for(String s: plugin.commands){

			plugins = this.pluginsTable.get(s);
			if (plugins == null)
				plugins = new ArrayList<IrcBotPlugin>();

			plugins.add(plugin);

			this.pluginsTable.put(s, plugins);
		}

	}

	public List<String> getCommands() {
		ArrayList<String> commands = new ArrayList<String>();

		for(String s : pluginsTable.keySet()) {
			commands.add(s);
		}

		return commands;
	}

	public List<IrcBotPlugin> getActions(String command) {
		List<IrcBotPlugin> actionList = pluginsTable.get(command);
		return actionList;
	}

}

