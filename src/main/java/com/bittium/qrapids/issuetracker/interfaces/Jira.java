package com.bittium.qrapids.issuetracker.interfaces;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.AssigneeType;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.bittium.qrapids.issuetracker.rest.CreateIssueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Jira implements IssueTracker<JiraRestClient> {

    @PostConstruct
    public void init() {
        // TODO: Make better mechanism to inform end user about empty properties
        if (this.jiraProjectComponentName.isEmpty()) {
            System.out.println("jira.project.component.name is NULL");
            SpringApplication.exit(this.appContext);
        }
        if (this.jiraProjectComponentDescription.isEmpty()) {
            System.out.println("jira.project.component.description is NULL");
            SpringApplication.exit(this.appContext);
        }
        if (this.jiraProjectComponentLead.isEmpty()) {
            System.out.println("jira.project.component.lead is NULL");
            SpringApplication.exit(this.appContext);
        }
    }

    public void createClient(String serverURI, String username, String password) {
        final JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
        this.mClient = jiraRestClientFactory
                .createWithBasicHttpAuthentication(URI.create(serverURI), username, password);
    }

    public CreateIssueResponse createIssue(String projectname, String type, String summary,
            String description) {

        CreateIssueResponse response = new CreateIssueResponse();

        BasicProject project = null;
        for (BasicProject p : this.mClient.getProjectClient().getAllProjects().claim()) {
            if (p.getKey().equals(projectname)) {
                project = p;
            }
        }
        if (project == null) {
            // TODO: RytiVei: Throw exception and handle it
        }

        IssueType issueType = null;
        for (IssueType t : this.mClient.getMetadataClient().getIssueTypes().claim()) {
            if (t.getName().equals(type)) {
                issueType = t;
            }
        }
        if (issueType == null) {
            // TODO: RytiVei: Throw exception and handle it
        }

        ComponentInput componentInput = new ComponentInput(this.jiraProjectComponentName,
                this.jiraProjectComponentDescription, this.jiraProjectComponentLead,
                AssigneeType.UNASSIGNED);
        this.mClient.getComponentClient().createComponent(project.getKey(), componentInput);
        List<String> components = new ArrayList<>();
        components.add(this.jiraProjectComponentName);
        IssueInputBuilder inputBuilder = new IssueInputBuilder(project, issueType, summary);
        inputBuilder.setDescription(description);
        inputBuilder.setComponentsNames(components);
        IssueInput input = inputBuilder.build();
        BasicIssue basicIssue = this.mClient.getIssueClient().createIssue(input).claim();
        response = new CreateIssueResponse(basicIssue.getKey(), basicIssue.getSelf().toString());

        return response;
    }

    @Value("${jira.project.component.name}")
    private String jiraProjectComponentName;

    @Value("${jira.project.component.description}")
    private String jiraProjectComponentDescription;

    @Value("${jira.project.component.lead}")
    private String jiraProjectComponentLead;

    @Autowired
    private ApplicationContext appContext;

    private JiraRestClient mClient;
}
