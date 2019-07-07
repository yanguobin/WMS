//package com.ken.wms;
//
//import com.ken.wms.common.service.Interface.StockRecordManageService;
//import com.ken.wms.common.service.Interface.SystemLogService;
//import com.ken.wms.dao.AccessRecordMapper;
//import com.ken.wms.dao.StockInMapper;
//import com.ken.wms.dao.StockOutMapper;
//import com.ken.wms.dao.UserOperationRecordMapper;
//import com.ken.wms.domain.*;
//import com.ken.wms.exception.StockRecordManageServiceException;
//import com.ken.wms.exception.SystemLogServiceException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Ken on 2017/4/5.
// */
//@ContextConfiguration(locations = "classpath:config/SpringApplicationConfiguration.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class MapperTest {
//
//    @Autowired
//    private
//    StockInMapper stockInMapper;
//    @Autowired
//    private StockOutMapper stockOutMapper;
//    @Autowired
//    private StockRecordManageService stockRecordManageService;
//    @Autowired
//    private AccessRecordMapper accessRecordMapper;
//    @Autowired
//    private SystemLogService systemLogService;
//    @Autowired
//    private UserOperationRecordMapper userOperationRecordMapper;
//
//    @Test
//    public void selectUserOperationRecordServiceTest() throws SystemLogServiceException {
//        Map<String, Object> result = systemLogService.selectUserOperationRecord(1001, "", "");
//        List<UserOperationRecordDTO> userOperationRecordDTOS = (List<UserOperationRecordDTO>) result.get("data");
//        userOperationRecordDTOS.forEach(System.out::println);
//    }
//
//    @Test
//    public void addUserOperationRecordServiceTest() throws SystemLogServiceException {
//        systemLogService.insertUserOperationRecord(1001, "ken", "insert new row", "fail");
//    }
//
//    @Test
//    public void addUserOperationRecordTest(){
//        UserOperationRecordDO userOperationRecordDO = new UserOperationRecordDO();
//        userOperationRecordDO.setUserID(1001);
//        userOperationRecordDO.setUserName("ken");
//        userOperationRecordDO.setOperationName("set param");
//        userOperationRecordDO.setOperationTime(new Date());
//        userOperationRecordDO.setOperationResult("success");
//
//        userOperationRecordMapper.insertUserOperationRecord(userOperationRecordDO);
//    }
//
//    @Test
//    public void selectUserOperationRecordTest(){
//        List<UserOperationRecordDO> userOperationRecordDOS = userOperationRecordMapper.selectUserOperationRecord(1002, null, null);
//        userOperationRecordDOS.forEach(System.out::println);
//    }
//
//    @Test
//    public void selectAccessRecordServiceTest() throws SystemLogServiceException {
//        Map<String, Object> result = systemLogService.selectAccessRecord(null, "all", "", "");
//        List<AccessRecordDO> accessRecordDOS = (List<AccessRecordDO>) result.get("data");
//        accessRecordDOS.forEach(System.out::println);
//    }
//
//    @Test
//    public void selectAccessRecordByUserIDTest(){
//        List<AccessRecordDO> accessRecordDOS = accessRecordMapper.selectAccessRecords(null, "all", null, null);
//        accessRecordDOS.forEach(System.out::println);
//    }
//
//    @Test
//    public void selectStockRecordTest() throws StockRecordManageServiceException {
//        Map<String, Object> result = stockRecordManageService.selectStockRecord(-1, "", "", "all", 5, 5);
//    }
//
//    @Test
//    public void selectByRepositoryIDAndDateTest() throws ParseException {
//        String startDateString = "2017-04-03";
//        String endDateString = "2017-04-06";
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date startDate = dateFormat.parse(startDateString);
//        Date endDate = dateFormat.parse(endDateString);
//        Date newDate = new Date();
//        System.out.println(endDate);
//        System.out.println(newDate);
//        List<StockInDO> stockInDOS = stockInMapper.selectByRepositoryIDAndDate(1005, startDate, endDate);
//        stockInDOS.forEach(System.out::println);
//    }
//
//    @Test
//    public void selectStockOutByRepositoryIDAndDateTest() {
//        List<StockOutDO> stockOutDOS = stockOutMapper.selectByRepositoryIDAndDate(1003, null, null);
//        stockOutDOS.forEach(System.out::println);
//    }
//}
