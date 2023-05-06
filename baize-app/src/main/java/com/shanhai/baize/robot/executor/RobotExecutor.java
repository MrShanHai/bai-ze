package com.shanhai.baize.robot.executor;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.dto.CustomerAddCmd;
import com.shanhai.baize.dto.RobotCmd;
import com.shanhai.baize.dto.data.ErrorCode;
import com.shanhai.baize.robot.event.EventListeningHandle;
import com.shanhai.baize.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
public class RobotExecutor {
    @Resource
    private ConfigBean configBean;
    @Resource
    private EventListeningHandle eventListeningService;

    public Response execute(RobotCmd cmd) {
        final Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(cmd.getQq()), cmd.getPassword(), new BotConfiguration() {
            {
                // 加载设备信息
                this.loadDeviceInfoJson(DeviceUtil.getDeviceInfoJson(cmd.getQq(), configBean));
                // 默认读取cache/device.json，可使用工具类生成设备信息：https://blog.ryoii.cn/mirai-devicejs-generator/
//                this.fileBasedDeviceInfo();
                // 使用安卓平板协议
                this.setProtocol(MiraiProtocol.ANDROID_PAD);
                // 工作空间目录，为根目录加登录的qq
                // this.setCacheDir(new File(configBean.getWorkspace() + File.separator + "qq" + File.separator + qq));
                this.setCacheDir(configBean.resolveWorkspace("qq" + File.separator + cmd.getQq()));
                // 开启所有列表缓存
                // this.enableContactCache();
                // 自定义缓存
                ContactListCache cache = new ContactListCache();
                // 开启好友列表缓存
                cache.setFriendListCacheEnabled(true);
                // 开启群成员列表缓存
                cache.setGroupMemberListCacheEnabled(true);
                // 可选设置有更新时的保存时间间隔, 默认 60 秒
                cache.setSaveIntervalMillis(60000);
                this.setContactListCache(cache);
                if (!configBean.isLogOut()) {
                    // 关闭日志输出
                    this.noBotLog();
                    this.noNetworkLog();
                }
            }
        });
        try {
            // 注册QQ机器人事件监听
            bot.getEventChannel().registerListenerHost(eventListeningService);
            // 登录QQ
            bot.login();
            // 阻塞当前线程直到 bot 离线
            bot.join();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return Response.buildSuccess();
    }
}
