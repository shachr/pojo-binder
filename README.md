# POJO Binder
Self-contained binder that will bind anything to a pojo.


### Version
1.0.1

## Usage
```
PojoBinder binder = new PojoBinder();

String qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
binder.read(QueryString.class, qs);

String json = "{"user": { "info": { "hobbies": ["soccer","playstation"]} }  }";
binder.read(Json.class, json);


Schema schema = getSchema();
byte[] bytes = getBytesFromSomewhere();
binder.read(Avro.class, bytes, schema);


AddUserRequest model = binder.bind(AddUserRequest.class);
```

License
----

MIT
