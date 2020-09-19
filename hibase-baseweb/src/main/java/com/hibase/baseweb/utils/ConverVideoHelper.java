package com.hibase.baseweb.utils;


import com.hibase.baseweb.constant.web.ResultConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
public class ConverVideoHelper {

    private String path;

    // ffmpeg.exe的目录
    private String ffmpegpath;

    // 新文件的完整路径
    private String newFileName;

    public final static String ALLOW_TYPE = ".avi,.mpg,.3gp,.mov,.mp4,.asf,.asx,.flv";

    public final static String FILE_TYPE_MP4 = ".mp4";

    public ConverVideoHelper() {
    }

    public ConverVideoHelper(String path, String ffmpegpath, String newFileName) {

        this.path = path;
        this.ffmpegpath = ffmpegpath;
        this.newFileName = newFileName;
    }

    /**
     * 开始转码
     */
    public boolean beginConver() {

        if (!checkfile(path)) {

            log.info("{}文件不存在", path);
            return false;
        }

        long beginTime = System.currentTimeMillis();

        log.info("文件{}开始转码，当前时间为{}", path, beginTime);

        String transResult = process();

        log.info("文件{}转码结束，消耗时间为{}, 转码结果{}", path, System.currentTimeMillis() - beginTime, transResult);

        if (ResultConstant.OPREATE_SUCCESS.equals(transResult)) {

            return true;
        }

        return false;
    }

    /**
     * 转码
     */
    private String process() {

        boolean checkResult = checkContentType();

        if (checkResult) {

            return processMp4(path);
        }

        return ResultConstant.OPREATE_SUCCESS;
    }

    /**
     * 是否可以转码
     */
    private boolean checkContentType() {

        String type = path.substring(path.lastIndexOf("."), path.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        List<String> allowTyps = Arrays.asList(ALLOW_TYPE.split(","));

        if(allowTyps.contains(type)){

            return true;
        }

        return false;
    }


    private boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }

    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
    private String processMp4(String oldfilepath) {

        List commend = new java.util.ArrayList();

        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(oldfilepath);
        commend.add("-acodec");
        commend.add("copy");
        commend.add("-vcodec");
        commend.add("libx264");
        commend.add("-preset");
        commend.add("superfast");
        commend.add("-y");
        commend.add(newFileName);

        try {

            ProcessBuilder builder = new ProcessBuilder();

            builder.command(commend);
            Process p = builder.start();
            doWaitFor(p);
            p.destroy();
            return ResultConstant.OPREATE_SUCCESS;
        } catch (Exception e) {

            log.error("视频转码成mp4错误", e);
        }

        return ResultConstant.OPREATE_FAIL;
    }

    /**
     * 进程执行
     */
    public int doWaitFor(Process p) {

        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;
        try {

            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false;

            while (!finished) {
                try {

                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        log.info("转码程序输出：{}", c);
                    }

                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        log.info("转码程序错误输出：{}", c);
                    }

                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {

                    log.error("视频转码进程出错", e);
                }
            }
        } catch (Exception e) {

            log.error("视频转码进程出错", e);
            return -1;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {

                log.error("视频转码流关闭出错", e);
                return exitValue;
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {

                    log.error("视频转码流关闭出错", e);
                    return -1;
                }
            }
        }

        return exitValue;
    }
}
