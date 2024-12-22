package com.example.newsfeed.service.validation.normal;

public interface DualValidationStrategy<T> {
    void validate(T target, T target2);
}
