#! /bin/bash

CLASSPATH=\
"bin\
:/usr/share/java/commons-httpclient.jar\
:/usr/share/java/jsoup.jar\
:jars/commons-logging-1.1.jar\
:jars/ws-commons-util-1.0.2.jar\
:jars/xmlrpc-client-3.1.3.jar\
:jars/xmlrpc-common-3.1.3.jar\
:jars/xmlrpc-server-3.1.3.jar\
:jars/jBugzilla.jar\
:jars/jTrac.jar"

echo $CLASSPATH
java -classpath $CLASSPATH "mozeq.irc.bot.IrcBot"
