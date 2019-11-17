package com.enjoyxstudy.gitlab.exporter;

import java.util.List;

import lombok.Value;

@Value
public class ProjectMember {

    private final Project project;

    private final List<Member> members;
}
