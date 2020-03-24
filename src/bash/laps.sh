LAPS_DIR="" # This is your LAPS installation directory (inside quotes)
MAIN_CLASS=edu.rit.gec8773.laps.Main
java -cp "$LAPS_DIR/LAPS.jar:$(pwd)" $MAIN_CLASS "$@"