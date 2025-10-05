package org.bookingplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseRequest<T> {
    @NotNull(message = "Request trace is required")
    @JsonProperty("requestTrace")
    private String requestTrace;
    
    @NotNull(message = "Request date time is required")
    @JsonProperty("requestDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestDateTime;
    
    @JsonProperty("requestParameters")
    private T requestParameters;
    
    public BaseRequest() {
        this.requestTrace = java.util.UUID.randomUUID().toString();
        this.requestDateTime = LocalDateTime.now();
    }
    
    public BaseRequest(T requestParameters) {
        this();
        this.requestParameters = requestParameters;
    }
}
