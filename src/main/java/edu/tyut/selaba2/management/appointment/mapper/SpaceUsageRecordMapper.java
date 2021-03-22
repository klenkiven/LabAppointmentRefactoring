package edu.tyut.selaba2.management.appointment.mapper;


import edu.tyut.selaba2.management.appointment.domain.SpaceUsageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预约系统Mapper
 *
 * @author KlenKiven
 */
public interface SpaceUsageRecordMapper {

    /**
     * 检索所有的历史预约记录
     *
     * @return 预约记录列表
     */
    public List<SpaceUsageRecord> selectAllRecord();

    /**
     * 根据条件检索预约记录
     *
     * @param spaceUsageRecord 检索条件
     * @return 预约记录列表
     */
    public List<SpaceUsageRecord> selectRecordByCondition(SpaceUsageRecord spaceUsageRecord);

    /**
     * 插入预约记录
     *
     * @param spaceUsageRecord 插入的对象
     * @return 是否成功
     */
    public Boolean insertRecord(SpaceUsageRecord spaceUsageRecord);

    /**
     * 修改预约记录 强烈不建议使用这个
     *
     * @param spaceUsageRecord 修改的对象
     * @return 是否成功
     */
    @Deprecated
    public Boolean updateRecord(SpaceUsageRecord spaceUsageRecord);

    /**
     * 删除预约记录 强烈不建议使用
     *
     * @param recordId 记录的ID
     * @return 是否成功
     */
    @Deprecated
    public Boolean deleteRecord(Long recordId);

}
