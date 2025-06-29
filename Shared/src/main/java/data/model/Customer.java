package data.model;

import java.io.Serializable;

/*
 * implements Serializable because objects of this class are transferred between server and client.
 */
public class Customer implements Serializable {
    private final Long customerId;
    private String loginName;
    private String password;

    public Customer(Long customerId, String loginName, String password) {
        this.customerId = customerId;
        this.loginName = loginName;
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
