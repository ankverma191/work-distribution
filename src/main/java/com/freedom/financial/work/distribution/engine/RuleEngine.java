package com.freedom.financial.work.distribution.engine;

@FunctionalInterface
public interface RuleEngine<T , E extends Exception , R> {
    R execute(T t) throws E;
}
