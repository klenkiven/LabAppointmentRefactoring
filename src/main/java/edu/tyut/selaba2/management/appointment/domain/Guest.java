package edu.tyut.selaba2.management.appointment.domain;

/**
 * 关于数据库映射的对象
 *
 * @author KlenKiven
 */
public class Guest extends BaseDomain {

    private Long guestId;
    private String name;
    private Long phoneNum;

    public Guest() {
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Long phoneNum) {
        this.phoneNum = phoneNum;
    }

}
