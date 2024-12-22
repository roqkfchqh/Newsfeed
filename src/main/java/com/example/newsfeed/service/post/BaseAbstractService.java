package com.example.newsfeed.service.post;

/**
 * @param <S> response
 * @param <Q> request
 */
public abstract class BaseAbstractService<S, Q> {

    public final S create(Q entity, Long userId){
        userValidate(userId);
        return executeCreate(entity, userId);
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

    //update
    protected abstract S executeUpdate(Q entity, Long userId, Long entityId);

    //delete
    protected abstract void executeDelete(Long entityId);

    //like
    protected abstract void executeLike(Long userId, Long entityId);
    protected abstract void executeDislike(Long userId, Long entityId);
}
