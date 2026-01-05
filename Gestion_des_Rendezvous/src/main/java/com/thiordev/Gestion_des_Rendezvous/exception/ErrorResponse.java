package com.thiordev.Gestion_des_Rendezvous.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
public class ErrorResponse {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;


        List<FieldError> fieldErrors;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class FieldError {
            private String fieldName;
            private String errorMessage;
            private Object rejectedValue;
        }

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

}
