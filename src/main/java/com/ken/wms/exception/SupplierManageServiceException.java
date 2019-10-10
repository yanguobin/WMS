package com.ken.wms.exception;

/**
 * SupplierManageService异常
 */
public class SupplierManageServiceException extends BusinessException {

    SupplierManageServiceException() {
        super();
    }

    public SupplierManageServiceException(Exception e) {
        super(e);
    }

    SupplierManageServiceException(Exception e, String exceptionDesc) {
        super(e, exceptionDesc);
    }

}
