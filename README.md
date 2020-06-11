# archimedes-legacy
The good old archimedes (cleaned up).


## Requirements

* Java 11
* Maven 3.5


## Build

* Type `mvn clean install` in a shell.


## Start

Call `start` script for your operation system.


## Open Todos

- Make image path configurable (see archimedes.legacy.Archimedes line 1976).
- Make build path relative by a configurable variable (e. g. "${base.output.path}").
- Extend the CodeFactory interface by a "prepare" method which could be use to e. g. create a list with option identifiers to provide those identifiers in option dialogs. 