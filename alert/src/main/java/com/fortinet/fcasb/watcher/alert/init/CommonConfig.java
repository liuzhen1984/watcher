package com.fortinet.fcasb.watcher.alert.init;


import ch.qos.logback.ext.spring.LogbackConfigurer;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by zliu on 17/1/27.
 */
@ComponentScan(basePackages = {
        "com.fortinet.fcasb.watcher.alert"
})
@PropertySource(value = {
        "classpath:/alert.properties",
        "file:/opt/alert/alert.properties"
}, ignoreResourceNotFound = true)
@Configuration
public class CommonConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfig.class);

    @Value("${config.log.path}")
    private String logbackPath;

    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private Integer smtpPort;
    @Value("${mail.smtp.username}")
    private String username;
    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.from.address}")
    private String from;
    private @Value("${mail.smtp.auth}") String auth;
    private @Value("${mail.smtp.timeout}") int timeout;


    private @Value("${es.transport.client.host}") String esHost;
    private @Value("${es.transport.client.port}") int esPort;
    private @Value("${es.cluster.name}") String esClusterName;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public Logger initLogback(){
        try {
            LogbackConfigurer.initLogging(logbackPath);
        } catch (Exception e) {
            LOGGER.error("Loading logback config file failed , please config logback file path：{}","classpath:/conf/logback.xml");
        }
        return LOGGER;
    }

    @Bean
    public TransportClient esClient() throws UnknownHostException {
//        Settings settings =Settings.builder()
//                .put("cluster.name",esClusterName)
////                .put("client.transport.sniff",true)
//                .build()
//                ;
//        TransportClient client = new PreBuiltTransportClient(settings)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
//        return client;
        return null;
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", auth);
        prop.put("mail.smtp.timeout", timeout);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.fallback", "false");
        prop.put("mail.smtp.socketFactory.port", smtpPort);
        mailSender.setJavaMailProperties(prop);
        return mailSender;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        return simpleMailMessage;
    }
}

