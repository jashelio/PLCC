#!/usr/bin/env bash

LAPS_DIR="$(cd "$(dirname "$0")" && pwd)" # This is your LAPS installation directory (inside quotes)
MAIN_CLASS=edu.rit.gec8773.laps.Main
java -cp "$LAPS_DIR/LAPS.jar:$(pwd)" $MAIN_CLASS "$@"