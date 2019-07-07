package com.ken.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.common.service.Interface.SystemLogService;
import com.ken.wms.dao.AccessRecordMapper;
import com.ken.wms.dao.UserOperationRecordMapper;
import com.ken.wms.domain.AccessRecordDO;
import com.ken.wms.domain.AccessRecordDTO;
import com.ken.wms.domain.UserOperationRecordDO;
import com.ken.wms.domain.UserOperationRecordDTO;
import com.ken.wms.exception.SystemLogServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统操作日志 Service 实现
 *
 * @author Ken
 * @since 2017/4/7.
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private AccessRecordMapper accessRecordMapper;
    @Autowired
    private UserOperationRecordMapper userOperationRecordMapper;

    /**
     * 插入用户登入登出记录
     *
     * @param userID     用户ID
     * @param userName   用户名
     * @param accessIP   登陆IP
     * @param accessType 记录类型
     */
    @Override
    public void insertAccessRecord(Integer userID, String userName, String accessIP, String accessType) throws SystemLogServiceException {
        // 创建 AccessRecordDO 对象
        AccessRecordDO accessRecordDO = new AccessRecordDO();
        accessRecordDO.setUserID(userID);
        accessRecordDO.setUserName(userName);
        accessRecordDO.setAccessTime(new Date());
        accessRecordDO.setAccessIP(accessIP);
        accessRecordDO.setAccessType(accessType);

        // 持久化 AccessRecordDO 对象到数据库
        try {
            accessRecordMapper.insertAccessRecord(accessRecordDO);
        } catch (PersistenceException e) {
            throw new SystemLogServiceException(e, "Fail to persist AccessRecordDO Object");
        }
    }

    /**
     * 选择指定用户ID、记录类型或日期范围的登入登出记录
     *
     * @param userID       用户ID
     * @param accessType   记录类型
     * @param startDateStr 记录起始日期
     * @param endDateStr   记录结束日期
     * @return 返回一个Map， 其中键值为 data 的值为所有符合条件的记录， 而键值为 total 的值为符合条件的记录总条数
     */
    @Override
    public Map<String, Object> selectAccessRecord(Integer userID, String accessType, String startDateStr, String endDateStr) throws SystemLogServiceException {
        return selectAccessRecord(userID, accessType, startDateStr, endDateStr, -1, -1);
    }

    /**
     * 分页查询指定用户ID、记录类型或日期范围的登入登出记录
     *
     * @param userID       用户ID
     * @param accessType   记录类型
     * @param startDateStr 记录起始日期
     * @param endDateStr   记录结束日期
     * @param offset       分页偏移值
     * @param limit        分页大小
     * @return 返回一个Map， 其中键值为 data 的值为所有符合条件的记录， 而键值为 total 的值为符合条件的记录总条数
     */
    @Override
    public Map<String, Object> selectAccessRecord(Integer userID, String accessType, String startDateStr, String endDateStr, int offset, int limit) throws SystemLogServiceException {
        // 准备结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<AccessRecordDTO> accessRecordDTOS = new ArrayList<>();
        long total = 0;
        boolean isPagination = true;

        // 检查是否需要分页查询
        if (offset < 0 || limit < 0)
            isPagination = false;

        // 转换 Date 对象
        Date startDate = null;
        Date endDate = null;
        Date newEndDate = null;
        try {
            if (StringUtils.isNotEmpty(startDateStr))
                startDate = dateFormatSimple.parse(startDateStr);
            if (StringUtils.isNotEmpty(endDateStr))
            {
                endDate = dateFormatSimple.parse(endDateStr);
                newEndDate = new Date(endDate.getTime()+(24*60*60*1000)-1);
            }
        } catch (ParseException e) {
            throw new SystemLogServiceException(e, "Fail to convert string to Date Object");
        }

        // 转换 accessType
        switch (accessType) {
            case "loginOnly":
                accessType = SystemLogService.ACCESS_TYPE_LOGIN;
                break;
            case "logoutOnly":
                accessType = SystemLogService.ACCESS_TYPE_LOGOUT;
                break;
            default:
                accessType = "all";
                break;
        }

        // 执行查询操作
        List<AccessRecordDO> accessRecordDOS;
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                accessRecordDOS = accessRecordMapper.selectAccessRecords(userID, accessType, startDate, newEndDate);
                if (accessRecordDOS != null) {
                    accessRecordDOS.forEach(accessRecordDO -> accessRecordDTOS.add(convertAccessRecordDOToAccessRecordDTO(accessRecordDO)));
                    total = new PageInfo<>(accessRecordDOS).getTotal();
                }
            } else {
                accessRecordDOS = accessRecordMapper.selectAccessRecords(userID, accessType, startDate, endDate);
                if (accessRecordDOS != null) {
                    accessRecordDOS.forEach(accessRecordDO -> accessRecordDTOS.add(convertAccessRecordDOToAccessRecordDTO(accessRecordDO)));
                    total = accessRecordDOS.size();
                }
            }
        } catch (PersistenceException e) {
            throw new SystemLogServiceException(e);
        }

        resultSet.put("data", accessRecordDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 插入用户操作记录
     *
     * @param userID          执行操作的用户ID
     * @param userName        执行操作的用户名
     * @param operationName   操作的名称
     * @param operationResult 操作的记过
     */
    @Override
    public void insertUserOperationRecord(Integer userID, String userName, String operationName, String operationResult) throws SystemLogServiceException {
        // 创建 UserOperationRecordDO 对象
        UserOperationRecordDO userOperationRecordDO = new UserOperationRecordDO();
        userOperationRecordDO.setUserID(userID);
        userOperationRecordDO.setUserName(userName);
        userOperationRecordDO.setOperationName(operationName);
        userOperationRecordDO.setOperationResult(operationResult);
        userOperationRecordDO.setOperationTime(new Date());

        // 将数据持久化到数据库
        try {
            userOperationRecordMapper.insertUserOperationRecord(userOperationRecordDO);
        } catch (PersistenceException e) {
            throw new SystemLogServiceException(e, "Fail to persist usrOperationRecordDo Object");
        }
    }

    /**
     * 查询指定用户ID或日期范围的用户操作记录
     *
     * @param userID       用户ID
     * @param startDateStr 记录的起始日期
     * @param endDateStr   记录的结束日期
     * @return 返回一个Map， 其中键值为 data 的值为所有符合条件的记录， 而键值为 total 的值为符合条件的记录总条数
     */
    @Override
    public Map<String, Object> selectUserOperationRecord(Integer userID, String startDateStr, String endDateStr) throws SystemLogServiceException {
        return selectUserOperationRecord(userID, startDateStr, endDateStr, -1, -1);
    }

    /**
     * 分页查询指定用户ID或日期范围的用户操作记录
     *
     * @param userID       用户ID
     * @param startDateStr 记录的起始日期
     * @param endDateStr   记录的结束日期
     * @param offset       分页的偏移值
     * @param limit        分页的大小
     * @return 返回一个Map， 其中键值为 data 的值为所有符合条件的记录， 而键值为 total 的值为符合条件的记录总条数
     */
    @Override
    public Map<String, Object> selectUserOperationRecord(Integer userID, String startDateStr, String endDateStr, int offset, int limit) throws SystemLogServiceException {
        // 准备结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<UserOperationRecordDTO> userOperationRecordDTOS = new ArrayList<>();
        long total = 0;
        boolean isPaginarion = true;

        // 检查是否需要分页
        if (offset < 0 && limit < 0)
            isPaginarion = false;

        // Date 转换
        Date startDate = null;
        Date endDate = null;
        Date newEndDate = null;
        try {
            if (StringUtils.isNotEmpty(startDateStr))
                startDate = dateFormatSimple.parse(startDateStr);
            if (StringUtils.isNotEmpty(endDateStr))
            {
                endDate = dateFormatSimple.parse(endDateStr);
                newEndDate = new Date(endDate.getTime()+(24*60*60*1000)-1);
            }
        } catch (ParseException e) {
            throw new SystemLogServiceException(e, "Fail to convert String format date to Date Object");
        }

        // 执行查询操作
        List<UserOperationRecordDO> userOperationRecordDOS;
        try {
            if (isPaginarion) {
                PageHelper.offsetPage(offset, limit);
                userOperationRecordDOS = userOperationRecordMapper.selectUserOperationRecord(userID, startDate, newEndDate);
                if (userOperationRecordDOS != null) {
                    userOperationRecordDOS.forEach(userOperationRecordDO -> userOperationRecordDTOS.add(convertUserOperationRecordDOToUserOperationRecordDTO(userOperationRecordDO)));
                    total = new PageInfo<>(userOperationRecordDOS).getTotal();
                }
            } else {
                userOperationRecordDOS = userOperationRecordMapper.selectUserOperationRecord(userID, startDate, endDate);
                if (userOperationRecordDOS != null)
                    userOperationRecordDOS.forEach(userOperationRecordDO -> userOperationRecordDTOS.add(convertUserOperationRecordDOToUserOperationRecordDTO(userOperationRecordDO)));
            }
        } catch (PersistenceException e) {
            throw new SystemLogServiceException(e);
        }

        resultSet.put("data", userOperationRecordDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * Date 具体格式
     */
    private DateFormat dateFormatDetail = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    private DateFormat dateFormatSimple = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 将 AccessRecordDO 对象转换为 AccessRecordDTO 对象
     *
     * @param accessRecordDO AccessRecordDO 对象
     * @return 返回 AccessRecordDTO 对象
     */
    private AccessRecordDTO convertAccessRecordDOToAccessRecordDTO(AccessRecordDO accessRecordDO) {
        AccessRecordDTO accessRecordDTO = new AccessRecordDTO();
        accessRecordDTO.setId(accessRecordDO.getId());
        accessRecordDTO.setUserID(accessRecordDO.getUserID());
        accessRecordDTO.setUserName(accessRecordDO.getUserName());
        accessRecordDTO.setAccessIP(accessRecordDO.getAccessIP());
        accessRecordDTO.setAccessType(accessRecordDO.getAccessType().equals(SystemLogService.ACCESS_TYPE_LOGIN) ? "登入" : "登出");
        accessRecordDTO.setAccessTime(dateFormatDetail.format(accessRecordDO.getAccessTime()));
        return accessRecordDTO;
    }

    /**
     * 将 UserOperationRecordDO 对象转换为 UserOperationRecordDTO 对象
     *
     * @param userOperationRecordDO UserOperationRecordDO 对象
     * @return 返回 UserOperationRecordDTO 对象
     */
    private UserOperationRecordDTO convertUserOperationRecordDOToUserOperationRecordDTO(UserOperationRecordDO userOperationRecordDO) {
        UserOperationRecordDTO userOperationRecordDTO = new UserOperationRecordDTO();
        userOperationRecordDTO.setId(userOperationRecordDO.getId());
        userOperationRecordDTO.setUserID(userOperationRecordDO.getUserID());
        userOperationRecordDTO.setUserName(userOperationRecordDO.getUserName());
        userOperationRecordDTO.setOperationName(userOperationRecordDO.getOperationName());
        userOperationRecordDTO.setOperationResult(userOperationRecordDO.getOperationResult());
        userOperationRecordDTO.setOperationTime(dateFormatDetail.format(userOperationRecordDO.getOperationTime()));
        return userOperationRecordDTO;
    }
}
