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

import com.legendzero.lzlib.data.SQLData;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class SQLBatch<T> extends SQLStatement {

    private final List<Function<? super T, Object>> mappingFunctions;

    public SQLBatch(String update, List<Function<? super T, Object>> mappingFunctions) {
        super(update);
        this.mappingFunctions = mappingFunctions;
    }

    public SQLBatch(SQLStatement sqlStatement, List<Function<? super T, Object>> mappingFunctions) {
        super(sqlStatement);
        this.mappingFunctions = mappingFunctions;
    }

    public List<Function<? super T, Object>> getMappingFunctions() {
        return this.mappingFunctions;
    }

    public final int[] batch(SQLData<?> data, Iterable<T> iterable) {
        return data.batch(this, iterable);
    }

    public final int[] batch(SQLData<?> data, Iterator<T> iterator) {
        return data.batch(this, iterator);
    }

    public final int[] batch(SQLData<?> data, T[] values) {
        return data.batch(this, values);
    }
}
