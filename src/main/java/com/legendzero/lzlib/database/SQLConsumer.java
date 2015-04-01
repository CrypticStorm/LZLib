package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface SQLConsumer extends Consumer<ResultSet> {

    void acceptSilent(ResultSet resultSet) throws SQLException;

    @Override
    default void accept(ResultSet resultSet) {
        try {
            acceptSilent(resultSet);
        } catch (SQLException e) {
            Logger.getLogger("SQLConsumer").log(Level.WARNING, "Error in SQLConsumer", e);
        }
    }
}
