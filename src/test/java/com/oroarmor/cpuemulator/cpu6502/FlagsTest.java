/*
 * MIT License
 *
 * Copyright (c) 2021 Eli Orona
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.cpuemulator.cpu6502;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlagsTest {
    @Test
    public void testFromByte() {
        Flags f = new Flags();
        f.fromByte((byte) 0b10100001);
        assertEquals(new Flags(true, false, false, false, false, false, true), f, "From byte is correct");
    }

    @Test
    public void testToByte() {
        Flags f = new Flags(true, false, false, false, false, false, true);
        byte b = f.toByte();
        assertEquals((byte) 0b10100001, b, "To byte is correct");
    }

    @Test
    public void testSetFlag() {
        Flags f = new Flags();
        f.setFlag((byte) 0, true);
        assertEquals((byte) 0b00100001, f.toByte(), "Correct bit is set");
        assertThrows(IllegalArgumentException.class, () -> f.setFlag((byte) 8, true), "Set flag throws exception when bit is >7");
        assertThrows(IllegalArgumentException.class, () -> f.setFlag((byte) -1, true), "Set flag throws exception when bit is <0");
    }
}