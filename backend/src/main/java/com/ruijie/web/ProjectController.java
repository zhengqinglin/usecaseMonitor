package com.ruijie.web;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.annotation.RestWrapper;
import com.ruijie.exception.ServiceException;
import com.ruijie.model.Project;
import com.ruijie.service.ProjectService;
import com.ruijie.service.SvnProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhengqinglin on 2018/6/18.
 * 项目的增删改查
 */
@RestController
@RequestMapping(value = "/api")
public class ProjectController {

    /**
     * 日志记录
     */
    private static Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private SvnProjectService svnProjectService;

    @Autowired
    private ProjectService projectService;



    /**
     * 获取项目列表
     * @return
     */
    @GetMapping(value = "/project")
    public String getProjectList() {
        JSONObject result = new JSONObject();
//        List<Project> list = projectService.find();
        Example example = new Example(Project.class);
        example.setOrderByClause("create_time desc");
        List<Project> list = projectService.findByExample(example);
        //list为空 todo
        result.put("total",list.size());
        result.put("projectList",list);
        LOG.debug("{}",result.toJSONString());
        return result.toJSONString();
    }

    /**
     * 新增项目
     */
    @RestWrapper
    @PostMapping(value = "/project")
    public void addProject(@RequestBody Project project) {
        //验证svnUrl的正确性，基本的校验由前端负责，后端负责存在性校验
        if (!svnProjectService.validSvnUrl(project.getSvnUrl())) {
            throw new ServiceException("svn路径不存在");
        }
        project.setId(UUID.randomUUID().toString().replace("-",""));
        project.setCreateTime(new Date());
        projectService.save(project);
        //拉取项目文件
        if (!svnProjectService.createProject(project)) {
            throw new ServiceException("创建项目失败，请尝试重试或者联系管理员");
        }
    }

    /**
     * 删除项目
     */
    @DeleteMapping(value = "/project/{id}")
    public String delProject(@PathVariable("id") String id) {
        projectService.deleteById(id);
        //删除项目文件

        return null;
    }

    /**
     * 修改项目
     * 不允许修改和svn相关的信息
     */
    @PutMapping(value = "/project")
    public String updateProject(@RequestBody Project project) {
        LOG.debug("{}",project);
        projectService.updateByIdSelective(project);
        //svnUrl是否允许修改
        return null;
    }

}
