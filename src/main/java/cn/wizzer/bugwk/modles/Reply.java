package cn.wizzer.bugwk.modles;

import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2018.08
 */
@Table("reply")
public class Reply implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("Bug ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String bugId;

    @Column
    @Comment("用户ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String userId;

    @Column
    @Comment("昵称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String nickname;

    @Column
    @Comment("内容")
    @ColDefine(type = ColType.TEXT)
    private String note;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("创建人")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String createBy;

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

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
