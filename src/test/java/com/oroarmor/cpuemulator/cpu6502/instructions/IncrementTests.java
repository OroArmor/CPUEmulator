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

import java.util.function.Supplier;

import com.oroarmor.cpuemulator.cpu6502.Bus;
import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.TestBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementTests {
    private CPU6502 cpu;
    private Bus bus;

    @BeforeEach
    public void reset() {
        cpu = new CPU6502();
        bus = new TestBus();
    }

    private void checkValue(CPU6502Instructions instruction, byte expectedValue, Supplier<Integer> actualValue, String testName){
        bus.writeByte(0xFFFC, instruction.getCode());
        byte flags = cpu.getFlags().toByte();
        for(int i = 0; i < instruction.getMaxCycles(); i++) {
            cpu.tick(bus);
        }

        assertEquals(expectedValue, (int) actualValue.get(), testName);
        byte expectedFlags = (byte) (flags | ((expectedValue < 0) ? 0b10000000 : 0) | ((expectedValue == 0) ? 0b00000010 : 0));
        assertEquals(expectedFlags, cpu.getFlags().toByte(), String.format("%s sets the correct flags", instruction));
        cpu.reset();
    }

    @Test
    public void xTests() {
        checkValue(CPU6502Instructions.INX, (byte) 1, cpu::getXRegister, "INX increments the X register");
        cpu.setXRegister((byte) 0xFF);
        checkValue(CPU6502Instructions.INX, (byte) 0, cpu::getXRegister, "INX increments the X register");
        cpu.setXRegister((byte) 0x7F);
        checkValue(CPU6502Instructions.INX, (byte) -128, cpu::getXRegister, "INX increments the X register");

        checkValue(CPU6502Instructions.DEX, (byte) -1, cpu::getXRegister, "DEX decrements the X register");
        cpu.setXRegister((byte) 1);
        checkValue(CPU6502Instructions.DEX, (byte) 0, cpu::getXRegister, "DEX decrements the X register");
        cpu.setXRegister((byte) 0x7F);
        checkValue(CPU6502Instructions.DEX, (byte) 126, cpu::getXRegister, "DEX decrements the X register");
    }

    @Test
    public void yTests() {
        checkValue(CPU6502Instructions.INY, (byte) 1, cpu::getYRegister, "INY increments the Y register");
        cpu.setYRegister((byte) 0xFF);
        checkValue(CPU6502Instructions.INY, (byte) 0, cpu::getYRegister, "INY increments the Y register");
        cpu.setYRegister((byte) 0x7F);
        checkValue(CPU6502Instructions.INY, (byte) -128, cpu::getYRegister, "INY increments the Y register");

        checkValue(CPU6502Instructions.DEY, (byte) -1, cpu::getYRegister, "DEY decrements the Y register");
        cpu.setYRegister((byte) 1);
        checkValue(CPU6502Instructions.DEY, (byte) 0, cpu::getYRegister, "DEY decrements the Y register");
        cpu.setYRegister((byte) 0x7F);
        checkValue(CPU6502Instructions.DEY, (byte) 126, cpu::getYRegister, "DEY decrements the Y register");
    }

    @Test
    public void memoryTests() {
        int location = 0x0010;
        bus.writeByte(0xFFFD, (byte) 0x10);
        checkValue(CPU6502Instructions.INC_ZP, (byte) 1, () -> (int) bus.readByte(location), "INC increments the correct memory location");
        bus.writeByte(location, (byte) 0xFF);
        checkValue(CPU6502Instructions.INC_ZP, (byte) 0, () -> (int) bus.readByte(location), "INC increments the correct memory location");
        bus.writeByte(location, (byte) 0x7F);
        checkValue(CPU6502Instructions.INC_ZP, (byte) -128, () -> (int) bus.readByte(location), "INC increments the correct memory location");

        bus.writeByte(location, (byte) 0);
        checkValue(CPU6502Instructions.DEC_ZP, (byte) -1, () -> (int) bus.readByte(location), "DEC decrements the correct memory location");
        bus.writeByte(location, (byte) 1);
        checkValue(CPU6502Instructions.DEC_ZP, (byte) 0, () -> (int) bus.readByte(location), "DEC decrements the correct memory location");
        bus.writeByte(location, (byte) 0x7F);
        checkValue(CPU6502Instructions.DEC_ZP, (byte) 126, () -> (int) bus.readByte(location), "DEC decrements the correct memory location");
    }
}
