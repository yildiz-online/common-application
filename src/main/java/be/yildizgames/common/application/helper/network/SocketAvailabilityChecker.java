/*
 * MIT License
 *
 * Copyright (c) 2019 Grégory Van den Borre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package be.yildizgames.common.application.helper.network;

/**
 * @author Grégory Van den Borre
 */
public class SocketAvailabilityChecker implements AvailabilityChecker {

    private static final long DELAY = 60000L;

    private long lastCheck = 0;

    private AvailabilityStatus current;

    private final SocketPingCheck checker;

    public SocketAvailabilityChecker(ServerAccessConfiguration configuration) {
        this.checker = new SocketPingCheck(configuration);
    }

    private AvailabilityStatus respond() {
        if (this.current == AvailabilityStatus.CHECKING) {
            return current;
        }
        var now = System.currentTimeMillis();
        if (now - lastCheck < DELAY) {
            return current;
        }
        this.current = AvailabilityStatus.CHECKING;
        this.current = this.checker.respond() ? AvailabilityStatus.ONLINE : AvailabilityStatus.OFFLINE;
        lastCheck = now;
        return current;
    }

    @Override
    public boolean isOnline() {
        return this.respond().equals(AvailabilityStatus.ONLINE);
    }

    public boolean isChecking() {
        return this.respond().equals(AvailabilityStatus.CHECKING);
    }

    public final boolean isOffLine() {
        return this.respond().equals(AvailabilityStatus.OFFLINE);
    }

    private enum AvailabilityStatus {

        CHECKING, ONLINE, OFFLINE

    }
}
