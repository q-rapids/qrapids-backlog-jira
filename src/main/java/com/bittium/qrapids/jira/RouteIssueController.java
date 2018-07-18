package com.bittium.qrapids.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class RouteIssueController {

    @ExceptionHandler(HttpServerErrorException.class)
    public void errorFromJira(HttpServerErrorException ex) {
        // HttpStatus responseStatus = ex.getStatusCode();
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public void clientError(HttpClientErrorException ex) {
        // HttpStatus responseStatus = ex.getStatusCode();
    }

    @RequestMapping(value = "/issue/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> createIssue(@RequestBody Map<String, String> issue)
            throws HttpStatusCodeException {

        List<String> keys = new ArrayList<>();
        keys.add("issue_summary");
        keys.add("issue_description");
        keys.add("issue_type");

        for (String key : keys) {
            if (!issue.containsKey(key)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } else {
                if (issue.get(key) == null | issue.get(key).isEmpty()) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
            }
        }

        int errorCode = 200;
        // access JIRA here
        if (errorCode != 200) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Map<String, String> response = new HashMap<>();
        response.put("issue_id", "ID-XXXX");
        response.put("issue_url", "http://dojo.bittium.com/ID-XXX");
        return response;
    }
}
