package com.ken.wms.exception;

/**
 * 系统操作日志 Service 异常
 */
public class SystemLogServiceException extends BusinessException {

    public SystemLogServiceException() {
        super();
    }

    public SystemLogServiceException(Exception e) {
        super(e);
    }

    public SystemLogServiceException(Exception e, String exceptionDesc) {
        super(e, exceptionDesc);
    }

    public SystemLogServiceException(String exceptionDesc) {
        super(exceptionDesc);
    }
}
