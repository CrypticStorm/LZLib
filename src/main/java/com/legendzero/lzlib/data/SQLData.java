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

package com.legendzero.lzlib.data;

import com.legendzero.lzlib.database.Database;
import com.legendzero.lzlib.database.SQLBatch;
import com.legendzero.lzlib.database.SQLQuery;
import com.legendzero.lzlib.database.SQLUpdate;

import java.util.Collection;
import java.util.Iterator;

public interface SQLData<E extends Database> extends Data<E> {

    default String getIdentifier() {
        return this.getStorage().getIdentifier();
    }

    default void setIdentifier(String identifier) {
        this.getStorage().setIdentifier(identifier);
    }

    default boolean save() {
        return this.getStorage().closeConnection();
    }

    default <T> T query(SQLQuery<T> query, Object... mapping) {
        return this.getStorage().query(query, mapping);
    }

    default Integer update(SQLUpdate update, Object... mapping) {
        return this.getStorage().update(update, mapping);
    }

    default <T> int[] batch(SQLBatch<T> batch, Iterable<T> iterable) {
        return this.getStorage().batch(batch, iterable);
    }

    default <T> int[] batch(SQLBatch<T> batch, Iterator<T> iterator) {
        return this.getStorage().batch(batch, iterator);
    }

    default <T> int[] batch(SQLBatch<T> batch, T... values) {
        return this.getStorage().batch(batch, values);
    }
}
