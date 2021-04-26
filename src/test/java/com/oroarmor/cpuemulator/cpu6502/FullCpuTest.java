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

import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class FullCpuTest {
    @Test
    public void testFullCPU() {
        CPU6502 cpu = new CPU6502();
        TestBus bus = new TestBus();
        InputStream testFile = FullCpuTest.class.getClassLoader().getResourceAsStream("com/oroarmor/cpuemulator/cpu6502/nes_test_file.bin");
        int location = 0;
        try {
            while (testFile.available() > 0) {
                bus.writeByte(location++, (byte) testFile.read());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cpu.reset();
    }
}
