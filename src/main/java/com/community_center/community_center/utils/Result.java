package com.community_center.community_center.utils;

import com.community_center.community_center.utils.error.ErrorApplication;

public class Result<T> {
    public boolean success;
    public T response;
    public ErrorApplication error;

    Result(boolean success) {
        this.success = success;
    }

    private void setError(ErrorApplication error) {
        this.error = error;
    }

    private void setData(T data) {
        this.response = data;
    }

    public static <T> Result<T> error(ErrorApplication errorApplication) {
        Result<T> result = new Result<>(false);
        result.setError(errorApplication);
        return result;
    }

    public static <T> Result<T> success(T response) {
        Result<T> result = new Result<>(true);
        result.setData(response);
        return result;
    }
}
