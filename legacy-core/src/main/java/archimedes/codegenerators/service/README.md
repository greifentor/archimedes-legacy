# Service Code Generator
---

## Options

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