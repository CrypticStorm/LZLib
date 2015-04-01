package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class SQLQueryPredicate extends SQLQuery<Boolean> {

    private final Predicate<? super ResultSet> predicate;

    public SQLQueryPredicate(String query, SQLPredicate predicate) {
        super(query);
        this.predicate = predicate;
    }

    public Predicate<? super ResultSet> getPredicate() {
        return this.predicate;
    }

    @Override
    public Boolean apply(ResultSet resultSet) {
        return this.predicate.test(resultSet);
    }
}
