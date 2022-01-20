package simbot.arknights.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import simbot.arknights.bean.AgentText;
import simbot.arknights.dao.AgentTextDao;

@Service
public class AgentService {

    @Autowired
    AgentTextDao agentTextDao;

    public AgentText getAgentTextByName(String name){
        return agentTextDao.selectOne(new QueryWrapper<AgentText>().lambda().like(AgentText::getAgentName,name).last("limit 1"));
    }

}
