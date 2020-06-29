# archimedes-legacy
The good old archimedes (cleaned up).


## Requirements

* Java 11
* Maven 3.5


## Build

* Type `mvn clean install` in a shell.


## Start

Call `start` script for your operation system.

To change the JVM to another as is configured as default, create a file named `java-engine-path.bat` and type something like:
```
SET PATH=C:\JVMs\java11\bin;%PATH%
```

into the file.

This working with Window only in the moment.


## Ideas

### More than One Code Factory

- Archimedes should be able to process mor than one code factory for a single code generation run.
- The code factory monitor dialog should be extended by an additional progress bar for the code factories.
- A specific listener (not this which is managed by the code factories) should implemented and used to update the new progress bar.
