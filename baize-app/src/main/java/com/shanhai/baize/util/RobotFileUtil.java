package com.shanhai.baize.util;

import cn.hutool.core.util.RandomUtil;

import java.io.File;

/**
 * 文件处理工具类
 */
public class RobotFileUtil {

    /**
     * 递归获取一个随机的文件，自动判断是否在文件夹
     *
     * @param file 需要获取的文件夹
     * @return 获取到的文件，可能为null
     */
    public static File randomFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                return file;
            } else if (file.isDirectory()) {
                File[] imgList = file.listFiles();
                if (imgList != null && imgList.length > 0) {
                    return randomFile(RandomUtil.randomEle(imgList));
                }
            }
        }
        return null;
    }
}
