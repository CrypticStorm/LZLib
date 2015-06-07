package com.legendzero.lzlib.database;

import com.legendzero.lzlib.data.SQLData;

import java.sql.ResultSet;
import java.util.function.Function;

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
}
