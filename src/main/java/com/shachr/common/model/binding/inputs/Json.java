package com.shachr.common.model.binding.inputs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.shachr.common.model.binding.inputs.abstraction.InputWithNoArguments;

import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 */
public class Json implements InputWithNoArguments<String> {

    private final static ObjectMapper _mapper;
    static{
        JsonFactory factory = new JsonFactory();
        _mapper = new ObjectMapper(factory);
    }

    @Override
    public Map<String, Object> read(Map<String, Object> hash, String data) throws InputException {

        try {
            ObjectReader reader = _mapper.readerForUpdating(hash);
            return reader.readValue(data);
            //TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>(){};
            //return _mapper.readValue((String)data, typeRef);
        } catch (Throwable e) {
            throw new InputException("failed to parse json string", e);
        }
    }
}
