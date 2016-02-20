package com.shachr.common.model.binding.inputs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.avro.Schema;
import scala.Tuple2;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by shachar on 17/02/2016.
 */
public class AvroInput extends Input{
    @Override
    public Map<String, Object> read(Object data, Map<String, Object> hash) throws InputException {

        // https://github.com/FasterXML/jackson-dataformat-avro

        Tuple2<Schema,byte[]> tuple = (Tuple2<Schema,byte[]>)data;
        AvroSchema schema = new AvroSchema(tuple._1());

        byte[] bytes = tuple._2();
        ObjectMapper mapper = new ObjectMapper(new AvroFactory());
        try {
            ObjectReader reader = mapper.readerForUpdating(hash);
            reader.with(schema).readValue(bytes);
        } catch (IOException e) {
            throw new InputException("failed to open DataFileReader", e);
        }

        return hash;
    }
}
