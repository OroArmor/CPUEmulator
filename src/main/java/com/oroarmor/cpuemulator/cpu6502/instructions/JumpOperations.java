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

import com.oroarmor.cpuemulator.cpu6502.AddressingModes.AddressingMode;
import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.Memory;

public class JumpOperations {
    /**
     * Sets the stack pointer to the address specified by the {@link AddressingMode}
     *
     * @see CPU6502Instructions.CPU6502InstructionProcessor#runInstruction(int, CPU6502, Memory, CPU6502Instructions)
     */
    public static boolean jump(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        cpu.setProgramCounter(cpu.getCurrentAddressPointer());
        return true;
    }

    /**
     * Sets the stack pointer to the address specified by the {@link AddressingMode} and pushes the current program counter minus one onto the stack
     *
     * @see CPU6502Instructions.CPU6502InstructionProcessor#runInstruction(int, CPU6502, Memory, CPU6502Instructions)
     */
    public static boolean jumpSubRoutine(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        if (currentOpCycle == 3) {
            cpu.setProgramCounter(cpu.getProgramCounter() - 1);
            return false;
        }
        if (currentOpCycle == 4) {
            memory.setByte(0x0100 + cpu.getStackPointer(), (byte) ((cpu.getProgramCounter() >> 8) & 0x00FF));
            cpu.decrementStackPointer();
            return false;
        }

        memory.setByte(0x0100 + cpu.getStackPointer(), (byte) ((cpu.getProgramCounter()) & 0x00FF));
        cpu.decrementStackPointer();

        cpu.setProgramCounter(cpu.getCurrentAddressPointer());

        return true;
    }

    /**
     * Sets the program counter to the value on the stack, then adds one
     *
     * @see CPU6502Instructions.CPU6502InstructionProcessor#runInstruction(int, CPU6502, Memory, CPU6502Instructions)
     */
    public static boolean returnSubRoutine(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction) {
        if (currentOpCycle == 2) {
            cpu.incrementStackPointer();
            return false;
        }
        if (currentOpCycle == 3) {
            byte lo = memory.read(0x0100 + cpu.getStackPointer());
            cpu.setProgramCounter(Byte.toUnsignedInt(lo));
            return false;
        }
        if (currentOpCycle == 4) {
            cpu.incrementStackPointer();
            return false;
        }
        if (currentOpCycle == 5) {
            byte high = memory.read(0x0100 + cpu.getStackPointer());
            cpu.setProgramCounter(Byte.toUnsignedInt(high) << 8 | cpu.getProgramCounter());
            return false;
        }

        cpu.incrementProgramCounter();
        return true;
    }
}
