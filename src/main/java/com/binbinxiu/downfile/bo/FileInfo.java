package com.binbinxiu.downfile.bo;

import lombok.Data;

@Data
public class FileInfo {
    private String name;
    private String path;

    private boolean file;

    public FileInfo(String name, String path,boolean file) {
        this.name = name;
        this.path = path;
        this.file = file;
    }

    public FileInfo() {
    }
}
