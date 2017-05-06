package com.seongil.mvplife.sample.repository.exception;

/**
 * @author seong-il, kim
 * @since 17. 4. 21
 */
public class InvalidFirebaseUser extends Throwable {

    public InvalidFirebaseUser(String message) {
        super(message);
    }
}
