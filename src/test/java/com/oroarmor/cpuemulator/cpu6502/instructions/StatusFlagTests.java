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
import com.oroarmor.cpuemulator.cpu6502.TestBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusFlagTests {
    private CPU6502 cpu;
    private Bus bus;

    @BeforeEach
    public void beforeTest() {
        cpu = new CPU6502();
        bus = new TestBus();
    }

    @Test
    public void testCarryFlags() {
        testFlag(cpu, bus, CPU6502Instructions.SEC, 0, true, "Set Carry Flag");
        testFlag(cpu, bus, CPU6502Instructions.CLC, 0, false, "Clear Carry Flag");
    }

    @Test
    public void testDecimalModeFlags() {
        testFlag(cpu, bus, CPU6502Instructions.SED, 3, true, "Set Decimal Mode Flag");
        testFlag(cpu, bus, CPU6502Instructions.CLD, 3, false, "Clear Decimal Mode Flag");
    }

    @Test
    public void testInterruptDisableFlags() {
        testFlag(cpu, bus, CPU6502Instructions.SEI, 2, true, "Set Interrupt Disable Flag");
        testFlag(cpu, bus, CPU6502Instructions.CLI, 2, false, "Clear Interrupt Disable Flag");
    }

    @Test
    public void testOverflowFlags() {
        testFlag(cpu, bus, CPU6502Instructions.CLV, 6, false, "Clear Overflow Flag");
    }

    private void testFlag(CPU6502 cpu, Bus bus, CPU6502Instructions instruction, int index, boolean shouldBeOn, String name) {
        cpu.reset();
        cpu.getFlags().setFlag((byte) index, !shouldBeOn);
        bus.writeByte(0xFFFC, instruction.getCode());

        cpu.tick(bus);
        cpu.tick(bus);

        assertEquals(shouldBeOn, cpu.getFlags().getFlag((byte) index),name +" correctly set flag " + index + " to " + (shouldBeOn ? "on" : "off"));
    }
}
