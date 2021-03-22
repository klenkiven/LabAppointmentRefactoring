package edu.tyut.selaba2.management.appointment.service;


import edu.tyut.selaba2.management.appointment.constant.AppointmentConstant;
import edu.tyut.selaba2.management.appointment.domain.Guest;
import edu.tyut.selaba2.management.appointment.mapper.GuestMapper;
import edu.tyut.selaba2.management.appointment.utils.DBUtils;
import org.springframework.stereotype.Service;

/**
 * 访客服务层的编写
 *
 * @author KlenKiven
 */
@Service
public class GuestService {

//    private final Long MAX_ID = 4000000000L;
    private final GuestMapper guestMapper = (GuestMapper) DBUtils.getMapper(GuestMapper.class);

    /**
     * 用户检测/插入服务
     *
     * @param universeId 工号学号
     * @param name 预约负责人姓名
     * @param phoneNum 预约负责人电话号码
     * @return 状态码
     */
    public int guestRecord(Long universeId, String name, Long phoneNum) {
        Guest guest = guestMapper.selectGuestByID(universeId);

        // 验证手机号是否与工号匹配
        if (guest != null && !guest.getPhoneNum().equals(phoneNum))
            return AppointmentConstant.STATUS_PHONE_NUMBER_INVALIDITY;

        // 访客不存在的话，那就直接安全的新增
        if (guest == null) {
            guest = new Guest();
            guest.setGuestId(universeId);
            guest.setName(name);

            // 验证手机号码没有被使用过
            Guest phoneNumHolder = guestMapper.selectGuestByPhoneNum(phoneNum);
            if (phoneNumHolder != null)
                return AppointmentConstant.STATUS_PHONE_NUMBER_INVALIDITY;

            guest.setPhoneNum(phoneNum);

            if ( ! guestMapper.insertGuest(guest) )
                return AppointmentConstant.STATUS_SERVER_ERROR;
        }

        return AppointmentConstant.STATUS_SUCCESS;
    }

    /**
     * 通过ID获得一个Guest的实例
     *
     * @param Id 访客的ID
     * @return 访客的实例对象
     */
    public Guest getGuestByID(Long Id) {
        return guestMapper.selectGuestByID(Id);
    }
}
