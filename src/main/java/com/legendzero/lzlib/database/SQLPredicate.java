package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface SQLPredicate extends Predicate<ResultSet> {

    boolean testSilent(ResultSet resultSet) throws SQLException;

    @Override
    default boolean test(ResultSet resultSet) {
        try {
            return testSilent(resultSet);
        } catch (SQLException e) {
            Logger.getLogger("SQLPredicate").log(Level.WARNING, "Error in SQLPredicate", e);
        }
        return false;
    }
}
