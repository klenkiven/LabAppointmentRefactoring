package edu.tyut.selaba2.management.appointment.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

    public static SqlSessionFactory sqlSessionFactory = genSessionFactory();

    /**
     * 根据 resources 里面的 JDBC 文件获得数据库的链接
     *
     * @return MySQL Connection
     */
    @Deprecated
    public static Connection getConnection() {
        InputStream is = DBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        Connection conn = null;
        try {
            pros.load(is);
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");
            // 在测试环境中, SPI服务并未正常载入驱动程序
            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            System.err.println("[ERROR] 文件不存在或者打开异常");
        } catch (SQLException throwable) {
            System.err.println("[ERROR] 未正常获得SQL连接");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] 无法正常载入Mariadb Driver");
        }
        return conn;
    }

    /**
     * 利用 Mybatis 的接口绑定技术，反射式的获得一个 Mapper 的对象实例
     *
     * @param classObj 接口的Class对象
     * @return mapper对象
     */
    public static Object getMapper(Class<?> classObj) {
        SqlSessionFactory sessionFactory = sqlSessionFactory;
        // TODO 这里是自动提交的，到时候验证数据的时候，可能会有问题，也可能没有，哭了
        SqlSession sqlSession = sessionFactory.openSession(true);

        // 通过mybatis获得一个Mapper的实例
        Object mapper = sqlSession.getMapper(classObj);
        return mapper;
    }

    /**
     * Get A SqlSessionFactory
     *
     * @return A SqlSessionFactory
     */
    private static SqlSessionFactory genSessionFactory() {
        InputStream inputStream = null;
        SqlSessionFactory sqlSessionFactory = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            System.err.println("[ERROR] Resource can't be found or read!");
        }
        return sqlSessionFactory;
    }
}
