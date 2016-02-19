package com.shachr.common.model.binding.inputs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.avro.Schema;
import scala.Tuple2;

import java.io.IOException;
import java.util.Map;

/**
 * Created by shachar on 17/02/2016.
 */
public class AvroInput extends Input{
    @Override
    public Map<String, Object> read(Object data, Map<String, Object> hash) throws InputException {

        Tuple2<Schema,byte[]> tuple = (Tuple2<Schema,byte[]>)data;
        AvroSchema schema = new AvroSchema(tuple._1());
        byte[] bytes = tuple._2();

        ObjectMapper mapper = new ObjectMapper(new AvroFactory());

        // TODO:
        // https://github.com/FasterXML/jackson-dataformat-avro


        try {
            ObjectReader reader = mapper.readerForUpdating(hash);
            reader.with(schema).readValue(bytes);
//            GenericDatumReader<GenericRecord> reader = new GenericDatumReader(schema);
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
//
//            GenericRecord record = null;
//            while(!decoder.isEnd()){
//                record = reader.read(record, decoder);
//                System.out.println("Name " + record.get("name") + " Age " + record.get("age"));
//            }

        } catch (IOException e) {
            throw new InputException("failed to open DataFileReader", e);
        }

        return hash;
    }
}
