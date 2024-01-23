/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2021-2024 Grégory Van den Borre
 *  More infos available: https://engine.yildiz-games.be
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright
 *  notice and this permission notice shall be included in all copies or substantial portions of the  Software.
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package be.yildizgames.common.application.helper.network;

/**
 * Checks the availability of a network resource using socket connections.
 *
 * @author Grégory Van den Borre
 */
public class SocketAvailabilityChecker implements AvailabilityChecker {

    /**
     * The delay in milliseconds between availability checks.
     */
    private static final long DELAY = 60000L;

    /**
     * The timestamp of the last availability check.
     */
    private long lastCheck = 0;

    /**
     * The current connection status.
     */
    private ConnectionStatus current;

    /**
     * Handles the socket connection check.
     */
    private final SocketPingCheck checker;

    /**
     * Creates a new checker with the given server configuration.
     *
     * @param configuration the server access configuration
     */
    public SocketAvailabilityChecker(ServerAccessConfiguration configuration) {
        this.checker = new SocketPingCheck(configuration);
    }

    /**
     * Responds to an availability check request.
     *
     * @return the availability status.
     */
    private ConnectionStatus respond() {
        if (this.current == ConnectionStatus.CHECKING) {
            return this.current;
        }
        var now = System.currentTimeMillis();
        if (now - this.lastCheck < DELAY) {
            return this.current;
        }
        this.current = ConnectionStatus.CHECKING;
        this.current = this.checker.respond() ? ConnectionStatus.ONLINE : ConnectionStatus.OFFLINE;
        this.lastCheck = now;
        return this.current;
    }

    @Override
    public final boolean isOnline() {
        return this.respond().equals(ConnectionStatus.ONLINE);
    }

    /**
     * Checks if the availability status is currently checking.
     *
     * @return true if checking availability, false otherwise
     */
    public final boolean isChecking() {
        return this.respond().equals(ConnectionStatus.CHECKING);
    }

    /**
     * Checks if the target resource is currently reported as offline.
     *
     * @return true if offline, false otherwise
     */
    public final boolean isOffLine() {
        return this.respond().equals(ConnectionStatus.OFFLINE);
    }

    /**
     * The possible availability statuses.
     */
    private enum ConnectionStatus {

        /**
         * Checking the availability.
         */
        CHECKING,

        /**
         * Available online.
         */
        ONLINE,

        /**
         * Unavailable offline.
         */
        OFFLINE

    }
}
