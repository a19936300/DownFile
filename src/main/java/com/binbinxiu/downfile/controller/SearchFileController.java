package com.binbinxiu.downfile.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.binbinxiu.downfile.bo.FileInfo;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/search")
public class SearchFileController {

    private static List<FileInfo> fileInfoList;

    @PostMapping("/generateIndex")
    public String genIndex(@RequestParam("dirPath") String dirPath,@RequestParam("flush") String flush) throws IOException {
        if(CollUtil.isNotEmpty(fileInfoList) && !"true".equals(flush)){
            return "不刷新";
        }

        fileInfoList = CollUtil.newArrayList();
        Resource templates = ResourceUtil.getResourceObj("templates");
        String jsonPath = templates.getUrl().getPath() + "/index.json";
        if(FileUtil.exist(jsonPath)){
            String json = FileUtil.readString(jsonPath, Charset.defaultCharset());
            fileInfoList = JSONUtil.toList(json, FileInfo.class);
        }else {
            generateIndex("F:\\A-王宾宾",fileInfoList);
            String jsonStr = JSONUtil.toJsonStr(fileInfoList);
            FileUtil.writeString(jsonStr,templates.getUrl().getPath() + "/index.json",Charset.defaultCharset());
        }
        return "成功";
    }


    private void generateIndex(String dirPath,List<FileInfo> fileInfoList){
        File[] ls = FileUtil.ls(dirPath);
        for (File file : ls) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setPath(file.getAbsolutePath());
            fileInfo.setName(file.getName());
            fileInfo.setFile(!file.isDirectory());
            if(file.isDirectory()){
                generateIndex(file.getAbsolutePath(),fileInfoList);
            }
            fileInfoList.add(fileInfo);
        }
    }

    @GetMapping("/likeFile/{fileName}")
    public List<FileInfo> likeFile(@PathVariable String fileName){
        List<FileInfo> collect = fileInfoList.stream().filter(s -> s.getName().contains(fileName)).collect(Collectors.toList());
        return collect;
    }
}
