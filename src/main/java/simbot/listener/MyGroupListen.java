package simbot.listener;

import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.WeatherUtil;

import java.util.List;
import java.util.Map;

/**
 * 群消息监听的示例类。
 * 所有需要被管理的类都需要标注 {@link Service} 注解。
 *
 * 由于当前是处于springboot环境下，因此强烈建议类上的注释使用：
 * <ul>
 *     <li>{@link org.springframework.stereotype.Component}</li>
 *     <li>{@link Service}</li>
 * </ul>
 * 等注解来代替simbot的 {@link Beans}。
 *
 * 同样的，依赖注入也请使用 {@link org.springframework.beans.factory.annotation.Autowired} 等Springboot相关的注解。
 *
 * @author ForteScarlet
 */
@Service
@Beans
public class MyGroupListen {

    /** log */
    private static final Logger LOG = LoggerFactory.getLogger(MyGroupListen.class);

    /**
     * 注入得到一个消息构建器工厂。
     */
    @Depend
    @Autowired
    private MessageContentBuilderFactory messageBuilderFactory;

    /**
     * 此监听函数代表，收到消息的时候，将消息的各种信息打印出来。
     *
     * 此处使用的是模板注解 {@link OnGroup}, 其代表监听一个群消息。
     *
     * 由于你监听的是一个群消息，因此你可以通过 {@link GroupMsg} 作为参数来接收群消息内容。
     */
    @OnGroup
    @Filter(groups = {"785296788"},value = "教练", matchType = MatchType.STARTS_WITH)
    public void onGroupMsg(GroupMsg groupMsg, Sender sender) {
        // 打印此次消息中的 纯文本消息内容。
        // 纯文本消息中，不会包含任何特殊消息（例如图片、表情等）。
        System.out.println(groupMsg.getText());

        // 打印此次消息中的 消息内容。
        // 消息内容会包含所有的消息内容，也包括特殊消息。特殊消息使用CAT码进行表示。
        // 需要注意的是，绝大多数情况下，getMsg() 的效率低于甚至远低于 getText()
        System.out.println(groupMsg.getMsg());

        // 获取此次消息中的 消息主体。
        // messageContent代表消息主体，其中通过可以获得 msg, 以及特殊消息列表。
        // 特殊消息列表为 List<Neko>, 其中，Neko是CAT码的封装类型。

        MessageContent msgContent = groupMsg.getMsgContent();

        // 打印消息主体
        System.out.println(msgContent);
        // 打印消息主体中的所有图片的链接（如果有的话）
        List<Neko> imageCats = msgContent.getCats("image");
        System.out.println("img counts: " + imageCats.size());
        for (Neko image : imageCats) {
            System.out.println("Img url: " + image.get("url"));
        }


        // 获取发消息的人。
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        // 打印发消息者的账号与昵称。
        System.out.println(accountInfo.getAccountCode());
        System.out.println(accountInfo.getAccountNickname());


        // 获取群信息
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        // 打印群号与名称
        System.out.println(groupInfo.getGroupCode());
        System.out.println(groupInfo.getGroupName());

        // 得到一个消息构建器。
        /*MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
        MessageContent msg = builder
                // at当事人
                .at(accountInfo)
                // tips 通过 \n 换行
                .text(" 教练是笨蛋！")
                .build();*/
        // 向 privateMsg 的账号发送消息，消息为当前接收到的消息。
        sender.sendGroupMsg(785296788, "笨蛋教练！");
        //System.out.println(msg);
    }


    @OnGroup
    @Filter(groups = {"785296788","614728070"},value = "天气", matchType = MatchType.ENDS_WITH)
    public void onWeatherMsg(GroupMsg groupMsg, MsgSender sender) {
        try{
            String msgText = groupMsg.getText();

            String msgCity = msgText.replace("天气","");

            Map weatherMap= WeatherUtil.weatherToday(msgCity);

            // 得到一个消息构建器。
            MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
            MessageContent msg = builder
                    // at当事人
                    .at(groupMsg.getAccountInfo()).text(weatherMap.get("result").toString())
                    .build();
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo(),msg);
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo(),"发送了未知的错误！ 0.0");
            throw new RuntimeException("获取天气失败",e);
        }

    }







}
