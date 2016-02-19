package com.shachr.common.model.binding;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.shachr.common.model.binding.inputs.*;
import org.apache.avro.Schema;
import scala.Tuple2;

import java.io.IOException;
import java.util.*;


public class ModelBinder {


    final static Input _jsonInput;
    final static Input _qsInput;
    final static Input _avroInput;
    static {
        _qsInput = new QueryStringInput();
        _jsonInput = new JsonInput();
        _avroInput = new AvroInput();
    }

    final ObjectMapper _mapper;
    Map<String,Object> _map;

    public ModelBinder(Class<?>... annotationClass){
        this(new HashMap(), annotationClass);
    }

    public ModelBinder(Map<String,Object> hash, Class<?>... annotationClass){
        assert hash != null;
        assert annotationClass != null;
        BeanDeserializerModifier modifier = new AnnotationBeanDeserializerModifier(false, annotationClass);
        DeserializerFactory dFactory = BeanDeserializerFactory.instance.withDeserializerModifier(modifier);
        _mapper = new ObjectMapper(null, null, new DefaultDeserializationContext.Impl(dFactory));
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _map = hash;
    }

    public ModelBinder(){
        _mapper = new ObjectMapper();
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _map = new HashMap();
    }


    public void Json(Object json) throws InputException {
        _map = _jsonInput.read(json, _map);
    }

    public void queryString(Object qs) throws InputException {
        _map = _qsInput.read(qs, _map);
    }

    public void avro(Object bytes, Schema schema)throws InputException {
        _map = _avroInput.read(new Tuple2(schema, bytes), _map);
    }

    public <T> T create(Class<T> clazz) throws ClassNotFoundException{
        return _mapper.convertValue(_map, clazz);
    }

    public <T> T update(T object) throws IOException {
        JsonNode jsonNode = _mapper.valueToTree(_map);
        ObjectReader updater = _mapper.readerForUpdating(object);
        return updater.readValue(jsonNode);
    }

    private void _merge(Map<String, Object> destination, Map<String, Object> source) {
        for(Object key : source.keySet()) {
            String key2 = (String) key;
            Object dup = source.get(key2);
            if(dup instanceof HashMap && destination.containsKey(key2)
                    && destination.get(key2) instanceof HashMap) {
                Map<String, Object> kk = (Map<String, Object>) destination.get(key2);
                _merge(kk, (Map<String, Object>) dup);
            } else if(dup instanceof HashMap && destination.containsKey(key2)
                    && !(destination.get(key2) instanceof HashMap)) {
                HashMap kk = new HashMap<String, Object>();
                kk.put(key2, destination.get(key2));
                _merge(kk, (Map<String, Object>) dup);
            } else {
                destination.put(key2, dup);
            }
        }
    }

}

