package com.ken.wms.dao;

import org.springframework.stereotype.Repository;

/**
 *
 * @author Ken
 * @since 2017/3/1.
 */
@Repository
public interface RolesMapper {

    /**
     * 获取角色对应的ID
     * @param roleName 角色名
     * @return 返回角色的ID
     */
    Integer getRoleID(String roleName);

}
