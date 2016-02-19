package com.shachr.common.model.binding.inputs;

import com.shachr.common.model.binding.utils.QueryString;

import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 */
public class QueryStringInput extends Input {

    @Override
    public Map<String,Object> read(Object data, Map<String, Object> hash) throws InputException {
        try {
            return QueryString.parse(data.toString(), "UTF-8", hash);
        }catch (Throwable ex){
            throw new InputException("failed to parse query-string input", ex);
        }
    }
}
