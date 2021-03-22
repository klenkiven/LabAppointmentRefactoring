package edu.tyut.selaba2.management.appointment.task;


import edu.tyut.selaba2.management.appointment.domain.VerificationCode;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class VerificationGenTask {

    private String verificationCode = UUID.randomUUID().toString().substring(0, 8);
    private final VerificationCode ver = VerificationCode.getInstance();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> taskHandle;

    /**
     * 开始生成随机验证码的任务
     */
    public void startTask() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                verificationCode = UUID.randomUUID().toString().substring(0, 8);
                ver.setVerificationString(verificationCode);
            }
        };

        // 设定定期任务
        // TODO 这里发生了写死的问题，需要注解来解决 暂定5分钟
        taskHandle = scheduler.scheduleAtFixedRate(runnable, 1, 300, TimeUnit.SECONDS);
    }

    /**
     * 结束生成随机验证码的任务
     */
    public void endTask() {
        scheduler.shutdown();
    }
}
