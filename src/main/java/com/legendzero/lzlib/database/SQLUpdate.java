package com.legendzero.lzlib.database;

import com.legendzero.lzlib.data.SQLData;

import java.sql.ResultSet;
import java.util.function.Consumer;

public class SQLUpdate extends SQLStatement {

    public SQLUpdate(String update) {
        super(update);
    }

    public SQLUpdate(SQLStatement sqlStatement) {
        super(sqlStatement);
    }

    public final Integer update(SQLData<?> data, Object... mapping) {
        return data.update(this, mapping);
    }
}
