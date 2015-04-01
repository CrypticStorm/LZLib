package com.legendzero.lzlib.database;

import com.legendzero.lzlib.data.SQLData;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;

public abstract class SQLQuery<T> extends SQLStatement implements Function<ResultSet, T> {

    public SQLQuery(String query) {
        super(query);
    }

    public SQLQuery(SQLStatement sqlStatement) {
        super(sqlStatement);
    }

    public final T query(SQLData<?> data, Object... mapping) {
        return data.query(this, mapping);
    }

    public final <R extends Collection<T>> R queryByRow(SQLData<?> data, Collector<T, ?, R> collector, Object... mapping) {
        return data.queryByRow(this, collector, mapping);
    }

    public final void queryByRow(SQLData<?> data, Consumer<? super T> consumer, Object... mapping) {
        data.queryByRow(this, consumer, mapping);
    }
}
