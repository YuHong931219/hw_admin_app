package cn.zmmax.zebar.email;

import java.util.Properties;

import lombok.Data;

@Data
public class MailInfo {

    private String mailServerHost;// 发送邮件的服务器的IP
    private String mailServerPort;// 发送邮件的服务器的端口
    private String fromAddress;// 邮件发送者的地址
    private String toAddress;   // 邮件接收者的地址
    private String userName;// 登陆邮件发送服务器的用户名
    private String password;// 登陆邮件发送服务器的密码
    private boolean validate = true;// 是否需要身份验证
    private String subject;// 邮件主题
    private String content;// 邮件的文本内容
    private String[] attachFileNames;// 邮件附件的文件名

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

}
