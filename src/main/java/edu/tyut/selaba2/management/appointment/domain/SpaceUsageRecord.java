package edu.tyut.selaba2.management.appointment.domain;

import java.sql.Timestamp;

/**
 * 数据库的关于空间使用记录的对象
 *
 * @author KlenKiven
 */
public class SpaceUsageRecord extends BaseDomain {

    private Long guestId;
    private Integer peopleNum;
    private Timestamp startTime;
    private Timestamp endTime;

    public SpaceUsageRecord() {
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

}
