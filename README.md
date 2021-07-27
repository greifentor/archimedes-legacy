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

[Common Functionallity](./legacy-core/src/main/java/archimedes/codegenerators/README.md)

[Persistence JPA](./legacy-core/src/main/java/archimedes/codegenerators/persistence/jpa/README.md)

[Service](./legacy-core/src/main/java/archimedes/codegenerators/service/README.md)

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


## Model Checkers

Archimedes has a set of model checkers which are able to create the info, 
warning and error messages in the right lower corner of the application 
window.

There are three ways to get messages in this label (and dialog which opens
if you double click the label).

### Built-In Model Checkers

There is a set of built in model checkers which are activated automatically
for any data model after starting the application. This checker do checks on 
the data model as shown below:

* Check for domains which are not used (WARNING).
* Check for columns which have no domain assignment (ERROR).
* Check for potential foreign key columns (WARNING).
* Check for tables which does not have a primary key attribute (WARNING). 

It also starts the model checker which processes a Java script (see below
"Model Checker By Script").


### Model Checkers of Code Factories

Additionally any Code Factory implementation could provide its own model 
checker implementations. This will be described in the documentation of the 
code factories.


### Model Checker by Script

Any data model could have a Java script in its configuration which is able
to produce model checker messages. This script get four parameters which 
can be used inside the script:

* **guiBundle** - A class with GUI information.
* **messages** - An empty list of ModelCheckerMessage object. This should be filled by the script.
* **model** - An access to the data model.
* **table** - A list of all table of the data model (for convenience).

You find a senseless example script below:
```java
load("nashorn:mozilla_compat.js");
importPackage(Packages.archimedes.acf.checker);
importPackage(Packages.corentx.dates);
importPackage(java.lang);

System.out.println("Scripted Model Checker Started ...");

for (i = 0, leni = tables.size(); i < leni; i++) {
    messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.INFO, guiBundle.getResourceText("Table checked: {0}", tables.get(i).getName())));
}

System.out.println("Scripted Model Checker Finished.");
```

This script will print the names of any table in the data model to the console.


## Exporter

[Liquibase Script](./legacy-core/src/main/java/archimedes/legacy/exporter/README.md)