package com.shanhai.baize.util;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 消息处理工具
 *
 * @author BillDowney
 * @date 2021/4/1 14:37
 */
@Slf4j
public class MessageUtil {

    /**
     * 创建一个消息构造器
     *
     * @return {@link MessageChainBuilder}
     */
    public static MessageChainBuilder createBuilder() {
        return new MessageChainBuilder();
    }

    /**
     * 构建图片消息
     *
     * @param sender Group Or Friend Or Member 对象
     * @param image  图片文件
     * @return 构建的图片消息
     */
    public static Image buildImageMessage(Contact sender, File image) {
        ExternalResource externalImage = null;
        try {
            externalImage = ExternalResource.create(image);
            return sender.uploadImage(externalImage);
        } catch (Exception e) {
            log.error("文件消息转换失败", e);
            throw new RuntimeException(e);
        } finally {
            if (externalImage != null) {
                try {
                    externalImage.close();
                } catch (IOException e) {
                    log.error("externalImage关闭失败", e);
                }
            }
        }
    }

    /**
     * 构建图片消息
     *
     * @param sender Group Or Friend Or Member 对象
     * @param image  图片文件
     * @return 构建的图片消息
     */
    public static Image buildImageMessage(Contact sender, InputStream image) {
        ExternalResource externalImage = null;
        try {
            externalImage = ExternalResource.create(image);
            return sender.uploadImage(externalImage);
        } catch (Exception e) {
            log.error("文件消息转换失败", e);
            throw new RuntimeException(e);
        } finally {
            if (externalImage != null) {
                try {
                    externalImage.close();
                } catch (IOException e) {
                    log.error("externalImage关闭失败", e);
                }
            }
        }
    }
}
