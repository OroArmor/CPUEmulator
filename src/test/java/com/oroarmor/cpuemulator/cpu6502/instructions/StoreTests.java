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

import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.Memory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreTests {
    @Test
    public void storeAccumulatorTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setAccumulator((byte) 127);

        // sta $80
        // 85   80
        memory.setByte(0xFFFC, CPU6502Instructions.STA_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getAccumulator(), memory.read(0x80), "STA sets correct value");
    }

    @Test
    public void storeXTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setXRegister((byte) 127);

        // stx $80
        // 86   80
        memory.setByte(0xFFFC, CPU6502Instructions.STX_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getXRegister(), memory.read(0x80), "STX sets correct value");
    }

    @Test
    public void storeYTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setYRegister((byte) 127);

        // sty $80
        // 84   80
        memory.setByte(0xFFFC, CPU6502Instructions.STY_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getYRegister(), memory.read(0x80), "STY sets correct value");
    }
}
