package com.shachr.common.model.binding.inputs.abstraction;

import com.shachr.common.model.binding.inputs.InputException;

import java.util.Map;

/**
 * Created by shachar on 20/02/2016.
 */
public interface InputWithOneArgument<T,A> extends Input {
    Map<String,Object> read(Map<String, Object> hash, T data,  A arg1) throws InputException;
}
