package com.bittium.qrapids.jira;

import java.net.URI;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JiraAPI implements IssueAPI<JiraRestClient> {
    public JiraRestClient createClient() {
        final JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
        final JiraRestClient jiraRestClient =
                jiraRestClientFactory.createWithBasicHttpAuthentication(
                        URI.create("http://myjira.mycompany.com"), "scott", "abc123");
        // Promise<Iterable> projects = jiraRestClient.getProjectClient().getAllProjects();
        // try {
        // for (BasicProject project : projects.get()) {
        // System.out.println("ProjectName:" + project.getName());
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return jiraRestClient;
    }
}
