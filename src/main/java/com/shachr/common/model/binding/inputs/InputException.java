package com.shachr.common.model.binding.inputs;

public class InputException extends Exception{
    public InputException(String msg, Throwable baseEx){
        super(msg, baseEx);
    }
}
