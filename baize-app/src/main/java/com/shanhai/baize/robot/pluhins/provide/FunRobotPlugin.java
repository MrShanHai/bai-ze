package com.shanhai.baize.robot.pluhins.provide;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shanhai.baize.config.ConfigBean;
import com.shanhai.baize.robot.annotations.HookMethod;
import com.shanhai.baize.robot.annotations.HookNotice;
import com.shanhai.baize.robot.pluhins.RobotPlugin;
import com.shanhai.baize.robot.pluhins.RobotPluginContent;
import com.shanhai.baize.robot.robotEventEnum.RobotEventEnum;
import com.shanhai.baize.util.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 娱乐插件
 *
 * @author BillDowney
 * @date 2021/4/7 16:55
 */
@Component
@HookNotice(name = "娱乐插件", start = true)
public class FunRobotPlugin implements RobotPlugin {
    @Autowired
    private ConfigBean configBean;
    /**
     * 名言类型
     */
    private static final Map<Integer, String> MING_YAN_TYPE = new HashMap<Integer, String>() {
        {
            put(1, "爱情");
            put(2, "道德");
            put(3, "青春");
            put(4, "愿望");
            put(5, "集体");
            put(6, "理想");
            put(7, "志向");
            put(8, "人才");
            put(9, "谦虚");
            put(10, "人格");
            put(11, "天才");
            put(12, "青年");
            put(13, "社会");
            put(14, "国家");
            put(15, "财富");
            put(16, "智慧");
            put(17, "修养");
            put(18, "工作");
            put(19, "妇女");
            put(20, "儿童");
            put(21, "思想");
            put(22, "理智");
            put(23, "学习");
            put(24, "科学");
            put(25, "信仰");
            put(26, "诚信");
            put(27, "读书");
            put(28, "成败");
            put(29, "奉献");
            put(30, "劳动");
            put(31, "节约");
            put(32, "教育");
            put(33, "企业");
            put(34, "事业");
            put(35, "时间");
            put(36, "勤奋");
            put(37, "民族");
            put(38, "真理");
            put(39, "友谊");
            put(40, "自由");
            put(41, "心理");
            put(42, "心灵");
            put(43, "人生");
            put(44, "幸福");
            put(45, "团结");
        }
    };

    /**
     * 垃圾分类
     */
    private static final Map<Integer, String> GARBAGE_TYPE = new HashMap<Integer, String>() {
        {
            put(1, "可回收");
            put(2, "有害");
            put(3, "厨余(湿)");
            put(4, "其他(干)");
        }
    };

    @HookMethod(equal = "名言")
    public void sentences(RobotPluginContent content) {
        String url = "http://poetry.apiopen.top/sentences";
        String result = HttpUtil.get(url);
        JSONObject json = JSONUtil.parseObj(result);
        String msg;
        if (json.getInt("code") == 200) {
            msg = json.getJSONObject("result").getStr("name") + "——" + json.getJSONObject("result").getStr("from");
        } else {
            msg = String.format("请求链接出错【%s】：%s", url, result);
        }
        content.putReplyMessage(msg);
    }

    @HookMethod(equal = "舔狗日记")
    public void tiangou(RobotPluginContent content) {
        List<String> list = Arrays.asList(
                // "http://www.vizy8.cn/api2/tgrj/api.php",
                "https://cloud.qqshabi.cn/api/tiangou/api.php",
                "https://api.oick.cn/dog/api.php",
                "https://api.ixiaowai.cn/tgrj/index.php",
                "alapi"
        );
        String dogUrl = RandomUtil.randomEle(list);
        if ("alapi".equals(dogUrl)) {
            content.putReplyMessage(this.getAlapiText("dog?format=text"));
        } else {
            content.putReplyMessage(HttpUtil.get(dogUrl));
        }
    }

    // TODO 暂时注释掉，接口失效了
    // @HookMethod(equal = "笑话")
    public void joke(RobotPluginContent content) {
        JSONObject jokeJson = new JSONObject(HttpUtil.get("http://i.itpk.cn/api.php?question=笑话"));
        content.putReplyMessage(String.format("《%s》\r\n%s", jokeJson.getStr("title"), jokeJson.getStr("content")));
    }

    @HookMethod(equal = "毒鸡汤")
    public void du(RobotPluginContent content) {
        JSONObject data = new JSONObject(HttpUtil.get("https://api.shadiao.app/du"));
        data = data.getJSONObject("data");
        content.putReplyMessage("【" + data.getStr("type") + "】" + data.getStr("text"));
    }

    @HookMethod(equal = "彩虹屁")
    public void chp(RobotPluginContent content) {
        JSONObject data = new JSONObject(HttpUtil.get("https://api.shadiao.app/chp"));
        data = data.getJSONObject("data");
        content.putReplyMessage("【" + data.getStr("type") + "】" + data.getStr("text"));
    }

    @HookMethod(equal = "祖安语录")
    public void zuanbot(RobotPluginContent content) {
        content.putReplyMessage(HttpUtil.get("https://zuanbot.com/api.php?level=min&lang=zh_cn"));
    }

    @HookMethod(equal = "朋友圈文案")
    public void pyq(RobotPluginContent content) {
        JSONObject data = new JSONObject(HttpUtil.get("https://api.shadiao.app/pyq"));
        data = data.getJSONObject("data");
        content.putReplyMessage("【" + data.getStr("type") + "】" + data.getStr("text"));
    }

    @HookMethod(equal = "微博热搜")
    public void wbtop(RobotPluginContent content) {
        JSONArray wbResult = this.getAlapiJson(content, "new/wbtop?num=20", null);
        if (wbResult != null) {
            if (wbResult.isEmpty()) {
                content.putReplyMessage("微博热搜居然没有~");
            } else {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < wbResult.size(); i++) {
                    JSONObject item = wbResult.getJSONObject(i);
                    message.append(i + 1).append("、")
                            .append(item.getStr("hot_word"))
                            .append("(").append(item.getStr("hot_word_num")).append(")")
                            .append('\n');
                }
                content.putReplyMessage(message.toString());
            }
        }
    }

    @HookMethod(equal = "名人名言")
    public void mingyan(RobotPluginContent content) {
        JSONObject mingYanResult = this.getAlapiJson(content, "mingyan", null);
        if (mingYanResult != null) {
            String message = "【" + MING_YAN_TYPE.get(mingYanResult.getInt("typeid")) + "】" +
                    mingYanResult.getStr("content") +
                    "—— " + mingYanResult.getStr("author");
            content.putReplyMessage(message);
        }
    }

    @HookMethod(equal = "土味情话")
    public void qinghua(RobotPluginContent content) {
        content.putReplyMessage(this.getAlapiText("qinghua?format=text"));
    }

    @HookMethod(equal = "网易云乐评")
    public void comment(RobotPluginContent content) {
        JSONObject commentResult = this.getAlapiJson(content, "comment", null);
        if (commentResult != null) {
            MessageChainBuilder builder = MessageUtil.createBuilder();
            builder.append(new At(content.getMessageEvent().getSender().getId()));
            StringBuilder message = new StringBuilder();
            message.append("\n歌曲名称：").append(commentResult.getStr("title"))
                    .append("\n歌曲作者：").append(commentResult.getStr("author"))
                    .append("\n歌曲所属专辑：").append(commentResult.getStr("album"))
                    .append("\n歌曲发行时间：").append(commentResult.getStr("published_date"))
                    .append("\n歌曲描述：").append(commentResult.getStr("description"))
                    .append("\n歌曲资源链接：").append(commentResult.getStr("mp3_url"))
                    .append("\n评论用户：").append(commentResult.getStr("comment_nickname"))
                    .append("\n评论发表日期：").append(commentResult.getStr("comment_published_date"))
                    .append("\n评论点赞：").append(commentResult.getStr("comment_liked_count"))
                    .append("\n评论正文：").append(commentResult.getStr("comment_content"));
            builder.append(message.toString());
            content.putReplyMessage(builder);
        }
    }

    @HookMethod(equal = "污话")
    public void sweet(RobotPluginContent content) {
        content.putReplyMessage(HttpUtil.get("https://api.sumt.cn/api/sweet.php?format=txt&token=" + configBean.getKateApiToken()));
    }

    @HookMethod(equal = "语录")
    public void quotation(RobotPluginContent content) {
        content.putReplyMessage(HttpUtil.get("https://api.sumt.cn/api/quotation.php?format=txt&token=" + configBean.getKateApiToken()));
    }

    @HookMethod(equal = "一言")
    public void yiyan(RobotPluginContent content) {
        JSONObject yiyan = JSONUtil.parseObj(HttpUtil.get("https://v1.hitokoto.cn/?encode=json"));
        content.putReplyMessage(String.format("%s——%s「%s」"
                , yiyan.getStr("hitokoto")
                , yiyan.getStr("from_who", "")
                , yiyan.getStr("from")));
    }

    @HookMethod(start = "藏头诗", desc = "{四个中文字}")
    public void poem(RobotPluginContent content) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("keyword", content.getFilterContent());
        JSONObject poemResult = this.getAlapiJson(content, "ai/poem", params);
        if (poemResult != null) {
            MessageChainBuilder builder = MessageUtil.createBuilder();
            builder.append(new At(content.getMessageEvent().getSender().getId()));
            String message = "\n关键字：" + poemResult.getStr("keyword") + "\n" +
                    "诗句：\n" + poemResult.getStr("poem");
            builder.append(message);
            content.putReplyMessage(builder);
        }
    }

    @HookMethod(start = "垃圾分类", desc = "{垃圾名称}")
    public void garbage(RobotPluginContent content) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("name", content.getFilterContent());
        params.put("num", 20);
        JSONArray garbageResult = this.getAlapiJson(content, "garbage", params);
        if (garbageResult != null) {
            MessageChainBuilder builder = MessageUtil.createBuilder();
            builder.append(new At(content.getMessageEvent().getSender().getId()));
            if (garbageResult.isEmpty()) {
                builder.append(String.format("\n没有找到【%s】对应的垃圾分类信息~", content.getFilterContent()));
            } else {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < garbageResult.size(); i++) {
                    JSONObject item = garbageResult.getJSONObject(i);
                    message.append("\n").append(i + 1).append("、")
                            .append(item.getStr("name"))
                            .append("【").append(GARBAGE_TYPE.get(item.getInt("type"))).append("】");
//                                message.append(i + 1).append("、")
//                                        .append("\n名称：").append(item.getStr("name"))
//                                        .append("\n垃圾分类：").append(GARBAGE_TYPE.get(item.getInt("type")))
//                                        .append("\n分类解释：").append(item.getStr("explain"))
//                                        .append("\n包含类型：").append(item.getStr("contain"))
//                                        .append("\n投放提示：").append(item.getStr("tip"));
                }
                builder.append(message.toString());
            }
            content.putReplyMessage(builder);
        }
    }

    @HookMethod(start = "历史上的今天", equal = "历史上的今天", desc = "{非必填日期：0101}")
    public void eventHistory(RobotPluginContent content) {
        this.getEventHistory(content, content.getFilterContent());
    }

    @HookMethod(start = "斗图", desc = "{关键词}")
    public void stickerImgByApi(RobotPluginContent content) {
        String name = content.getFilterContent();
        JSONArray jsonArray = this.getAlapiJson(content, "doutu", Dict.create().set("page", 1).set("type", 7).set("keyword", name));
        if (jsonArray != null) {
            String url = (String) RandomUtil.randomEle(jsonArray);
            Contact contact = content.getContact();
            HttpResponse response = HttpRequest.get(url).setFollowRedirects(true).execute();
            Image image = MessageUtil.buildImageMessage(contact, response.bodyStream());
            if (image != null) {
                content.putReplyMessage(contact, image);
            }
        }
    }

    @HookMethod(start = "资讯", desc = "{新闻|微语|60秒}")
    public void news(RobotPluginContent content) {
        String name = content.getFilterContent();
        JSONObject json = this.getAlapiJson(content, "zaobao", Dict.create().set("format", "json"));
        if (json != null) {
            switch (name) {
                case "新闻":
                    JSONArray news = json.getJSONArray("news");
                    StringBuilder message = new StringBuilder();
                    news.forEach(item -> {
                        message.append(item).append('\n');
                    });
                    content.putReplyMessage(message.toString());
                    break;
                case "微语":
                    content.putReplyMessage(json.getStr("weiyu"));
                    break;
                case "60秒":
                    String url = json.getStr("image");
                    Contact contact = content.getContact();
                    HttpResponse response = HttpRequest.get(url).setFollowRedirects(true).execute();
                    Image image = MessageUtil.buildImageMessage(contact, response.bodyStream());
                    content.putReplyMessage(contact, image);
                    break;
                default:
                    content.putReplyMessage("当前只支持关键字：新闻、微语、60秒");
                    break;

            }
        }
    }

    /**
     * 请求https://www.alapi.cn/的接口
     *
     * @param action 接口后缀
     * @return 获取的数据，可能为text或json，自行处理
     */
    private String getAlapiText(String action) {
        String urlSuffix = "https://v2.alapi.cn/api/";
        return HttpRequest.get(urlSuffix + action).form("token", configBean.getAlapiToken()).execute().body();
    }

    /**
     * 获取json数据
     *
     * @param content 插件上下文
     * @param action  url后缀
     * @param params  参数
     * @return 获取的data中的内容
     */
    private <T> T getAlapiJson(RobotPluginContent content, String action, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("token", configBean.getAlapiToken());
        HttpResponse response = HttpRequest.post("https://v2.alapi.cn/api/" + action).form(params).execute();
        JSONObject result = JSONUtil.parseObj(response.body());
        if (result.getInt("code") == 200) {
            return (T) result.get("data");
        } else {
            content.putReplyMessage("数据请求失败：\n" + result.getStr("msg"));
            return null;
        }
    }

    /**
     * 获取历史上的今天
     *
     * @param content  插件上下文
     * @param monthday 月份和日期，如 1014,不填默认获取今天的事件
     */
    public void getEventHistory(RobotPluginContent content, String monthday) {
        Map<String, Object> params = new HashMap<>(1);
        if (StrUtil.isNotEmpty(monthday)) {
            params.put("monthday", monthday);
        }
        JSONArray eventHistoryResult = this.getAlapiJson(content, "eventHistory", params);
        if (eventHistoryResult != null) {
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < eventHistoryResult.size(); i++) {
                JSONObject item = eventHistoryResult.getJSONObject(i);
                message.append("\n").append(i + 1).append("、")
                        .append("【").append(item.getStr("date")).append("】").append(item.getStr("title"));
            }
            content.putReplyMessage(message.substring(1));
        }
    }

}
