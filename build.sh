#! /bin/sh

git submodule init
git submodule update
# make jTrac
pushd `pwd`
cd jars/jTrac/
mkdir bin 2>&1 > /dev/null
make jar
popd
# make jBugzilla
pushd `pwd`
cd jars/jBugzilla
mkdir bin 2>&1 > /dev/null
make jar
popd
# make the bot
make
