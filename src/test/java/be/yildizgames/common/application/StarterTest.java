/*
 * This file is part of the Yildiz-Engine project, licenced under the MIT License  (MIT)
 *  Copyright (c) 2019-2023 Grégory Van den Borre
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
package be.yildizgames.common.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

class StarterTest {

    @Nested
    class Constructor {

        @Test
        void happyFlow() {
            deleteFile();
            Starter s = new Starter() {
                @Override
                public void start() {

                }
            };
            Assertions.assertNotNull(s);
        }

    }

    @Nested
    class SetApplication {

        @Test
        void happyFlow(){
            deleteFile();
            Properties p = new Properties();
            p.setProperty("t", "11");
            Application a = Application.prepare("test");
            a.withConfiguration(new String[]{}, p);
            Starter s = new Starter() {
                @Override
                public void start() {

                }
            };
            s.setApplication(a);
            Assertions.assertEquals("11", s.getApplicationProperties().getProperty("t"));
        }

        @Test
        void nullParam() {
            deleteFile();
            Starter s = new Starter() {
                @Override
                public void start() {

                }
            };
            Assertions.assertThrows(NullPointerException.class, () -> s.setApplication(null));
        }

    }

    private static void deleteFile() {
        File f = new File("configuration.properties");
        if(f.exists()) {
            f.delete();
        }
    }

}
