package com.ram.db;

/**
 * Created by Ravi on 22-Jun-19.
 */
@FunctionalInterface
public interface JpaTransaction<T, R, E extends Throwable> {

    R apply(T t) throws E;

}
