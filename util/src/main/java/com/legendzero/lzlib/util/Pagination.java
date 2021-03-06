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

package com.legendzero.lzlib.util;

import com.google.common.collect.Lists;

import java.util.List;

public final class Pagination {
    
    private Pagination() {}

    public static <T> List<T> paginate(List<T> objects, int page, int items) {
        int min = page * items;
        int max = Math.min(min + items, objects.size());
        return objects.subList(min, max);
    }
    
    public static <T> List<List<T>> paginateAll(List<T> objects, int items) {
        List<List<T>> pages = Lists.newArrayList();
        for (int i = 0; i < getTotalPages(objects, items); i++) {
            pages.add(paginate(objects, i, items));
        }
        return pages;
    }
    
    public static int getTotalPages(List<?> objects, int items) {
        return (objects.size() + (items - 1)) / items;
    }
    
}
