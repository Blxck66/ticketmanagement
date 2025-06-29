package data.model;

import java.io.Serializable;

/*
 * implements Serializable because objects of this class are transferred between server and client.
 */
public class Employee implements Serializable {

    private final Long employeeId;
    private String loginName;
    private String password;
    private Long strikeCount;

    public Employee(Long employeeId, String loginName, String password, Long strikeCount) {
        this.employeeId = employeeId;
        this.loginName = loginName;
        this.password = password;
        this.strikeCount = strikeCount;
    }

    public Long getEmployeeId() {
        return employeeId;
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

    public Long getStrikeCount() {
        return strikeCount;
    }

    public void setStrikeCount(Long strikeCount) {
        this.strikeCount = strikeCount;
    }
}
