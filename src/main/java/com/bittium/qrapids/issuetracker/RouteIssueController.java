package com.bittium.qrapids.issuetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.bittium.qrapids.issuetracker.jira.JiraAPI;
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
public class RouteIssueController {

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

        JiraAPI jira = new JiraAPI();
        // TODO: RytiVei: Read these from config. Now replace by hand and rebuild for testing.
        jira.createClient("replace_me_SERVER", "replace_me_USER", "replace_me_PASSWORD");

        // TODO: RytiVei: Read projectkey from config. Now replace by hand and rebuild for testing.
        IssueCreatedResponse response =
                jira.createIssue("replace_me_PROJECTKEY", issue.get("issue_type"),
                        issue.get("issue_summary"), issue.get("issue_description"));

        return response;
    }
}
