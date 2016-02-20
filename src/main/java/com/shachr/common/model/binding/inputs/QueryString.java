package com.shachr.common.model.binding.inputs;

import com.shachr.common.model.binding.inputs.abstraction.InputWithNoArguments;
import com.shachr.common.model.binding.utils.QueryStringParser;;

import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 */
public class QueryString implements InputWithNoArguments<String> {

    @Override
    public Map<String,Object> read(Map<String, Object> hash, String data) throws InputException {
        try {
            return QueryStringParser.parse(data.toString(), "UTF-8", hash);
        }catch (Throwable ex){
            throw new InputException("failed to parse query-string input", ex);
        }
    }
}
