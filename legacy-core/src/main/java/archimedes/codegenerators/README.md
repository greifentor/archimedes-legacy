# Codegenerators

## Options

### Model

#### ALTERNATE_FK_NAME

Allows to define an alternate foreign key name pattern as parameter of the option.
In this pattern placeholders are allowed as descriped below:

* **${BaseColumnName}** The name of the column which is referencing.
* **${BaseTableName}** The name of the table which is referencing.
* **${RefColumnName}** The name of the column referenced by the foreign key.
* **${RefTableName}** The name of the table whose column is referenced by the foreign key.

All names are taken from the data model directly.

For example a foreign key pointing from "Team.Company_Id" to "Company.Id" with option
```ALTERNATE_FK_NAME:FK_${BaseTableName}_${BaseColumnName}_TO_${RefTableName}_${RefColumnName}``` would create 
a foreign key as shown below:

```
FK_Team_Company_Id_TO_Company_Id
```

#### ALTERNATE_MODULE_PREFIX

Allows to change the module prefix (working with MODULE_MODE only). Default is the application name as configured by 
"Bearbeiten > Diagrammparameter > Codegenerator > Applikationsname".

Let the option parameter empty to have no prefix.

Change default module names can be done in the specific module by ``ALTERNATE_..._MODULE_PREFIX`` options.


##### MODULE_MODE

Set this option to get different modules for service other layers (e. g. GUI). If not set, all code will be written to
the ``src`` folder of the configured by "Bearbeiten > Diagrammparameter > Codegenerator > Code-Basis-Pfad".


#### JAVAX_PACKAGE_NAME

Since some of the "javax" package contents have been moved to "jakarta" package, it is necessary to respect this changes 
in the generated code also. Via this option, the "javax" package prefix could be changed to another prefix
like "jakarta".

```JAVA_PACKAGE_NAME:jakarta```
