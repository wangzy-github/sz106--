package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

/**
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/13
 * @Time: 17:00
 */
public interface MemberDao {
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
     * @param month
     * @return
     */
    Integer findMemberCountByMonth(String month);

    /**
     * 获取指定日的新增会员数
     *
     * @param regTime
     * @return
     */
    Integer getNewMemberByDate(String regTime);

    /**
     * 获取总会员数
     *
     * @return
     */
    Integer getTotalMember();

    /**
     * 获取指定日期之后的新增会员数
     *
     * @param regTime
     * @return
     */
    Integer getNewMemberAfterDate(String regTime);
}
