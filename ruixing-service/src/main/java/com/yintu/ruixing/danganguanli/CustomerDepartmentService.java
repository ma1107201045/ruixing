package com.yintu.ruixing.danganguanli;

import com.yintu.ruixing.common.util.BaseService;
import com.yintu.ruixing.common.util.TreeNodeUtil;

import java.util.List;

/**
 * @author mlf
 * @version 1.0
 * @date 2020/9/20 13:53
 */
public interface CustomerDepartmentService extends BaseService<CustomerDepartmentEntity, Integer> {

    /**
     * 指定删除id，以及次节点父节点
     *
     * @param id      部门id
     * @param isFirst 是否第一次
     */
    void removeByIdAndIsFirst(Integer id, Boolean isFirst);

    List<TreeNodeUtil> findByParentIdAndTypeId(Integer parentId, Short type);

    List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type);

    List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type, List<TreeNodeUtil> treeNodeUtils);

    List<TreeNodeUtil> findCustomerTypeAndCustomerDepartmentTree();
}
