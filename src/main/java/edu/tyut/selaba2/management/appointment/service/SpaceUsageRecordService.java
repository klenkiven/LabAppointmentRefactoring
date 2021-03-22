package edu.tyut.selaba2.management.appointment.service;



import edu.tyut.selaba2.management.appointment.constant.AppointmentConstant;
import edu.tyut.selaba2.management.appointment.domain.Guest;
import edu.tyut.selaba2.management.appointment.domain.SpaceUsageRecord;
import edu.tyut.selaba2.management.appointment.mapper.SpaceUsageRecordMapper;
import edu.tyut.selaba2.management.appointment.utils.DBUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * 预约记录服务
 * @author KlenKiven
 */
@Service
public class SpaceUsageRecordService {

    private final SpaceUsageRecordMapper spaceUsageRecordMapper = (SpaceUsageRecordMapper) DBUtils.getMapper(SpaceUsageRecordMapper.class);
    private final QQRobotService qqRobotService = new QQRobotService();

    /**
     * 预约记录
     * TODO 没有做事务操作，容易发生同步问题
     *
     * @param guest     已经过验证的安全访客
     * @param startTime 预约开始时间
     * @param endTime   预约结束时间
     * @param peopleNum 预约人数
     * @return 状态码
     */
    public int spaceUseRecord(Guest guest, Timestamp startTime, Timestamp endTime, int peopleNum) {

        // 获取已验证用户的相关信息
        Long universeId = guest.getGuestId();
        String name = guest.getName();
        Long phoneNum = guest.getPhoneNum();

        // 验证人数是否合法
        if (peopleNum <= 0 || peopleNum > AppointmentConstant.PEOPLE_MAX_CAPACITY)
            return AppointmentConstant.STATUS_PEOPLE_COUNT_ERROR;

        // 配置预约记录
        SpaceUsageRecord spaceUsageRecord = new SpaceUsageRecord();
        spaceUsageRecord.setStartTime(startTime);
        spaceUsageRecord.setEndTime(endTime);
        spaceUsageRecord.setPeopleNum(peopleNum);
        spaceUsageRecord.setGuestId(universeId);

        // 时间是否合法
        int statusCode = checkTimeValidity(spaceUsageRecord);
        if (statusCode != AppointmentConstant.STATUS_SUCCESS)
            return statusCode;

        Boolean updateResult = spaceUsageRecordMapper.insertRecord(spaceUsageRecord);
        if (updateResult) {

            // TODO 深度耦合的代码部分，想办法移出去
            String message = qqRobotService.composeMessage(guest, spaceUsageRecord);
            qqRobotService.messagePost(message);

            return AppointmentConstant.STATUS_SUCCESS;
        }
        else
            return AppointmentConstant.STATUS_SERVER_ERROR;
    }

    /**
     * 记录的验证合法性
     * 检验记录时间是否冲突的问题
     *
     * @param spaceUsageRecord 预约记录
     * @return 预约记录状态码
     */
    private int checkTimeValidity(SpaceUsageRecord spaceUsageRecord) {

        Timestamp appointStartTime = spaceUsageRecord.getStartTime();
        Timestamp appointEndTime = spaceUsageRecord.getEndTime();

        // 基础情况：开始时间是不是晚于结束时间
        if (appointEndTime.getTime() < appointStartTime.getTime())
            return AppointmentConstant.STATUS_ABNORMAL_START_END_TIME;

        // 基础情况：开始时间早于今天
        if (appointStartTime.getTime() < System.currentTimeMillis())
            return AppointmentConstant.STATUS_ABNORMAL_START_END_TIME;

        SpaceUsageRecord selectConfig = new SpaceUsageRecord();


        // 时间碰撞情况一：下一个的开始时间，早于当前的结束时间
        selectConfig.setEndTime(appointStartTime);
        List<SpaceUsageRecord> spaceUsageRecords = spaceUsageRecordMapper.selectRecordByCondition(selectConfig);

        long firstOneStartTime = Long.MAX_VALUE;
        if (spaceUsageRecords.size() != 0)
            firstOneStartTime = spaceUsageRecords.get(0).getStartTime().getTime();

        long currentEndTime = appointEndTime.getTime();
        if (firstOneStartTime <= currentEndTime)
            return AppointmentConstant.STATUS_TIME_CONFLICT;


        // 时间碰撞情况二：上一个的结束时间，晚于当前的开始时间
        selectConfig.setEndTime(null);
        selectConfig.setStartTime(appointEndTime);
        spaceUsageRecords = spaceUsageRecordMapper.selectRecordByCondition(selectConfig);

        long lastOneEndTime = 0;
        if (spaceUsageRecords.size() != 0)  // 判断为空的问题，否则可能出现问题
            lastOneEndTime = spaceUsageRecords.get(spaceUsageRecords.size() - 1).getEndTime().getTime();

        long currentStartTime = appointStartTime.getTime();
        if (lastOneEndTime >= currentStartTime)
            return AppointmentConstant.STATUS_TIME_CONFLICT;

        return AppointmentConstant.STATUS_SUCCESS;
    }
}