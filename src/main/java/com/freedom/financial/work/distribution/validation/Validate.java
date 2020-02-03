package com.freedom.financial.work.distribution.validation;

@FunctionalInterface
public interface Validate<T , E extends Exception , R> {
    R validate(T t) throws E;
}
