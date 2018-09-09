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
    public String createProject(Project project) {
        // project work copy
        File ws = new File(workspace);
        if(!ws.exists() && !ws.mkdirs()){
            LOG.error("创建工作空间{}失败",workspace);
            throw new ServiceException("服务器内部异常，请联系管理员");
        }
        //存储结构：从workspace/{projectName}更改为workspace/{projectId}
        File dest = new File(workspace,project.getId());
        if(!dest.exists() && !dest.mkdirs()){
            LOG.error("创建项目目录{}失败",dest.getAbsolutePath());
            throw new ServiceException("服务器内部异常，请联系管理员");
        }
        // 检出文件到工作空间
        checkoutWorkCopy(project.getSvnUrl(),dest);

        return dest.getAbsolutePath();
    }

    /**
     * 根据项目ID获取工作目录
     * @param projectId
     * @return
     */
    public String getWorkCopy(String projectId) {
        File ws = new File(new File(workspace), projectId);
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

        File ws = new File(new File(workspace), project.getId());

        if(!SVNWCUtil.isVersionedDirectory(ws)){
            SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, ws, SVNDepth.INFINITY);
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

        checkVersionedDirectory(clientManager,wc_project);

        SVNUtils.commit(clientManager, wc_project, false, "svnkit");

        return true;
    }

    /**
     * 递归检查不在版本控制的文件，并add到svn
     * @param clientManager
     * @param wc
     */
    private void checkVersionedDirectory(SVNClientManager clientManager,File wc){
        if(!SVNWCUtil.isVersionedDirectory(wc)){
            SVNUtils.addEntry(clientManager, wc);
        }
        if(wc.isDirectory()){
            for(File sub:wc.listFiles()){
                if(sub.isDirectory() && sub.getName().equals(".svn")){
                    continue;
                }
                checkVersionedDirectory(clientManager,sub);
            }
        }
    }

    /**
     * 检出文件到指定目录
     * @param svnUrl
     * @param dir
     */
    private void checkoutWorkCopy(String svnUrl, File dir){
        LOG.info("当前操作目录:{}",dir.getAbsolutePath());
        SVNClientManager clientManager = SVNUtils.authSvn(svnUrl, userName, password);
        if (null == clientManager) {
            LOG.error("获取svn客户端连接失败");
            throw new ServiceException("内部服务器错误，请联系管理员");
        }
        SVNURL repositoryURL = null;
        try {
            repositoryURL = SVNURL.parseURIEncoded(svnUrl);
        } catch (SVNException e) {
            LOG.error("解析SVN路径{}出现异常",svnUrl,e);
            throw new ServiceException("内部服务器错误，请联系管理员");
        }
        try {
            if(!SVNUtils.isWorkingCopy(dir)){
                if(!SVNUtils.isURLExist(repositoryURL,userName,password)){
                    SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, dir, SVNDepth.EMPTY);
                }else{
                    SVNUtils.checkout(clientManager, repositoryURL, SVNRevision.HEAD, dir, SVNDepth.INFINITY);
                }
            }else{
                SVNUtils.update(clientManager, dir, SVNRevision.HEAD, SVNDepth.INFINITY);
            }
        } catch (SVNException e) {
            LOG.error("从{}检出文件或者更新文件到目录{}出现异常",svnUrl,dir,e);
            throw new ServiceException("内部服务器错误，请联系管理员");
        }
    }

    /**
     * 判断路径是否可达：
     * 1.是否存在
     * 2.是否有权限访问
     * @param svnUrl
     * @return
     */
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
        boolean result;
        try {
            result = SVNUtils.isURLExist(repositoryURL,userName,password);
        }catch (SVNException e){
            LOG.error("判断路径存在性出现异常",e);
            throw new ServiceException("当前svn账号权限不足");
        }
        return result;
    }

}
