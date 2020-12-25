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

    List<TreeNodeUtil> findByParentIdAndTypeId(Integer parentId, Short type);

    List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type);

    List<TreeNodeUtil> findByParentIdAndCustomerIdAndTypeId(Integer parentId, Integer customerId, Short type, List<TreeNodeUtil> treeNodeUtils);

    List<TreeNodeUtil> findCustomerTypeAndCustomerDepartmentTree();
}
