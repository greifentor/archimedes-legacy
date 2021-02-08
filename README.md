# archimedes-legacy
The good old archimedes (cleaned up).


## Requirements

* Java 11
* Maven 3.5
* Project "dm-comp" from github "Greifentor" in the same super folder than this project.


### Project structure

* [your work-space folder]
** archimedes-legacy
** dm-comp


## Build

* Type `mvn clean install` in a shell.


## Start

Call `start` script for your operation system.

To change the JVM to another as is configured as default, create a file named `java-engine-path.bat` and type 
something like:
```
SET PATH=C:\JVMs\java11\bin;%PATH%
```

into the file.

This working with Window only in the moment.


## News

### 1.91.1 (30.06.2020)

There is a new field for additional diagram info ("Zusätzliche Diagramminfo"). It could be filled with a string or 
identifiers for diagram options. The option identifiers have this format: 

```
${Option."OptionName"} e. g. for option VERSION_TAG (value "4711") it would be: ${Option.VERSION_TAG}

For the option above the string "Version Tag is: ${Option."OptionName"}" leads to "Version Tag is: 4711". 
```

The new field is printed in the header block of the diagram view directly following the version date. It has been added
to allow printing option values to the diagram.