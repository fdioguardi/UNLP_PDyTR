#!/bin/bash

# Test if copies are correct

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

rm -f -- ./{"$REMOTE","$LOCAL","$SHARED"}/files/*.txt

start_rmiregistry
javac ./{"$REMOTE","$LOCAL","$SHARED"}/*.java

start_remote_object
java "$LOCAL".AskRemote localhost

declare -a files=(./{"$REMOTE","$LOCAL"}/files/*.txt)
for a_file in "${files[@]}"; do
  for two_file in "${files[@]}"; do
    diff "$a_file" "$two_file" \
      || echo "$a_file and $two_file are different" \
      || exit
  done
done
unset files

rm ./{"$REMOTE","$LOCAL","$SHARED"}/*.class
killall rmiregistry
killall java
