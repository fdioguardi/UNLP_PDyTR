#!/bin/sh

# Test if copies are correct

REMOTE="./files"
LOCAL="./local_files"

start_rmiregistry() {
  rmiregistry &
  while ! ss -lt | grep -q rmiregistry; do
    sleep 0.1
  done
}

start_remote_object() {
  java StartRemoteObject &
  while ! [ "$(ss -ta | grep -v TIME-WAIT | grep -c ":rmiregistry")" -ge 3 ]; do
    sleep 0.1
  done
}

rm -f -- "$REMOTE"/*.txt "$LOCAL"/*.txt

start_rmiregistry
javac ./*.java

start_remote_object
java AskRemote localhost

for a_file in "$REMOTE"/*.txt; do
  for two_file in "$LOCAL"/*.txt; do
    diff "$a_file" "$two_file" \
      || echo "$a_file and $two_file are different" \
      || exit
  done
done

rm ./*.class
killall rmiregistry
killall java
