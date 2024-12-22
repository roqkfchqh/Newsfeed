package com.example.newsfeed.service.validation.normal;

public interface SingleValidationStrategy<T> {
    void validate(T target);
}