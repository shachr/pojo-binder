package com.shachr.common.model.binding;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.shachr.common.model.binding.inputs.*;
import com.shachr.common.model.binding.inputs.abstraction.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PojoBinder {

    final static Map<Class,Input> _inputs;
    static {
        _inputs = new HashMap();
    }

    final ObjectMapper _mapper;
    Map<String,Object> _map;

    public PojoBinder(Class<?>... annotationClass){
        this(new HashMap(), annotationClass);
    }

    public PojoBinder(Map<String,Object> hash, Class<?>... annotationClass){
        assert hash != null;
        assert annotationClass != null;
        BeanDeserializerModifier modifier = new AnnotationBeanDeserializerModifier(false, annotationClass);
        DeserializerFactory dFactory = BeanDeserializerFactory.instance.withDeserializerModifier(modifier);
        _mapper = new ObjectMapper(null, null, new DefaultDeserializationContext.Impl(dFactory));
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _map = hash;
    }

    public PojoBinder(){
        _mapper = new ObjectMapper();
        _mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _map = new HashMap();
    }


    public <T> T bind(Class<T> clazz) throws IOException, IllegalAccessException, InstantiationException {
        T pojo = clazz.newInstance();
        JsonNode jsonNode = _mapper.valueToTree(_map);
        ObjectReader updater = _mapper.readerForUpdating(pojo);
        return updater.readValue(jsonNode);
    }

    public <T> T bind(T object) throws IOException {
        JsonNode jsonNode = _mapper.valueToTree(_map);
        ObjectReader updater = _mapper.readerForUpdating(object);
        return updater.readValue(jsonNode);
    }

    public <T extends InputWithNoArguments, Value > void read(Class<T> clazz, Value value) throws InputException {
        T input = null;
        try {
            if (!_inputs.containsKey(clazz)) {
                input = clazz.newInstance();
                _inputs.put(clazz, input);
            } else
                input = (T) _inputs.get(clazz);

            input.read(_map, value);
        }catch (Throwable ex){
            throw new InputException("failed to read input", ex);
        }
    }

    public <T extends InputWithOneArgument, Value, Arg1> void read(Class<T> clazz, Value value, Arg1 arg1) throws InputException {
        try {
            T input = null;
            if(!_inputs.containsKey(clazz)) {
                input = clazz.newInstance();
                _inputs.put(clazz, input);
            }
            else
                input = (T)_inputs.get(clazz);

            input.read(_map, value, arg1);
        }catch (Throwable ex){
            throw new InputException("failed to read input", ex);
        }
    }

    public <T extends InputWithTwoArguments, Value, Arg1,Arg2> void read(Class<T> clazz, Value value, Arg1 arg1, Arg2 arg2) throws InputException {
        try{
            T input = null;
            if(!_inputs.containsKey(clazz)) {
                input = clazz.newInstance();
                _inputs.put(clazz, input);
            }
            else
                input = (T)_inputs.get(clazz);

            input.read(_map, value, arg1,arg2);
        }catch (Throwable ex){
            throw new InputException("failed to read input", ex);
        }
    }

    public <T extends InputWithThreeArguments, Value, Arg1,Arg2,Arg3> void read(Class<T> clazz, Value value, Arg1 arg1, Arg2 arg2, Arg3 arg3) throws  InputException {
        try{
            T input = null;
            if(!_inputs.containsKey(clazz)) {
                input = clazz.newInstance();
                _inputs.put(clazz, input);
            }
            else
                input = (T)_inputs.get(clazz);

            input.read(_map, value, arg1,arg2, arg3);

        }catch (Throwable ex){
            throw new InputException("failed to read input", ex);
        }
    }

    public <T extends InputWithFourArguments, Value, Arg1, Arg2, Arg3, Arg4> void read(Class<T> clazz, Value value, Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4) throws InputException {
        try{
            T input = null;
            if(!_inputs.containsKey(clazz)) {
                input = clazz.newInstance();
                _inputs.put(clazz, input);
            }
            else
                input = (T)_inputs.get(clazz);

            input.read(_map, value, arg1,arg2, arg3, arg4);
        }catch (Throwable ex){
            throw new InputException("failed to read input", ex);
        }
    }

}

