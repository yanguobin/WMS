package com.ken.wms.exception;

/**
 * RepositoryAdminManageService异常
 *
 * @author Ken
 * @since 2017/3/8.
 */
public class RepositoryAdminManageServiceException extends BusinessException {

    public RepositoryAdminManageServiceException(){
        super();
    }

    public RepositoryAdminManageServiceException(Exception e){
        super(e);
    }

    public RepositoryAdminManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public RepositoryAdminManageServiceException(String exceptionDesc){
        super(exceptionDesc);
    }
}
