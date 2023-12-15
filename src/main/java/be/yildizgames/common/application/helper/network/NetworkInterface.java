/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *
 *  Copyright (c) 2019 Grégory Van den Borre
 *
 *  More infos available: https://engine.yildiz-games.be
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without
 *  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *  OR COPYRIGHT  HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE  SOFTWARE.
 *
 */
package be.yildizgames.common.application.helper.network;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Provide the IP address of the machine on the local network.
 * @author Grégory Van den Borre
 */
public class NetworkInterface {

    /**
     * Cached value to avoid to have to create the socket more than once.
     */
    private static String address;

    /**
     * Private constructor to prevent instantiation.
     */
    private NetworkInterface() {
        super();
    }

    /**
     * Provide the IP address.
     * @return The ip V 4 address, never null.
     * @throws IllegalStateException if an error occurred.
     */
    public static String getPreferredAddress() {
        if(address == null) {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 8080);
                address = socket.getLocalAddress().getHostAddress();
            } catch (Exception e) {
                throw new IllegalArgumentException("No address found", e);
            }
        }
        return address;
    }

}
