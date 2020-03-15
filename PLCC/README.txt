Note: Python3 and the Java SDK (version 5 or greater) must be installed
on your Windows platform.

You should be able to get PLCC working on Windows by using the attached
batch files.

IMPORTANT: Before running these files, set up the LIBPLCC environment
variable to point to the absolute path of THIS folder name and add THIS
folder name to your PATH environment variable.

If the environment variable is giving you trouble when running the Python
plcc.py file, you may need to change line 48 of the plcc.py Python file
to return the absolute path name of THIS folder.

You will also need javac and python to be in your PATH environment variable,
should they not have been set by their installers. Make sure that the python
executable runs Python3, not Python2.

Here's a summary of the batch files:

plcc.bat file:          runs plcc.py on 'file'
plccmk.bat [-c] [file]: runs plcc.py on 'file' and compiles all of
                            the resulting Java files in the Java directory.
                        The optional '-c' flag will remove all previous
                            Java files if there were any
                        The 'file' name defaults to 'parser'
scan.bat:               Runs the Java/Scan program (only scan for tokens)
parse.bat:              Runs the Java/Parser program (only scan and parse)
rep.bat:                Runs the Java/Rep program
                            (scan, parse, and enter read-eval-print loop)
rep-t.bat:              Runs the Java/Rep program with the trace flag
