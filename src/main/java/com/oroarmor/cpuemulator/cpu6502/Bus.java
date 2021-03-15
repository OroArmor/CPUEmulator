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

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for the bus of the cpu. If two {@link BusDevice}s listen to the same location, the one added first
 * will be notified, and no other devices will be notified
 */
public class Bus {
    public final List<BusDevice> devices = new ArrayList<>();

    /**
     * Sets the byte at location to the new value
     *
     * @param location The location
     * @param value    The value
     */
    public void writeByte(int location, byte value) {
        for (BusDevice device : devices) {
            if (device.isValidAddress(location)) {
                device.writeValue(location, value);
                return;
            }
        }
    }

    /**
     * Returns the byte at location
     *
     * @param location The location
     * @return The value
     */
    public byte readByte(int location) {
        for (BusDevice device : devices) {
            if (device.isValidAddress(location)) {
                return device.readValue(location);
            }
        }
        return 0;
    }

    /**
     * Attaches a device to the bus
     *
     * @param device The device to attach
     */
    public void attachDevice(BusDevice device) {
        devices.add(device);
    }

    /**
     * An interface for all devices that can read and write from the bus
     */
    public interface BusDevice {
        /**
         * Reads a value from this device
         *
         * @param location The location to read from
         * @return The value
         */
        byte readValue(int location);

        /**
         * Writes a value to this device
         *
         * @param location The location to write to
         * @param value    The value
         */
        void writeValue(int location, byte value);

        /**
         * Returns if the location is a valid address that the device listens too
         *
         * @param location The location
         * @return True when the device listens to the location
         */
        boolean isValidAddress(int location);
    }
}
