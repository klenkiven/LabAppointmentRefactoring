package edu.tyut.selaba2.management.appointment.service;


import edu.tyut.selaba2.management.appointment.constant.AppointmentConstant;
import edu.tyut.selaba2.management.appointment.domain.VerificationCode;
import org.springframework.stereotype.Service;

/**
 * 验证服务
 *
 * @author KlenKiven
 */
@Service
public class VerificationService {

    private final VerificationCode ver = VerificationCode.getInstance();

    /**
     * 验证验证码是否正确
     *
     * @param verification 验证码
     * @return 状态码
     */
    public int checkVerificationValidity(String verification) {

        if ( ! verification.equals(ver.getVerificationString()))
            return AppointmentConstant.STATUS_FAIL_VERIFICATION;

        return AppointmentConstant.STATUS_SUCCESS;
    }

}
