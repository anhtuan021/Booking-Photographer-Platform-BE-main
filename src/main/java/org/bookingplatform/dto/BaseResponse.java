package org.bookingplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseResponse<T> {
    
    @JsonProperty("requestTrace")
    private String requestTrace;
    
    @JsonProperty("responseDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime responseDateTime;
    
    @JsonProperty("responseStatus")
    private ResponseStatus responseStatus;
    
    @JsonProperty("responseData")
    private T responseData;
    
    public BaseResponse() {
        this.responseDateTime = LocalDateTime.now();
    }

    public BaseResponse(String requestTrace, T responseData) {
        this();
        this.requestTrace = requestTrace;
        this.responseData = responseData;
        this.responseStatus = new ResponseStatus("00000000", "Success");
    }

    public BaseResponse(String requestTrace, String responseCode, String responseMessage) {
        this();
        this.requestTrace = requestTrace;
        this.responseStatus = new ResponseStatus(responseCode, responseMessage);
    }

    @Builder
    public BaseResponse(String requestTrace, LocalDateTime responseDateTime,
                        ResponseStatus responseStatus, T responseData) {
        this.requestTrace = requestTrace;
        this.responseDateTime = responseDateTime != null ? responseDateTime : LocalDateTime.now();
        this.responseStatus = responseStatus;
        this.responseData = responseData;
    }

    @Data
    @Builder
    public static class ResponseStatus {
        @JsonProperty("responseCode")
        private String responseCode;
        
        @JsonProperty("responseMessage")
        private String responseMessage;

        public ResponseStatus() {}

        public ResponseStatus(String responseCode, String responseMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
        }
    }
}
