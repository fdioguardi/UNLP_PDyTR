#!/bin/bash

# Prove concurrency breaks everything

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

start_clients() {
  declare -a procs
  for _ in {1..3}; do
    for file in "$LOCAL"/files/*.txt; do
      java "$LOCAL".AskRemote localhost "$(basename "$file")" &
      procs+=("$!")
    done
  done
  wait "${procs[@]}"
  unset procs
}

set_files() {
  [ -d "$REMOTE/files" ] || mkdir "$REMOTE/files"
  [ -d "$LOCAL/files" ] || mkdir "$LOCAL/files"
  rm -f -- ./{"$REMOTE","$LOCAL"}/files/*.txt
  echo "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" > "$LOCAL/files/a.txt"
  echo "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" > "$LOCAL/files/b.txt"
  echo "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc" > "$LOCAL/files/c.txt"
}

main() {
  set_files
  javac ./{"$REMOTE","$LOCAL","$SHARED"}/*.java
  rmiregistry &
  wait_rmiregistry && java "$REMOTE".StartRemoteObject &
  wait_server && start_clients
  rm ./{"$REMOTE","$LOCAL","$SHARED"}/*.class
  killall rmiregistry java
}

main "$@"
