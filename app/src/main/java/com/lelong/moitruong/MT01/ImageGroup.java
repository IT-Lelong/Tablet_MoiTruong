package com.lelong.moitruong.MT01;

import java.io.File;
import java.util.List;

public class ImageGroup {
    private String date;
    private String department;
    private String hangmuc;
    private List<File> imagefiles;

    public ImageGroup(String date, String department,String hangmuc, List<File> imagefiles) {
        this.date = date;
        this.department = department;
        this.hangmuc = hangmuc;
        this.imagefiles = imagefiles;
    }

    public String getDate() {
        return date;
    }

    public String getDepartment() {
        return department;
    }

    public String getHangmuc() {
        return hangmuc;
    }

    public List<File> getImage() {
        return imagefiles;
    }
}

