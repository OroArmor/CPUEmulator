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

public class IncrementOperations {
    /**
     * Adds value to the value from the supplier, then stores in into the consumer setting the correct flags
     *
     * @param register       The supplier for the register value
     * @param registerSetter The setter for the register value
     * @param value          The value to add to the register
     * @see CPU6502Instructions.CPU6502InstructionProcessor#runInstruction(int, CPU6502, Bus, CPU6502Instructions)
     */
    public static boolean incrementRegister(CPU6502 cpu, Supplier<Integer> register, Consumer<Byte> registerSetter, byte value) {
        byte newValue = (byte) ((byte) (int) register.get() + value);
        registerSetter.accept(newValue);

        cpu.incrementProgramCounter();

        byte flags = (byte) (((newValue < 0 ? 0b10000000 : 0) + (newValue == 0 ? 0b00000010 : 0)) | cpu.getFlags().toByte());
        cpu.getFlags().fromByte(flags);
        return true;
    }

    /**
     * Increments the X register
     *
     * @see #incrementRegister(CPU6502, Supplier, Consumer, byte)
     */
    public static boolean incrementXRegister(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return incrementRegister(cpu, cpu::getXRegister, cpu::setXRegister, (byte) 1);
    }

    /**
     * Decrements the X register
     *
     * @see #incrementRegister(CPU6502, Supplier, Consumer, byte)
     */
    public static boolean decrementXRegister(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return incrementRegister(cpu, cpu::getXRegister, cpu::setXRegister, (byte) -1);
    }

    /**
     * Increments the Y register
     *
     * @see #incrementRegister(CPU6502, Supplier, Consumer, byte)
     */
    public static boolean incrementYRegister(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return incrementRegister(cpu, cpu::getYRegister, cpu::setYRegister, (byte) 1);
    }

    /**
     * Decrements the Y register
     *
     * @see #incrementRegister(CPU6502, Supplier, Consumer, byte)
     */
    public static boolean decrementYRegister(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return incrementRegister(cpu, cpu::getYRegister, cpu::setYRegister, (byte) -1);
    }

    private static byte memoryValue;

    /**
     * Increments the memory location specified by the addressing mode
     *
     * @see #updateMemory(int, CPU6502, Bus, int)
     */
    public static boolean incrementMemory(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instructions) {
        return updateMemory(currentOpCycle, cpu, bus, 1);
    }

    /**
     * Decrements the memory location specified by the addressing mode
     *
     * @see #updateMemory(int, CPU6502, Bus, int)
     */
    public static boolean decrementMemory(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instructions) {
        return updateMemory(currentOpCycle, cpu, bus, -1);
    }

    /**
     * Adds the direction to the value at {@link CPU6502#getCurrentAddressPointer()} and stores it back into the same location
     * @param currentOpCycle the current instruction cycle
     * @param cpu The cpu
     * @param bus The bus
     * @param direction The direction to go
     * @return True when the operation is complete
     */
    private static boolean updateMemory(int currentOpCycle, CPU6502 cpu, Bus bus, int direction) {
        if (currentOpCycle == 0) {
            memoryValue = bus.readByte(cpu.getCurrentAddressPointer());
            return false;
        }

        if (currentOpCycle == 1) {
            memoryValue += direction;
            return false;
        }

        bus.writeByte(cpu.getCurrentAddressPointer(), memoryValue);

        cpu.incrementProgramCounter();

        byte flags = (byte) (((memoryValue < 0 ? 0b10000000 : 0) + (memoryValue == 0 ? 0b00000010 : 0)) | cpu.getFlags().toByte());
        cpu.getFlags().fromByte(flags);
        return true;
    }
}
