package com.example.newsfeed.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * @param <S> response
 * @param <Q> request
 * @param <R> readResponseDto << 수정 필요함. 아마도?
 */
public abstract class BaseAbstractService<S, Q, R> {

    public final S create(Q entity, Long userId){
        userValidate(userId);
        return executeCreate(entity, userId);
    }

    public final List<S> getAll(Long userId, Sort sort){
        userValidate(userId);
        return executeGetAll(userId, sort);
    }

    public final Map<S, List<R>> get(Long userId, Pageable pageable){
        userValidate(userId);
        return executeGet(userId, pageable);
    }

    public final S update(Q entity, Long userId, Long entityId){
        userValidate(userId);
        validate(entityId);
        return executeUpdate(entity, userId, entityId);
    }

    public final void delete(Long userId, Long entityId){
        userValidate(userId);
        validate(entityId);
        executeDelete(entityId);
    }

    public final void like(Long userId, Long entityId){
        userValidate(userId);
        validate(entityId);
        executeLike(userId, entityId);
    }

    public final void dislike(Long userId, Long entityId){
        userValidate(userId);
        validate(entityId);
        executeDislike(userId, entityId);
    }

    //validator
    protected abstract void userValidate(Long userId);
    protected abstract void validate(Long entityId);

    //create(save)
    protected abstract S executeCreate(Q entity, Long userId);

    //getAll
    protected abstract List<S> executeGetAll(Long userId, Sort sort);

    //get
    protected abstract Map<S, List<R>> executeGet(Long userId, Pageable pageable);

    //update
    protected abstract S executeUpdate(Q entity, Long userId, Long entityId);

    //delete
    protected abstract void executeDelete(Long entityId);

    //like
    protected abstract void executeLike(Long userId, Long entityId);
    protected abstract void executeDislike(Long userId, Long entityId);
}
