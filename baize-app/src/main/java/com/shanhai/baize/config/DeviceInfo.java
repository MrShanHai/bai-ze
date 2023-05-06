package com.shanhai.baize.config;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte[] display = "BillDowney.".concat(RandomUtil.randomNumbers(6)).concat(".001").getBytes();

    private byte[] product = "BillDowney".getBytes();

    private byte[] device = "BillDowney".getBytes();

    private byte[] board = "BillDowney".getBytes();

    private byte[] brand = "BillDowney".getBytes();

    private byte[] model = "BillDowney".getBytes();

    private byte[] bootloader = "unknown".getBytes();

    private byte[] fingerprint = "BillDowney/BillDowney/BillDowney:10/BillDowney.200122.001/".concat(RandomUtil.randomNumbers(7)).concat(":user/release-keys").getBytes();

    private byte[] bootId = UUID.randomUUID().toString().toUpperCase().getBytes();

    private byte[] procVersion = "Linux version 3.0.31-".concat(RandomUtil.randomString(8)).concat(" (android-build@xxx.xxx.xxx.xxx.com)").getBytes();

    private JSONArray baseBand = new JSONArray();

    private Version version = new Version();

    private byte[] simInfo = "T-Mobile".getBytes();

    private byte[] osType = "android".getBytes();

    private byte[] macAddress = "02:00:00:00:00:00".getBytes();

    private byte[] wifiBSSID = "02:00:00:00:00:00".getBytes();

    private byte[] wifiSSID = "<unknown ssid>".getBytes();

    private byte[] imsiMd5 = SecureUtil.md5().digest(RandomUtil.randomBytes(16));

    private String imei = RandomUtil.randomNumbers(15);

    private byte[] apn = "wifi".getBytes();

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class Version implements Serializable {
        private byte[] incremental = "5891938".getBytes();
        private byte[] release = "10".getBytes();
        private byte[] codename = "REL".getBytes();
        private int sdk = 29;
    }
}

