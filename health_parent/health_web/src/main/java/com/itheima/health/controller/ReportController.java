package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @PackageName: com.itheima.health.controller
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/15
 * @Time: 17:24
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 根据月份获取会员统计信息
     *
     * @return
     */
    @GetMapping("/getMemberReportByMonth")
    public Result getMemberReportByMonth(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 校验输入的月份范围
        if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
            return new Result(false, "请输入查询的月份");
        }
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            if (startDate.after(endDate)) {
                return new Result(false, "选择的月份范围有误");
            }
            Date now  = new Date();
            String[] startArr = start.split("-");
            String[] endArr = end.split("-");
            int startY = Integer.valueOf(startArr[0]);
            int startM = Integer.valueOf(startArr[1]);
            int endY = Integer.valueOf(endArr[0]);
            int endM = Integer.valueOf(endArr[1]);
            if (
                    (startDate.after(now))||(endDate.after(now))
            ) {
                return new Result(false, "请查询当前时间之前的月份");
            }
            Map<String, Object> memberReport = new HashMap<>();
            // 计算需要展示几个月份的数据
            int number = (endY - startY) * 12
                    + (endM - startM) + 1;
            // 获取开始时间的日历
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取去年的日历
            // calendar.add(Calendar.YEAR, -1);
            // 获取每个月
            List<String> months = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                months.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.MONTH, 1);
            }
            List<Integer> memberCount = memberService.getMemberReport(months);
            memberReport.put("months", months);
            memberReport.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, memberReport);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "选择的月份范围有误");
        }
    }

    /**
     * 获取最近一年会员统计信息
     *
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Map<String, Object> memberReport = new HashMap<>();
        // 获取去年的日历
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        // 获取每个月
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            months.add(sdf.format(calendar.getTime()));
        }
        List<Integer> memberCount = memberService.getMemberReport(months);
        memberReport.put("months", months);
        memberReport.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, memberReport);
    }

    /**
     * 获取套餐统计信息
     *
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {
        List<Map<String, Object>> setmealCount = setmealService.getSetmealReport();
        List<String> setmealNames
                = setmealCount.stream().map(map -> ((String) map.get("name"))).collect(Collectors.toList());
        Map<String, Object> setmealReport = new HashMap<>();
        setmealReport.put("setmealNames", setmealNames);
        setmealReport.put("setmealCount", setmealCount);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, setmealReport);
    }

    /**
     * 查询运营统计数据
     *
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> businessReportData = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, businessReportData);
    }

    /**
     * 导出Excel文档
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res) {
        // 获取运营统计数据
        Map<String, Object> business = reportService.getBusinessReportData();
        // 获取模板,req.getSession().getServletContext().getRealPath()获取到webapp目录
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        try (Workbook wk = new XSSFWorkbook(template);
             OutputStream outputStream = res.getOutputStream();) {
            // 获取工作表
            Sheet sheetAt = wk.getSheetAt(0);
            // 日期
            sheetAt.getRow(2).getCell(5).setCellValue((String) business.get("reportDate"));
            // 今日新增会员数
            // 总会员数
            // 本周新增会员数
            // 本月新增会员数
            sheetAt.getRow(4).getCell(5).setCellValue((Integer) business.get("todayNewMember"));
            sheetAt.getRow(4).getCell(7).setCellValue((Integer) business.get("totalMember"));
            sheetAt.getRow(5).getCell(5).setCellValue((Integer) business.get("thisWeekNewMember"));
            sheetAt.getRow(5).getCell(7).setCellValue((Integer) business.get("thisMonthNewMember"));
            // 今日预约数
            // 今日到诊数
            // 本周预约数
            // 本周到诊数
            // 本月预约数
            // 本月到诊数
            sheetAt.getRow(7).getCell(5).setCellValue((Integer) business.get("todayOrderNumber"));
            sheetAt.getRow(7).getCell(7).setCellValue((Integer) business.get("todayVisitsNumber"));
            sheetAt.getRow(8).getCell(5).setCellValue((Integer) business.get("thisWeekOrderNumber"));
            sheetAt.getRow(8).getCell(7).setCellValue((Integer) business.get("thisWeekVisitsNumber"));
            sheetAt.getRow(9).getCell(5).setCellValue((Integer) business.get("thisMonthOrderNumber"));
            sheetAt.getRow(9).getCell(7).setCellValue((Integer) business.get("thisMonthVisitsNumber"));
            // 热门套餐
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) business.get("hotSetmeal");
            if (hotSetmeal != null && hotSetmeal.size() > 0) {
                int row = 12;
                for (Map<String, Object> setmeal : hotSetmeal) {
                    sheetAt.getRow(row).getCell(4).setCellValue((String) setmeal.get("name"));
                    sheetAt.getRow(row).getCell(5).setCellValue((Long) setmeal.get("setmeal_count"));
                    BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                    sheetAt.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                    sheetAt.getRow(row).getCell(7).setCellValue((String) setmeal.get("remark"));
                    row++;
                }
            }
            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            // 解决下载的文件名 中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition", "attachement;filename=" + filename);
            wk.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出Pdf文档
     */
    @GetMapping("/exportBusinessReportPdf")
    public Result exportBusinessReportPdf(HttpServletRequest req, HttpServletResponse res) {
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String basePath = req.getSession().getServletContext().getRealPath("/template");
        // jrxml路径
        String jrxml = basePath + File.separator + "health_business3.jrxml";
        // jasper路径
        String jasper = basePath + File.separator + "health_business3.jasper";
        try {
            // 编译模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            Map<String, Object> businessReport = reportService.getBusinessReportData();
            // 热门套餐(list -> Detail1)
            List<Map<String, Object>> hotSetmeals = (List<Map<String, Object>>) businessReport.get("hotSetmeal");
            // 填充数据 JRBeanCollectionDataSource自定义数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, businessReport, new JRBeanCollectionDataSource(hotSetmeals));
            res.setContentType("application/pdf");
            res.setHeader("Content-Disposition", "attachement;filename=运营统计数据报表.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, res.getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "导出运营数据统计pdf失败");
    }
}
