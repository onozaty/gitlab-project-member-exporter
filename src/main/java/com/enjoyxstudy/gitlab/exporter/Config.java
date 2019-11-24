package com.enjoyxstudy.gitlab.exporter;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Config {

    @NonNull
    private String gitLabUrl;

    @NonNull
    private String personalAccessToken;

    public static Config of(Path configFilePath) throws JsonParseException, JsonMappingException, IOException {
        return new ObjectMapper().readValue(configFilePath.toFile(), Config.class);
    }
}
