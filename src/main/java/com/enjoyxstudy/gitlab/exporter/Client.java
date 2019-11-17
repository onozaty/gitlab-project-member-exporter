package com.enjoyxstudy.gitlab.exporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.Builder;
import lombok.Builder.Default;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Builder
public class Client {

    private final OkHttpClient httpClient = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    private final String gitLabUrl;

    private final String accessToken;

    @Default
    private final int perPage = 100;

    public List<Group> getGroups() throws IOException {

        return get(
                "groups",
                new TypeReference<List<Group>>() {
                });
    }

    public List<Memebr> getGroupMembers(int groupId) throws IOException {

        return get(
                "groups/" + groupId + "/members",
                new TypeReference<List<Memebr>>() {
                });
    }

    public List<Project> getProjects() throws IOException {

        return get(
                "projects",
                new TypeReference<List<Project>>() {
                });
    }

    public List<Memebr> getProjectMembers(int projectId) throws IOException {

        return get(
                "projects/" + projectId + "/members",
                new TypeReference<List<Memebr>>() {
                });
    }

    private <T> List<T> get(String endpoint, TypeReference<List<T>> typeReference)
            throws IOException {

        List<T> results = new ArrayList<>();

        int prevPage = 1;

        while (prevPage > 0) {

            Response response = get(endpoint, prevPage);

            List<T> pageResult = objectMapper.readValue(
                    response.body().string(),
                    typeReference);
            results.addAll(pageResult);

            prevPage = getPrevPage(response);
        }

        return results;
    }

    private Response get(String endpoint, int page)
            throws IOException {

        okhttp3.HttpUrl.Builder httpUrlBuilder = HttpUrl.get(gitLabUrl)
                .resolve("/api/v4/")
                .resolve(endpoint)
                .newBuilder();

        httpUrlBuilder.addQueryParameter("page", String.valueOf(page));
        httpUrlBuilder.addQueryParameter("per_page", String.valueOf(perPage));

        Request request = new Request.Builder()
                .url(httpUrlBuilder.build())
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failed to call GitLab API. " + response);
        }

        return response;
    }

    private int getPrevPage(Response response) {

        String prevPage = response.header("X-Next-Page");
        if (StringUtils.isEmpty(prevPage)) {
            return 0;
        }

        return Integer.valueOf(prevPage);
    }
}
