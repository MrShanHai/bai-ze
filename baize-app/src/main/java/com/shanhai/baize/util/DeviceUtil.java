package com.shanhai.baize.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONObject;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.config.DeviceInfo;

import java.io.File;

public class DeviceUtil {
    /**
     * 获取机器人设备信息的JSON字符串
     *
     * @return
     */
    public static String getDeviceInfoJson(String qq, ConfigBean configBean) {
        // 设备信息文件，相对于缓存目录
        File file = configBean.resolveWorkspace("deviceInfo-".concat(qq).concat(".json"));
        String deviceInfoJson = null;
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            deviceInfoJson = fileReader.readString();
        } else {
            deviceInfoJson = new JSONObject(new DeviceInfo()).toStringPretty();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(deviceInfoJson);
        }
        return deviceInfoJson;
    }
}