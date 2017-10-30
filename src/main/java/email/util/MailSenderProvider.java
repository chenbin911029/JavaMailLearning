package email.util;

public enum MailSenderProvider {

    DEFAULT("13790432378@163.com",
            "smtp.163.com",
            25,
            "13790432378@163.com",
            "chenbin911029",
            "http://email2.163.com/",
            "天马流星");
    /**
     * 邮件服务提供商名称。
     */
    String provider;
    /**
     * 邮件服务提供商服务设置。
     */
    String host;
    /**
     * 服务端口。
     */
    int port;
    /**
     * 发送方账户。
     */
    String account;
    /**
     * 发送方密码。
     */
    String password;
    /**
     * 邮件服务网站。
     */
    String portal;
    String nickname;

    private MailSenderProvider(String provider,
                               String host,
                               int port,
                               String account,
                               String password,
                               String portal,
                               String nickname){
        this.provider = provider;
        this.host = host;
        this.port = port;
        this.account =account;
        this.password = password;
        this.portal = portal;
        this.nickname = nickname;
    }

    public static MailSenderProvider create(String provider) {
        provider = (provider == null) ? "DEFAULT" : provider.trim().toUpperCase();
        int index;
        try {
            String providerSuffix = provider.substring((index = provider.lastIndexOf('@')) > -1 ? index + 1 : 0);
            providerSuffix = "E_" + providerSuffix.replaceAll("\\.", "_").toUpperCase();
            return MailSenderProvider.valueOf(providerSuffix);
        } catch (IllegalArgumentException ex) {
            return MailSenderProvider.DEFAULT;
        }
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
