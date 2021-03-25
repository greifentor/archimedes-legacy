# archimedes-legacy
The good old archimedes (cleaned up).


## Ideas

* Read in Liquibase via a memory database (Liquibase -> memory database -> JDBC -> Archimedes model).


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

### 2.0.1 (24.03.2021)

* Ticks on the work sheet raster are standard now. It could be disabled by holding the SHIFT key pressed on dragging a
GUI object.


## Compare Data Models

Select "Export / Explore" (Export / Untersuchen) for each data model to compare with options

```
TO_UPPER_CASE|SUPPRESS_LENGTH|FK_BY_COLUMNNAME
```

Save the results and compare the files via a diff tool.