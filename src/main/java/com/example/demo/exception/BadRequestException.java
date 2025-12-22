// BadRequestException.java
package com.example.demo.exception;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}

// ResourceNotFoundException.java
package com.example.demo.exception;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}