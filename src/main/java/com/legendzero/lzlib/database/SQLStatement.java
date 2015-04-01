package com.legendzero.lzlib.database;

public abstract class SQLStatement {

    private final String statement;

    public SQLStatement(String statement) {
        this.statement = statement;
    }

    public SQLStatement(SQLStatement sqlStatement) {
        this(sqlStatement.getStatement());
    }

    public final String getStatement() {
        return this.statement;
    }
}
