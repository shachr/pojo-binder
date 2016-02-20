# POJO Binder
Self-contained pojo binder that will bind anything to a pojo.


### Version
1.0.0

## Usage
```
POJO pojo = new POJO()
POJOBinder binder = new POJOBinder();

String qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
binder.queryString(qs);

String json = "{"user": { "info": { "hobbies": ["soccer","playstation"]} }  }";
binder.Json(json);

Schema schema = getSchema();
byte[] bytes = getBytesFromSomewhere();
binder.avro(bytes, schema);

binder.update(pojo)
```

License
----

MIT
