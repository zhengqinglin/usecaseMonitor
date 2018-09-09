package com.ruijie.service;

import com.ruijie.dao.ProjectMapper;
import com.ruijie.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author zql
* @description:
* @date: 21:26 2018/6/29
*/
@Service
public class ProjectService extends BaseDaoService<Project> {


    @Autowired
    private SvnProjectService svnProjectService;

//    @Autowired
//    private ProjectMapper projectMapper;

    /**
     *  根据id获取workcopy
     */
    public boolean updateProject(String projectId) {
        Project project = super.findOne(projectId);
        //更新文件
        return svnProjectService.updateProjectFromSvn(project);
    }

    /**
     *  根据id获取workcopy
     */
    public String getWorkCopyPath(String projectId) {
        return svnProjectService.getWorkCopy(projectId);
    }


    //将项目相关的操作放到这里 todo
}
