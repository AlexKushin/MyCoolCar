package com.mycoolcar.util;

import org.springframework.http.HttpStatusCode;

public record ApiResponse(HttpStatusCode statusCode, String message) {
}
