package edu.tyut.selaba2.management.appointment.domain;

import java.sql.Timestamp;

public class VerificationCode {

    private String VERIFICATION_STRING;
    private static final VerificationCode verificationCode = new VerificationCode();
    private Timestamp createTime;

    private VerificationCode() {
    }

    public static VerificationCode getInstance() {
        return verificationCode;
    }

    public  String getVerificationString() {
        return VERIFICATION_STRING;
    }

    public void setVerificationString(String verificationString) {
        this.VERIFICATION_STRING = verificationString;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
