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
 * A helper class for representing the flags of the {@link CPU6502}
 * <table>
 * <tr><th>Byte</th><th>Name</th><th>Code</th></tr>
 * <tr><td>0</td><td>Carry Flag</td><td>C</td></tr>
 * <tr><td>1</td><td>Zero Flag</td><td>Z</td></tr>
 * <tr><td>2</td><td>Interrupt Disable Flag</td><td>I</td></tr>
 * <tr><td>3</td><td>Decimal Mode Flag</td><td>D</td></tr>
 * <tr><td>4</td><td>Break Flag</td><td>B</td></tr>
 * <tr><td>5</td><td>unused</td><td>U</td></tr>
 * <tr><td>6</td><td>Overflow Flag</td><td>O</td></tr>
 * <tr><td>7</td><td>Negative Flag</td><td>N</td></tr>
 * </table>
 */
public class Flags {
    private final boolean unusedFlag = true;
    private boolean carryFlag = false;
    private boolean zeroFlag = false;
    private boolean interruptDisableFlag = false;
    private boolean decimalModeFlag = false;
    private boolean breakFlag = false;
    private boolean overflowFlag = false;
    private boolean negativeFlag = false;

    /**
     * Create default flags
     */
    public Flags() {
    }

    /**
     * Set the individual flags on creation
     */
    public Flags(boolean carryFlag, boolean zeroFlag, boolean interruptDisableFlag, boolean decimalModeFlag, boolean breakFlag, boolean overflowFlag, boolean negativeFlag) {
        this.carryFlag = carryFlag;
        this.zeroFlag = zeroFlag;
        this.interruptDisableFlag = interruptDisableFlag;
        this.decimalModeFlag = decimalModeFlag;
        this.breakFlag = breakFlag;
        this.overflowFlag = overflowFlag;
        this.negativeFlag = negativeFlag;
    }

    /**
     * Reads the byte into the different indexes of the flag
     *
     * @param b The byte to read from
     */
    public void fromByte(byte b) {
        this.carryFlag = 0b00000001 == (b & 0b0000001);
        this.zeroFlag = 0b00000010 == (b & 0b00000010);
        this.interruptDisableFlag = 0b00000100 == (b & 0b00000100);
        this.decimalModeFlag = 0b00001000 == (b & 0b00001000);
        this.breakFlag = 0b00010000 == (b & 0b00010000);
        this.overflowFlag = 0b01000000 == (b & 0b01000000);
        this.negativeFlag = 0b10000000 == (b & 0b10000000);
    }

    /**
     * Writes the flags into a byte
     *
     * @return The byte for the flags
     */
    public byte toByte() {
        return (byte) ((getValue(carryFlag)) +
                (getValue(zeroFlag) << 1) +
                (getValue(interruptDisableFlag) << 2) +
                (getValue(decimalModeFlag) << 3) +
                (getValue(breakFlag) << 4) +
                (getValue(unusedFlag) << 5) +
                (getValue(overflowFlag) << 6) +
                (getValue(negativeFlag) << 7)
        );
    }

    private byte getValue(boolean b) {
        return (byte) (b ? 1 : 0);
    }

    /**
     * Sets the flag at the index to the value
     *
     * @param flagBit The index of the flag (0 to 7)
     * @param value   The value for the flag
     */
    public void setFlag(byte flagBit, boolean value) {
        switch (flagBit) {
            case 0:
                carryFlag = value;
                break;
            case 1:
                zeroFlag = value;
                break;
            case 2:
                interruptDisableFlag = value;
                break;
            case 3:
                decimalModeFlag = value;
                break;
            case 4:
                breakFlag = value;
                break;
            case 5:
                break;
            case 6:
                overflowFlag = value;
                break;
            case 7:
                negativeFlag = value;
                break;
            default:
                throw new IllegalArgumentException(String.format("Bit %d is not a valid bit to set", flagBit));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flags flags = (Flags) o;
        return this.toByte() == flags.toByte();
    }

    @Override
    public int hashCode() {
        return Byte.toUnsignedInt(this.toByte());
    }

    /**
     * Gets the flag at the index to the value
     *
     * @param flagBit The index of the flag (0 to 7)
     * @return The value for the flag
     */
    public boolean getFlag(byte flagBit) {
        switch (flagBit) {
            case 0:
                return carryFlag;
            case 1:
                return zeroFlag;
            case 2:
                return interruptDisableFlag;
            case 3:
                return decimalModeFlag;
            case 4:
                return breakFlag;
            case 5:
                return unusedFlag;
            case 6:
                return overflowFlag;
            case 7:
                return negativeFlag;
            default:
                throw new IllegalArgumentException(String.format("Bit %d is not a valid bit to set", flagBit));
        }
    }
}
