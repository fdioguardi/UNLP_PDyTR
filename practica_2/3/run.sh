#!/bin/bash

# Test if copies are correct

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

set_files() {
  [ -d "$REMOTE/files" ] || mkdir "$REMOTE/files"
  [ -d "$LOCAL/files" ] || mkdir "$LOCAL/files"
  rm -f -- ./{"$REMOTE","$LOCAL"}/files/*.txt
}

diff_files() {
  declare -a files=(./{"$REMOTE","$LOCAL"}/files/*.txt)
  for a_file in "${files[@]}"; do
    for two_file in "${files[@]}"; do
      diff "$a_file" "$two_file" \
        || echo "$a_file and $two_file are different" \
        || exit
    done
  done
  unset files
}

main() {
  set_files
  javac ./{"$REMOTE","$LOCAL","$SHARED"}/*.java

  rmiregistry &
  wait_rmiregistry && java "$REMOTE".StartRemoteObject &
  wait_server && java "$LOCAL".AskRemote localhost

  diff_files && echo "Copied successfully"

  rm ./{"$REMOTE","$LOCAL","$SHARED"}/*.class
  killall rmiregistry java
}

main "$@"
