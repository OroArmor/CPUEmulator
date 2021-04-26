package com.oroarmor.cpuemulator.cpu6502;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class FullCpuTest {
    @Test
    public void testFullCPU() {
        CPU6502 cpu = new CPU6502();
        TestBus bus = new TestBus();
        InputStream testFile = FullCpuTest.class.getClassLoader().getResourceAsStream("com/oroarmor/cpuemulator/cpu6502/nes_test_file.bin");
        int location = 0;
        try {
            while (testFile.available() > 0) {
                bus.writeByte(location++, (byte) testFile.read());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cpu.reset();
    }
}
