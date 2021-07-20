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