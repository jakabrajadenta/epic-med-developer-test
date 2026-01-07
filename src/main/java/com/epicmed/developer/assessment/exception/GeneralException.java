package com.epicmed.developer.assessment.exception;

/**
 * General exception as parent for detail exception.
 * Default RC 05 - Do not honor
 */
public class GeneralException extends RuntimeException {

    public GeneralException(){
        super();
    }

    public GeneralException(String message){
        super(message);
    }

    public GeneralException(Throwable throwable){
        super(throwable);
    }

    public GeneralException(String message, Throwable throwable){
        super(message,throwable);
    }
}
