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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.util.Vector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Vectors {

    public static Vector fromCSV(String string) {
        return fromCSV(string.split(","));
    }

    public static Vector fromCSV(String[] strings) {
        double x = 0;
        double y = 0;
        double z = 0;
        try {
            switch (strings.length) {
                case 3:
                    z = Double.valueOf(strings[2]);
                case 2:
                    y = Double.valueOf(strings[1]);
                case 1:
                    x = Double.valueOf(strings[0]);
                case 0:
                    break;
                default:

            }
        } catch (NumberFormatException e) {
            return null;
        }
        return new Vector(x, y, z);
    }

    public static String toCSV(Vector vector) {
        return vector.getX() + "," + vector.getY() + "," + vector.getZ();
    }
}
