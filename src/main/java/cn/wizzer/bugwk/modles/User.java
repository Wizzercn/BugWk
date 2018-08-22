package cn.wizzer.bugwk.modles;

import cn.wizzer.bugwk.commons.enums.Role;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2018.08
 */
@Table("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String loginname;

    @Column
    @Comment("昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("姓名")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String realname;

    @Column
    @Comment("密码")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String loginpass;

    @Column
    @Comment("密码加盐")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String salt;

    @Column
    @Comment("角色CODE")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private Role role;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("创建时间")
    @ColDefine(type = ColType.INT, width = 9)
    private Long createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getLoginpass() {
        return loginpass;
    }

    public void setLoginpass(String loginpass) {
        this.loginpass = loginpass;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
