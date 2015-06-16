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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Database {

    protected Connection connection;
    protected Logger logger;

    private String identifier;
    private boolean enabled;

    public Database(String identifier, String driverClass) {
        this.connection = null;
        this.logger = Logger.getLogger(identifier);
        this.identifier = identifier;

        this.enabled = this.checkClass(identifier, driverClass);
    }

    private boolean checkClass(String database, String driverClass) {
        if (driverClass != null) {
            try {
                Class.forName(driverClass);
                return true;
            } catch (ClassNotFoundException e) {
                this.logger.log(Level.SEVERE, "Error loading " + database + " driver.", e);
                return false;
            }
        } else {
            return true;
        }
    }

    public final String getIdentifier() {
        return this.identifier;
    }

    public final void setIdentifier(String identifier) {
        this.logger = Logger.getLogger(identifier);
        this.identifier = identifier;
    }

    public final boolean isEnabled() {
        return this.enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            if (this.enabled) {
                this.closeConnection();
            }
            this.enabled = enabled;
        }
    }

    protected abstract Connection openConnection() throws SQLException;

    protected final Connection getConnection() {
        if (this.enabled) {
            try {
                if (this.connection == null || this.connection.isClosed()) {
                    this.connection = this.openConnection();
                }
            } catch (SQLException e) {
                this.logger.log(Level.SEVERE, "Error opening connection", e);
            }
            return this.connection;
        } else {
            return null;
        }
    }

    public boolean closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                this.logger.log(Level.SEVERE, "Error closing connection", e);
                return false;
            }
        }
        return true;
    }

    protected final PreparedStatement prepareStatement(String query) throws SQLException {
        return this.getConnection().prepareStatement(query);
    }

    protected final PreparedStatement map(PreparedStatement statement, Object[] mapping)
            throws SQLException {
        int index = 1;
        for (Object object : mapping) {
            statement.setObject(index++, object);
        }
        return statement;
    }

    public <T> T query(SQLQuery<T> query, Object... mappings) {
        try (PreparedStatement statement = this.prepareStatement(query.getStatement())) {
            try (ResultSet resultSet = this.map(statement, mappings).executeQuery()) {
                return query.apply(resultSet);
            }
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "Error querying database", e);
        }
        return null;
    }

    public Integer update(SQLUpdate update, Object... mapping) {
        try (PreparedStatement statement = this.prepareStatement(update.getStatement())) {
            return this.map(statement, mapping).executeUpdate();
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "Error querying database", e);
            return -1;
        }
    }

    public <T> int[] batch(SQLBatch<T> batch, Iterable<T> iterable) {
        List<Function<? super T, Object>> mappingFunctions = batch.getMappingFunctions();
        try (PreparedStatement statement = this.prepareStatement(batch.getStatement())) {
            for (T object : iterable) {
                Object[] parameters = new Object[mappingFunctions.size()];

                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = mappingFunctions.get(i).apply(object);
                }
                this.map(statement, parameters).addBatch();
            }
            return statement.executeBatch();
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "Error querying database", e);
            return null;
        }
    }

    public <T> int[] batch(SQLBatch<T> batch, Iterator<T> iterator) {
        List<Function<? super T, Object>> mappingFunctions = batch.getMappingFunctions();
        try (PreparedStatement statement = this.prepareStatement(batch.getStatement())) {
            while (iterator.hasNext()) {
                T object = iterator.next();
                Object[] parameters = new Object[mappingFunctions.size()];

                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = mappingFunctions.get(i).apply(object);
                }
                this.map(statement, parameters).addBatch();
            }
            return statement.executeBatch();
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "Error querying database", e);
            return null;
        }
    }

    public <T> int[] batch(SQLBatch<T> batch, T[] values) {
        List<Function<? super T, Object>> mappingFunctions = batch.getMappingFunctions();
        try (PreparedStatement statement = this.prepareStatement(batch.getStatement())) {
            for (T object : values) {
                Object[] parameters = new Object[mappingFunctions.size()];

                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = mappingFunctions.get(i).apply(object);
                }
                this.map(statement, parameters).addBatch();
            }
            return statement.executeBatch();
        } catch (SQLException e) {
            this.logger.log(Level.WARNING, "Error querying database", e);
            return null;
        }
    }
}
