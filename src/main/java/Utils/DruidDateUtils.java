package Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidDateUtils {
    //使用Druid数据库连接池技术获取数据库连接
    public static DataSource ds = null;
    //如果资源只需要加载一次，后续使用的都是同样的资源，可以直接写在静态代码块里面
    static {
        try {
            //加载properties文件的内容到Properties对象中
            Properties pp = new Properties();
            FileInputStream fis = new FileInputStream(new File("E:\\javaweb\\demo\\fixedasset\\src\\main\\java\\druid.properties"));
            pp.load(fis);
           //创建Druid连接池，使用配置文件中的参数
                    ds = DruidDataSourceFactory.createDataSource(pp);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();
        return conn;
    }

}
