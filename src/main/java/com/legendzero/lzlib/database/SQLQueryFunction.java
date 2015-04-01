package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.util.function.Function;

public class SQLQueryFunction<T> extends SQLQuery<T> {

    private final Function<? super ResultSet, ? extends T> function;

    public SQLQueryFunction(String query, SQLFunction<T> function) {
        super(query);
        this.function = function;
    }

    public Function<? super ResultSet, ? extends T> getFunction() {
        return this.function;
    }

    @Override
    public T apply(ResultSet resultSet) {
        return this.function.apply(resultSet);
    }
}
