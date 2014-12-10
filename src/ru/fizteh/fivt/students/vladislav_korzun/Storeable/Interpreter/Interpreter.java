package ru.fizteh.fivt.students.vladislav_korzun.Storeable.Interpreter;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class Interpreter {
    private InputStream in;
    private PrintStream out;
    private Object connector;
    private Map<String, Command> commands;
    
    public Interpreter(Object connector, Command[] commands,
            InputStream in, PrintStream out) throws Exception {
        if (in == null) {
            throw new IllegalArgumentException("Input stream is null");
        } 
        if (out == null) {
            throw new IllegalArgumentException("Output stream is null");
        } 
        this.commands = new HashMap<>();
        this.in = in;
        this.out = out;
        this.connector = connector;
        for (Command cmd : commands) {
            this.commands.put(cmd.getName(), cmd);
        }
    }
    
    public Interpreter(Object connector, Command[] commands) throws Exception {
        this(connector, commands, System.in, System.out);
    }
    
    public void run(String[] args) throws Exception {
        if (args.length == 0) {
            interactiveMode();
        } else {
            batchMode(args);
        }
    }
    
    private void interactiveMode() throws Exception {
       try (Scanner in = new Scanner(this.in)) {
            List<String[]> inputString = new LinkedList<String[]>();
            XMLParser parser = new XMLParser();
            String request = new String();
            String commandName = new String();
            String[] args = null;
            do {
                this.out.print("$ ");
                request = in.nextLine();
                inputString = parser.parse(request);
                for (String[] cmd : inputString) {
                    commandName = cmd[0];
                    args = new String[cmd.length - 1];
                    for (int i = 1; i < cmd.length; i++) {
                        args[i - 1] = new String(cmd[i]);
                    }
                }
                Command command = commands.get(commandName);
                command.execute(connector, args);
            } while (!commandName.equals("exit"));
        }
    }
    
    void batchMode(String[] args) throws Exception {
        List<String[]> inputString = new LinkedList<String[]>();
        XMLParser parser = new XMLParser();
        String request = new String();
        String commandName = new String();
        for (int i = 0; i < args.length; i++) {
            request += args[i] + " ";
        }
        inputString = parser.parse(request);
        for (String[] cmd : inputString) {
            commandName = cmd[0];
            args = new String[cmd.length - 1];
            for (int i = 1; i < cmd.length; i++) {
                args[i] = new String(cmd[i]);
            }
        }
        Command command = commands.get(commandName);
        command.execute(connector, args);
    }
    
    class XMLParser {
        List<String[]> parse(String args) {
            List<String[]> answer = new LinkedList<String[]>();
            args = args.trim();
            int i = 0;
            while (i < args.length()) {
                String commandBuffer = new String();
                while (args.charAt(i) != '<') {
                    commandBuffer += args.charAt(i);
                    i++;
                }
                commandBuffer = commandBuffer.trim();
                String xmlString = new String();
                while (i < args.length()) {
                    if (args.charAt(i) == ';') {
                        String ending = args.substring(0, i);
                        if (ending.endsWith("</row>")) {
                            break;
                        }
                    }
                    xmlString += args.charAt(i);
                    i++;
                }
                String[] commandName = commandBuffer.split(" ");
                int commandSize = commandName.length;
                if (commandSize == 1) {
                    String[] oneCommand = new String[] {
                            commandName[0],
                            xmlString
                    };
                    answer.add(oneCommand);
                } else if (commandSize == 2) {
                    String[] oneCommand = new String[] {
                            commandName[0],
                            commandName[1],
                            xmlString
                    };
                    answer.add(oneCommand);
                } 
            }
            return answer;
        }
    }
}
    



