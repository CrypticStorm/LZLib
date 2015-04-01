package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.util.function.Consumer;

public class SQLQueryConsumer extends SQLQuery<Void> {

    private final Consumer<? super ResultSet> consumer;

    public SQLQueryConsumer(String query, SQLConsumer consumer) {
        super(query);
        this.consumer = consumer;
    }

    public Consumer<? super ResultSet> getConsumer() {
        return this.consumer;
    }

    @Override
    public Void apply(ResultSet resultSet) {
        this.consumer.accept(resultSet);
        return null;
    }
}
