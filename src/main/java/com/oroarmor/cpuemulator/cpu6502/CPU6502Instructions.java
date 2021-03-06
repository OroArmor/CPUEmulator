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

import com.oroarmor.cpuemulator.cpu6502.instructions.*;

// regex : (\w*\((0x\S*), (\w*)::(\w*), AddressingModes::(\w*)\, (\d)\),?)
// replace : /**\n * Runs {@link $3#$4(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#$5(int, CPU6502, Bus)}, Opcode: <code>$2</code>, Max Cycles: $6\n */\n$1

/**
 * A Enum Holding all possible operations that the 6502 Processor can run. <br>
 * Each {@link CPU6502Instructions} has an opcode, a {@link CPU6502InstructionProcessor} to operate on, a {@link AddressingModes.AddressingMode} to get the address to operate on, and a max cycle
 */
public enum CPU6502Instructions {
    /* Load and store operations */
    /* LDA operations */
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Bus)}, Opcode: <code>0xA9</code>, Max Cycles: 2
     */
    LDA_IMM(0xA9, LoadOperations::loadAccumulator, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0xA5</code>, Max Cycles: 3
     */
    LDA_ZP(0xA5, LoadOperations::loadAccumulator, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0xB5</code>, Max Cycles: 4
     */
    LDA_ZPX(0xB5, LoadOperations::loadAccumulator, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0xAD</code>, Max Cycles: 4
     */
    LDA_ABS(0xAD, LoadOperations::loadAccumulator, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Bus)}, Opcode: <code>0xBD</code>, Max Cycles: 5
     */
    LDA_ABSX(0xBD, LoadOperations::loadAccumulator, AddressingModes::absoluteX, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Bus)}, Opcode: <code>0xB9</code>, Max Cycles: 5
     */
    LDA_ABSY(0xB9, LoadOperations::loadAccumulator, AddressingModes::absoluteY, 5),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectX(int, CPU6502, Bus)}, Opcode: <code>0xA1</code>, Max Cycles: 6
     */
    LDA_INX(0xA1, LoadOperations::loadAccumulator, AddressingModes::indirectX, 6),
    /**
     * Runs {@link LoadOperations#loadAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectY(int, CPU6502, Bus)}, Opcode: <code>0xB1</code>, Max Cycles: 6
     */
    LDA_INY(0xB1, LoadOperations::loadAccumulator, AddressingModes::indirectY, 6),

    /* LDX Operations */
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Bus)}, Opcode: <code>0xA2</code>, Max Cycles: 2
     */
    LDX_IMM(0xA2, LoadOperations::loadX, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0xA6</code>, Max Cycles: 3
     */
    LDX_ZP(0xA6, LoadOperations::loadX, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageY(int, CPU6502, Bus)}, Opcode: <code>0xB6</code>, Max Cycles: 4
     */
    LDX_ZPY(0xB6, LoadOperations::loadX, AddressingModes::zeroPageY, 4),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0xAE</code>, Max Cycles: 4
     */
    LDX_ABS(0xAE, LoadOperations::loadX, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Bus)}, Opcode: <code>0xBE</code>, Max Cycles: 5
     */
    LDX_ABSY(0xBE, LoadOperations::loadX, AddressingModes::absoluteY, 5),

    /* LDY Opertations */
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#immediate(int, CPU6502, Bus)}, Opcode: <code>0xA0</code>, Max Cycles: 2
     */
    LDY_IMM(0xA0, LoadOperations::loadY, AddressingModes::immediate, 2),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0xA4</code>, Max Cycles: 3
     */
    LDY_ZP(0xA4, LoadOperations::loadY, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0xB4</code>, Max Cycles: 4
     */
    LDY_ZPX(0xB4, LoadOperations::loadY, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0xAC</code>, Max Cycles: 4
     */
    LDY_ABS(0xAC, LoadOperations::loadY, AddressingModes::absolute, 4),
    /**
     * Runs {@link LoadOperations#loadY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Bus)}, Opcode: <code>0xBC</code>, Max Cycles: 5
     */
    LDY_ABSX(0xBC, LoadOperations::loadY, AddressingModes::absoluteX, 5),

    /* STA Operations */
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0x85</code>, Max Cycles: 3
     */
    STA_ZP(0x85, StoreOperations::storeAccumulator, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0x95</code>, Max Cycles: 4
     */
    STA_ZPX(0x95, StoreOperations::storeAccumulator, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0x8D</code>, Max Cycles: 4
     */
    STA_ABS(0x8D, StoreOperations::storeAccumulator, AddressingModes::absolute, 4),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Bus)}, Opcode: <code>0x9D</code>, Max Cycles: 5
     */
    STA_ABSX(0x9D, StoreOperations::storeAccumulator, AddressingModes::absoluteX, 5),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteY(int, CPU6502, Bus)}, Opcode: <code>0x99</code>, Max Cycles: 5
     */
    STA_ABSY(0x99, StoreOperations::storeAccumulator, AddressingModes::absoluteY, 5),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectX(int, CPU6502, Bus)}, Opcode: <code>0x9D</code>, Max Cycles: 6
     */
    STA_INX(0x9D, StoreOperations::storeAccumulator, AddressingModes::indirectX, 6),
    /**
     * Runs {@link StoreOperations#storeAccumulator(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirectY(int, CPU6502, Bus)}, Opcode: <code>0x99</code>, Max Cycles: 6
     */
    STA_INY(0x99, StoreOperations::storeAccumulator, AddressingModes::indirectY, 6),

    /* STX Operations */
    /**
     * Runs {@link StoreOperations#storeX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0x86</code>, Max Cycles: 3
     */
    STX_ZP(0x86, StoreOperations::storeX, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link StoreOperations#storeX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageY(int, CPU6502, Bus)}, Opcode: <code>0x96</code>, Max Cycles: 4
     */
    STX_ZPY(0x96, StoreOperations::storeX, AddressingModes::zeroPageY, 4),
    /**
     * Runs {@link StoreOperations#storeX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0x8E</code>, Max Cycles: 4
     */
    STX_ABS(0x8E, StoreOperations::storeX, AddressingModes::absolute, 4),

    /* STY Operations */
    /**
     * Runs {@link StoreOperations#storeY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0x84</code>, Max Cycles: 3
     */
    STY_ZP(0x84, StoreOperations::storeY, AddressingModes::zeroPage, 3),
    /**
     * Runs {@link StoreOperations#storeY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0x94</code>, Max Cycles: 4
     */
    STY_ZPX(0x94, StoreOperations::storeY, AddressingModes::zeroPageX, 4),
    /**
     * Runs {@link StoreOperations#storeY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0x8C</code>, Max Cycles: 4
     */
    STY_ABS(0x8C, StoreOperations::storeY, AddressingModes::absolute, 4),

    /* Transfer Operations */
    /**
     * Runs {@link TransferOperations#transferAX(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xAA</code>, Max Cycles: 2
     */
    TAX(0xAA, TransferOperations::transferAX, AddressingModes::implied, 2),
    /**
     * Runs {@link TransferOperations#transferAY(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xA8</code>, Max Cycles: 2
     */
    TAY(0xA8, TransferOperations::transferAY, AddressingModes::implied, 2),
    /**
     * Runs {@link TransferOperations#transferXA(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xBA</code>, Max Cycles: 2
     */
    TXA(0xBA, TransferOperations::transferXA, AddressingModes::implied, 2),
    /**
     * Runs {@link TransferOperations#transferYA(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x8A</code>, Max Cycles: 2
     */
    TYA(0x8A, TransferOperations::transferYA, AddressingModes::implied, 2),

    TSX, TXS, PHA, PHP, PLA, PLP,
    AND, EOR, ORA, BIT,
    ADC, SBC, CMP, CPX, CPY,

    /* Increment and Decrement Operations */
    /**
     * Runs {@link IncrementOperations#incrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0xE6</code>, Max Cycles: 5
     */
    INC_ZP(0xE6, IncrementOperations::incrementMemory, AddressingModes::zeroPage, 5),
    /**
     * Runs {@link IncrementOperations#incrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0xF6</code>, Max Cycles: 6
     */
    INC_ZPX(0xF6, IncrementOperations::incrementMemory, AddressingModes::zeroPageX, 6),
    /**
     * Runs {@link IncrementOperations#incrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0xEE</code>, Max Cycles: 6
     */
    INC_ABS(0xEE, IncrementOperations::incrementMemory, AddressingModes::absolute, 6),
    /**
     * Runs {@link IncrementOperations#incrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Bus)}, Opcode: <code>0xFE</code>, Max Cycles: 7
     */
    INC_ABSX(0xFE, IncrementOperations::incrementMemory, AddressingModes::absoluteX, 7),

    /**
     * Runs {@link IncrementOperations#incrementXRegister(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xE8</code>, Max Cycles: 2
     */
    INX(0xE8, IncrementOperations::incrementXRegister, AddressingModes::implied, 2),
    /**
     * Runs {@link IncrementOperations#incrementYRegister(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xC8</code>, Max Cycles: 2
     */
    INY(0xC8, IncrementOperations::incrementYRegister, AddressingModes::implied, 2),

    /**
     * Runs {@link IncrementOperations#decrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPage(int, CPU6502, Bus)}, Opcode: <code>0xC6</code>, Max Cycles: 5
     */
    DEC_ZP(0xC6, IncrementOperations::decrementMemory, AddressingModes::zeroPage, 5),
    /**
     * Runs {@link IncrementOperations#decrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#zeroPageX(int, CPU6502, Bus)}, Opcode: <code>0xD6</code>, Max Cycles: 6
     */
    DEC_ZPX(0xD6, IncrementOperations::decrementMemory, AddressingModes::zeroPageX, 6),
    /**
     * Runs {@link IncrementOperations#decrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0xCE</code>, Max Cycles: 6
     */
    DEC_ABS(0xCE, IncrementOperations::decrementMemory, AddressingModes::absolute, 6),
    /**
     * Runs {@link IncrementOperations#decrementMemory(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absoluteX(int, CPU6502, Bus)}, Opcode: <code>0xDE</code>, Max Cycles: 7
     */
    DEC_ABSX(0xDE, IncrementOperations::decrementMemory, AddressingModes::absoluteX, 7),

    /**
     * Runs {@link IncrementOperations#decrementXRegister(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xCA</code>, Max Cycles: 2
     */
    DEX(0xCA, IncrementOperations::decrementXRegister, AddressingModes::implied, 2),
    /**
     * Runs {@link IncrementOperations#decrementYRegister(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x88</code>, Max Cycles: 2
     */
    DEY(0x88, IncrementOperations::decrementYRegister, AddressingModes::implied, 2),

    /* Rotate Operations */
    ASL, LSR, ROL, ROR,

    /* Jump Operations */
    /**
     * Runs {@link JumpOperations#jump(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0x4C</code>, Max Cycles: 3
     */
    JMP_ABS(0x4C, JumpOperations::jump, AddressingModes::absolute, 3),
    /**
     * Runs {@link JumpOperations#jump(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#indirect(int, CPU6502, Bus)}, Opcode: <code>0x6C</code>, Max Cycles: 5
     */
    JMP_IND(0x6C, JumpOperations::jump, AddressingModes::indirect, 5),
    /**
     * Runs {@link JumpOperations#jumpSubRoutine(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#absolute(int, CPU6502, Bus)}, Opcode: <code>0x20</code>, Max Cycles: 6
     */
    JSR(0x20, JumpOperations::jumpSubRoutine, AddressingModes::absolute, 6),
    /**
     * Runs {@link JumpOperations#returnSubRoutine(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x60</code>, Max Cycles: 6
     */
    RTS(0x60, JumpOperations::returnSubRoutine, AddressingModes::implied, 6),

    BCC, BCS, BEQ, BMI, BNE, BPL, BVC, BVS,

    /* Flag Operations */
    /**
     * Runs {@link StatusFlagOperations#clearCarryFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x18</code>, Max Cycles: 2
     */
    CLC(0x18, StatusFlagOperations::clearCarryFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#clearDecimalModeFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xD8</code>, Max Cycles: 2
     */
    CLD(0xD8, StatusFlagOperations::clearDecimalModeFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#clearInterruptDisableFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x58</code>, Max Cycles: 2
     */
    CLI(0x58, StatusFlagOperations::clearInterruptDisableFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#clearOverflowFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xB8</code>, Max Cycles: 2
     */
    CLV(0xB8, StatusFlagOperations::clearOverflowFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#setCarryFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x38</code>, Max Cycles: 2
     */
    SEC(0x38, StatusFlagOperations::setCarryFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#setDecimalModeFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0xF8</code>, Max Cycles: 2
     */
    SED(0xF8, StatusFlagOperations::setDecimalModeFlag, AddressingModes::implied, 2),
    /**
     * Runs {@link StatusFlagOperations#setInterruptDisableFlag(int, CPU6502, Bus, CPU6502Instructions)} with AddressingMode {@link AddressingModes#implied(int, CPU6502, Bus)}, Opcode: <code>0x78</code>, Max Cycles: 2
     */
    SEI(0x78, StatusFlagOperations::setInterruptDisableFlag, AddressingModes::implied, 2),


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
         * @param bus            The {@link Bus} to read from and write to
         * @param instruction    The {@link CPU6502Instructions} for this run of the instruction
         * @return True when the operation has completed. This tells the CPU to fetch the next instruction.
         */
        boolean runInstruction(int currentOpCycle, CPU6502 cpu, Bus bus, CPU6502Instructions instruction);
    }
}
