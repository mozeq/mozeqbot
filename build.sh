#! /bin/bash

git submodule init
git submodule update
# make jTrac
pushd `pwd`
cd jars/jTrac/
mkdir bin &> /dev/null
make jar
popd
# make jBugzilla
pushd `pwd`
cd jars/jBugzilla
mkdir bin &> /dev/null
make jar
popd
# make the bot
mkdir bin &> /dev/null
make
