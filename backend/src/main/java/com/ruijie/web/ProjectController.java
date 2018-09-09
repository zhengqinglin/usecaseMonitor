package com.ruijie.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruijie.annotation.RestWrapper;
import com.ruijie.exception.ServiceException;
import com.ruijie.model.Project;
import com.ruijie.service.ProjectService;
import com.ruijie.service.SvnProjectService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
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
    public PageInfo<Project> getProjectList(@RequestParam(value = "pageNum", required = false) Integer pageNum,@RequestParam(value = "name",required = false) String projectName) {
        if (null == pageNum || pageNum < 0) {
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,10);
        Example example = new Example(Project.class);
        example.setOrderByClause("create_time desc");
        if (StringUtils.isNotBlank(projectName)) {
            example.createCriteria().andLike("name",projectName);
        }
        List<Project> list = projectService.findByExample(example);
        PageInfo<Project> result = new PageInfo<>(list);
        return result;
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
        String workingCopyPath = svnProjectService.createProject(project);
        project.setWorkingCopyPath(workingCopyPath);
        projectService.save(project);
        //返回值待定 todo
    }

    /**
     * 删除项目
     */
    @DeleteMapping(value = "/project/{id}")
    public String delProject(@PathVariable("id") String id) {
        Project project = projectService.findOne(id);
        if (null == project) {
            return null;
        }
        LOG.error("workingCopyPath:{}",project.getWorkingCopyPath());
        if (new File(project.getWorkingCopyPath()).delete()){
            LOG.error("删除文件{}成功",project.getWorkingCopyPath());
        }
        projectService.deleteById(id);
//        new File(project.getWorkingCopyPath()).delete()
        return null;
        //返回值待定 todo
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
        //返回值待定 todo
    }

    public static void main(String[] args) {
//        System.out.println(new File("C:\\opt\\svn\\workspace","测试").getAbsolutePath());
//        if (new File("C:\\opt\\svn\\workspace\\测试").delete()){
//            System.out.println("success");
//        }
        File dest = new File("C:\\opt\\svn\\workspace" + File.separator + "测试");
        if(!dest.exists() && !dest.mkdirs()){
            LOG.error("创建项目目录{}失败",dest.getAbsolutePath());
            throw new ServiceException("服务器内部异常，请联系管理员");
        }
    }

}
