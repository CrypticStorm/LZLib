package com.legendzero.lzlib.database;

import com.legendzero.lzlib.data.SQLData;

import java.util.Collection;
import java.util.function.Function;

public class SQLBatch extends SQLStatement {

    public SQLBatch(String update) {
        super(update);
    }

    public SQLBatch(SQLStatement sqlStatement) {
        super(sqlStatement);
    }

    public final <T> int[] batch(SQLData<?> data, Collection<T> collection, Function<? super T, Object>... mapping) {
        return data.batch(this, collection, mapping);
    }
}
