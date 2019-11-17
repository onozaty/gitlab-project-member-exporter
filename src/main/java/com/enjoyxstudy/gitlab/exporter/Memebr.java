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
public class Memebr {

    private int id;

    private String name;

    private String username;

    private String state;

    private int accessLevel;

    public String getPermission() {

        // https://docs.gitlab.com/ee/api/access_requests.html
        switch (accessLevel) {
            case 10:
                return "Guest";
            case 20:
                return "Reporter";
            case 30:
                return "Developer";
            case 40:
                return "Maintainer";
            case 50:
                return "Owner";
            default:
                throw new IllegalStateException();
        }
    }
}
