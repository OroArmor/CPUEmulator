package com.oroarmor.cpuemulator.cpu6502.instructions;

import com.oroarmor.cpuemulator.cpu6502.CPU6502;
import com.oroarmor.cpuemulator.cpu6502.CPU6502Instructions;
import com.oroarmor.cpuemulator.cpu6502.Memory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreTests {
    @Test
    public void storeAccumulatorTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setAccumulator((byte) 127);

        // sta $80
        // 85   80
        memory.setByte(0xFFFC, CPU6502Instructions.STA_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getAccumulator(), memory.read(0x80), "STA sets correct value");
    }

    @Test
    public void storeXTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setXRegister((byte) 127);

        // stx $80
        // 86   80
        memory.setByte(0xFFFC, CPU6502Instructions.STX_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getXRegister(), memory.read(0x80), "STX sets correct value");
    }

    @Test
    public void storeYTest() {
        CPU6502 cpu = new CPU6502();
        Memory memory = new Memory();

        cpu.setYRegister((byte) 127);

        // sty $80
        // 84   80
        memory.setByte(0xFFFC, CPU6502Instructions.STY_ZP.getCode());
        memory.setByte(0xFFFD, (byte) 0x80);

        cpu.tick(memory);
        cpu.tick(memory);
        cpu.tick(memory);

        assertEquals((byte) cpu.getYRegister(), memory.read(0x80), "STY sets correct value");
    }
}