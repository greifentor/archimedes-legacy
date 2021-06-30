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

#### COMMENTS

Values: 
* **Off** - Suppresses the comment generation (CAUTION! This will suppress also overwriting already generated files!).