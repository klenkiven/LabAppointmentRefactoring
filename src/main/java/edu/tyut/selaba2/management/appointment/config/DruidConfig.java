package edu.tyut.selaba2.management.appointment.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import edu.tyut.selaba2.management.appointment.utils.DBUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DruidConfig {

    @Bean
    public DataSource getDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();

        // 配置 druid 的数据源
        druidDataSource.setUsername(getJdbcInfo("jdbc.username"));
        druidDataSource.setPassword(getJdbcInfo("jdbc.password"));
        druidDataSource.setUrl(getJdbcInfo("jdbc.url"));
        druidDataSource.setDriverClassName(getJdbcInfo("jdbc.driverClass"));

        return druidDataSource;
    }

    /**
     * 获取 jdbc 配置文件中的相关信息
     *
     * @param key 配置名
     * @return 配置的值
     */
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
