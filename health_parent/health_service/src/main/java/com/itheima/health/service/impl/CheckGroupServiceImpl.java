package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @PackageName: com.itheima.health.serivce.impl
 * @Another: Wangzy
 * @Version: 1.0
 * @Date: 2021/1/6
 * @Time: 17:43
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //添加检查组中的检查项
        Integer checkGroupId = checkGroup.getId();
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkItemId);
            }
        }
    }

    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    @Transactional
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //更新检查组
        checkGroupDao.update(checkGroup);
        //更新检查组与检查项的关联
        Integer checkGroupId = checkGroup.getId();
        checkGroupDao.deleteCheckGroupCheckItem(checkGroupId);
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkItemId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        // 判断检查组是否有关联的套餐
        if (checkGroupDao.findSetMealCountByCheckGroupId(id) > 0) {
            throw new HealthException(MessageConstant.CHECKGROUP_IN_USE);
        }
        // 删除与检查项的关联
        checkGroupDao.deleteCheckGroupCheckItem(id);
        // 删除检查组
        checkGroupDao.deleteById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
