package simbot.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.listener.MyGroupListen;
import util.UuidUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


@Service
@Beans
public class AgentListen {
    /** log */
    private static final Logger LOG = LoggerFactory.getLogger(MyGroupListen.class);

    private static final String IN = "src/static/images/background_2.png";
    private static final String OUT = "src/static/temp/";
    private static final Integer D_WIDTH = 1024;
    private static final Integer D_HEIGHT = 576;
    private static final Integer SIZE = 26;
    private static final Integer TIME = 100;
    private static final Integer HEIGHT = 210;
    private static final Integer LINE_HEIGHT = 45;

    /**
     * 注入得到一个消息构建器工厂。
     */
    @Depend
    @Autowired
    private MessageContentBuilderFactory messageBuilderFactory;

    @OnGroup
    @Filter(groups = {"785296788"},value = "干员查询", matchType = MatchType.STARTS_WITH)
    public void onGroupAgentMsg(GroupMsg groupMsg, Sender sender) {
        try{
            String msgText = groupMsg.getText();

            String msgAgent = msgText.replace("干员查询","").trim();

            BufferedImage thumbImage = new BufferedImage(D_WIDTH, D_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbImage.createGraphics();

            //底图
            File file = new File(IN);
            Image src = javax.imageio.ImageIO.read(file);
            g.drawImage(src.getScaledInstance(D_WIDTH,D_HEIGHT,Image.SCALE_SMOOTH), 0, 0, null);

            //消除文字锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            //消除画图锯齿，很诡异，有时候有变化有时候没变化，建议关
            //g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_DEFAULT);

            //设置白色黑体
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑",Font.BOLD,SIZE));
            g.drawString(msgAgent,TIME,HEIGHT+LINE_HEIGHT);
            g.dispose();

            //生成uuid作为名字，防止图片相互覆盖
            String uuid = UuidUtils.getUuid();
            //输出图片
            String path = OUT+uuid+".jpg";
            File exOut = new File(OUT);
            if(!exOut.exists()){
                exOut.mkdir();
            }
            String formatName = path.substring(path.lastIndexOf(".") + 1);
            ImageIO.write(thumbImage, /*"GIF"*/ formatName /* format desired */ , new File(path) /* target */ );
            File agentFile = new File(path);
            // 得到一个消息构建器。
            MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
            MessageContent msg = builder.image(agentFile.getAbsolutePath())
                    .build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),msg);
            agentFile.delete();
        }catch (Exception e){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),"发生了未知的错误！ 0.0");
            throw new RuntimeException("查询失败",e);
        }
    }

}
