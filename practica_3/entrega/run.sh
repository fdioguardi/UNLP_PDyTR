#!/bin/sh

usage() {
  echo "usage: $0 PACKAGE CLASS [(-o | --option) OPT]" && exit "$1"
}

# Check parameters

args=""
while [ -n "$1" ]; do
  case "$1" in
    --option | -o)
      [ -z "$option" ] || usage 7
      option="$2"
      shift
      ;;

    --* | -.) usage 22 ;;

    *)
      if [ -z "$exercise" ]; then
        exercise="$1"
      elif [ -z "$side" ]; then
        side="$1"
      elif [ -z "$args" ]; then
	args="$1"
      else
	args="$args"" $1"
      fi
      ;;

  esac
  shift
done

[ -z "$exercise" ] || [ -z "$side" ] && usage 1

# Run maven
# shellcheck disable=SC2086
mvn -DskipTests $option exec:java -Dexec.mainClass=pdytr."$exercise"."$side" -Dexec.args="$args"
