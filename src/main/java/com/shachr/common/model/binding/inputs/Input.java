package com.shachr.common.model.binding.inputs;

import java.util.Map;

/**
 * Created by shachar on 14/02/2016.
 */
public abstract class Input {
    public abstract Map<String,Object> read(Object data, Map<String, Object> hash) throws InputException;
}

