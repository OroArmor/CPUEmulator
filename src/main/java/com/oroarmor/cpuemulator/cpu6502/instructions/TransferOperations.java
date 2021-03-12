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

import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions.CPU6502InstructionProcessor;
import com.oroarmor.cpuemulator.cpu6502.Memory;

public class TransferOperations {
    /**
     * Sets the value of the consumingRegister with the value of the suppliedRegister
     *
     * @param suppliedRegister  The getter for the register
     * @param consumingRegister The setter for the register
     * @return true
     * 
     * @see CPU6502InstructionProcessor#runInstruction(int, CPU6502, Memory, CPU6502Instructions)
     */
    public static boolean transfer(CPU6502 cpu6502, Supplier<Integer> suppliedRegister, Consumer<Integer> consumingRegister) {
        consumingRegister.accept(suppliedRegister.get());
        cpu6502.incrementProgramCounter();
        return true;
    }

    /**
     * Transfers the A register to the X register
     */
    public static boolean transferAX(int i, CPU6502 cpu6502, Memory memory, CPU6502Instructions instructions) {
        return transfer(cpu6502, cpu6502::getAccumulator, cpu6502::setXRegister);
    }

    /**
     * Transfers the A register to the Y register
     */
    public static boolean transferAY(int i, CPU6502 cpu6502, Memory memory, CPU6502Instructions instructions) {
        return transfer(cpu6502, cpu6502::getAccumulator, cpu6502::setYRegister);
    }

    /**
     * Transfers the X register to the A register
     */
    public static boolean transferXA(int i, CPU6502 cpu6502, Memory memory, CPU6502Instructions instructions) {
        return transfer(cpu6502, cpu6502::getXRegister, cpu6502::setAccumulator);
    }

    /**
     * Transfers the Y register to the A register
     */
    public static boolean transferYA(int i, CPU6502 cpu6502, Memory memory, CPU6502Instructions instructions) {
        return transfer(cpu6502, cpu6502::getYRegister, cpu6502::setAccumulator);
    }
}
