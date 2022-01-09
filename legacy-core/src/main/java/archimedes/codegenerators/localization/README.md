# Localization Code Generator
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
``