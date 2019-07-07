package com.ken.wms.dao;

import com.ken.wms.domain.UserOperationRecordDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户操作记录Mapper
 *
 * @author Ken
 * @since 2017/4/9.
 */
@Repository
public interface UserOperationRecordMapper {

    /**
     * 选择指定用户ID，或时间范围的用户操作记录
     *
     * @param userID    指定的用户ID
     * @param startDate 记录的起始日期
     * @param endDate   记录的结束日期
     * @return 返回所有符合条件的记录
     */
    List<UserOperationRecordDO> selectUserOperationRecord(@Param("userID") Integer userID,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);

    /**
     * 插入用户操作记录
     *
     * @param userOperationRecordDO 用户操作记录
     */
    void insertUserOperationRecord(UserOperationRecordDO userOperationRecordDO);
}
