package com.shachr.common.model.binding.inputs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.shachr.common.model.binding.inputs.abstraction.InputWithOneArgument;
import org.apache.avro.Schema;

import java.io.IOException;
import java.util.Map;

/**
 * Created by shachar on 17/02/2016.
 */
public class Avro implements InputWithOneArgument<byte[],Schema> {

    public Map<String, Object> read(Map<String, Object> hash, byte[] data, Schema schema) throws InputException {

        // https://github.com/FasterXML/jackson-dataformat-avro
        AvroSchema avroSchema = new AvroSchema(schema);
        ObjectMapper mapper = new ObjectMapper(new AvroFactory());
        try {
            ObjectReader reader = mapper.readerForUpdating(hash);
            reader.with(avroSchema).readValue(data);
        } catch (IOException e) {
            throw new InputException("failed to open DataFileReader", e);
        }

        return hash;
    }
}
