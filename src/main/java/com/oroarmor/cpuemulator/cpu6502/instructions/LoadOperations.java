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

import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.Memory;

public class LoadOperations {
    /**
     * Loads the value at the address specified by the {@link CPU6502Instructions#getAddressingMode} into {@link CPU6502#getAccumulator}
     *
     * @see LoadOperations#loadValue(int, CPU6502, Memory, CPU6502Instructions, Consumer)
     */
    public static boolean loadAccumulator(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setAccumulator);
    }

    /**
     * Loads the value at the address specified by the {@link CPU6502Instructions#getAddressingMode} into {@link CPU6502#getXRegister()}
     *
     * @see LoadOperations#loadValue(int, CPU6502, Memory, CPU6502Instructions, Consumer)
     */
    public static boolean loadX(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setXRegister);
    }

    /**
     * Loads the value at the address specified by the {@link CPU6502Instructions#getAddressingMode} into {@link CPU6502#getYRegister()}
     *
     * @see LoadOperations#loadValue(int, CPU6502, Memory, CPU6502Instructions, Consumer)
     */
    public static boolean loadY(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setYRegister);
    }

    /**
     * Loads the value at the address specified by the {@link CPU6502Instructions#getAddressingMode} into the consumer for the register set
     *
     * @param registerSetter The consumer for setting the cpu register
     * @see CPU6502Instructions.CPU6502InstructionProcessor
     */
    public static boolean loadValue(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction, Consumer<Byte> registerSetter) {
        int index = cpu.getCurrentAddressPointer();
        byte newValue = memory.read(index);
        registerSetter.accept(newValue);
        cpu.incrementProgramCounter();

        byte flags = (byte) (((newValue < 0 ? 0b10000000 : 0) + (newValue == 0 ? 0b00000010 : 0)) | cpu.getFlags().toByte());
        cpu.getFlags().fromByte(flags);
        return true;
    }
}
