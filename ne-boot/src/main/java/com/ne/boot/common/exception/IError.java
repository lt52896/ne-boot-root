package com.ne.boot.common.exception;

/**
 * User: Robin
 * Time: 下午4:42
 */
public interface IError {

    String getNamespace();

    String getErrorCode();

    String getErrorMessage();
}
