package simbot.arknights.bean;

import java.io.Serializable;
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

    private static final long serialVersionUID = 1L;
}