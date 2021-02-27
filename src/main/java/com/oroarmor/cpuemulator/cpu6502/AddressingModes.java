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

public class AddressingModes {
    public static boolean immediate(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep != 1) {
            throw new IllegalArgumentException("Immediate addressing only takes one cycle");
        }
        cpu.setCurrentAddressPointer(cpu.getProgramCounter());
        cpu.incrementProgramCounter();
        return true;
    }

    public static boolean zeroPage(int cycleStep, CPU6502 cpu, Memory memory){
        if(cycleStep > 2) {
            throw new IllegalArgumentException("Zero page addressing only takes two cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    public static boolean zeroPageX(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 3) {
            throw new IllegalArgumentException("Zero page with x addressing only takes two cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getXRegister());
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    public static boolean zeroPageY(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 3) {
            throw new IllegalArgumentException("Zero page with y addressing only takes two cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return false;
        }

        cpu.setCurrentAddressPointer(0x00FF & cpu.getCurrentAddressPointer());
        return true;
    }

    public static boolean absolute(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 3) {
            throw new IllegalArgumentException("Absolute addressing only takes three cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        return true;
    }

    public static boolean absoluteX(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 4) {
            throw new IllegalArgumentException("Absolute addressing with x addressing only takes 4 cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        if(cycleStep == 3) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getXRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == ((Byte.toUnsignedInt(memory.read(cpu.getProgramCounter() - 1))) << 8);
        }

        return true;
    }

    public static boolean absoluteY(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 4) {
            throw new IllegalArgumentException("Absolute addressing with y addressing only takes 4 cycles");
        }

        if (cycleStep == 1) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(cpu.getProgramCounter())));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() | (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()))) << 8);
            cpu.incrementProgramCounter();
            return false;
        }

        if(cycleStep == 3) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == (Byte.toUnsignedInt(memory.read(cpu.getProgramCounter() - 1))) << 8;
        }

        return true;
    }

    private static int indirectReadAddress = -1;
    public static boolean indirectX(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 5) {
            throw new IllegalArgumentException("Indirect with x addressing only takes five cycles");
        }

        if (cycleStep == 1) {
            indirectReadAddress = Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress + cpu.getXRegister())) & 0x00FF);
            return false;
        }

        if (cycleStep == 3) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read((indirectReadAddress + cpu.getXRegister() + 1) & 0x00FF)) << 8 | cpu.getCurrentAddressPointer());
            return false;
        }

        return cycleStep == 5;
    }

    public static boolean indirectY(int cycleStep, CPU6502 cpu, Memory memory) {
        if(cycleStep > 5) {
            throw new IllegalArgumentException("Indirect with y addressing only takes five cycles");
        }

        if (cycleStep == 1) {
            indirectReadAddress = Byte.toUnsignedInt(memory.read(cpu.getProgramCounter()));
            cpu.incrementProgramCounter();
            return false;
        }

        if (cycleStep == 2) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read(indirectReadAddress & 0x00FF)));
            return false;
        }

        if (cycleStep == 3) {
            cpu.setCurrentAddressPointer(Byte.toUnsignedInt(memory.read((indirectReadAddress + 1) & 0x00FF)) << 8 | cpu.getCurrentAddressPointer());
            return false;
        }

        if (cycleStep == 4) {
            cpu.setCurrentAddressPointer(cpu.getCurrentAddressPointer() + cpu.getYRegister());
            return (cpu.getCurrentAddressPointer() & 0xFF00) == Byte.toUnsignedInt(memory.read((indirectReadAddress + 1) & 0x00FF)) << 8;
        }

        return true;
    }
}
