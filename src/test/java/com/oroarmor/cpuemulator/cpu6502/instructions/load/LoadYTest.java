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
import com.oroarmor.cpuemulator.cpu6502.Memory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadYTest {
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

    @Test
    void loadYImmediate() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        // ldy #80
        // a0  #80
        memory.setByte(0xFFFC, CPU6502Instructions.LDY_IMM.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "immediate with negative number");

        // ldy #00
        // a0  #00
        cpu.reset();
        flags = cpu.getFlags().toByte();
        memory.setByte(0xFFFD, (byte) 0x00);

        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "immediate with zero value");
    }

    @Test
    void loadYZeroPage() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        // ldy $10
        // a4  10
        memory.setByte(0xFFFC, CPU6502Instructions.LDY_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x10);
        memory.setByte(0x0010, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with negative number");

        memory.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with zero value");
    }

    @Test
    void loadYZeroPageX() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setXRegister((byte) 0x10);

        // ldy $10,x
        // b4  $10
        memory.setByte(0xFFFC, CPU6502Instructions.LDY_ZPY.getCode());
        memory.setByte(0xFFFD, (byte) 0x10);
        memory.setByte(0x0020, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with x with negative number");

        cpu.setXRegister((byte) 0xFF);
        memory.setByte(0xFFFD, (byte) 0x10);
        memory.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with x with zero value");
    }

    @Test
    void loadYAbsolute() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        // ldy $ABCD
        // ac  $ABCD

        memory.setByte(0xFFFC, CPU6502Instructions.LDY_ABS.getCode());
        memory.setByte(0xFFFD, (byte) 0xCD);
        memory.setByte(0xFFFE, (byte) 0xAB);
        memory.setByte(0xABCD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with negative number");

        memory.setByte(0xABCD, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with zero value");
    }

    @Test
    void loadYAbsoluteY() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setXRegister((byte) 0x1);

        // ldy $ABCD, x
        // bc  $ABCD

        memory.setByte(0xFFFC, CPU6502Instructions.LDY_ABSY.getCode());
        memory.setByte(0xFFFD, (byte) 0xCD);
        memory.setByte(0xFFFE, (byte) 0xAB);
        memory.setByte(0xABCE, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with x with negative number");

        cpu.reset();
        memory.setByte(0xAC00, (byte) 0x00);
        cpu.setXRegister((byte) (0xAC00 - 0xABCD));
        flags = cpu.getFlags().toByte();

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        checkYAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with x wrapping with zero value");
    }
}