package com.ken.wms.dao;

import com.ken.wms.domain.AccessRecordDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户登入登出记录因映射器
 *
 * @author Ken
 * @since 2017/3/5.
 */
@Repository
public interface AccessRecordMapper {

    /**
     * 插入一条用户用户登入登出记录
     *
     * @param accessRecordDO 用户登入登出记录
     */
    void insertAccessRecord(AccessRecordDO accessRecordDO);

    /**
     * 选择指定用户ID、记录类型、时间范围的登入登出记录
     *
     * @param userID     用户ID
     * @param accessType 记录类型（登入、登出或所有）
     * @param startDate  记录的起始日期
     * @param endDate    记录的结束日期
     * @return 返回所有符合条件的记录
     */
    List<AccessRecordDO> selectAccessRecords(@Param("userID") Integer userID,
                                             @Param("accessType") String accessType,
                                             @Param("startDate") Date startDate,
                                             @Param("endDate") Date endDate);
}
