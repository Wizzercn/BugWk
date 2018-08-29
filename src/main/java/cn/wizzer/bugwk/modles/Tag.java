package cn.wizzer.bugwk.modles;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2018.08
 */
@Table("tag")
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String name;

    @Column
    @Comment("重复次数")
    @ColDefine(type = ColType.INT, width = 9)
    @Prev({
            @SQL(db = DB.MYSQL, value = "SELECT IFNULL(MAX(repeats),0)+1 FROM tag"),
            @SQL(db = DB.ORACLE, value = "SELECT COALESCE(MAX(repeats),0)+1 FROM tag")
    })
    private Integer repeats;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
