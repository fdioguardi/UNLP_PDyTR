#!/bin/bash

# Prove concurrency breaks everything

shopt -s nullglob

REMOTE="server"
LOCAL="client"
SHARED="shared"

start_rmiregistry() {
  rmiregistry &
  while ! ss -lt | grep -q rmiregistry; do
    sleep 0.1
  done
}

start_remote_object() {
  java "$REMOTE".StartRemoteObject &
  while ! [ "$(ss -ta | grep -v TIME-WAIT | grep -c ":rmiregistry")" -ge 3 ]; do
    sleep 0.1
  done
}

[ -d "$REMOTE/files" ] || mkdir "$REMOTE/files"
[ -d "$LOCAL/files" ] || mkdir "$LOCAL/files"
rm -f -- ./{"$REMOTE","$LOCAL"}/files/*.txt
echo "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" > "$LOCAL/files/a.txt"
echo "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" > "$LOCAL/files/b.txt"
echo "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" > "$LOCAL/files/c.txt"

start_rmiregistry
javac ./{"$REMOTE","$LOCAL","$SHARED"}/*.java

start_remote_object

java "$LOCAL".AskRemote localhost a.txt &
a="$!"
java "$LOCAL".AskRemote localhost b.txt &
b="$!"
java "$LOCAL".AskRemote localhost c.txt &
c="$!"

wait "$a" "$b" "$c"

rm ./{"$REMOTE","$LOCAL","$SHARED"}/*.class
killall rmiregistry
killall java
