#!/bin/bash

# Execute the test to output response times

shopt -s nullglob

REMOTE="server"
LOCAL="client"
SHARED="shared"

wait_rmiregistry() {
  while ! ss -lt | grep -q rmiregistry; do
    sleep 0.1
  done
}

wait_server() {
  while ! [ "$(ss -ta | grep -v TIME-WAIT | grep -c ":rmiregistry")" -ge 3 ]; do
    sleep 0.1
  done
}

main() {
  javac ./{"$REMOTE","$LOCAL","$SHARED"}/*.java

  rmiregistry &
  wait_rmiregistry && java "$REMOTE".StartRemoteObject &
  wait_server && java "$LOCAL".AskRemote localhost

  rm ./{"$REMOTE","$LOCAL","$SHARED"}/*.class
  killall rmiregistry java
}

main "$@"
