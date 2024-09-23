package com.community_center.community_center.utils.error;

import lombok.Data;

import java.util.List;

@Data
public class ErrorApplication {
    private String process;
    private String message;
    private int code;
    private List<ErrorApplicationDetails> details;

    public ErrorApplication(
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
