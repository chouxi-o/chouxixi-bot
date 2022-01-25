package simbot.arknights.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * agent_main_info
 * @author
 */
@Data
public class AgentMainInfo implements Serializable {
    /**
     * 干员名称
     */
    private String agentName;

    /**
     * 干员外文名
     */
    private String agentNameEn;


    /**
     * 干员日文名
     */
    private String agentNameJp;

    /**
     * 干员id
     */
    private String agentId;

    /**
     * 干员序号
     */
    private Integer agentSeq;

    /**
     * 特性
     */
    private String features;

    /**
     * 稀有度
     */
    private Byte rareDegree;

    /**
     * 职业
     */
    private String job;

    /**
     * 子职业
     */
    private String childrenJob;

    /**
     * 情报编号
     */
    private String infoCode;

    /**
     * 所属国家
     */
    private String belongCountry;

    /**
     * 所属组织
     */
    private String belongOrg;

    /**
     * 所属团队
     */
    private String belongTeam;

    /**
     * 位置
     */
    private String loc;

    /**
     * 标签
     */
    private String tab;

    /**
     * 画师
     */
    private String painter;

    /**
     * 中文配音
     */
    private String chineseDub;

    /**
     * 日文配音
     */
    private String japaneseDub;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}