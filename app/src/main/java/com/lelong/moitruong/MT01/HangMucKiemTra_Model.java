package com.lelong.moitruong.MT01;

import java.io.Serializable;

public class HangMucKiemTra_Model implements Serializable {

    private String g_tc_fcc004;
    private String g_tc_fcc005;
    private String g_tc_fcc006;
    private String g_tc_fcc007;
    private String g_tc_fcc008;
    private String g_tc_fce007;
    private String g_tc_fce008;
    private String g_tc_fce006;

    public void setG_tc_fcc004(String g_tc_fcc004) {
        this.g_tc_fcc004 = g_tc_fcc004;
    }

    public void setG_tc_fcc005(String g_tc_fcc005) {
        this.g_tc_fcc005 = g_tc_fcc005;
    }

    public void setG_tc_fcc006(String g_tc_fcc006) {
        this.g_tc_fcc006 = g_tc_fcc006;
    }

    public void setG_tc_fcc007(String g_tc_fcc007) {
        this.g_tc_fcc007 = g_tc_fcc007;
    }

    public void setG_tc_fcc008(String g_tc_fcc008) {
        this.g_tc_fcc008 = g_tc_fcc008;
    }

    public void setG_tc_fce006(String g_tc_fce006) {
        this.g_tc_fce006 = g_tc_fce006;
    }

    public void setG_tc_fce007(String g_tc_fce007) {
        this.g_tc_fce007 = g_tc_fce007;
    }

    public void setG_tc_fce008(String g_tc_fce008) {
        this.g_tc_fce008 = g_tc_fce008;
    }

    public String getG_tc_fcc004() {
        return g_tc_fcc004;
    }

    public String getG_tc_fcc005() {
        return g_tc_fcc005;
    }

    public String getG_tc_fcc006() {
        return g_tc_fcc006;
    }

    public String getG_tc_fcc007() {
        return g_tc_fcc007;
    }

    public String getG_tc_fcc008() {
        return g_tc_fcc008;
    }

    public String getG_tc_fce008() {
        return g_tc_fce008;
    }

    public String getG_tc_fce006() {
        return g_tc_fce006;
    }

    public String getG_tc_fce007() {
        return g_tc_fce007;
    }


    public HangMucKiemTra_Model(String g_tc_fcc004,
                                String g_tc_fcc005,
                                String g_tc_fcc006,
                                String g_tc_fcc007,
                                String g_tc_fcc008,
                                String g_tc_fce006,
                                String g_tc_fce007,
                                String g_tc_fce008) {

        this.g_tc_fcc004 = g_tc_fcc004;
        this.g_tc_fcc005 = g_tc_fcc005;
        this.g_tc_fcc006 = g_tc_fcc006;
        this.g_tc_fcc007 = g_tc_fcc007;
        this.g_tc_fcc008 = g_tc_fcc008;

        this.g_tc_fce006 = g_tc_fce006;
        this.g_tc_fce007 = g_tc_fce007;
        this.g_tc_fce008 = g_tc_fce008;
    }
}
