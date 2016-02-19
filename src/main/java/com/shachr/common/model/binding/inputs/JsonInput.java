package com.shachr.common.model.binding.inputs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 */
public class JsonInput extends Input {

    private final static ObjectMapper _mapper;
    static{
        JsonFactory factory = new JsonFactory();
        _mapper = new ObjectMapper(factory);
    }

    @Override
    public Map<String, Object> read(Object data, Map<String, Object> hash) throws InputException {

        try {
            ObjectReader reader = _mapper.readerForUpdating(hash);
            return reader.readValue((String)data);
            //TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>(){};
            //return _mapper.readValue((String)data, typeRef);
        } catch (Throwable e) {
            throw new InputException("failed to parse json string", e);
        }
    }
}
