package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class SQLQueryFunction<T> extends SQLQuery<T> {

    private final SQLFunction<T> function;

    public SQLQueryFunction(String query, SQLFunction<T> function) {
        super(query);
        this.function = function;
    }

    public SQLFunction<T> getFunction() {
        return this.function;
    }

    @Override
    public T apply(ResultSet resultSet) {
        return this.function.apply(resultSet);
    }

    public <R extends Collection<T>> SQLQueryFunction<R> byRow(R collection) {
        return new SQLQueryFunction<>(this.getStatement(), this.getFunction().byRow(collection));
    }

    public <R extends Collection<T>> SQLQueryFunction<R> byRow(Collector<T, ?, R> collector) {
        return new SQLQueryFunction<>(this.getStatement(), this.getFunction().byRow(collector));
    }
}
