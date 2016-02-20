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

## Advanced Usage
```
BindingContext binderContext = new BindingContext();
PojoBinder queryBinder = new PojoBinder(binderContext, Query.class);
PojoBinder bodyBinder = new PojoBinder(binderContext, Body.class);

String qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
queryBinder.read(QueryString.class, qs);

String json = "{"user": { "info": { "hobbies": ["soccer","playstation"]} }  }";
dataBinder.read(Json.class, json);


AddUserRequest model = queryBinder.bind(AddUserRequest.class);
AddUserRequest model = dataBinder.bind(model);
```

License
----

MIT
