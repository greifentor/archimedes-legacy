# Persistence JPA Code Generator
---

## Options

### Columns

#### AUTOINCREMENT

Activates the generation of an autoincrement field as key attribute in the entity class and adds

```
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

to the ``@Id``  field.