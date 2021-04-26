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
import com.oroarmor.cpuemulator.cpu6502.Flags;

public class StatusFlagOperations {
    /**
     * Sets the carry flag
     *
     * @return true
     */
    public static boolean setCarryFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 0, true);
    }

    /**
     * Clears the carry flag
     *
     * @return true
     */
    public static boolean clearCarryFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 0, false);
    }

    /**
     * Sets the decimal mode flag
     *
     * @return true
     */
    public static boolean setDecimalModeFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 3, true);
    }

    /**
     * Clears the decimal mode flag
     *
     * @return true
     */
    public static boolean clearDecimalModeFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 3, false);
    }

    /**
     * Sets the interrupt disable flag
     *
     * @return true
     */
    public static boolean setInterruptDisableFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 2, true);
    }

    /**
     * Clears the interrupt disable flag flag
     *
     * @return true
     */
    public static boolean clearInterruptDisableFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 2, false);
    }

    /**
     * Clears the overflow flag flag
     *
     * @return true
     */
    public static boolean clearOverflowFlag(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction) {
        return setFlag(cpu.getFlags(), 6, false);
    }

    /**
     * Sets the index of the flag to on
     *
     * @param flags The flags
     * @param index The index
     * @param on    True for on
     * @return true
     */
    public static boolean setFlag(Flags flags, int index, boolean on) {
        flags.setFlag((byte) index, on);
        return true;
    }
}
