# Persistence JPA Code Generator
---

## Options

### Columns

#### AUTO_INCREMENT

Activates the generation of an autoincrement field as key attribute in the entity class and adds different annotations
to the ``@Id`` field(s). As a parameter a method for the id generation could be specified:

|Parameter|Description|Code|
|---------|-----------|----|
|IDENTITY |Specifies GenerationType IDENTITY|@GeneratedValue(strategy = GenerationType.IDENTITY)|

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

The repository will work with the related DBO's but the persistence adapter returns related model objects.

##### IDENTITY

Sets the annotations for a default autoincrement field.

```
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

##### LIST_ACCESS

Provides a method which returns a list of objects for the table selected by the marked attribute.

E. g.: 
```
List<Book> findAllByTpoic(Topic topic)
```

###### SEQUENCE

Sets annotations to get the autoincrement via a sequence.

```
@SequenceGenerator(allocationSize = 1, name = "[SimpleClassName]Sequence", sequenceName = "[TableName.toLowerCase()]_[IdFieldName.toLowerCase()]_seq")
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "[SimpleClassName]Sequence")
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


#### MAPPERS

Allows to switch to an alternative mapper generation. Passing the value ``mapstruct`` as option parameter will create
mapstruct interfaces instead of converter classes. Take care that the required dependencies are in your POM file to
avoid compilation errors.


#### MODULE_MODE

Creates a prefix for the output files with the module name of the code generator and the lowercase application name:

```
/home/myuser/myproject/myapplication-service/src ...
```

#### REFERENCE_MODE

Allow to choose the method of reference processing:

* **ID**: No object references will be generated in the DBO's. They will be represented as fields of the column type.
These method keep the foreign keys away from the DBO logic.
* **OBJECT**: References are represented by object references with the necessary JPA annotations. This will also take an
effect to the generated converters.


### Table

#### REFERENCE_MODE

Allow to choose the method of reference processing for a specific table. Description of the feature see above.


#### SUPPRESS_SUBCLASS_SELECTORS

Suppresses the generation of the ``find`` methods for subclasses in the ``GeneratedJPAPersistenceAdapterClassCodeGenerator``.
Is usefull in combination with mapstruct mapper generation.
