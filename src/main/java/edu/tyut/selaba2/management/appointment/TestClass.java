package edu.tyut.selaba2.management.appointment;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import edu.tyut.selaba2.management.appointment.config.DruidConfig;
import edu.tyut.selaba2.management.appointment.config.MybatisConfig;
import edu.tyut.selaba2.management.appointment.domain.Guest;
import edu.tyut.selaba2.management.appointment.mapper.GuestMapper;
import edu.tyut.selaba2.management.appointment.utils.DBUtils;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TestClass {



    @Test
    public void testInsertGuest() {
        Guest guest = new Guest();

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MybatisConfig.class);

        GuestMapper guestMapper = ctx.getBean(GuestMapper.class);

        guest.setGuestId(2000010001L);
        guest.setName("张三");
        guest.setPhoneNum(13765492451L);
        System.out.println(guestMapper.insertGuest(guest));

        guest.setGuestId(2000010002L);
        guest.setName("李四");
        guest.setPhoneNum(13765492452L);
        System.out.println(guestMapper.insertGuest(guest));


        guest.setGuestId(2000010003L);
        guest.setName("王五");
        guest.setPhoneNum(13765492453L);
        System.out.println(guestMapper.insertGuest(guest));
    }

    @Test
    public void testSelectGuests() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MybatisConfig.class);

        GuestMapper guestMapper = ctx.getBean(GuestMapper.class);

        List<Guest> guests = guestMapper.selectAllGuestsList();
        for (Guest g: guests) {
            System.out.print(g.getGuestId());
            System.out.println(g.getName());
        }
    }

    @Test
    public void testApplicationContext() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(MybatisConfig.class, DruidConfig.class);
        ctx.refresh();
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
        System.out.println(ctx.getBean(MybatisConfig.class));
    }

    @Test
    public void testDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();

        // 配置 druid 的数据源
        druidDataSource.setUsername(getJdbcInfo("jdbc.username"));
        druidDataSource.setPassword(getJdbcInfo("jdbc.password"));
        druidDataSource.setUrl(getJdbcInfo("jdbc.url"));
        druidDataSource.setDriverClassName(getJdbcInfo("jdbc.driverClass"));

        System.out.println(druidDataSource);

        try {
            DruidPooledConnection connection = druidDataSource.getConnection();
            System.out.println(connection);
            System.out.println(druidDataSource);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String getJdbcInfo(String key) {
        InputStream is = DBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        String value = "";
        try {
            pros.load(is);
            value = pros.getProperty(key);
        } catch (IOException e) {
            System.err.println("[ERROR] 文件不存在或者打开异常");
        }

        return value;
    }
}
