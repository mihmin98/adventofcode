package org.mihmin98;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.mihmin98.solutions.Solution;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class App {
    public static Options buildOptions() {
        Options options = new Options();

        options.addOption("h", "help", false, "Prints this message");

        options.addOption(Option.builder("d").longOpt("day")
                .desc("Day of the AOC")
                .hasArg()
                .argName("DAY")
                .required()
                .type(Integer.class)
                .build());

        options.addOption(Option.builder("i").longOpt("input")
                .desc("Path to the input file")
                .hasArg()
                .argName("INPUT_PATH")
                .required()
                .type(String.class)
                .build());

        return options;
    }

    public static Path getJarRelativePath() throws URISyntaxException {
        String cwdString = System.getProperty("user.dir");
        Path cwdPath = Paths.get(cwdString);

        URI execUri = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        Path execPath = Paths.get(execUri);

        return cwdPath.relativize(execPath);
    }

    public static boolean checkForHelpOption(final Option help, final String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(help);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);
        return cmd.hasOption(help.getOpt());
    }

    public static void printHelp(Options options) {
        try {
            Path jarRelativePath = getJarRelativePath();
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.setOptionComparator(null);
            helpFormatter.printHelp("java -jar " + jarRelativePath, options, true);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Solution getDaySolution(CommandLine cmdLine) {
        int dayInt = Integer.parseInt(cmdLine.getOptionValue("day"));
        if (dayInt < 1 || dayInt > 25) {
            throw new RuntimeException("Invalid day: " + dayInt);
        }
        String dayStr = StringUtils.leftPad(Integer.toString(dayInt), 2, "0");
        String className = String.format("org.mihmin98.solutions.Day%s", dayStr);

        try {
            Class<?> solutionClass = Class.forName(className);
            return (Solution) solutionClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readInputLines(String inputPathStr) {
        Path inputPath = Paths.get(inputPathStr);
        try {
            return Files.readAllLines(inputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Options options = buildOptions();

        try {
            if (checkForHelpOption(options.getOption("help"), args)) {
                printHelp(options);
                return;
            }
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);

            Solution solutionInstance = getDaySolution(cmd);
            List<String> input = readInputLines(cmd.getOptionValue("input"));

            System.out.println("Part 1:");
            solutionInstance.solvePart1(input);
            System.out.println("\nPart 2:");
            solutionInstance.solvePart2(input);

        } catch (ParseException e) {
            System.out.println(e.getClass().getCanonicalName() + ": " + e.getMessage());
            printHelp(options);
            return;
        }
    }
}
