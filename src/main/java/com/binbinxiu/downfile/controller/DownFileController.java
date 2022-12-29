package com.binbinxiu.downfile.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.binbinxiu.downfile.bo.FileInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DownFileController {

    @RequestMapping("/test")
    public String testHtml(Model model){
        File[] files = FileUtil.ls("F:\\");
        List<String> collect = StreamUtil.of(files).map(s -> s.getAbsolutePath()).collect(Collectors.toList());
        System.out.println("这是集合");
        model.addAttribute("files",collect);
        return "test";
    }


    @PostMapping("/getDirFiles")
    @ResponseBody
    public List<FileInfo> indexHtml(@RequestParam String dirPath) throws Exception {
        if(!FileUtil.isDirectory(dirPath)){
            throw new Exception( "不是一个正确目录");
        }

        File[] files = FileUtil.ls(dirPath);
        List<FileInfo> collect = StreamUtil.of(files).map(s -> new FileInfo(s.getName(),s.getAbsolutePath(),s.isFile())).collect(Collectors.toList());
        return collect;
    }


    @PostMapping("/downFile")
    public void downFile(@RequestParam String filePath, HttpServletResponse response) throws IOException {
        if(FileUtil.isFile(filePath)){
            BufferedInputStream inputStream = FileUtil.getInputStream(filePath);
            IoUtil.copy(inputStream,response.getOutputStream());
        }
    }

    public static void main(String[] args) {
        File[] files = FileUtil.ls("F:\\");
        List<String> collect = StreamUtil.of(files).map(s -> s.getAbsolutePath()).collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }
    }
}
