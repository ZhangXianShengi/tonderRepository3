package com.demo.zyf.pojo;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration//声明这是一个java配置类  相当于xml文件、
@PropertySource("classpath:application.properties")//扫描资源文件  读取文件里的信息   （指定外部属性文件）
public class JdbcConfiguration {
    @Autowired
    private JdbcProperties jdbcProperties;

    @Bean//在方法生声明  把方法上返回的加入到Bean容器中  代替Bean标签
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        //this调用全局变量
        dataSource.setDriverClassName(this.jdbcProperties.getDriverClassName());
        dataSource.setUrl(this.jdbcProperties.getUrl());
        dataSource.setUsername(this.jdbcProperties.getUsername());
        dataSource.setPassword(this.jdbcProperties.getPassword());
        return dataSource;
    }

}
