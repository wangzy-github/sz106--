package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 19:44
 */
public interface MemberService {
    /**
     * 根据手机号查询会员
     *
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 新增会员
     *
     * @param member
     */
    void add(Member member);

    /**
     * 统计指定月份的会员
     *
     * @param months
     * @return
     */
    List<Integer> getMemberReport(List<String> months);
}
