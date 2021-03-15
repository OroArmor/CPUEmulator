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

package com.oroarmor.cpuemulator.cpu6502.instructions;

import com.oroarmor.cpuemulator.cpu6502.Bus;
import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JumpTests {
    private CPU6502 cpu;
    private Bus bus;

    @BeforeEach
    public void reset() {
        cpu = new CPU6502();
        bus = new Bus();
    }

    @Test
    public void testJump() {
        // JMP ($1000)
        // 6C 10 00

        bus.setByte(0xFFFC, CPU6502Instructions.JMP_IND.getCode());
        bus.setByte(0xFFFD, (byte) 0x00);
        bus.setByte(0xFFFE, (byte) 0x10);
        bus.setByte(0x1000, (byte) 0x00);
        bus.setByte(0x1001, (byte) 0x20);

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        assertEquals(0x2000, cpu.getProgramCounter(), "JMP Ind sets the correct program counter");
    }

    @Test
    public void testJumpAbs() {
        // JMP ($1000)
        // 6C 10 00

        bus.setByte(0xFFFC, CPU6502Instructions.JMP_ABS.getCode());
        bus.setByte(0xFFFD, (byte) 0x00);
        bus.setByte(0xFFFE, (byte) 0x10);

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        assertEquals(0x1000, cpu.getProgramCounter(), "JMP Abs sets the correct program counter");
    }

    @Test
    public void testJumpSubroutine() {
        // JMP ($1000)
        // 6C 10 00

        bus.setByte(0xFFFC, CPU6502Instructions.JSR.getCode());
        bus.setByte(0xFFFD, (byte) 0x00);
        bus.setByte(0xFFFE, (byte) 0x10);

        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);
        cpu.tick(bus);

        assertEquals(0x1000, cpu.getProgramCounter(), "JSR sets the correct program counter");
    }

    @Test
    public void testReturnSubroutine() {
        // JMP ($1000)
        // 6C 10 00

        bus.setByte(0xFFFC, CPU6502Instructions.JSR.getCode());
        bus.setByte(0xFFFD, (byte) 0x00);
        bus.setByte(0xFFFE, (byte) 0x10);
        bus.setByte(0x1000, CPU6502Instructions.RTS.getCode());

        for (int i = 0; i < 12; i++) {
            cpu.tick(bus);
        }

        assertEquals(0xFFFE, cpu.getProgramCounter(), "RTS sets the correct program counter");
    }
}
