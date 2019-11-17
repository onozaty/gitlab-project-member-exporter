package com.enjoyxstudy.gitlab.exporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberCollector {

    private final Client client;

    public List<ProjectMember> collect() throws IOException {

        List<ProjectMember> projectMembers = new ArrayList<>();

        Map<Integer, List<Member>> groupMemberMap = getGroupMembersMap();

        List<Project> projects = client.getProjects();
        for (Project project : projects) {

            int groupId = project.getNamespace().getId();

            List<Member> members = new ArrayList<>(client.getProjectMembers(project.getId()));
            if (groupMemberMap.containsKey(groupId)) {
                members.addAll(0, groupMemberMap.get(groupId)); // Groupの方を先頭に
            }

            projectMembers.add(new ProjectMember(project, members));
        }

        return projectMembers;
    }

    private Map<Integer, List<Member>> getGroupMembersMap() throws IOException {

        Map<Integer, List<Member>> groupMembersMap = new HashMap<>();

        List<Group> groups = client.getGroups();

        for (Group group : groups) {

            List<Member> members = client.getGroupMembers(group.getId());
            groupMembersMap.put(group.getId(), members);
        }

        return groupMembersMap;
    }
}
