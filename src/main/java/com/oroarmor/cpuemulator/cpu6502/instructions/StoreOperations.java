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

public class StoreOperations {
    /**
     * Stores the value in the {@link CPU6502#getAccumulator} at the address specified by the {@link CPU6502Instructions#getAddressingMode}
     *
     * @see StoreOperations#storeValue(int, CPU6502, Bus, CPU6502Instructions, Supplier)
     */
    public static boolean storeAccumulator(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return storeValue(currentOpCycle, cpu, bus, instruction, cpu::getAccumulator);
    }

    /**
     * Stores the value in the {@link CPU6502#getXRegister} at the address specified by the {@link CPU6502Instructions#getAddressingMode}
     *
     * @see StoreOperations#storeValue(int, CPU6502, Bus, CPU6502Instructions, Supplier)
     */
    public static boolean storeX(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return storeValue(currentOpCycle, cpu, bus, instruction, cpu::getXRegister);
    }

    /**
     * Stores the value in the {@link CPU6502#getYRegister} at the address specified by the {@link CPU6502Instructions#getAddressingMode}
     *
     * @see StoreOperations#storeValue(int, CPU6502, Bus, CPU6502Instructions, Supplier)
     */
    public static boolean storeY(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return storeValue(currentOpCycle, cpu, bus, instruction, cpu::getYRegister);
    }

    /**
     * Stores the value from the supplier into the address specified by the {@link CPU6502Instructions#getAddressingMode}
     *
     * @param registerGetter The consumer for setting the cpu register
     * @see CPU6502Instructions.CPU6502InstructionProcessor#runInstruction(int, CPU6502, Bus, CPU6502Instructions)
     */
    public static boolean storeValue(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction, Supplier<Integer> registerGetter) {
        int index = cpu.getCurrentAddressPointer();
        byte newValue = registerGetter.get().byteValue();
        bus.setByte(index, newValue);
        cpu.incrementProgramCounter();
        return true;
    }
}
