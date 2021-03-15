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

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.oroarmor.cpuemulator.cpu6502.Bus;
import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferTests {
    private void transfer(CPU6502Instructions instruction, Consumer<Byte> setter, Supplier<Integer> setValue, byte value, CPU6502 cpu, Bus bus) {
        setter.accept(value);

        bus.setByte(0xFFFC, instruction.getCode());

        cpu.tick(bus);
        cpu.tick(bus);

        assertEquals(value, (int) setValue.get(), instruction + " sets the " + instruction.toString().substring(2) + " register");
        assertEquals(value < 0, cpu.getFlags().getFlag((byte) 7), "Negative flag set with negative value");
        assertEquals(value == 0, cpu.getFlags().getFlag((byte) 1), "Zero flag set with zero value");
        cpu.reset();
    }

    @Test
    public void transferTests() {
        CPU6502 cpu = new CPU6502();
        Bus bus = new Bus();

        transfer(CPU6502Instructions.TAX, cpu::setAccumulator, cpu::getXRegister, (byte) 0x80, cpu, bus);
        transfer(CPU6502Instructions.TAX, cpu::setAccumulator, cpu::getXRegister, (byte) 0x00, cpu, bus);

        transfer(CPU6502Instructions.TAY, cpu::setAccumulator, cpu::getYRegister, (byte) 0x80, cpu, bus);
        transfer(CPU6502Instructions.TAY, cpu::setAccumulator, cpu::getYRegister, (byte) 0x00, cpu, bus);

        transfer(CPU6502Instructions.TXA, cpu::setXRegister, cpu::getAccumulator, (byte) 0x80, cpu, bus);
        transfer(CPU6502Instructions.TXA, cpu::setXRegister, cpu::getAccumulator, (byte) 0x00, cpu, bus);

        transfer(CPU6502Instructions.TYA, cpu::setYRegister, cpu::getAccumulator, (byte) 0x80, cpu, bus);
        transfer(CPU6502Instructions.TYA, cpu::setYRegister, cpu::getAccumulator, (byte) 0x00, cpu, bus);
    }
}
