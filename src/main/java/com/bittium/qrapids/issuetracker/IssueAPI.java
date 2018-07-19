package com.bittium.qrapids.issuetracker;

import com.atlassian.jira.rest.client.api.JiraRestClient;

/**
 * TODO: RytiVei: in future this will have multiple bounded types
 *
 * public interface IssueAPI<T extends JiraRestClient & RedmineRestClient>
 *
 * OR
 *
 * you can have a interface to hide the issuetracker interfaces and just bound to that interface
 * public interface IssueAPI<T extends IssueBase>
 */
public interface IssueAPI<T extends JiraRestClient> {

    public void createClient(String serverURI, String username, String password);

    public IssueCreatedResponse createIssue(String projectname, String type, String summary,
            String description);
}
