all:
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/Configuration.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/ConfigLoader.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/IrcMessage.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/IrcBotPlugin.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/PluginTable.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/PluginManager.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/IrcConnection.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/IrcBot.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/plugins/ControlPlugin.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/plugins/HelloPlugin.java"
	javac -d "bin" -classpath "bin" "src/mozeq/irc/bot/plugins/KudosPlugin.java"
	javac -d "bin" -classpath "bin:/usr/share/java/jsoup.jar" "src/mozeq/irc/bot/plugins/LunchTimePlugin.java"
	javac -d "bin" -classpath "bin:/usr/share/java/jsoup.jar" "src/mozeq/irc/bot/plugins/PaladeoPlugin.java"
	javac -d "bin" -classpath "bin:/usr/share/java/jsoup.jar:jars/commons-logging-1.1.jar:jars/ws-commons-util-1.0.2.jar:jars/xmlrpc-client-3.1.3.jar:jars/xmlrpc-common-3.1.3.jar:jars/xmlrpc-server-3.1.3.jar:jars/jTrac.jar" "src/mozeq/irc/bot/plugins/TracPlugin.java"
	javac -d "bin" -classpath "bin:/usr/share/java/jsoup.jar:jars/commons-logging-1.1.jar:jars/ws-commons-util-1.0.2.jar:jars/xmlrpc-client-3.1.3.jar:jars/xmlrpc-common-3.1.3.jar:jars/xmlrpc-server-3.1.3.jar:jars/jTrac.jar:jars/jBugzilla.jar" "src/mozeq/irc/bot/plugins/BugzillaPlugin.java"
	javac -d "bin" -classpath "bin:/usr/share/java/jsoup.jar" "src/mozeq/irc/bot/plugins/U3OpicPlugin.java"