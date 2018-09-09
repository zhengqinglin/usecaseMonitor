package com.ruijie.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.ruijie.exception.ServiceException;
import com.ruijie.model.Project;
import com.ruijie.model.TestReport;
import com.ruijie.model.UserCase;
import com.ruijie.model.bo.StatisticsResult;
import com.ruijie.service.ProjectService;
import com.ruijie.service.TestReportService;
import com.ruijie.util.ExcelToBean;
import com.ruijie.util.ReflectUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class TestReportController {

    private static Logger LOG = LoggerFactory.getLogger(TestReportController.class);

    @Autowired
    private TestReportService testReportService;

    @Autowired
    private ProjectService projectService;

    private static DecimalFormat decimalFormat = new DecimalFormat("#.0");

    /**
     * 获取创建报告的选项:用例包的名称、模块名称
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/testReport/options/{projectId}")
    public String getOptions(@PathVariable(value = "projectId") String projectId) throws Exception {
        //根据projectId，查询svn或者本地copy，读取文件
        String filePath = projectService.getWorkCopyPath(projectId);
        Collection<File> fileList = getExcelFileList(filePath);
        JSONObject result = new JSONObject();
        int index = 0;
        JSONObject child;
        JSONArray module;
        JSONArray packageArray = new JSONArray();
        for (File one : fileList) {
            //文件名称
            String fileNameNoType = Files.getNameWithoutExtension(one.getAbsolutePath());
            child = new JSONObject();
            child.put("id", fileNameNoType);
            child.put("value", fileNameNoType);
            packageArray.add(child);

            //模块名称
            //这里需要根据不同后缀或者其他方式选择不同的WorkBoook
            List<String> moduleList = ExcelToBean.parseExcelForSheetName(ExcelToBean.resolveExcelFileType(one));
            module = new JSONArray();
            for (int i = 0; i < moduleList.size(); i++) {
                child = new JSONObject();
                child.put("id", "m" + index);
                child.put("value", moduleList.get(i));
                module.add(child);
            }
            result.put(fileNameNoType, module);
            index++;
        }
        result.put("packages", packageArray);
        JSONObject response = new JSONObject();
        response.put("options", result);
        LOG.debug("{}", response.toJSONString());

        return response.toJSONString();
    }

    //需要支持xls|xlsx|xlsm
    private Collection<File> getExcelFileList(String filePath) {
        return FileUtils.listFiles(new File(filePath), FileFilterUtils.or(
                FileFilterUtils.suffixFileFilter(".xlsx", IOCase.INSENSITIVE),
                FileFilterUtils.suffixFileFilter(".xlsm", IOCase.INSENSITIVE),
                FileFilterUtils.suffixFileFilter(".xls", IOCase.INSENSITIVE)), null);
    }

    /**
     * 查询项目对应的测试报告列表
     */
    @GetMapping(value = "/testReport/list/{projectId}")
    public PageInfo<TestReport> getReportList(@PathVariable(value = "projectId") String projectId, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "name", required = false) String name) {
        if (null == pageNum || pageNum < 0) {
            pageNum = 1;
        }
        PageHelper.startPage(pageNum, 10);
        //更新数据并入库
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("projectId", projectId);
        if (StringUtils.isNotBlank(name)) {
            paraMap.put("package", name);
        }
        Example example = new Example(TestReport.class);
        example.setOrderByClause("create_time desc");
        example.createCriteria().andEqualTo(paraMap);
        List<TestReport> list = testReportService.findByExample(example);
        list = refreshReportByProjectId(projectId, list);
        PageInfo<TestReport> result = new PageInfo<>(list);
        return result;
    }

    /**
     * 查询测试报告
     */
    @GetMapping(value = "/testReport/{id}")
    public TestReport getReportById(@PathVariable(value = "id") String id) throws Exception {
        //更新数据
        TestReport report = testReportService.findOne(id);
        String projectId = report.getProjectId();
        Project project = projectService.findOne(projectId);
        report = updateTestReport(project,report);
//        String filePath = projectService.getWorkCopyPath(projectId);
//        String round = project.getRound();
//        String prioritySet = report.getPriorities();
//        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.prefixFileFilter(report.getUsecasePackage()), null);
//        for (File one : fileList) {
//            //模块名称
//            List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(ExcelToBean.resolveExcelFileType(one), report.getModule());
//
//            List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);
//
//            //统计优先级的级别
//
//            //统计执行情况
//            StatisticsResult statisticsResult = countTestReport(lists, prioritySet, round);
//
//            report.setTotal(statisticsResult.getSum());
//            report.setFail(statisticsResult.getFail());
//            report.setPass(statisticsResult.getPass());
//            report.setFinish(statisticsResult.getFail() + statisticsResult.getPass());
//            double finishRate = (report.getFinish() / (double) report.getTotal() )* 100;
//            report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
//            testReportService.updateById(report);
//        }
        return report;
    }

    private StatisticsResult countTestReport(List<UserCase> lists,String prioritySet, String round) {
        //统计执行情况
        int sum = 0;
        int pass = 0;
        int fail = 0;
        try {
            for (UserCase userCase : lists) {
                if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
                    sum++;
                    if (null == ReflectUtils.getGetMethod(userCase, round)) {
                        continue;
                    }
                    switch (ReflectUtils.getGetMethod(userCase, round).toString().toLowerCase()) {
                        case "pass":
                            pass++;
                            break;
                        case "fail":
                            fail++;
                            break;
                        default:
                            LOG.warn("异常值");
                            break;
                    }

                }
            }
        } catch (Exception e) {
            LOG.error("统计执行结果出现异常",e);
        }
        return new StatisticsResult(sum,pass,fail);

    }

    private List<TestReport> refreshReportByProjectId(String projectId, List<TestReport> testReportList) {
        //根据项目ID找到项目对应的work copy
        //加入缓存，递归遍历，文件名为key todo
        //执行svn update操作
        //
        Project project = projectService.findOne(projectId);
        //更新项目，如果失败则抛出异常
        if (!projectService.updateProject(projectId)) {
            throw new ServiceException("内部服务器错误,请联系管理员");
        }
        String filePath = projectService.getWorkCopyPath(projectId);
        LOG.error("filePath:{}", filePath);
        if (null == testReportList || testReportList.size() == 0) {
            return null;
        }
        //根据文件名，不带后缀，找到文件的路劲
        //获取当前的测试轮次
        String round = project.getRound();
        String prioritySet;
        Collection<File> fileList = getExcelFileList(filePath);
        Map<String, File> fileMap = Maps.newHashMap();
        for (File file : fileList) {
            fileMap.put(Files.getNameWithoutExtension(file.getAbsolutePath()), file);
        }
        //文件的内容是否需要统一读取？
        for (TestReport report : testReportList) {
            updateTestReport(project,report);
//            prioritySet = report.getPriorities();
//            //模块名称
//            List<Map<String, Object>> list = null;
//            try {
//                list = ExcelToBean.parseExcelByParam(ExcelToBean.resolveExcelFileType(fileMap.get(report.getUsecasePackage())), report.getModule());
//                List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);
//                //统计执行情况
//                StatisticsResult statisticsResult = countTestReport(lists, prioritySet, round);
//
//                report.setTotal(statisticsResult.getSum());
//                report.setFail(statisticsResult.getFail());
//                report.setPass(statisticsResult.getPass());
//                report.setFinish(statisticsResult.getFail() + statisticsResult.getPass());
//                double finishRate = (report.getFinish() / (double) report.getTotal() )* 100;
//                report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
//                testReportService.updateById(report);
//            } catch (IOException e) {
//                LOG.error("",e);
//            } catch (Exception e) {
//                LOG.error("",e);
//            }
        }
        return testReportList;
    }

    /**
     * 新增测试报告
     */
    @PostMapping(value = "/testReport")
    public Object createReport(@RequestBody TestReport report) throws Exception {
        report.setId(UUID.randomUUID().toString().replace("-", ""));
        testReportService.save(report);
        //从文件读取测试结果
        //找到文件
        String projectId = report.getProjectId();
        Project project = projectService.findOne(projectId);
        updateTestReport(project,report);
//        String filePath = project.getWorkingCopyPath();
//        String round = project.getRound();
//        String prioritySet = report.getPriorities();
//        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.prefixFileFilter(report.getUsecasePackage()), null);
//        for (File one : fileList) {
//            //模块名称
//            List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(ExcelToBean.resolveExcelFileType(one), report.getModule());
//
//            List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);
//
//            //统计执行情况
//            StatisticsResult statisticsResult = countTestReport(lists, prioritySet, round);
//
//            report.setTotal(statisticsResult.getSum());
//            report.setFail(statisticsResult.getFail());
//            report.setPass(statisticsResult.getPass());
//            report.setFinish(statisticsResult.getFail() + statisticsResult.getPass());
//            double finishRate = (report.getFinish() / (double) report.getTotal() )* 100;
//            report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
//            testReportService.updateById(report);
//        }
        return report;
    }

    /**
     * 删除测试报告
     */
    @DeleteMapping(value = "/testReport/{id}")
    public String deleteReport(@PathVariable(value = "id") String id) {
        testReportService.deleteById(id);
        return null;
    }

    private TestReport updateTestReport(Project project,TestReport report) {
        String filePath = project.getWorkingCopyPath();
        String round = project.getRound();
        String prioritySet = report.getPriorities();
        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.prefixFileFilter(report.getUsecasePackage()), null);
        try {
            for (File one : fileList) {
                //模块名称
                List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(ExcelToBean.resolveExcelFileType(one), report.getModule());

                List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);

                //统计执行情况
                StatisticsResult statisticsResult = countTestReport(lists, prioritySet, round);

                report.setTotal(statisticsResult.getSum());
                report.setFail(statisticsResult.getFail());
                report.setPass(statisticsResult.getPass());
                report.setFinish(statisticsResult.getFail() + statisticsResult.getPass());
                double finishRate = (report.getFinish() / (double) report.getTotal() )* 100;
                report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
                testReportService.updateById(report);
            }
        }catch (Exception e) {
            LOG.error("",e);
        }

        return report;
    }


}
