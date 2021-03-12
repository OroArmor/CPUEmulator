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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTests {
    private CPU6502 cpu;
    private Memory memory;

    @BeforeEach
    public void reset() {
        cpu = new CPU6502();
        memory = new Memory();
    }

    @Test
    public void transferAtoX(){
        cpu.setAccumulator(0x80);

        memory.setByte(0xFFFC, CPU6502Instructions.TAX.getCode());

        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals(cpu.getAccumulator(), cpu.getXRegister(), "TAX sets the X register");
    }

    @Test
    public void transferAtoY(){
        cpu.setAccumulator(0x80);

        memory.setByte(0xFFFC, CPU6502Instructions.TAY.getCode());

        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals(cpu.getAccumulator(), cpu.getYRegister(), "TAY sets the Y register");
    }

    @Test
    public void transferXtoA(){
        cpu.setXRegister(0x80);

        memory.setByte(0xFFFC, CPU6502Instructions.TXA.getCode());

        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals(cpu.getXRegister(), cpu.getAccumulator(), "TXA sets the A register");
    }

    @Test
    public void transferYtoA(){
        cpu.setYRegister(0x80);

        memory.setByte(0xFFFC, CPU6502Instructions.TYA.getCode());

        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals(cpu.getYRegister(), cpu.getAccumulator(), "TYA sets the A register");
    }
}
