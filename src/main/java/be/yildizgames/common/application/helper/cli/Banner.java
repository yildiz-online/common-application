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

package be.yildizgames.common.application.helper.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A banner to display at the startup of the application in the terminal.
 * This will not be displayed in logs.
 * @author Grégory Van den Borre
 */
public class Banner {

    private static final int LINE_LENGTH = 80;

    private static final String BORDER_LINE = "*".repeat(LINE_LENGTH);

    private static final String SIDE_LINE =   "*" + " ".repeat(LINE_LENGTH - 2) + "*";

    private final String appName;

    private final List<String> lines = new ArrayList<>();

    private final List<BannerLine> addedLines = new ArrayList<>();

    public Banner(String appName) {
        this.appName = appName;
        this.addLines();
    }

    public void addLine(BannerLine line) {
        this.addedLines.add(line);
    }

    public void fromFile(Path path) {
        this.lines.clear();
        try (Stream<String> content = Files.lines(path)) {
            content.forEach(this.lines::add);
        } catch (IOException e) {
            Terminal.println(e.toString());
            this.addLines();
            lines.add("*-- ERROR reading file: " + path);
        }
    }

    private void addLines() {
        this.lines.add(BORDER_LINE);
        this.lines.add(SIDE_LINE);
        int start = ((LINE_LENGTH - 2) - appName.length()) >> 1;
        this.lines.add("*" + " ".repeat(start) + appName + " ".repeat(start) + "*");
        this.lines.add(SIDE_LINE);
        String s = "*   Powered by Yildiz-Engine";
        this.lines.add(s + " ".repeat(LINE_LENGTH - s.length() - 1) + "*");
        String e = "https://engine.yildiz-games.be   *";
        this.lines.add("*" + " ".repeat(LINE_LENGTH - e.length() - 1) + e);
    }

    public void display() {
        this.lines.forEach(Terminal::println);
        this.addedLines.forEach(l -> Terminal.println(l.print()));
        Terminal.println(SIDE_LINE);
        Terminal.println(BORDER_LINE);
    }
}
