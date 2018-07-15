package com.ruijie.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.ruijie.model.Project;
import com.ruijie.model.TestReport;
import com.ruijie.model.UserCase;
import com.ruijie.service.ProjectService;
import com.ruijie.service.TestReportService;
import com.ruijie.util.ExcelToBean;
import com.ruijie.util.ReflectUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
//@RequestMapping(value = "/api")
public class TestReportController {

    private static Logger LOG = LoggerFactory.getLogger(TestReportController.class);

    @Autowired
    private TestReportService testReportService;

    @Autowired
    private ProjectService projectService;

    private static DecimalFormat decimalFormat = new DecimalFormat("#.0");

    /**
     * 获取创建报告的选项:用例包的名称、模块名称
     * @param projectId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/testReport/options/{projectId}")
    public String getOptions(@PathVariable(value = "projectId") String projectId) throws Exception {
        //根据projectId，查询svn或者本地copy，读取文件
        String filePath = projectService.getWorkCopyFilesPath(projectId);

        //需要支持xls|xlsx|xlsm
        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.or(
                FileFilterUtils.suffixFileFilter(".xlsx", IOCase.INSENSITIVE),
                FileFilterUtils.suffixFileFilter(".xlsm",IOCase.INSENSITIVE),
                FileFilterUtils.suffixFileFilter(".xls",IOCase.INSENSITIVE)), null);
        JSONObject result = new JSONObject();
        int index = 0;
        JSONObject child;
        JSONArray module;
        JSONArray packageArray = new JSONArray();
        for (File one : fileList) {
            //文件名称
            String fileNameNoType = one.getName().split("\\.xls")[0];
            child = new JSONObject();
            child.put("id",fileNameNoType);
            child.put("value",fileNameNoType);
            packageArray.add(child);

            //模块名称
            //这里需要根据不同后缀或者其他方式选择不同的WorkBoook todo
            List<String> moduleList = ExcelToBean.parseExcelForSheetName(new XSSFWorkbook(new FileInputStream(one)));
            module = new JSONArray();
//            for (String moduleName:moduleList) {
//                module.add(moduleName);
//            }
            for (int i=0;i<moduleList.size();i++) {
                child = new JSONObject();
                child.put("id","m"+index);
                child.put("value",moduleList.get(i));
                module.add(child);
            }
            result.put(fileNameNoType,module);
            index++;
        }
        result.put("packages",packageArray);
        JSONObject response = new JSONObject();
        response.put("options",result);
        LOG.debug("{}",response.toJSONString());

        return response.toJSONString();
    }

    /**
     * 查询项目对应的测试报告列表
     */
    @GetMapping(value = "/testReport/list/{projectId}")
    public PageInfo<TestReport> getReportList(@PathVariable(value = "projectId") String projectId,@RequestParam(value = "pageNum", required = false) Integer pageNum,@RequestParam(value = "name",required = false) String name) {
        if (null == pageNum || pageNum < 0) {
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,10);
        //更新数据并入库
        Map<String, Object> paraMap = Maps.newHashMap();
        paraMap.put("projectId",projectId);
        if (StringUtils.isNotBlank(name)) {
            paraMap.put("package",name);
        }
//        List<TestReport> list = testReportService.find(paraMap);
        Example example = new Example(Project.class);
        example.setOrderByClause("create_time desc");
        example.createCriteria().andEqualTo(paraMap);
        List<TestReport> list = testReportService.findByExample(example);
//        JSONObject result = new JSONObject();
//        result.put("reports",list);
//        return result.toJSONString();
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
        String filePath = projectService.getWorkCopyFilesPath(projectId);
        //
        String round = project.getRound();
        String prioritySet = report.getPriorities();
        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.prefixFileFilter(report.getUsecasePackage()), null);
        for (File one : fileList) {
            //模块名称
            List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(new XSSFWorkbook(new FileInputStream(one)), report.getModule());

            List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);

            //统计优先级的级别

            //统计执行情况
            int sum = 0;
            int pass = 0;
            int fail = 0;
            for (UserCase userCase : lists) {
                if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
                    sum++;
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


//                switch (round) {
//                    case "round1":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound1())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound1())) {
//                                fail++;
//                            }
//
//                        }
//                        break;
//                    case "round2":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound2())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound2())) {
//                                fail++;
//                            }
//
//                        }
//                        break;
//                    case "round3":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound3())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound3())) {
//                                fail++;
//                            }
//
//                        }
//                        break;
//                    case "round4":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound4())) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound4())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound4())) {
//                                fail++;
//                            }
//
//                        }
//                        break;
//                    case "round5":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound5())) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound5())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound6())) {
//                                fail++;
//                            }
//                        }
//                        break;
//                    case "round6":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound6())) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound6())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound6())) {
//                                fail++;
//                            }
//                        }
//                        break;
//                    case "round7":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound7())) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound7())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound7())) {
//                                fail++;
//                            }
//                        }
//                        break;
//                    case "round8":
//                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound8())) {
//                            sum++;
//                            if ("pass".equalsIgnoreCase(userCase.getRound8())) {
//                                pass++;
//                            } else if ("fail".equalsIgnoreCase(userCase.getRound8())) {
//                                fail++;
//                            }
//                        }
//                        break;
//                    default:
//                        break;
//                }

            }
            report.setTotal(sum);
            report.setFail(fail);
            report.setPass(pass);
            report.setFinish(fail + pass);
            //保留一位小数 todo
            double finishRate = ((fail + pass) / (double) sum) * 100;
            report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
            testReportService.updateById(report);
        }
        return report;
    }
    private void refreshReportByProjectId(String projectId, List<TestReport> testReportList) {
        Project project = projectService.findOne(projectId);
        String filePath = projectService.getWorkCopyFilesPath(projectId);
        String round = project.getRound();
        if (null == testReportList || testReportList.size() == 0) {
            //从数据库拉起所有的report
        }
        String prioritySet = report.getPriorities();
        Collection<File> fileList = FileUtils.listFiles(new File(filePath), FileFilterUtils.prefixFileFilter(report.getUsecasePackage()), null);
        for (File one : fileList) {
            //模块名称
            List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(new XSSFWorkbook(new FileInputStream(one)), report.getModule());

            List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);

            //统计优先级的级别

            //统计执行情况
            int sum = 0;
            int pass = 0;
            int fail = 0;
            for (UserCase userCase : lists) {
                if (prioritySet.contains(userCase.getPriority().split("\\.0")[0])) {
                    sum++;
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
            report.setTotal(sum);
            report.setFail(fail);
            report.setPass(pass);
            report.setFinish(fail + pass);
            //保留一位小数 todo
            double finishRate = ((fail + pass) / (double) sum) * 100;
            report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
    }
    /**
     * 新增测试报告
     */
    @PostMapping(value = "/testReport")
    public Object createReport(@RequestBody TestReport report) throws Exception {
        report.setId(UUID.randomUUID().toString().replace("-",""));
        testReportService.save(report);
        //从文件读取测试结果
        //找到文件
        String projectId = report.getProjectId();
        Project project = projectService.findOne(projectId);
        String filePath = projectService.getWorkCopyFilesPath(projectId);
        //
        String round = project.getRound();
        String prioritySet = report.getPriorities();
        Collection<File> fileList = FileUtils.listFiles(new File(filePath),FileFilterUtils.prefixFileFilter(report.getUsecasePackage()),null);
        for (File one : fileList) {
            //模块名称
            List<Map<String, Object>> list = ExcelToBean.parseExcelByParam(new XSSFWorkbook(new FileInputStream(one)),report.getModule());

            List<UserCase> lists = ExcelToBean.toObjectList(list, UserCase.class);

            //统计优先级的级别

            //统计执行情况
            int sum = 0;
            int pass = 0;
            int fail = 0;
            for (UserCase userCase : lists) {
                switch (round) {
                    case "round1":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) ) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound1())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound1())) {
                                fail++;
                            }

                        }
                        break;
                    case "round2":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) ) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound2())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound2())) {
                                fail++;
                            }

                        }
                        break;
                    case "round3":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) ) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound3())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound3())) {
                                fail++;
                            }

                        }
                        break;
                    case "round4":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound4())) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound4())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound4())) {
                                fail++;
                            }

                        }
                        break;
                    case "round5":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound5())) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound5())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound6())) {
                                fail++;
                            }
                        }
                        break;
                    case "round6":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound6())) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound6())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound6())) {
                                fail++;
                            }
                        }
                        break;
                    case "round7":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound7())) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound7())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound7())) {
                                fail++;
                            }
                        }
                        break;
                    case "round8":
                        if (prioritySet.contains(userCase.getPriority().split("\\.0")[0]) && StringUtils.isNotBlank(userCase.getRound8())) {
                            sum++;
                            if ("pass".equalsIgnoreCase(userCase.getRound8())) {
                                pass++;
                            } else if ("fail".equalsIgnoreCase(userCase.getRound8())) {
                                fail++;
                            }
                        }
                        break;
                    default:
                        break;
                }

            }
            report.setTotal(sum);
            report.setFail(fail);
            report.setPass(pass);
            report.setFinish(fail+pass);
            //保留一位小数
            double finishRate = ((fail+pass) / (double)sum) * 100;
            report.setFinishRate(Double.parseDouble(decimalFormat.format(finishRate)));
            testReportService.updateById(report);
        }
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


}
