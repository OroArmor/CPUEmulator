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

package com.oroarmor.cpuemulator.cpu6502.instructions.load;

import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import org.junit.jupiter.api.Test;

class LoadYTest extends LoadTestHelpers {

    protected LoadYTest() {
        super(() -> cpu.getYRegister(), "LDY");
    }

    @Test
    void loadYImmediate() {
        testLoadValue(cpu, bus, (byte) 0x80, 0xFFFD, CPU6502Instructions.LDY_IMM, "immediate with negative number", 2);
        testLoadValue(cpu, bus, (byte) 0x00, 0xFFFD, CPU6502Instructions.LDY_IMM, "immediate with zero value", 2);
    }

    @Test
    void loadYZeroPage() {
        bus.writeByte(0xFFFD, (byte) 0x10);
        testLoadValue(cpu, bus, (byte) 0x80, 0x0010, CPU6502Instructions.LDY_ZP, "zero page with negative number", 3);
        testLoadValue(cpu, bus, (byte) 0x00, 0x0010, CPU6502Instructions.LDY_ZP, "zero page with zero value", 3);
    }

    @Test
    void loadYZeroPageX() {
        cpu.setXRegister((byte) 0x10);
        bus.writeByte(0xFFFD, (byte) 0x10);
        testLoadValue(cpu, bus, (byte) 0x80, 0x0020, CPU6502Instructions.LDY_ZPX, "zero page with x with negative number", 4);
        testLoadValue(cpu, bus, (byte) 0x00, 0x0020, CPU6502Instructions.LDY_ZPX, "zero page with x with zero value", 4);
    }

    @Test
    void loadYAbsolute() {
        bus.writeByte(0xFFFD, (byte) 0xCD);
        bus.writeByte(0xFFFE, (byte) 0xAB);
        testLoadValue(cpu, bus, (byte) 0x80, 0xABCD, CPU6502Instructions.LDY_ABS, "absolute with negative number", 4);
        testLoadValue(cpu, bus, (byte) 0x00, 0xABCD, CPU6502Instructions.LDY_ABS, "absolute with zero value", 4);
    }

    @Test
    void loadYAbsoluteY() {
        cpu.setXRegister((byte) 0x1);
        bus.writeByte(0xFFFD, (byte) 0xCD);
        bus.writeByte(0xFFFE, (byte) 0xAB);
        testLoadValue(cpu, bus, (byte) 0x80, 0xABCE, CPU6502Instructions.LDY_ABSX, "absolute with x with negative number", 4);

        cpu.setXRegister((byte) (0xAC00 - 0xABCD));
        testLoadValue(cpu, bus, (byte) 0x00, 0xAC00, CPU6502Instructions.LDY_ABSX, "absolute with x wrapping with zero value", 5);
    }
}