package edu.tyut.selaba2.management.appointment.mapper;


import edu.tyut.selaba2.management.appointment.domain.Guest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 访客的相关操作的Mapper
 */
public interface GuestMapper {

    /**
     * 检索所有访客的信息
     *
     * @return 访客信息表
     */
    public List<Guest> selectAllGuestsList();

    /**
     * 通过访客ID查询访客的信息
     *
     * @param guestId 访客ID
     * @return 访客的信息
     */
    public Guest selectGuestByID(Long guestId);

    /**
     * 根据访客的电话号码检索访客信息
     *
     * @param phoneNum 电话号码
     * @return 访客的信息
     */
    public Guest selectGuestByPhoneNum(Long phoneNum);

    /**
     * 更新访客的信息
     *
     * @param guest 用户的新信息
     * @return 是否更新成功
     */
    public Boolean updateGuest(Guest guest);

    /**
     * 删除特定访客根据ID
     *
     * @param guestId 访客ID
     * @return 是否成功
     */
    public Boolean deleteGuestByID(Long guestId);

    /**
     * 新增访客
     *
     * @param newGuest 新增访客
     * @return 是否成功
     */
    public Boolean insertGuest(Guest newGuest);
}
