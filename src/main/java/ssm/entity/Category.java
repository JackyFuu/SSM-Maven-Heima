package ssm.entity;

import java.io.Serializable;

//存入redis的pojo类必须实现Serializable
public class Category implements Serializable {
    private String cid;
    private String cname;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
