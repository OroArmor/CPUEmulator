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
import com.oroarmor.cpuemulator.cpu6502.instructions.StoreOperations;

// regex : (\w*\((0x\S*), (\w*)::(\w*), AddressingModes::(\w*)\, (\d)\),?)
// replace : /**\n * Runs {@link $3#$4(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#$5(int, CPU6502, Memory)}, Opcode: <code>$2</code>, Max Cycles: $6\n */\n$1

/**
 * A Enum Holding all possible operations that the 6502 Processor can run. <br>
 * Each {@link CPU6502Instructions} has an opcode, a {@link CPU6502InstructionProcessor} to operate on, a {@link AddressingModes.AddressingMode} to get the address to operate on, and a max cycle
 */
public enum CPU6502Instructions {
    /* Load and store operations */
    /* LDA operations */
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Memory)}, Opcode: <code>0xA9</code>, Max Cycles: 2
     */
    LDA_IMM(0xA9, LoadOperations::loadAccumulator, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Memory)}, Opcode: <code>0xA5</code>, Max Cycles: 3
     */
    LDA_ZP(0xA5, LoadOperations::loadAccumulator, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Memory)}, Opcode: <code>0xB5</code>, Max Cycles: 4
     */
    LDA_ZPX(0xB5, LoadOperations::loadAccumulator, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Memory)}, Opcode: <code>0xAD</code>, Max Cycles: 4
     */
    LDA_ABS(0xAD, LoadOperations::loadAccumulator, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Memory)}, Opcode: <code>0xBD</code>, Max Cycles: 5
     */
    LDA_ABSX(0xBD, LoadOperations::loadAccumulator, AddressingModes::absoluteX, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Memory)}, Opcode: <code>0xB9</code>, Max Cycles: 5
     */
    LDA_ABSY(0xB9, LoadOperations::loadAccumulator, AddressingModes::absoluteY, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectX(int, CPU6502, Memory)}, Opcode: <code>0xA1</code>, Max Cycles: 6
     */
    LDA_INX(0xA1, LoadOperations::loadAccumulator, AddressingModes::indirectX, 6),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectY(int, CPU6502, Memory)}, Opcode: <code>0xB1</code>, Max Cycles: 6
     */
    LDA_INY(0xB1, LoadOperations::loadAccumulator, AddressingModes::indirectY, 6),

    /* LDX Operations */
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Memory)}, Opcode: <code>0xA2</code>, Max Cycles: 2
     */
    LDX_IMM(0xA2, LoadOperations::loadX, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Memory)}, Opcode: <code>0xA6</code>, Max Cycles: 3
     */
    LDX_ZP(0xA6, LoadOperations::loadX, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageY(int, CPU6502, Memory)}, Opcode: <code>0xB6</code>, Max Cycles: 4
     */
    LDX_ZPY(0xB6, LoadOperations::loadX, AddressingModes::zeroPageY, 4),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Memory)}, Opcode: <code>0xAE</code>, Max Cycles: 4
     */
    LDX_ABS(0xAE, LoadOperations::loadX, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Memory)}, Opcode: <code>0xBE</code>, Max Cycles: 5
     */
    LDX_ABSY(0xBE, LoadOperations::loadX, AddressingModes::absoluteY, 5),

    /* LDY Opertations */
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Memory)}, Opcode: <code>0xA0</code>, Max Cycles: 2
     */
    LDY_IMM(0xA0, LoadOperations::loadY, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Memory)}, Opcode: <code>0xA4</code>, Max Cycles: 3
     */
    LDY_ZP(0xA4, LoadOperations::loadY, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Memory)}, Opcode: <code>0xB4</code>, Max Cycles: 4
     */
    LDY_ZPY(0xB4, LoadOperations::loadY, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Memory)}, Opcode: <code>0xAC</code>, Max Cycles: 4
     */
    LDY_ABS(0xAC, LoadOperations::loadY, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Memory, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Memory)}, Opcode: <code>0xBC</code>, Max Cycles: 5
     */
    LDY_ABSY(0xBC, LoadOperations::loadY, AddressingModes::absoluteX, 5),

    /* Store Operations */

    /* Store Accumulator */
    STA_ZP(0x85, StoreOperations::storeAccumulator, AddressingModes::zeroPage, 3),
    STA_ZPX(0x95, StoreOperations::storeAccumulator, AddressingModes::zeroPageX, 4),
    STA_ABS(0x8D, StoreOperations::storeAccumulator, AddressingModes::absolute, 4),
    STA_ABSX(0x9D, StoreOperations::storeAccumulator, AddressingModes::absoluteX, 5),
    STA_ABSY(0x99, StoreOperations::storeAccumulator, AddressingModes::absoluteY, 5),
    STA_INX(0x9D, StoreOperations::storeAccumulator, AddressingModes::indirectX, 6),
    STA_INY(0x99, StoreOperations::storeAccumulator, AddressingModes::indirectY, 6),

    /* Store X */
    STX_ZP(0x86, StoreOperations::storeX, AddressingModes::zeroPage, 3),
    STX_ZPY(0x96, StoreOperations::storeX, AddressingModes::zeroPageY, 4),
    STX_ABS(0x8E, StoreOperations::storeX, AddressingModes::absolute, 4),
    /* Store Y */
    STY_ZP(0x84, StoreOperations::storeY, AddressingModes::zeroPage, 3),
    STY_ZPX(0x94, StoreOperations::storeY, AddressingModes::zeroPageX, 4),
    STY_ABS(0x8C, StoreOperations::storeY, AddressingModes::absolute, 4),

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
    private final CPU6502InstructionProcessor instructionProcessor;
    private final AddressingModes.AddressingMode addressingMode;
    private final int maxCycles;

    /**
     * Default constructor for testing
     */
    CPU6502Instructions() {
        this(0, null, null, 0);
    }

    /**
     * Helper constructor to not cast opcode to a byte.
     *
     * @param instruction          The instruction opcode
     * @param instructionProcessor The {@link CPU6502InstructionProcessor} for the instruction
     * @param addressingMode       The {@link AddressingModes.AddressingMode} for the instruction
     * @param maxCycles            The maximum cycles this operation can take
     * @see CPU6502Instructions#CPU6502Instructions(byte, CPU6502InstructionProcessor, AddressingModes.AddressingMode, int)
     */
    CPU6502Instructions(int instruction, CPU6502InstructionProcessor instructionProcessor, AddressingModes.AddressingMode addressingMode, int maxCycles) {
        this((byte) instruction, instructionProcessor, addressingMode, maxCycles);
    }

    /**
     * Creates a new {@link CPU6502Instructions} from the given data
     *
     * @param instruction          The instruction opcode
     * @param instructionProcessor The {@link CPU6502InstructionProcessor} for the instruction
     * @param addressingMode       The {@link AddressingModes.AddressingMode} for the instruction
     * @param maxCycles            The maximum cycles this operation can take
     */
    CPU6502Instructions(byte instruction, CPU6502InstructionProcessor instructionProcessor, AddressingModes.AddressingMode addressingMode, int maxCycles) {
        this.instruction = instruction;
        this.instructionProcessor = instructionProcessor;
        this.addressingMode = addressingMode;
        this.maxCycles = maxCycles;
    }

    /**
     * Gets the matching {@link CPU6502Instructions} from the given opcode
     *
     * @param instruction The opcode for the instruction
     * @return The {@link CPU6502Instructions}
     */
    public static CPU6502Instructions getFrom(byte instruction) {
        return Arrays.stream(values()).filter(cpu6502Instructions -> cpu6502Instructions.getCode() == instruction).findFirst().orElse(null);
    }

    /**
     * @return The Opcode for the instruction
     */
    public byte getCode() {
        return instruction;
    }

    /**
     * @return The {@link CPU6502InstructionProcessor} for the instruction
     */
    public CPU6502InstructionProcessor getInstructionProcessor() {
        return instructionProcessor;
    }

    /**
     * @return The {@link AddressingModes.AddressingMode} for the instruction
     */
    public AddressingModes.AddressingMode getAddressingMode() {
        return addressingMode;
    }

    /**
     * @return The max cycles
     */
    public int getMaxCycles() {
        return maxCycles;
    }

    /**
     * A functional interface for all instruction processors to use.<br>
     * Returning true signifies that this operation is over, while false signifies it has more cycles to run.
     */
    @FunctionalInterface
    public interface CPU6502InstructionProcessor {
        /**
         * Runs one step of the instruction
         *
         * @param currentOpCycle The current clock cycle into this operation. Starts at 1 because 0 is fetching the instruction
         * @param cpu            The {@link CPU6502} to operate on
         * @param memory         The {@link Memory} to read from and write to
         * @param instruction    The {@link CPU6502Instructions} for this run of the instruction
         * @return True when the operation has completed. This tells the CPU to fetch the next instruction.
         */
        boolean runInstruction(int currentOpCycle, CPU6502 cpu, Memory memory, CPU6502Instructions instruction);
    }
}
