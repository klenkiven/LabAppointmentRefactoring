package edu.tyut.selaba2.management.appointment.controller;

import edu.tyut.selaba2.management.appointment.constant.AppointmentConstant;
import edu.tyut.selaba2.management.appointment.domain.Guest;
import edu.tyut.selaba2.management.appointment.service.GuestService;
import edu.tyut.selaba2.management.appointment.service.SpaceUsageRecordService;
import edu.tyut.selaba2.management.appointment.service.VerificationService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@ComponentScan("edu.tyut.selaba2.management.appointment.service")
public class AppointmentController {

    // Autowired implicitly
    SpaceUsageRecordService spaceUsageRecordService;
    GuestService guestService;
    VerificationService verificationService;

    @PostMapping("/appointment")
    @ResponseBody
    public Map<String, Integer> postAppointmentInfo(@RequestParam(name = "id") Long id,
                                                   @RequestParam(name = "name") String name,
                                                   @RequestParam(name = "userCount") Integer userCount,
                                                   @RequestParam(name = "tel") Long tel,
                                                   @RequestParam(name = "verification") String verification,
                                                   @RequestParam(name = "beginTime") String beginTimeStr,
                                                   @RequestParam(name = "endTime") String endTimeStr) {

        // 返回数据
        Map<String, Integer> status = new HashMap<>();

        // 前端日期数据处理
        Timestamp beginTime = null;
        Timestamp endTime = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            beginTime = new Timestamp(simpleDateFormat.parse(beginTimeStr).getTime());
            endTime = new Timestamp(simpleDateFormat.parse(endTimeStr).getTime());
        } catch (ParseException e) {
            System.out.println("[ERROR] 日期时间转换错误");
        }

        // 业务逻辑处理
        int statusCode = AppointmentConstant.STATUS_SUCCESS;
        int verificationStatus = verificationService.checkVerificationValidity(verification);
        if (verificationStatus == AppointmentConstant.STATUS_SUCCESS){

            int guestCheckStatus = guestService.guestRecord(id, name, tel);
            if (guestCheckStatus == AppointmentConstant.STATUS_SUCCESS) {

                Guest guest = guestService.getGuestByID(id);
                int recordStatus = spaceUsageRecordService.spaceUseRecord(guest, beginTime, endTime, userCount);
                if (recordStatus != AppointmentConstant.STATUS_SUCCESS)
                    statusCode = recordStatus;

            } else
                statusCode = guestCheckStatus;

        } else
            statusCode = verificationStatus;

        status.put("status", statusCode);
        return status;
    }


    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }
}
