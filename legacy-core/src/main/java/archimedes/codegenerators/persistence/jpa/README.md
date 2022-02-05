# Persistence JPA Code Generator
---

## Options

### Columns

#### AUTOINCREMENT

Activates the generation of an autoincrement field as key attribute in the entity class and adds different annotations to the ``@Id`` field(s).

##### IDENTITY

Sets the annotations for a default autoincrement field.

```
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

###### SEQUENCE

Sets annotations to get the autoincrement via a sequence.

```
@SequenceGenerator(allocationSize = 1, name = "[SimpleClassName]Sequence", sequenceName = "[TableName.toLowerCase()]_[IdFieldName.toLowerCase()]_seq")
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "[SimpleClassName]Sequence")
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

* **ID**: No object references will be generated in the DBO's. They will be represented as fields of the column type.
These method keep the foreign keys away from the DBO logic.
* **OBJECT**: References are represented by object references with the necessary JPA annotations. This will also take an
effect to the generated converters.


### Table

#### REFERENCE_MODE

Allow to choose the method of reference processing for a specific table. Description of the feature see above.