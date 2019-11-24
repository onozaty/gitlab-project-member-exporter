package com.enjoyxstudy.gitlab.exporter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberExporter {

    private final Client client;

    public static void main(String[] args) throws IOException {

        Options options = new Options();
        options.addOption(
                Option.builder("c")
                        .desc("Config file path.")
                        .hasArg()
                        .argName("config file")
                        .required()
                        .build());

        options.addOption(
                Option.builder("pm")
                        .desc("Project members export file path.")
                        .hasArg()
                        .argName("project members file")
                        .build());
        options.addOption(
                Option.builder("p")
                        .desc("Projects export file path.")
                        .hasArg()
                        .argName("projects file")
                        .build());
        options.addOption(
                Option.builder("u")
                        .desc("Users export file path.")
                        .hasArg()
                        .argName("users file")
                        .build());

        CommandLine line;
        try {
            line = new DefaultParser().parse(options, args);

        } catch (ParseException e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            printUsage(options);
            return;
        }

        Path configFilePath = Paths.get(line.getOptionValue("c"));

        Config config = Config.of(configFilePath);

        Client client = Client.builder()
                .gitLabUrl(config.getGitLabUrl())
                .personalAccessToken(config.getPersonalAccessToken())
                .build();

        ProjectMemberExporter projectMemberExporter = new ProjectMemberExporter(client);

        if (line.hasOption("pm")) {

            Path exportFilePath = Paths.get(line.getOptionValue("pm"));

            System.out.println("Project members export has started.");
            projectMemberExporter.exportProjectMembers(exportFilePath);
            System.out.println("Project members export is complete.");
        }

        if (line.hasOption("p")) {

            Path exportFilePath = Paths.get(line.getOptionValue("p"));

            System.out.println("Projects export has started.");
            projectMemberExporter.exportProjects(exportFilePath);
            System.out.println("Projects export is complete.");
        }

        if (line.hasOption("u")) {

            Path exportFilePath = Paths.get(line.getOptionValue("u"));

            System.out.println("Users export has started.");
            projectMemberExporter.exportUsers(exportFilePath);
            System.out.println("Users export is complete.");
        }
    }

    private static void printUsage(Options options) {
        HelpFormatter help = new HelpFormatter();
        help.setWidth(100);

        // ヘルプを出力
        help.printHelp("java -jar gitlab-project-member-exporter-all.jar", options, true);
        System.exit(1);
    }

    public void exportProjectMembers(Path exportFilePath) throws IOException {

        List<ProjectMember> projectMembers = new ProjectMemberCollector(client).collect();

        try (
                Writer writer = Files.newBufferedWriter(exportFilePath, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL)) {

            writer.write("\uFEFF"); // BOM

            // Header
            csvPrinter.printRecord(
                    "Project: Name",
                    "Project: Visibility",
                    "User: Name",
                    "User: Username",
                    "User: State",
                    "Permission");

            for (ProjectMember projectMember : projectMembers) {

                Project project = projectMember.getProject();
                List<Member> members = projectMember.getMembers();

                for (Member member : members) {
                    csvPrinter.printRecord(
                            project.getNameWithNamespace(),
                            project.getVisibility(),
                            member.getName(),
                            member.getUsername(),
                            member.getState(),
                            member.getPermission());
                }
            }
        }
    }

    public void exportProjects(Path exportFilePath) throws IOException {

        List<Project> projects = client.getProjects();

        try (
                Writer writer = Files.newBufferedWriter(exportFilePath, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL)) {

            writer.write("\uFEFF"); // BOM

            // Header
            csvPrinter.printRecord(
                    "Name",
                    "Visibility",
                    "Created at",
                    "Last activity at");

            for (Project project : projects) {

                csvPrinter.printRecord(
                        project.getNameWithNamespace(),
                        project.getVisibility(),
                        project.getCreatedAt(),
                        project.getLastActivityAt());
            }
        }
    }

    public void exportUsers(Path exportFilePath) throws IOException {

        List<User> users = client.getUsers();

        try (
                Writer writer = Files.newBufferedWriter(exportFilePath, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL)) {

            writer.write("\uFEFF"); // BOM

            // Header
            csvPrinter.printRecord(
                    "Name",
                    "Username",
                    "Email",
                    "State",
                    "Admin",
                    "Created at",
                    "Last sign in at",
                    "Last activity on");

            for (User user : users) {

                csvPrinter.printRecord(
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getState(),
                        user.isAdmin(),
                        user.getCreatedAt(),
                        user.getLastSignInAt(),
                        user.getLastActivityOn());
            }
        }
    }
}
