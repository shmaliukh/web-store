package com.vshmaliukh.webstore.services;

public interface EntityValidator<T> {

    default boolean isValidEntity(T entity){
        return entity != null;
    }

}
