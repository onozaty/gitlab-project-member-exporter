package com.enjoyxstudy.gitlab.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    private int id;

    private String name;

    private String nameWithNamespace;

    private String path;

    private String pathWithNamespace;

    private String visibility;

    private Namespace namespace;

    private String createdAt;

    private String lastActivityAt;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Namespace {

        private int id;

        private String name;

        private String kind;

        private Integer parentId;
    }
}
