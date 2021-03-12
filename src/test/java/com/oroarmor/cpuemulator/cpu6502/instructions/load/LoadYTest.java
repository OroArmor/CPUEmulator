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

import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.Bus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadYTest {
    private CPU6502 cpu;
    private Bus bus;

    private static void checkYAndFlags(byte expectedY, byte expectedFlags, CPU6502 cpu, String ldaType) {
        assertEquals(expectedY, cpu.getYRegister(), String.format("LDY %s sets correct value, %d", ldaType, expectedY));
        StringBuilder expectedFlagsString = new StringBuilder(Integer.toBinaryString(expectedFlags));
        int missing = 8 - expectedFlagsString.length();
        while (missing > 0) {
            expectedFlagsString.insert(0, "0");
            missing--;
        }
        assertEquals(expectedFlags, cpu.getFlags().toByte(), String.format("LDY %s sets Flags (%s) correctly with %d", ldaType, "0b" + expectedFlagsString, expectedY));
    }

    @BeforeEach
    public void reset() {
        cpu = new CPU6502();
        bus = new Bus();
    }

    private void testLoadY(CPU6502 cpu, Bus bus, byte value, int location, CPU6502Instructions instruction, String name, int cycles) {
        bus.setByte(0xFFFC, instruction.getCode());
        bus.setByte(location, value);
        byte flags = cpu.getFlags().toByte();

        for (int i = 0; i < cycles; i++) {
            cpu.tick(bus);
        }

        checkYAndFlags(value, (byte) (flags | ((value < 0) ? 0b10000000 : 0) | ((value == 0) ? 0b00000010 : 0)), cpu, name);
        cpu.reset();
    }

    @Test
    void loadYImmediate() {
        testLoadY(cpu, bus, (byte) 0x80, 0xFFFD, CPU6502Instructions.LDY_IMM, "immediate with negative number", 2);
        testLoadY(cpu, bus, (byte) 0x00, 0xFFFD, CPU6502Instructions.LDY_IMM, "immediate with zero value", 2);
    }

    @Test
    void loadYZeroPage() {
        bus.setByte(0xFFFD, (byte) 0x10);
        testLoadY(cpu, bus, (byte) 0x80, 0x0010, CPU6502Instructions.LDY_ZP, "zero page with negative number", 3);
        testLoadY(cpu, bus, (byte) 0x00, 0x0010, CPU6502Instructions.LDY_ZP, "zero page with zero value", 3);
    }

    @Test
    void loadYZeroPageX() {
        cpu.setXRegister((byte) 0x10);
        bus.setByte(0xFFFD, (byte) 0x10);
        testLoadY(cpu, bus, (byte) 0x80, 0x0020, CPU6502Instructions.LDY_ZPY, "zero page with x with negative number", 4);
        testLoadY(cpu, bus, (byte) 0x00, 0x0020, CPU6502Instructions.LDY_ZPY, "zero page with x with zero value", 4);
    }

    @Test
    void loadYAbsolute() {
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        testLoadY(cpu, bus, (byte) 0x80, 0xABCD, CPU6502Instructions.LDY_ABS, "absolute with negative number", 4);
        testLoadY(cpu, bus, (byte) 0x00, 0xABCD, CPU6502Instructions.LDY_ABS, "absolute with zero value", 4);
    }

    @Test
    void loadYAbsoluteY() {
        cpu.setXRegister((byte) 0x1);
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        testLoadY(cpu, bus, (byte) 0x80, 0xABCE, CPU6502Instructions.LDY_ABSX, "absolute with x with negative number", 4);

        cpu.setXRegister((byte) (0xAC00 - 0xABCD));
        testLoadY(cpu, bus, (byte) 0x00, 0xAC00, CPU6502Instructions.LDY_ABSX, "absolute with x wrapping with zero value", 5);
    }
}