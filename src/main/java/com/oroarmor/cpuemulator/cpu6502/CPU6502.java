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
 * A class representing the 6502 CPU and its registers
 */
public class CPU6502 {
    private final Flags flags = new Flags();
    private int programCounter = 0xFFFC;
    private int stackPointer = 0xFD;
    private int accumulator = 0x0;
    private int xRegister = 0x0;
    private int yRegister = 0x0;
    private CPU6502Instructions currentInstruction;
    private int currentInstructionCycle;

    private int currentAddressPointer = 0;

    /**
     * Resets the CPU
     */
    public void reset() {
        programCounter = 0xFFFC;
        stackPointer = 0xFD;
        accumulator = 0x0;
        xRegister = 0x0;
        yRegister = 0x0;
        flags.fromByte((byte) 0b00000000);
        currentInstruction = null;
        currentInstructionCycle = 0;
    }

    /**
     * Clocks the CPU once
     *
     * @param bus The memory for the CPU
     */
    public void tick(Bus bus) {
        if (currentInstruction == null) {
            currentInstruction = CPU6502Instructions.getFrom(bus.readByte(programCounter));
            if (currentInstruction == null || bus.readByte(programCounter) == 0x00) {
                throw new UnsupportedOperationException(String.format("Unknown Op Code: %s", Integer.toHexString(bus.readByte(programCounter)).toUpperCase()));
            }
            programCounter++;
            currentInstructionCycle = 1;
            return;
        }

        if (currentInstructionCycle > currentInstruction.getMaxCycles()) {
            throw new IllegalArgumentException(String.format("%s only has %d operation(s), %d was requested", currentInstruction, currentInstruction.getMaxCycles(), currentInstructionCycle));
        }

        if (currentInstruction.getAddressingMode().address(currentInstructionCycle, this, bus)) {
            currentInstruction = currentInstruction.getInstructionProcessor().runInstruction(currentInstructionCycle, this, bus, currentInstruction) ? null : currentInstruction;
        }

        if (currentInstructionCycle == 2 && currentInstruction == CPU6502Instructions.JMP_ABS) {
            currentInstruction.getInstructionProcessor().runInstruction(currentInstructionCycle, this, bus, currentInstruction);
            currentInstruction = null;
        }

        currentInstructionCycle++;
    }

    public int getCurrentAddressPointer() {
        return currentAddressPointer;
    }
    public void setCurrentAddressPointer(int currentAddressPointer) {
        this.currentAddressPointer = currentAddressPointer;
    }

    public int getProgramCounter() {
        return programCounter;
    }
    public void incrementProgramCounter() {
        this.programCounter++;
    }
    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getStackPointer() {
        return stackPointer;
    }
    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }
    public void incrementStackPointer() {
        this.stackPointer++;
    }
    public void decrementStackPointer() {
        this.stackPointer--;
    }

    public int getAccumulator() {
        return accumulator;
    }
    public void setAccumulator(byte accumulator) {
        this.accumulator = accumulator;
    }

    public int getXRegister() {
        return xRegister;
    }
    public void setXRegister(byte xRegister) {
        this.xRegister = xRegister;
    }

    public int getYRegister() {
        return yRegister;
    }
    public void setYRegister(byte yRegister) {
        this.yRegister = yRegister;
    }

    public Flags getFlags() {
        return flags;
    }
}
