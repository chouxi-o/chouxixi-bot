package simbot.arknights.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import simbot.arknights.bean.AgentText;

@Repository
@Mapper
public interface AgentTextDao extends BaseMapper<AgentText> {
    //初始化用户
    @Insert("INSERT INTO agent_text VALUES(#{agentName},#{agentText},#{createTime})")
    void addAgentText(AgentText agentText);
}