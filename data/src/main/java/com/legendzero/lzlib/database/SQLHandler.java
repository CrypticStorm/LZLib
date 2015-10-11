/*
 * Copyright (c) 2015 Legend Zero LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.legendzero.lzlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface SQLHandler<T> extends Function<ResultSet, T> {

    T applySilent(ResultSet resultSet) throws SQLException;

    @Override
    default T apply(ResultSet resultSet) {
        try {
            return applySilent(resultSet);
        } catch (SQLException e) {
            Logger.getLogger("SQLHandler").log(Level.WARNING, "Error in SQLFunction", e);
        }
        return null;
    }

    static <T> SQLHandler<T> first(SQLHandler<T> sqlHandler) {
        return rs -> {
            rs.first();
            return sqlHandler.applySilent(rs);
        };
    }

    static <T> SQLHandler<T> first(SQLHandler<T> sqlHandler, T def) {
        return rs -> {
            if (rs.first()) {
                return sqlHandler.applySilent(rs);
            } else {
                return def;
            }
        };
    }

    static <T, R extends Collection<T>> SQLHandler<R> byRow(SQLHandler<T> sqlHandler, R collection) {
        return rs -> {
            while (rs.next()) {
                collection.add(sqlHandler.applySilent(rs));
            }
            return collection;
        };
    }

    static <T, R> SQLHandler<R> byRow(SQLHandler<T> sqlHandler, Collector<? super T, ?, R> collector) {
        return rs -> {
            Stream.Builder<T> builder = Stream.builder();
            while (rs.next()) {
                builder.accept(sqlHandler.applySilent(rs));
            }
            return builder.build().collect(collector);
        };
    }

    static SQLHandler<Boolean> entryExists() {
        return ResultSet::first;
    }
}
