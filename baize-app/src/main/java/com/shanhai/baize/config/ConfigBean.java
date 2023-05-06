package com.shanhai.baize.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 项目配置类
 *
 * @author BillDowney
 * @date 2021/4/7 20:29
 */
@Component
@ConfigurationProperties(value = "project.qq-robot")
@Getter
public class ConfigBean {
    /**
     * 应用是否启动完成
     */
    @Setter
    private transient boolean started = false;
    /**
     * 机器人启动标记
     */
    private boolean enable = true;
    /**
     * 命令匹配符
     */
    private String cmdChar = "#";
    /**
     * 机器人的工作空间
     */
    private String workspace = "cache";
    /**
     * 是否输出机器人日志
     */
    private boolean logOut = true;
    /**
     * root管理员qq列表
     */
    @Setter
    private Set<Long> rootManageQq = new HashSet<>();
    /**
     * 普通管理员qq列表
     */
    @Setter
    private Set<Long> normalManageQq = new HashSet<>();
    /**
     * 群管理权限是否有管理机器人的权限
     */
    private boolean groupAdminPermission = true;
    /**
     * #群白名单，强制过滤
     */
    @Setter
    private Set<Long> groupWhiteList = new HashSet<>();
    /**
     * alapi-token认证令牌
     */
    private String alapiToken = "";
    /**
     * kate-api-token认证令牌
     */
    private String kateApiToken = "";
    /**
     * qq群启用插件标记
     */
    private Map<Long, Map<String, Boolean>> pluginsStartFlag = new HashMap<>();
    /**
     * 开启入群欢迎词
     */
    private boolean openJoinGroupMessage = false;
    /**
     * 群管理插件配置-欢迎入群配置
     */
    private String joinGroupMessage = "欢迎入群";
    /**
     * gpt相关配置
     */
    @Setter
    private ConfigGptBean gpt;
    /**
     * 远程截屏获取url的列表
     */
    @Setter
    private List<String> remoteScreenUrls;

    public void setEnable(boolean enable) {
        this.enable = enable;
        this.writeFile();
    }

    public void setCmdChar(String cmdChar) {
        this.cmdChar = cmdChar;
        this.writeFile();
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
        this.writeFile();
    }

    public void setLogOut(boolean logOut) {
        this.logOut = logOut;
        this.writeFile();
    }

    public void addNormalManageQq(Long qq) {
        this.normalManageQq.add(qq);
        this.writeFile();
    }

    public void removeNormalManageQq(Long qq) {
        this.normalManageQq.remove(qq);
        this.writeFile();
    }

    public void setGroupAdminPermission(boolean groupAdminPermission) {
        this.groupAdminPermission = groupAdminPermission;
        this.writeFile();
    }

    public void addGroupWhiteList(Long qq) {
        this.groupWhiteList.add(qq);
        this.writeFile();
    }

    public void removeGroupWhiteList(Long qq) {
        this.groupWhiteList.remove(qq);
        this.writeFile();
    }

    public void setAlapiToken(String alapiToken) {
        this.alapiToken = alapiToken;
        this.writeFile();
    }

    public void setKateApiToken(String kateApiToken) {
        this.kateApiToken = kateApiToken;
        this.writeFile();
    }

    public void setPluginsStartFlag(Map<Long, Map<String, Boolean>> pluginsStartFlag) {
        this.pluginsStartFlag = pluginsStartFlag;
        this.writeFile();
    }

    public void setOpenJoinGroupMessage(boolean openJoinGroupMessage) {
        this.openJoinGroupMessage = openJoinGroupMessage;
    }

    public void setJoinGroupMessage(String joinGroupMessage) {
        this.joinGroupMessage = joinGroupMessage;
        this.writeFile();
    }

    /**
     * 将配置写入工作目录
     */
    public void writeFile() {
        if (this.started) {
            // 配置文件存放路径
            File configFile = this.resolveWorkspace("config.json");
            JSONObject jsonObject = JSONUtil.parseObj(this);
            FileUtil.writeString(jsonObject.toStringPretty(), configFile, StandardCharsets.UTF_8);
        }
    }

    /**
     * 读取json文件到内存中
     */
    public void readFile() {
        // 防止读取赋值的时候覆盖值
        this.started = false;
        // 配置文件存放路径
        File configFile = this.resolveWorkspace("config.json");
        if (configFile.exists()) {
            ConfigBean configBean = JSONUtil.readJSONObject(configFile, StandardCharsets.UTF_8).toBean(ConfigBean.class);
            CopyOptions copyOptions = new CopyOptions();
            copyOptions.ignoreNullValue();
            BeanUtil.copyProperties(configBean, this, copyOptions);
        }
    }

    /**
     * 相对于工作空间的路径
     */
    public File resolveWorkspace(String path) {
        return new File(BotConfiguration.getDefault().getWorkingDir(), new File(this.getWorkspace(), path).getPath());
    }
}
