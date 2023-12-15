/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2021-2023 Grégory Van den Borre
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Checks server availability by connecting to the server via a socket.
 *
 * @author Grégory Van den Borre
 */
class SocketPingCheck {

    /**
     * The server access configuration.
     */
    private final ServerAccessConfiguration configuration;

    /**
     * Creates a new checker with the given configuration.
     *
     * @param configuration the server configuration
     */
    SocketPingCheck(ServerAccessConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Attempts to connect to the server via a socket.
     *
     * @return true if the connection succeeds, false otherwise
     */
    final boolean respond() {
        var host = configuration.getServerUrl().replace("http://", "").split(":")[0];
        int port = Integer.parseInt(configuration.getServerUrl().replace("http://", "").split(":")[1].replace("/", ""));
        try (var socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
