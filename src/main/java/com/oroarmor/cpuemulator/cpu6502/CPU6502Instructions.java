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

import java.util.Arrays;

import com.oroarmor.cpuemulator.cpu6502.instructions.LoadOperations;

// regex : (\w*\((0x\S*), (\w*)::(\w*), AddressingModes::(\w*)\),?)
// replace : /**\n * Runs {@link $3#$4(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#$5(int, CPU6502, Memory)}, opcode <code>$2</code>\n */\n$1

public enum CPU6502Instructions {
    /* Load and store operations */
    /* LDA operations */
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Memory)}, opcode <code>0xA9</code>
     */
    LDA_IMM(0xA9, LoadOperations::loadAccumulator, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Memory)}, opcode <code>0xA5</code>
     */
    LDA_ZP(0xA5, LoadOperations::loadAccumulator, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Memory)}, opcode <code>0xB5</code>
     */
    LDA_ZPX(0xB5, LoadOperations::loadAccumulator, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Memory)}, opcode <code>0xAD</code>
     */
    LDA_ABS(0xAD, LoadOperations::loadAccumulator, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Memory)}, opcode <code>0xBD</code>
     */
    LDA_ABSX(0xBD, LoadOperations::loadAccumulator, AddressingModes::absoluteX, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Memory)}, opcode <code>0xB9</code>
     */
    LDA_ABSY(0xB9, LoadOperations::loadAccumulator, AddressingModes::absoluteY, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectX(int, CPU6502, Memory)}, opcode <code>0xA1</code>
     */
    LDA_INX(0xA1, LoadOperations::loadAccumulator, AddressingModes::indirectX, 6),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectY(int, CPU6502, Memory)}, opcode <code>0xB1</code>
     */
    LDA_INY(0xB1, LoadOperations::loadAccumulator, AddressingModes::indirectY , 6),

    /* LDX Operations */
    LDX_IMM(0xA2, LoadOperations::loadX, AddressingModes::immediate, 2),
    LDX_ZP(0xA6, LoadOperations::loadX, AddressingModes::zeroPage, 3),
    LDX_ZPY(0xB6, LoadOperations::loadX, AddressingModes::zeroPageY, 4),
    LDX_ABS(0xAE, LoadOperations::loadX, AddressingModes::absolute, 4),
    LDX_ABSY(0xBE, LoadOperations::loadX, AddressingModes::absoluteY, 5),

    /* LDY Opertations */
    LDY_IMM(0xA0, LoadOperations::loadY, AddressingModes::immediate, 2),
    LDY_ZP(0xA4, LoadOperations::loadY, AddressingModes::zeroPage, 3),
    LDY_ZPY(0xB4, LoadOperations::loadY, AddressingModes::zeroPageX, 4),
    LDY_ABS(0xAC, LoadOperations::loadY, AddressingModes::absolute, 4),
    LDY_ABSY(0xBC, LoadOperations::loadY, AddressingModes::absoluteX, 5),
    STA, STX, STY,

    /* */
    TAX, TAY, TXA, TYA,
    TSX, TXS, PHA, PHP, PLA, PLP,
    AND, EOR, ORA, BIT,
    ADC, SBC, CMP, CPX, CPY,
    INC, INX, INY, DEC, DEX, DEY,
    ASL, LSR, ROL, ROR,
    JMP, JSR, RTS,
    BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS,
    CLC, CLD, CLI, CLV, SEC, SED, SEI,
    BRK, NOP, RTI;

    private final byte instruction;
    private final CPU6502Instruction instructionProcessor;
    private final AddressingMode addressingMode;
    private final int maxCycles;

    CPU6502Instructions() {
        this(0, null, null, 0);
    }

    CPU6502Instructions(int instruction, CPU6502Instruction instructionProcessor, AddressingMode addressingMode, int maxCycles) {
        this((byte) instruction, instructionProcessor, addressingMode, maxCycles);
    }

    CPU6502Instructions(byte instruction, CPU6502Instruction instructionProcessor, AddressingMode addressingMode, int maxCycles) {
        this.instruction = instruction;
        this.instructionProcessor = instructionProcessor;
        this.addressingMode = addressingMode;
        this.maxCycles = maxCycles;
    }

    public byte getCode() {
        return instruction;
    }

    public CPU6502Instruction getInstructionProcessor() {
        return instructionProcessor;
    }

    public int getMaxCycles() {
        return maxCycles;
    }

    public AddressingMode getAddressingMode() {
        return addressingMode;
    }

    public static CPU6502Instructions getFrom(byte instruction) {
        return Arrays.stream(values()).filter(cpu6502Instructions -> cpu6502Instructions.getCode() == instruction).findFirst().orElse(null);
    }
}
