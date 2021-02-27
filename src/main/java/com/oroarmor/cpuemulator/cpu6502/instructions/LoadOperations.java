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

import com.oroarmor.cpuemulator.cpu6502.*;

public class LoadOperations {
    public static boolean loadAccumulator(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setAccumulator);
    }

    public static boolean loadX(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setXRegister);
    }

    public static boolean loadY(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        return loadValue(currentOpCycle, cpu, memory, instruction, cpu::setYRegister);
    }

    private static boolean loadValue(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction, Consumer<Byte> setter) {
        if(currentOpCycle > instruction.getMaxCycles()) {
            throw new IllegalArgumentException(String.format("%s only has %d operation(s), %d was requested", instruction, instruction.getMaxCycles(), currentOpCycle));
        }

        if (instruction.getAddressingMode().address(currentOpCycle, cpu, memory)) {
            int index = cpu.getCurrentAddressPointer();
            byte newValue = memory.read(index);
            setter.accept(newValue);
            cpu.incrementProgramCounter();

            byte flags = (byte) (((newValue < 0 ? 0b10000000 : 0) + (newValue == 0 ? 0b00000010 : 0)) | cpu.getFlags().toByte());
            cpu.getFlags().fromByte(flags);
            return true;
        }
        return false;
    }
}