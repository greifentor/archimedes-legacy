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

Will be provided since version 2.1.1 is released.


## Code Factories

### Included Code Factories

[Persistence JPA](./archimedes-legacy-core/src/main/java/archimedes/codegenerators/persistence/jpa/README.md)

### Templates

Each Archimedes Code Factory works with Velocity templates. These are found in the sub folders of 
"archimedes-legacy/legacy-core/src/main/resources/templates". This may be configured to another path by property
"CodeFactory.templates.path". The folder name for each code factory is to configure by property 
"{CodeFactorySimpleClassName}.templates.folder" (e. g. "RESTControllerCodeFactory.templates.folder"). The default 
values of the configurations should work in standard cases.


## Compare Data Models

Select "Export / Explore" (Export / Untersuchen) for each data model to compare with options

```
TO_UPPER_CASE|SUPPRESS_LENGTH|FK_BY_COLUMNNAME
```

Save the results and compare the files via a diff tool.