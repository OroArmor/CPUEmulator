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

import java.util.function.Supplier;

import com.oroarmor.cpuemulator.cpu6502.Bus;
import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadTestHelpers {
    protected static CPU6502 cpu;
    protected static Bus bus;

    protected Supplier<Integer> getValue;
    protected String name;

    protected LoadTestHelpers(Supplier<Integer> getValue, String name) {
        this.getValue = getValue;
        this.name = name;
    }

    @BeforeEach
    public void reset() {
        cpu = new CPU6502();
        bus = new Bus();
    }

    protected void testLoadValue(CPU6502 cpu, Bus bus, byte value, int location, CPU6502Instructions instruction, String name, int cycles) {
        bus.setByte(0xFFFC, instruction.getCode());
        bus.setByte(location, value);
        byte flags = cpu.getFlags().toByte();

        for (int i = 0; i < cycles; i++) {
            cpu.tick(bus);
        }

        byte expectedFlags = (byte) (flags | ((value < 0) ? 0b10000000 : 0) | ((value == 0) ? 0b00000010 : 0));
        assertEquals(value, (byte) (int) getValue.get(), String.format("%s %s sets correct value, %d", this.name, name, value));
        StringBuilder expectedFlagsString = new StringBuilder(Integer.toBinaryString(expectedFlags));
        int missing = 8 - expectedFlagsString.length();
        while (missing > 0) {
            expectedFlagsString.insert(0, "0");
            missing--;
        }
        assertEquals(expectedFlags, cpu.getFlags().toByte(), String.format("%s %s sets Flags (0b%s) correctly with %d", this.name, name, expectedFlagsString, value));
        cpu.reset();
    }
}
