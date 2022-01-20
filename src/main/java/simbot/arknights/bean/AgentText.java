package simbot.arknights.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * agent_text
 * @author 
 */
@Data
public class AgentText implements Serializable {
    private String agentName;

    private String agentText;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}