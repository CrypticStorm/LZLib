package com.legendzero.lzlib.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
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

    public <T> int[] batch(SQLBatch batch, Collection<T> collection, Function<? super T, Object>... mappings) {
        try (PreparedStatement statement = this.prepareStatement(batch.getStatement())) {
            for (T object : collection) {
                Object[] parameters = new Object[mappings.length];

                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = mappings[i].apply(object);
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
