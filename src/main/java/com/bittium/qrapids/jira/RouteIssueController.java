package com.bittium.qrapids.jira;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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

    /** this catches both client side and server side exceptions */
    @ExceptionHandler(value = HttpStatusCodeException.class)
    public void clientBadRequest(HttpServletResponse response, HttpStatusCodeException ex)
            throws IOException {
        response.sendError(ex.getRawStatusCode(), ex.getLocalizedMessage());
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
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                        String.format("Key '%s' not found from %s", key, keys));
            } else {
                if (issue.get(key) == null | issue.get(key).isEmpty()) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
                            String.format("Key '%s' is null or empty", key));
                }
            }
        }

        int errorCode = 200;
        // access JIRA here
        if (errorCode != 200) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "JIRA messed up");
        }

        Map<String, String> response = new HashMap<>();
        response.put("issue_id", "ID-XXXX");
        response.put("issue_url", "http://dojo.bittium.com/ID-XXX");
        return response;
    }
}
