package com.enjoyxstudy.gitlab.exporter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private int id;

    private String username;

    private String name;

    private String email;

    @JsonProperty("is_admin")
    private boolean isAdmin;

    private String state;

    private String createdAt;

    private String lastSignInAt;

    private String lastActivityOn;
}
