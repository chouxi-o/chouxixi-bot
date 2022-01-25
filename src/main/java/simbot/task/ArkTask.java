package simbot.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import love.forte.common.ioc.annotation.Depend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simbot.arknights.bean.AgentMainInfo;
import simbot.arknights.bean.AgentText;
import simbot.arknights.dao.AgentMainInfoDao;
import simbot.arknights.service.AgentService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ArkTask {

    @Depend
    @Autowired
    AgentService agentService;

    @Depend
    @Autowired
    AgentMainInfoDao agentMainInfoDao;


    //@Scheduled(cron = "0 0/1 * * * ? ")
    public void dealAgentText() {
        //1.查询出原始记录
        List<AgentText> agentTextList = agentService.getAllAgentText();


        for (AgentText agentText : agentTextList) {
            String agentInfoStr = this.getSplitInfo(agentText, "==干员信息==");
            String agentWayStr = this.getSplitInfo(agentText, "==获得方式==");
            String agentAttrStr = this.getSplitInfo(agentText, "==属性==");
            AgentMainInfo mainInfo = this.getAgentInfoByStr(agentInfoStr);
            if (agentMainInfoDao.selectList(new QueryWrapper<AgentMainInfo>().lambda().eq(AgentMainInfo::getAgentName, mainInfo.getAgentName())).size() == 0) {
                agentMainInfoDao.insert(mainInfo);
            }
            /*System.out.println(agentInfoStr);
            System.out.println(agentWayStr);
            System.out.println(agentAttrStr);*/
        }
    }

    /**
     * 拆分块信息
     *
     * @param agentText  原始文本
     * @param split_code 拆分关键字
     * @return 拆分后的信息
     */
    private String getSplitInfo(AgentText agentText, String split_code) {
        return agentText.getAgentText().split(split_code)[1].split("==")[0];
    }

    private AgentMainInfo getAgentInfoByStr(String agentInfoStr) {
        AgentMainInfo mainInfo = new AgentMainInfo();
        String[] stringList = agentInfoStr.split("\n\\|");
        for (String s : stringList) {
            try {
                String[] splitStr = s.split("=");
                if(splitStr.length < 2){
                    continue;
                }
                String strName = splitStr[0].trim();
                String strValue = splitStr[1];
                switch (strName) {
                    case "干员名":
                        mainInfo.setAgentName(strValue);
                        break;
                    case "干员名jp":
                        mainInfo.setAgentNameJp(strValue);
                        break;
                    case "干员外文名":
                        mainInfo.setAgentNameEn(strValue);
                        break;
                    case "干员id":
                        mainInfo.setAgentId(strValue);
                        break;
                    case "干员序号":
                        mainInfo.setAgentSeq(Integer.parseInt(strValue));
                        break;
                    case "特性":
                        mainInfo.setFeatures(strValue);
                        break;
                    case "稀有度":
                        mainInfo.setRareDegree(Byte.parseByte(strValue));
                        break;
                    case "职业":
                        mainInfo.setJob(strValue);
                        break;
                    case "子职业":
                        mainInfo.setChildrenJob(strValue);
                        break;
                    case "情报编号":
                        mainInfo.setInfoCode(strValue);
                        break;
                    case "所属国家":
                        mainInfo.setBelongCountry(strValue);
                        break;
                    case "所属组织":
                        mainInfo.setBelongOrg(strValue);
                        break;
                    case "所属团队":
                        mainInfo.setBelongTeam(strValue);
                        break;
                    case "位置":
                        mainInfo.setLoc(strValue);
                        break;
                    case "标签":
                        mainInfo.setTab(strValue);
                        break;
                    case "画师":
                        mainInfo.setPainter(strValue);
                        break;
                    case "中文配音":
                        mainInfo.setChineseDub(strValue);
                        break;
                    case "日文配音":
                        mainInfo.setJapaneseDub(strValue);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println(mainInfo);
        return mainInfo;
    }

    public static void main(String args[]) {
        String str = "&lt;br/&gt;款式&lt;br/&gt;多&lt;br/&gt;+Regular fit&lt;br/&gt;+已适用于基于DeepMentality™的运动学习系统";
        String pattern = "&lt;.*?&gt;";
        String pattern1 = "\\+.*?\\+";

        Pattern r = Pattern.compile(pattern);
        Pattern r1 = Pattern.compile(pattern1);
        Matcher m = r.matcher(str);
        System.out.println(r1.matcher(m.replaceAll(",")).replaceAll(""));

    }
}
