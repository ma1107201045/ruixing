package com.yintu.ruixing.anzhuangtiaoshi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnZhuangTiaoShiCheZhanXiangMuTypeEntity {
    private Integer id;

    private String xiangmuleixing;

    private String yuliu1;

    private String yuliu2;









    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXiangmuleixing() {
        return xiangmuleixing;
    }

    public void setXiangmuleixing(String xiangmuleixing) {
        this.xiangmuleixing = xiangmuleixing == null ? null : xiangmuleixing.trim();
    }

    public String getYuliu1() {
        return yuliu1;
    }

    public void setYuliu1(String yuliu1) {
        this.yuliu1 = yuliu1 == null ? null : yuliu1.trim();
    }

    public String getYuliu2() {
        return yuliu2;
    }

    public void setYuliu2(String yuliu2) {
        this.yuliu2 = yuliu2 == null ? null : yuliu2.trim();
    }
}