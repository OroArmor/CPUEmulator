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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadAccumulatorTest {
    private static void checkAccumulatorAndFlags(byte expectedAccumulator, byte expectedFlags, CPU6502 cpu, String ldaType) {
        assertEquals(expectedAccumulator, cpu.getAccumulator(), String.format("LDA %s sets correct value, %d", ldaType, expectedAccumulator));
        StringBuilder expectedFlagsString = new StringBuilder(Integer.toBinaryString(expectedFlags));
        int missing = 8 - expectedFlagsString.length();
        while (missing > 0) {
            expectedFlagsString.insert(0, "0");
            missing--;
        }
        assertEquals(expectedFlags, cpu.getFlags().toByte(), String.format("LDA %s sets Flags (%s) correctly with %d", ldaType, "0b" + expectedFlagsString, expectedAccumulator));
    }

    @Test
    void loadAccumulatorImmediate() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // lda #80
        // a9   80
        bus.setByte(0xFFFC, CPU6502Instructions.LDA_IMM.getCode());
        bus.setByte(0xFFFD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "immediate with negative number");

        // lda #00
        // a9   00
        cpu.reset();
        flags = cpu.getFlags().toByte();
        bus.setByte(0xFFFD, (byte) 0x00);

        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "immediate with zero value");
    }

    @Test
    void loadAccumulatorZeroPage() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // lda $10
        // a5   10
        bus.setByte(0xFFFC, CPU6502Instructions.LDA_ZP.getCode());
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0010, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with negative number");

        bus.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with zero value");
    }

    @Test
    void loadAccumulatorZeroPageX() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setXRegister((byte) 0x10);

        // lda $10,x
        // b5   $10
        bus.setByte(0xFFFC, CPU6502Instructions.LDA_ZPX.getCode());
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0020, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with x with negative number");

        cpu.setXRegister((byte) 0xFF);
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with x wrapping with zero value");
    }

    @Test
    void loadAccumulatorAbsolute() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // lda $ABCD
        // ad   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDA_ABS.getCode());
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        bus.setByte(0xABCD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with negative number");

        bus.setByte(0xABCD, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with zero value");
    }

    @Test
    void loadAccumulatorAbsoluteX() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setXRegister((byte) 0x1);

        // lda $ABCD, x
        // bd   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDA_ABSX.getCode());
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        bus.setByte(0xABCE, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with x with negative number");


        cpu.reset();
        bus.setByte(0xAC00, (byte) 0x00);
        cpu.setXRegister((byte) (0xAC00 - 0xABCD));
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with x wrapping with zero value");
    }

    @Test
    void loadAccumulatorAbsoluteY() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setYRegister((byte) 0x1);

        // lda $ABCD, y
        // b9   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDA_ABSY.getCode());
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        bus.setByte(0xABCE, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with y with negative number");


        cpu.reset();
        bus.setByte(0xAC00, (byte) 0x00);
        cpu.setYRegister((byte) (0xAC00 - 0xABCD));
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with y wrapping with zero value");
    }

    @Test
    void loadAccumulatorIndirectX() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setXRegister((byte) 0x10);

        // lda ($ABCD, x)
        // a1   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDA_INX.getCode());
        bus.setByte(0x0010, (byte) 0xCD);
        bus.setByte(0x0011, (byte) 0xAB);
        bus.setByte(0xABCD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "indirect x with negative number");


        cpu.reset();
        bus.setByte(0xABCD, (byte) 0x00);
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "indirect x with zero value");
    }

    @Test
    void loadAccumulatorIndirectY() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setYRegister((byte) 0x10);

        // lda ($00), y
        // a1   $10

        bus.setByte(0xFFFC, CPU6502Instructions.LDA_INY.getCode());
        bus.setByte(0xFFFD, (byte) 0x10);

        bus.setByte(0x0010, (byte) 0x15);
        bus.setByte(0x0011, (byte) 0x00);
        bus.setByte(0x0025, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "indirect y with negative number");

        // lda ($00), y
        // a1   $10

        cpu.reset();
        bus.setByte(0x0010, (byte) 0xCD);
        bus.setByte(0x0011, (byte) 0xAB);
        bus.setByte(0xAC00, (byte) 0x00);
        cpu.setYRegister((byte) (0xAC00 - 0xABCD));
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkAccumulatorAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "indirect y wraps with zero value");
    }
}