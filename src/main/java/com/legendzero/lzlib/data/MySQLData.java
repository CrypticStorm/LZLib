package com.legendzero.lzlib.data;

import com.legendzero.lzlib.database.MySQL;

public class MySQLData implements SQLData<MySQL> {

    private final MySQL mySQL;

    public MySQLData(MySQL mySQL) {
        this.mySQL = mySQL;
    }

    @Override
    public MySQL getStorage() {
        return this.mySQL;
    }
}
