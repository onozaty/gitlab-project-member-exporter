package com.enjoyxstudy.gitlab.exporter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberExporter {

    private final Client client;

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "usage: java -jar gitlab-project-member-exporter-all.jar <config file path> <export file path>");
            System.exit(1);
        }

        Path configFilePath = Paths.get(args[0]);
        Path exportFilePath = Paths.get(args[1]);

        Config config = Config.of(configFilePath);
        Client client = Client.builder()
                .gitLabUrl(config.getGitLabUrl())
                .personalAccessToken(config.getPersonalAccessToken())
                .build();

        System.out.println("Export started.");

        new ProjectMemberExporter(client).export(exportFilePath);

        System.out.println("Export completed.");
    }

    public void export(Path exportFilePath) throws IOException {

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
}
