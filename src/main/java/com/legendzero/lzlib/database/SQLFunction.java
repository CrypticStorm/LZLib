package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}