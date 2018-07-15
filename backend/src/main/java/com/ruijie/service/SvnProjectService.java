package com.ruijie.service;

import com.ruijie.exception.ServiceException;
import com.ruijie.model.Project;
import com.ruijie.util.SVNUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;

/**
* @author zql
* @description:
* @date: 21:16 2018/6/30
*/
@Service
public class SvnProjectService {

    /**
     * 日志记录
     */
    protected final static Logger LOG = LoggerFactory.getLogger(SvnProjectService.class);


    /**
     * 项目的存放位置
     */
    @Value(value = "${app.svn.workspace}")
    private String workspace;


    /**
     * SVN的用户名
     */
    @Value(value = "${app.svn.userName}")
    private String userName;

    /**
     * SVN账户密码
     */
    @Value(value = "${app.svn.password}")
    private String password;



    private void init(){

    }

    public SvnProjectService(){
        super();
        init();
    }

    /**
     * 创建项目
     * @param project
     * 			Project
     * @return
     */
    public boolean createProject(Project project) {
        if(project == null){
            return false;
        }
        // project work copy
        File ws = new File(workspace);
        if(!ws.exists()){
            ws.mkdirs();
        }
        File dest = new File(workspace + File.separator + project.getName());
        if(!dest.exists()){
            dest.mkdirs();
        }
        // 确定工作空间
        checkWorkCopy(project);

        return true;
    }

    public String getWorkCopy(Project project) {
        File ws = new File(new File(workspace), project.getName());
        return ws.getAbsolutePath();
    }


    /**
     * 从SVN更新项目到work copy
     * @param project
     * 			Project
     * @return
     */
    public boolean updateProjectFromSvn(Project project) {
        if(null == project){
            return false;
        }

        SVNClientManager clientManager = SVNUtils.authSvn(project.getSvnUrl(), userName, password);
        if (null == clientManager) {
            LOG.error("SVN login error! >>> url:{} username:{} password:",project.getSvnUrl(), userName, password);
            return false;
        }

        // 注册一个更新事件处理器 todo
//        clientManager.getCommitClient().setEventHandler(new UpdateEventHandler());

        SVNURL repositoryURL;
        try {
            repositoryURL = SVNURL.parseURIEncoded(project.getSvnUrl());
        } catch (SVNException e) {
            LOG.error("解析SVN路径{}出现异常",project.getSvnUrl(),e);
            return false;
        }

        File ws = new File(new File(workspace), project.getName());

        if(!SVNWCUtil.isVersionedDirectory(ws)){
            SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, new File(workspace), SVNDepth.INFINITY);
        }else{
            //能否保证文件一致性 todo
            SVNUtils.update(clientManager, ws, SVNRevision.HEAD, SVNDepth.INFINITY);
        }
        return true;
    }

    /**
     * 提交项目到SVN
     * @param project
     * 			Project
     * @return
     */
    public boolean commitProjectToSvn(Project project) {
        SVNClientManager clientManager = SVNUtils.authSvn(project.getSvnUrl(), userName, password);

//        clientManager.getCommitClient().setEventHandler(new CommitEventHandler());

        File wc_project = new File( workspace + "/" + project.getName());

        checkVersiondDirectory(clientManager,wc_project);

        SVNUtils.commit(clientManager, wc_project, false, "svnkit");

        return true;
    }

    /**
     * 递归检查不在版本控制的文件，并add到svn
     * @param clientManager
     * @param wc
     */
    private void checkVersiondDirectory(SVNClientManager clientManager,File wc){
        if(!SVNWCUtil.isVersionedDirectory(wc)){
            SVNUtils.addEntry(clientManager, wc);
        }
        if(wc.isDirectory()){
            for(File sub:wc.listFiles()){
                if(sub.isDirectory() && sub.getName().equals(".svn")){
                    continue;
                }
                checkVersiondDirectory(clientManager,sub);
            }
        }
    }

    private void checkWorkCopy(Project project){

        SVNClientManager clientManager = SVNUtils.authSvn(project.getSvnUrl(), userName, password);

        SVNURL repositoryURL = null;
        try {
            // http://svn.ruijie.net/svn/RCD/Version/文档归档/项目文档/2018云课堂项目归档/RCC_V3.5.X/RCC_V3.5_R1/开发阶段/内部测试/第二轮测试/测试用例
            repositoryURL = SVNURL.parseURIEncoded(project.getSvnUrl());
        } catch (SVNException e) {
            LOG.error("解析SVN路径{}出现异常",project.getSvnUrl(),e);
        }

//        File wc = new File(workspace);
        File wc_project = new File( workspace + "/" + project.getName());
        try {

            if(!SVNUtils.isWorkingCopy(wc_project)){
                if(!SVNUtils.isURLExist(repositoryURL,userName,password)){
                    SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, wc_project, SVNDepth.EMPTY);
                }else{
                    SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, wc_project, SVNDepth.INFINITY);
                }
            }else{
                SVNUtils.update(clientManager, wc_project, SVNRevision.HEAD, SVNDepth.INFINITY);
            }
        } catch (SVNException e) {
            LOG.error("异常",e);
        }
    }

    //判断指定路径是否存在，以及访问性
    public boolean validSvnUrl(String svnUrl) {
        if (StringUtils.isBlank(svnUrl)) {
            throw new ServiceException("svn地址不能为空");
        }
        SVNURL repositoryURL;
        try {
            repositoryURL = SVNURL.parseURIEncoded(svnUrl);
        } catch (SVNException e) {
            LOG.error("解析{}出现异常",svnUrl,e);
            throw new ServiceException("svn地址不正确");
        }
        boolean result = false;
        try {
            result = SVNUtils.isURLExist(repositoryURL,userName,password);
        }catch (SVNException e){
            LOG.error("判断路径存在性出现异常",e);
            throw new ServiceException("当前svn账号权限不足");
        }
        return result;
    }




}
