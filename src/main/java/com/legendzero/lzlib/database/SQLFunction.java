package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface SQLFunction<T> extends Function<ResultSet, T> {

    T applySilent(ResultSet resultSet) throws SQLException;

    @Override
    default T apply(ResultSet resultSet) {
        try {
            return applySilent(resultSet);
        } catch (SQLException e) {
            Logger.getLogger("SQLFunction").log(Level.WARNING, "Error in SQLFunction", e);
        }
        return null;
    }

    default SQLFunction<T> firstRow() {
        return rs -> {
            rs.first();
            return SQLFunction.this.applySilent(rs);
        };
    }

    default SQLFunction<T> firstRowOrDefault(T def) {
        return rs -> {
            if (rs.first()) {
                return SQLFunction.this.applySilent(rs);
            } else {
                return def;
            }
        };
    }

    default <R extends Collection<T>> SQLFunction<R> byRow(R collection) {
        return rs -> {
            while (rs.next()) {
                collection.add(SQLFunction.this.applySilent(rs));
            }
            return collection;
        };
    }

    default <R extends Collection<T>> SQLFunction<R> byRow(Collector<T, ?, R> collector) {
        return rs -> {
            Stream.Builder<T> builder = Stream.builder();
            while (rs.next()) {
                builder.add(SQLFunction.this.applySilent(rs));
            }
            return builder.build().collect(collector);
        };
    }
}
