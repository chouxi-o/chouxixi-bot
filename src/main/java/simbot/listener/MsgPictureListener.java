package simbot.listener;

import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.utils.CQCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.HttpClientUtil;
import util.UuidUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 壁纸/色图监听
 */

@Service
@Beans
public class MsgPictureListener {

    /**
     * 注入得到一个消息构建器工厂。
     */
    @Depend
    @Autowired
    private MessageContentBuilderFactory messageBuilderFactory;

    //来份色图API
    private static final String FORCOLORIMAGEURL[] =  {
            //"https://api.moonwl.cn/api/tu/acg.php",  //接口已凉 瑶:后续测试接口还能用
//            "http://api.mtyqx.cn/tapi/random.php",
            "http://api.mtyqx.cn/api/random.php?return=json"
    };

    //来份壁纸API
    private static final String FORWALLPAPER[] = {
            "https://acg.yanwz.cn/wallpaper/api.php",
            "http://api.btstu.cn/sjbz/?lx=suiji"
    };
    //手机壁纸API
    private static final String PHONEWALLPAPER = "http://api.btstu.cn/sjbz/?lx=m_dongman";

    @OnGroup
    @Filter(groups = {"785296788","614728070"},value = "来份涩图", matchType = MatchType.EQUALS)
    public void phoneWallpaper(GroupMsg groupMsg, MsgSender sender){
        CQCode at = CQCodeUtil.build().getCQCode_At(groupMsg.getAccountInfo().getAccountCode());

        // 得到一个消息构建器。
        MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();

        String imgUrl = "http://api.btstu.cn/sjbz/api.php?lx=dongman&format=json&method=mobile";
        try{
            //获得Json数据
            String result = HttpClientUtil.doGet(imgUrl);
            JSONObject object = JSONObject.parseObject(result);
            String value = object.getString("imgurl");
            //下载图片
            String image = this.downloadImg(value);
            File file = new File(image);
            //CQCode img = CQCodeUtil.build().getCQCode_Image("file://"+file.getAbsolutePath());
            MessageContent msg = builder
                    // at当事人
                    .at(groupMsg.getAccountInfo()).image(file.getAbsolutePath())
                    .build();
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo(),msg);
            file.delete();
        }catch (Exception e){
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo(),at+" 由于网络波动，手机壁纸加载失败！请重试 0.0");
            throw new RuntimeException("手机壁纸加载失败",e);
        }
    }


    //下载图片
    private String downloadImg(String imgurl){
        try{
            URL url = new URL(imgurl);
            //打开链接
            URLConnection con = url.openConnection();
            //设置请求超时 5sg3
            con.setConnectTimeout(5*1000);
//            InputStream inputStream = con.getInputStream();
            BufferedInputStream in = new BufferedInputStream(con.getInputStream());
            //文件名字
            String filePath = "G:\\temp\\" + UuidUtils.getUuid() + ".png";
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
            byte[] buffer = new byte[1024*8];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            //关闭流
            out.close();
            in.close();
            return filePath;
        }catch (Exception e){
            new RuntimeException("下载图片异常",e);
        }
        return null;
    }


}
