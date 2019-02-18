package com.bittium.qrapids.issuetracker.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import com.bittium.qrapids.issuetracker.interfaces.Jira;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class IssueEndpoints {

    @PostConstruct
    public void init() {
        // TODO: Make better mechanism to inform end user about empty properties
        if (this.jiraServer.isEmpty()) {
            System.out.println("jira.server is EMPTY");
            SpringApplication.exit(this.appContext);
        }
        if (this.jiraUser.isEmpty()) {
            System.out.println("jira.user is EMPTY");
            SpringApplication.exit(this.appContext);
        }
        if (this.jiraPassword.isEmpty()) {
            System.out.println("jira.password is EMPTY");
            SpringApplication.exit(this.appContext);
        }
        if (this.jiraProjectKey.isEmpty()) {
            System.out.println("jira.project.key is EMPTY");
            SpringApplication.exit(this.appContext);
        }
    }

    /** this catches both client side and server side exceptions */
    @ExceptionHandler(value = HttpStatusCodeException.class)
    public void clientBadRequest(HttpServletResponse response, HttpStatusCodeException ex)
            throws IOException {
        response.sendError(ex.getRawStatusCode(), ex.getLocalizedMessage());
    }

    @RequestMapping(value = "/issue/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> createIssueEndpoint(@RequestBody Map<String, String> issue)
            throws HttpStatusCodeException {

        List<String> keys = new ArrayList<>();
        keys.add("issue_summary");
        keys.add("issue_description");
        keys.add("issue_type");

        for (String key : keys) {
            if (!issue.containsKey(key)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                        String.format("[ API ] Key '%s' not found from %s", key, issue));
            } else {
                if (issue.get(key) == null | issue.get(key).isEmpty()) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                            String.format("[ API ] Key '%s' is null or empty", key));
                }
            }
        }

        jira.createClient(this.jiraServer, this.jiraUser, this.jiraPassword);
        CreateIssueResponse response =
                jira.createIssue(this.jiraProjectKey, issue.get("issue_type"),
                        issue.get("issue_summary"), issue.get("issue_description"));

        return response;
    }

    @Value("${jira.server}")
    private String jiraServer;

    @Value("${jira.user}")
    private String jiraUser;

    @Value("${jira.password}")
    private String jiraPassword;

    @Value("${jira.project.key}")
    private String jiraProjectKey;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private Jira jira;
}
