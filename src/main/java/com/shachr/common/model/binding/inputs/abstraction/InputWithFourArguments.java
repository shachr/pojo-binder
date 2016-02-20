package com.shachr.common.model.binding.inputs.abstraction;

import com.shachr.common.model.binding.inputs.InputException;

import java.util.Map;

/**
 * Created by shachar on 20/02/2016.
 */
public interface InputWithFourArguments<T,A1,A2,A3,A4> extends Input {
    Map<String,Object> read(Map<String, Object> hash, T data, A1 arg1, A2 arg2, A3 arg3, A4 arg4) throws InputException;
}