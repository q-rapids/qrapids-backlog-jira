package com.bittium.qrapids.issuetracker.rest;

import java.util.HashMap;

public class CreateIssueResponse extends HashMap<String, String> {

    private static final String ISSUE_URL = "issue_url";
    private static final String ISSUE_ID = "issue_id";
    private static final long serialVersionUID = -8014977994833926176L;

    public CreateIssueResponse() {
        super.put(ISSUE_ID, "");
        super.put(ISSUE_URL, "");
    }

    public CreateIssueResponse(String issueId, String issueUrl) {
        super.put(ISSUE_ID, issueId);
        super.put(ISSUE_URL, issueUrl);
    }
}
