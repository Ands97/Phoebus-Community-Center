package com.community_center.community_center.utils.error;

import java.util.List;

class ErrorApplicationDetails {
    public String name;
    public String description;
    public String code;
}

public class ErrorApplication {
    private String process;
    private String message;
    private int code;
    private List<ErrorApplicationDetails> details;

    ErrorApplication(
            String process,
            String message,
            int code,
            List<ErrorApplicationDetails> details
    ) {
        this.process = process;
        this.message = message;
        this.code = code;
        this.details = details;
    }
}
