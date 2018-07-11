package com.ruijie.model;


import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
* @author zql
* @description:
* @date: 8:47 2018/7/1
*/
@Table(name = "t_test_report")
public class TestReport {

    /**
     *  唯一ID
     */
    @Id
    private String id;

    /**
     * 对应项目的ID
     */
    private String projectId;

    /**
     * 用例包名称
     */
    @Column(name = "package")
    private String usecasePackage;

    /**
     * 功能模块
     */
    private String module;

    /**
     * 执行人
     */
    private String executor;

    /**
     * 优先级列表
     */
    private String priorities;

    /**
     * 用例总数
     */
    private Integer total;

    /**
     * 通过条数
     */
    private Integer pass;

    /**
     * 失败条数
     */
    private Integer fail;

    /**
     * 已执行条数
     */
    @Transient
    private Integer finish;

    /**
     * 完成率
     */
    private Double finishRate;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUsecasePackage() {
        return usecasePackage;
    }

    public void setUsecasePackage(String usecasePackage) {
        this.usecasePackage = usecasePackage;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getPriorities() {
        return priorities;
    }

    public void setPriorities(String priorities) {
        this.priorities = priorities;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPass() {
        return pass;
    }

    public void setPass(Integer pass) {
        this.pass = pass;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Integer getFinish() {
        return finish;
    }

    public void setFinish(Integer finish) {
        this.finish = finish;
    }

    public Double getFinishRate() {
        return finishRate;
    }

    public void setFinishRate(Double finishRate) {
        this.finishRate = finishRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
