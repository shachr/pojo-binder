/**
 * Created by shachar on 14/02/2016.
 */

import com.shachr.common.model.binding.PojoBinder;
import com.shachr.common.model.binding.BindingContext;
import com.shachr.common.model.binding.annotations.Body;
import com.shachr.common.model.binding.annotations.Header;
import com.shachr.common.model.binding.inputs.Avro;
import com.shachr.common.model.binding.inputs.InputException;
import com.shachr.common.model.binding.inputs.Json;
import com.shachr.common.model.binding.inputs.QueryString;
import models.RequestModel;
import models.User;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.junit.Assert;
import org.junit.Test;
import utils.PerfTimer;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PojoBinderTest {

    @Test
    public void test_qs_simple_model_binder() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {
        PojoBinder binder = new PojoBinder();
        Object qs = "user[name]=shachar&user[age]=32&user[info][hobbies][0]=basketball&user[info][hobbies][1]=xbox";
        binder.read(QueryString.class, qs);
        RequestModel model = binder.bind(RequestModel.class);

        Assert.assertNotNull(model.user);
        Assert.assertEquals(model.user.name, "shachar");
        Assert.assertEquals(model.user.age,32);
        Assert.assertNotNull(model.user.info);
        Assert.assertNotNull(model.user.info.hobbies);
        Assert.assertArrayEquals(model.user.info.hobbies.toArray(),new String[]{"basketball","xbox"});
    }

    @Test
    public void test_validation() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {
        PojoBinder binder = new PojoBinder();
        Object qs = "user[age]=32";
        binder.read(QueryString.class, qs);
        RequestModel model = binder.bind(RequestModel.class);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<RequestModel>> violations = validator.validate(model);
        Assert.assertTrue(violations.size()>0);
    }

    @Test
    public void test_qs_complicated_model_binder() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {
        PojoBinder binder = new PojoBinder();
        String qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
        binder.read(QueryString.class, qs);
        RequestModel model = binder.bind(RequestModel.class);
        Assert.assertNotNull(model.user);
        Assert.assertEquals(model.user.name, "shachar");
        Assert.assertEquals(model.user.age,32);
        Assert.assertNotNull(model.user.infos);
        Assert.assertNotNull(model.user.infos[0].hobbies);
        Assert.assertArrayEquals(model.user.infos[0].hobbies.toArray(),new String[]{"basketball","xbox"});
    }

    @Test
    public void test_json_simple_model_binder() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {

        PojoBinder binder = new PojoBinder();
        String json = "{\"user\": { \"name\": \"shachar\", \"age\": 32, \"info\": { \"hobbies\": [\"basketball\",\"xbox\"]} } }";
        binder.read(Json.class, json);
        RequestModel model = binder.bind(RequestModel.class);
        Assert.assertNotNull(model.user);
        Assert.assertEquals(model.user.name, "shachar");
        Assert.assertEquals(model.user.age,32);
        Assert.assertNotNull(model.user.info);
        Assert.assertNotNull(model.user.info.hobbies);
        Assert.assertArrayEquals(model.user.info.hobbies.toArray(),new String[]{"basketball","xbox"});
    }


    @Test
    public void test_json_complicated_model_binder() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {
        PojoBinder binder = new PojoBinder();
        String json = "{\"user\": { \"name\": \"shachar\", \"age\": 32, \"infos\": [{ \"hobbies\": [\"basketball\",\"xbox\"]}  ] } }";
        binder.read(Json.class, json);
        RequestModel model = binder.bind(RequestModel.class);
        Assert.assertNotNull(model.user);
        Assert.assertEquals(model.user.name, "shachar");
        Assert.assertEquals(model.user.age,32);
        Assert.assertNotNull(model.user.infos);
        Assert.assertNotNull(model.user.infos[0].hobbies);
        Assert.assertArrayEquals(model.user.infos[0].hobbies.toArray(),new String[]{"basketball","xbox"});
    }


    @Test
    public void test_multiple_inputs_model_binder() throws InputException, ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {

        PojoBinder binder = new PojoBinder();
        String json = "{\"user\": { \"info\": { \"hobbies\": [\"soccer\",\"playstation\"]} }  }";
        binder.read(Json.class, json);

        String qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
        binder.read(QueryString.class, qs);

        RequestModel model = binder.bind(RequestModel.class);

        Assert.assertNotNull(model.user);
        Assert.assertEquals(model.user.name, "shachar");
        Assert.assertEquals(model.user.age,32);
        Assert.assertNotNull(model.user.info);
        Assert.assertNotNull(model.user.info.hobbies);
        Assert.assertArrayEquals(model.user.info.hobbies.toArray(),new String[]{"soccer","playstation"});
        Assert.assertNotNull(model.user.infos);
        Assert.assertNotNull(model.user.infos[0].hobbies);
        Assert.assertArrayEquals(model.user.infos[0].hobbies.toArray(),new String[]{"basketball","xbox"});
    }


    @Test
    public void test_multiple_inputs_with_header_model_binder() throws InputException, ClassNotFoundException, IOException {

        Object qs;
        Object json;
        RequestModel model = new RequestModel();

        PerfTimer timer = new PerfTimer();
        for(int inx = 0;inx<10000;inx++) {
            timer.start();

            BindingContext binderContext = new BindingContext();
            PojoBinder dataBinder = new PojoBinder(binderContext, com.shachr.common.model.binding.annotations.QueryString.class, Body.class);
            PojoBinder headerBinder = new PojoBinder(binderContext, Header.class);

            qs = "token=origamilogic";
            headerBinder.read(QueryString.class, qs);
            model = headerBinder.bind(model);

            json = "{\"user\": { \"info\": { \"hobbies\": [\"soccer\",\"playstation\"]} }, \"token\": \"123\"  }";
            dataBinder.read(Json.class, json);

            qs = "user[name]=shachar&user[age]=32&user[infos][0][hobbies][0]=basketball&user[infos][0][hobbies][1]=xbox";
            dataBinder.read(QueryString.class, qs);
            model = dataBinder.bind(model);
            timer.end();
        }

        timer.print();
        int avgTime = timer.getAvgTime();

        Assert.assertTrue(avgTime<100);
        Assert.assertEquals("origamilogic", model.token);
        Assert.assertNotNull(model.user);
        Assert.assertEquals("shachar", model.user.name);
        Assert.assertEquals(32, model.user.age);
        Assert.assertNotNull(model.user.info);
        Assert.assertNotNull(model.user.info.hobbies);
        Assert.assertArrayEquals(new String[]{"soccer","playstation"}, model.user.info.hobbies.toArray());
        Assert.assertNotNull(model.user.infos);
        Assert.assertNotNull(model.user.infos[0].hobbies);
        Assert.assertArrayEquals(new String[]{"basketball","xbox"}, model.user.infos[0].hobbies.toArray());
    }

    @Test
    public void test_avro_input_model_binder() throws InputException, IOException {
        Schema schema = makeSchema();
        byte[] bytes = avroWrite(schema);
        User model = new User();
        PojoBinder binder = new PojoBinder();

        binder.read(Avro.class, bytes, schema);
        binder.bind(model);
        Assert.assertNotNull(model.name);
        Assert.assertEquals("Person A", model.name);
        Assert.assertEquals(23, model.age);

//        avroRead(schema, bytes);
    }

    public static Schema makeSchema() {
        List<Schema.Field> fields = new ArrayList<Schema.Field>();

        fields.add(new Schema.Field("name", Schema.create(Schema.Type.STRING),null,(Object)null));
        fields.add(new Schema.Field("age", Schema.create(Schema.Type.INT),null,(Object)null));

        Schema schema = Schema.createRecord("Person", null, "avro.test", false);
        schema.setFields(fields);

        return schema;
    }


    public static GenericData.Record makeObject (Schema schema, String name, int age) {
        GenericData.Record record = new GenericData.Record(schema);
        record.put("name", name);
        record.put("age", age);
        return record;
    }

    public static byte[] avroWrite (Schema schema) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);

        GenericDatumWriter<GenericData.Record> writer = new GenericDatumWriter(schema);
        writer.write(makeObject(schema, "Person A", 23), encoder);

        encoder.flush();
        byte[] bytes = outputStream.toByteArray();
        outputStream.close();
        return bytes;
    }


    public static void avroRead (Schema schema, byte[] bytes) throws IOException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);

        GenericDatumReader<GenericRecord> reader = new GenericDatumReader(schema);

        GenericRecord record = null;
        while(!decoder.isEnd()){
            record = reader.read(record, decoder);
            System.out.println("Name " + record.get("name") + " Age " + record.get("age"));
        }
    }

}


