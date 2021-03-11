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

package com.oroarmor.cpuemulator.cpu6502;

/**
 * The different types of addressing modes the 6502 processor supports
 */
public class AddressingModes {
    private static int indirectReadAddress = -1;

    /**
     * Implied Addressing Mode
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean implied(int currentOpCycle, CPU6502 cpu, Memory memory) {
        return currentOpCycle > 1;
    }

    /**
     * Loads the address from the {@link CPU6502#getProgramCounter} into the {@link CPU6502#getCurrentAddressPointer()}
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean immediate(int currentOpCycle, CPU6502 cpu, Memory memory) {
        cpu.setCurrentAddressPointer(cpu.getProgramCounter());
        cpu.incrementProgramCounter();
        return true;
    }

    /**
     * Loads the address from the zero page of RAM indexed by {@link CPU6502#getProgramCounter} into the {@link CPU6502#getCurrentAddressPointer()}
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean zeroPage(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    /**
     * Loads the address from the zero page of RAM indexed by {@link CPU6502#getProgramCounter} with the {@link CPU6502#getXRegister} added into the {@link CPU6502#getCurrentAddressPointer()}
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean zeroPageX(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getXRegister());
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    /**
     * Loads the address from the zero page of RAM indexed by {@link CPU6502#getProgramCounter} with the {@link CPU6502#getYRegister} added into the {@link CPU6502#getCurrentAddressPointer()}
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean zeroPageY(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    /**
     * Loads the address from the address at <code>(PC + 1) << 8 + PC</code> ({@link CPU6502#getProgramCounter}<code> = PC</code>) into the {@link CPU6502#getCurrentAddressPointer()}
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean absolute(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        return true;
    }

    /**
     * Loads the address from the address at <code>(PC + 1) << 8 + PC + X</code> ({@link CPU6502#getProgramCounter}<code> = PC</code>, {@link CPU6502#getXRegister}<code> = X</code>) into the {@link CPU6502#getCurrentAddressPointer()}<br>
     * This normally takes 3 cycles, but if adding <code>X</code> causes the address to move into the next page, it will take an additional cycle
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean absoluteX(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 3) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getXRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == ((Byte.toUnsignedInt(memory.read(cpu.getProgramCounter() - 1))) << 8);
        }

        return true;
    }

    /**
     * Loads the address from the address at <code>(PC + 1) << 8 + PC + Y</code> ({@link CPU6502#getProgramCounter}<code> = PC</code>, {@link CPU6502#getYRegister}<code> = Y</code>) into the {@link CPU6502#getCurrentAddressPointer()}<br>
     * This normally takes 3 cycles, but if adding <code>Y</code> causes the address to move into the next page, it will take an additional cycle
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean absoluteY(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 3) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter() - 1))) << 8;
        }

        return true;
    }

    /**
     * Loads the value that is pointed from the value the current program counter points to. <br>
     * <code>pc -> ind_addr -> value</code>
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean indirect(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            indirectReadAddress = Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }
        if (currentOpCycle == 2) {
            indirectReadAddress |= Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())) << 8;
            cpu.incrementProgramCounter();
            return false;
        }
        if (currentOpCycle == 3) {
            cpu.setCurrentAddressPointer(memory.read(indirectReadAddress));
            return false;
        }
        if ((indirectReadAddress & 0x00FF) == 0x00FF) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress & 0xFF00)) << 8 | cpu.getCurrentAddressPointer());
        } else {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress + 1)) << 8 | cpu.getCurrentAddressPointer());
        }
        return true;
    }

    /**
     * Loads the address from the zero page with the value at the PC, adding the X register to the high and low bits, <code>($PC + 1 + X) << 8 + ($PC + X)</code> {@link CPU6502#getProgramCounter} = PC, {@link CPU6502#getXRegister} = X into the {@link CPU6502#getCurrentAddressPointer()}<br>
     * This normally takes 4 cycles, but if adding <code>X</code> causes the address to wrap around to stay in the zero page, it will take an additional cycle
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean indirectX(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            indirectReadAddress = Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress + cpu.getXRegister())) & 0x00FF);
            return false;
        }

        if (currentOpCycle == 3) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read((indirectReadAddress + cpu.getXRegister() + 1) & 0x00FF)) << 8 | cpu.getCurrentAddressPointer());
            return false;
        }

        return currentOpCycle == 5;
    }

    /**
     * Loads the address from the zero page at {@link CPU6502#getProgramCounter} added to {@link CPU6502#getYRegister} into the {@link CPU6502#getCurrentAddressPointer()}<br>
     * This normally takes 4 cycles, but if adding <code>Y</code> causes the address to wrap around to stay in the zero page, it will take an additional cycle
     *
     * @see AddressingMode#address(int, CPU6502, Memory)
     */
    public static boolean indirectY(int currentOpCycle, CPU6502 cpu, Memory memory) {
        if (currentOpCycle == 1) {
            indirectReadAddress = Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (currentOpCycle == 2) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress & 0x00FF)));
            return false;
        }

        if (currentOpCycle == 3) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read((indirectReadAddress + 1) & 0x00FF)) << 8 | cpu.getCurrentAddressPointer());
            return false;
        }

        if (currentOpCycle == 4) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == Byte.toUnsignedInt(memory.read((indirectReadAddress + 1) & 0x00FF)) << 8;
        }

        return true;
    }

    /**
     * A functional interface for all addressing modes to use. <br>
     * Returning true signifies that this operation has loaded the address into {@link CPU6502#getCurrentAddressPointer()}, while false signifies it has more cycles to run.<br>
     * When running addressing modes, try to contain all variables inside the method, as fields outside the method are discouraged
     */
    @FunctionalInterface
    public interface AddressingMode {
        /**
         * Runs one step of loading the wanted address into the {@link CPU6502#getCurrentAddressPointer()}
         *
         * @param currentOpCycle The current clock cycle into this operation. Starts at 1 because 0 is fetching the instruction
         * @param cpu            The {@link CPU6502} to operate on
         * @param memory         The {@link Memory} to read from and write to
         * @return The address for the instruction to read from
         */
        boolean address(int currentOpCycle, CPU6502 cpu, Memory memory);
    }
}
