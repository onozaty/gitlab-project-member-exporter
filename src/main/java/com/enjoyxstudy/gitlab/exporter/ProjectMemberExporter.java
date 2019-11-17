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

public class ProjectMemberExporter {

    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.err.println(
                    "usage: java -jar gitlab-project-member-exporter-all.jar <GitLab url> <access token> <export file path>");
            System.exit(1);
        }

        String gitLabUrl = args[0];
        String accessToken = args[1];
        Path exportFilePath = Paths.get(args[2]);

        System.out.println("Export started.");

        new ProjectMemberExporter().export(gitLabUrl, accessToken, exportFilePath);

        System.out.println("Export completed.");
    }

    public void export(String gitLabUrl, String accessToken, Path exportFilePath) throws IOException {

        Client client = Client.builder()
                .gitLabUrl(gitLabUrl)
                .accessToken(accessToken)
                .build();

        List<ProjectMember> projectMembers = new ProjectMemberCollector(client).collect();

        try (
                Writer writer = Files.newBufferedWriter(exportFilePath, StandardCharsets.UTF_8);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL)) {

            writer.write("\uFEFF"); // BOM

            // Header
            csvPrinter.printRecord("Project", "User: Name", "User: Username", "User: State", "Permission");

            for (ProjectMember projectMember : projectMembers) {

                Project project = projectMember.getProject();
                List<Member> members = projectMember.getMembers();

                for (Member member : members) {
                    csvPrinter.printRecord(
                            project.getNameWithNamespace(),
                            member.getName(),
                            member.getUsername(),
                            member.getState(),
                            member.getPermission());
                }
            }
        }
    }
}
