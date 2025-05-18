package com.example.note.response;


public class ResponseApi<T> {

    private T result;
    private ResponseCode code;
    private String message;


    public ResponseApi(T result, ResponseCode code) {
        this.result = result;
        this.code = code;
    }

    public ResponseApi(T result, ResponseCode code, String message) {
        this.result = result;
        this.code = code;
        this.message = message;
    }



    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseCode getCode() {
        return code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
