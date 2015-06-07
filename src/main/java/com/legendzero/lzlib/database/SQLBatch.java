package com.legendzero.lzlib.database;

import com.legendzero.lzlib.data.SQLData;

import java.util.Iterator;
import java.util.function.Function;

public class SQLBatch<T> extends SQLStatement {

    private final Function<? super T, Object>[] mappingFunctions;

    public SQLBatch(String update, Function<? super T, Object>... mappingFunctions) {
        super(update);
        this.mappingFunctions = mappingFunctions;
    }

    public SQLBatch(SQLStatement sqlStatement, Function<? super T, Object>... mappingFunctions) {
        super(sqlStatement);
        this.mappingFunctions = mappingFunctions;
    }

    public Function<? super T, Object>[] getMappingFunctions() {
        return this.mappingFunctions;
    }

    public final int[] batch(SQLData<?> data, Iterable<T> iterable) {
        return data.batch(this, iterable);
    }

    public final int[] batch(SQLData<?> data, Iterator<T> iterator) {
        return data.batch(this, iterator);
    }

    public final int[] batch(SQLData<?> data, T... values) {
        return data.batch(this, values);
    }
}
