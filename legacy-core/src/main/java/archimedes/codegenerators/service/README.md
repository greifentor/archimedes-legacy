# Service Code Generator
---

## Options

### Column

#### FIND_BY

Creates a specific method to find objects of the table by values of the column. If the unique flag is set for the column
the method returns an optional:

```
Signature: Optional<TableClass> findByColumnName(ColumnClass)
```

If the unique flag is not set, the method returns a list of table class objects.

```
Signature: List<TableClass> findAllByColumnName(ColumnClass)
```

All classes will be model classes.

##### LIST_ACCESS

Provides a method which returns a list of objects for the table selected by the marked attribute.

E. g.:
```
List<Book> findAllByTpoic(Topic topic)
```


### Domain

#### ENUM

Creates a enum for the domain.

* **identifiers** - A set of identifiers for the enum (comma separated).

E. g.

```
ENUM:ONE,TWO,THREE
```


### Model

#### ALTERNATE_MODULE_PREFIX

* **prefix** or empty - Sets the passes prefix instead of the application name in MODULE_MODE.

```
/home/myuser/myproject/myprefix-service/src ...
```
or
```
/home/myuser/myproject/service/src ...
```
for an empty prefix.

#### COMMENTS

Values:
* **Off** - Suppresses the comment generation (CAUTION! This will suppress also overwriting already generated files!).

#### MODULE_MODE

Creates a prefix for the output files with the module name of the code generator and the lowercase application name:

```
/home/myuser/myproject/myapplication-service/src ...
```

#### REFERENCE_MODE

Allow to choose the method of reference processing:

* **ID**: No object references will be generated in the model classes. They will be represented as fields of the column
type. These method keep the foreign keys away from the model logic.
* **OBJECT**: References are represented by object references. This will also take an effect to the generated 
converters.


### Table

#### REFERENCE_MODE

Allow to choose the method of reference processing for a specific table. Description of the feature see above.