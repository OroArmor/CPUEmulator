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

class LoadXTest {
    private static void checkXAndFlags(byte expectedX, byte expectedFlags, CPU6502 cpu, String ldaType) {
        assertEquals(expectedX, cpu.getXRegister(), String.format("LDX %s sets correct value, %d", ldaType, expectedX));
        StringBuilder expectedFlagsString = new StringBuilder(Integer.toBinaryString(expectedFlags));
        int missing = 8 - expectedFlagsString.length();
        while (missing > 0) {
            expectedFlagsString.insert(0, "0");
            missing--;
        }
        assertEquals(expectedFlags, cpu.getFlags().toByte(), String.format("LDX %s sets Flags (%s) correctly with %d", ldaType, "0b" + expectedFlagsString, expectedX));
    }

    @Test
    void loadXImmediate() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // ldx #80
        // a2   80
        bus.setByte(0xFFFC, CPU6502Instructions.LDX_IMM.getCode());
        bus.setByte(0xFFFD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "immediate with negative number");

        // ldx #00
        // a2   00
        cpu.reset();
        flags = cpu.getFlags().toByte();
        bus.setByte(0xFFFD, (byte) 0x00);

        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "immediate with zero value");
    }

    @Test
    void loadXZeroPage() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // ldx $10
        // a6   10
        bus.setByte(0xFFFC, CPU6502Instructions.LDX_ZP.getCode());
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0010, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with negative number");

        bus.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with zero value");
    }

    @Test
    void loadXZeroPageY() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setYRegister((byte) 0x10);

        // ldx $10,u
        // b6   $10
        bus.setByte(0xFFFC, CPU6502Instructions.LDX_ZPY.getCode());
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0020, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "zero page with y with negative number");

        cpu.setYRegister((byte) 0xFF);
        bus.setByte(0xFFFD, (byte) 0x10);
        bus.setByte(0x0010, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "zero page with y with zero value");
    }

    @Test
    void loadXAbsolute() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        // ldx $ABCD
        // ae   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDX_ABS.getCode());
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        bus.setByte(0xABCD, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with negative number");

        bus.setByte(0xABCD, (byte) 0x00);
        cpu.reset();
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with zero value");
    }

    @Test
    void loadXAbsoluteY() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        cpu.setYRegister((byte) 0x1);

        // ldx $ABCD, y
        // be   $ABCD

        bus.setByte(0xFFFC, CPU6502Instructions.LDX_ABSY.getCode());
        bus.setByte(0xFFFD, (byte) 0xCD);
        bus.setByte(0xFFFE, (byte) 0xAB);
        bus.setByte(0xABCE, (byte) 0x80);
        byte flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x80, (byte) (flags | 0b10000000), cpu, "absolute with y with negative number");


        cpu.reset();
        bus.setByte(0xAC00, (byte) 0x00);
        cpu.setYRegister((byte) (0xAC00 - 0xABCD));
        flags = cpu.getFlags().toByte();

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        checkXAndFlags((byte) 0x00, (byte) (flags | 0b00000010), cpu, "absolute with y wrapping with zero value");
    }
}