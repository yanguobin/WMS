package com.ken.wms.exception;

/**
 * GoodsManageService异常
 */
public class GoodsManageServiceException extends BusinessException {

    GoodsManageServiceException() {
        super();
    }

    public GoodsManageServiceException(Exception e) {
        super(e);
    }

    GoodsManageServiceException(Exception e, String exceptionDesc) {
        super(e, exceptionDesc);
    }

}
