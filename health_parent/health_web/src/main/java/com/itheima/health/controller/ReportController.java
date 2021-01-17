package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
     * 获取会员统计信息
     *
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        Map<String, Object> memberReport=new HashMap<>();
        // 获取当前的日历
        Calendar calendar = Calendar.getInstance();
        // 获取去年的日历
        calendar.add(Calendar.YEAR,-1);
        // 获取过去12个月
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            months.add(sdf.format(calendar.getTime()));
        }
        List<Integer> memberCount = memberService.getMemberReport(months);
        memberReport.put("months",months);
        memberReport.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, memberReport);
    }

    /**
     * 获取套餐统计信息
     *
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        List<Map<String,Object>> setmealCount = setmealService.getSetmealReport();
        List<String> setmealNames
                = setmealCount.stream().map(map->((String)map.get("name"))).collect(Collectors.toList());
        Map<String,Object> setmealReport= new HashMap<>();
        setmealReport.put("setmealNames", setmealNames);
        setmealReport.put("setmealCount", setmealCount);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,setmealReport);
    }

    /**
     * 查询运营统计数据
     *
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> businessReportData = reportService.getBusinessReportData();
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessReportData);
    }

    /**
     * 导出Excel文档
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        // 获取运营统计数据
        Map<String,Object> business = reportService.getBusinessReportData();
        // 获取模板,req.getSession().getServletContext().getRealPath()获取到webapp目录
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        try(Workbook wk = new XSSFWorkbook(template);){
            Sheet sheetAt = wk.getSheetAt(0);
            // 日期
            sheetAt.getRow(2).getCell(5).setCellValue((String)business.get("reportDate"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
