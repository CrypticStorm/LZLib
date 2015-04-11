package com.legendzero.lzlib.database;

import java.sql.ResultSet;

public class SQLQueryConsumer extends SQLQuery<Void> {

    private final SQLConsumer consumer;

    public SQLQueryConsumer(String query, SQLConsumer consumer) {
        super(query);
        this.consumer = consumer;
    }

    public SQLConsumer getConsumer() {
        return this.consumer;
    }

    @Override
    public Void apply(ResultSet resultSet) {
        this.consumer.accept(resultSet);
        return null;
    }

    public SQLQueryConsumer byRow() {
        return new SQLQueryConsumer(this.getStatement(), this.getConsumer().byRow());
    }
}
