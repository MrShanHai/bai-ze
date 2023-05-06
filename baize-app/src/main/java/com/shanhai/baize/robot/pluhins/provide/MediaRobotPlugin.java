package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import com.shanhai.baize.util.RobotFileUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.RemoteFile;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 媒体插件
 *
 * @author BillDowney
 * @date 2021/4/9 9:45
 */
@Component
@HookNotice(name = "媒体插件")
public class MediaRobotPlugin implements RobotPlugin, InitializingBean {

    @Autowired
    private ConfigBean configBean;
    /**
     * 图片文件夹
     */
    private File imgFile;
    /**
     * 斗图文件夹
     */
    private File stickerFile;
    /**
     * 短视频文件夹
     */
    private File shortVideoFile;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 媒体文件夹
        File baseMediaPath = configBean.resolveWorkspace("media");
        this.imgFile = new File(baseMediaPath, "img");
        this.stickerFile = new File(baseMediaPath, "sticker");
        this.shortVideoFile = new File(baseMediaPath, "short_video");
        FileUtil.mkdir(this.imgFile);
        FileUtil.mkdir(this.stickerFile);
        FileUtil.mkdir(this.shortVideoFile);
    }

    @HookMethod(equal = "看图")
    public void lookImg(RobotPluginContent content) {
        this.sendMsg(content, imgFile, "没有图可以看哦~");
    }

    @HookMethod(equal = "斗图")
    public void stickerImg(RobotPluginContent content) {
        this.sendMsg(content, stickerFile, "没有表情包可以看哦~");
    }

    @HookMethod(equal = "短视频", event = RobotEventEnum.GROUP_MSG)
    public void shortVideo(RobotPluginContent content) {
        File shortVideo = RobotFileUtil.randomFile(shortVideoFile);
        if (shortVideo == null) {
            content.putReplyMessage("没有短视频可以看哦~");
        } else {
            // 暂时有bug
            Group group = content.getGroupMessageEvent().getGroup();
            RemoteFile remoteFile = group.getFilesRoot();
            content.putReplyMessage(group, remoteFile.resolve(shortVideo.getName()).upload(shortVideo));
        }
    }

    @HookMethod(equal = "美图")
    public void lspImg(RobotPluginContent content) {
        // TODO 这些api要收费了，暂时考虑不用
//        List<String> list = Arrays.asList(
//                // 淘宝买家秀
//                "rand.tbimg.php"
//                // 美女
//                , "rand.img.php?type=%E7%BE%8E%E5%A5%B3", "rand.acg.php?type=%E7%BE%8E%E5%A5%B3"
//                // 性感
//                , "rand.img.php?type=%E6%80%A7%E6%84%9F"
//                // 制服
//                , "rand.img.php?type=%E5%88%B6%E6%9C%8D"
//                // 二次元
//                , "rand.acg.php?type=%E4%BA%8C%E6%AC%A1%E5%85%83"
//                // 呆萌酱
//                , "rand.acg.php?type=%E5%91%86%E8%90%8C%E9%85%B1");
        List<String> list = Collections.singletonList("http://img.btu.pp.ua/random/api.php");
        this.sendImagesByUrl(content, RandomUtil.randomEle(list));
    }

    private void sendMsg(RobotPluginContent content, File folder, String errorMsg) {
        File file = RobotFileUtil.randomFile(folder);
        if (file == null) {
            content.putReplyMessage(errorMsg);
        } else {
            content.putReplyMessage(content.getContact(), MessageUtil.buildImageMessage(content.getContact(), file));
        }
    }

    /**
     * 根据url发送图片消息
     *
     * @param content 插件上下文
     * @param url     指向图片的url
     */
    private void sendImagesByUrl(RobotPluginContent content, String url) {
        Contact contact = content.getContact();
        HttpResponse response = HttpRequest.get(url).setFollowRedirects(true).execute();
        Image image = MessageUtil.buildImageMessage(contact, response.bodyStream());
        if (image != null) {
            content.putReplyMessage(contact, image);
        }
    }

    private void sendImagesByKateUrl(RobotPluginContent content, String url) {
        HttpResponse response = HttpRequest.get("https://api.sumt.cn/api/" + url).form("token", configBean.getKateApiToken()).form("format", "json").execute();
        Contact contact = content.getContact();
        JSONObject data = JSONUtil.parseObj(response.body());
        if (data.getInt("code") == 200) {
            response = HttpRequest.get(data.getStr("pic_url")).execute();
            Image image = MessageUtil.buildImageMessage(contact, response.bodyStream());
            if (image != null) {
                content.putReplyMessage(contact, image);
            }
        } else {
            content.putReplyMessage(contact, data.getStr("msg"));
        }
    }

}
