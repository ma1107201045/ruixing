package com.yintu.ruixing.jiejuefangan.impl;

import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.jiejuefangan.BiddingDao;
import com.yintu.ruixing.jiejuefangan.BiddingEntity;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.jiejuefangan.BiddingService;
import com.yintu.ruixing.common.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author:mlf
 * @date:2020/7/2 11:13
 */
@Service
@Transactional
public class BiddingServiceImpl implements BiddingService {
    @Autowired
    private BiddingDao biddingDao;
    @Autowired
    private MessageService messageService;

    @Override
    public void add(BiddingEntity entity) {
        biddingDao.insertSelective(entity);
        //投招标支持项目状态为3时发送消息
        if (entity.getProjectStatus().equals((short) 3)) {
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle("");
            messageEntity.setContext("“" + entity.getProjectName() + "”项目已中标，请关注项目进展情况，及时进行设计联络！");
            messageEntity.setType((short) 1);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }
    }

    @Override
    public void remove(Integer id) {
        biddingDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(BiddingEntity entity) {
        biddingDao.updateByPrimaryKeySelective(entity);
    }

    @Override
    public BiddingEntity findById(Integer id) {
        return biddingDao.selectByPrimaryKey(id);
    }

    @Override
    public List<BiddingEntity> findAll() {
        return biddingDao.selectAll();
    }

    @Override
    public List<BiddingEntity> findByYear(Integer year) {
        return biddingDao.selectByYear(year);
    }

    @Override
    public List<Integer> findByDistinctProjectDate() {
        return biddingDao.selectByDistinctProjectDate();
    }

    @Override
    public List<TreeNodeUtil> findByTree() {
        List<Integer> years = this.findByDistinctProjectDate();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (Integer year : years) {
            List<BiddingEntity> biddingEntities = this.findByYear(year);
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();

            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(1L);
            firstTreeNodeUtil.setLabel(String.valueOf(year));
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);

            for (BiddingEntity biddingEntity : biddingEntities) {
                List<TreeNodeUtil> thirdTreeNodeUtils = new ArrayList<>();
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(2L);
                secondTreeNodeUtil.setLabel(biddingEntity.getProjectName());
                Map<String, Object> map = new HashMap<>();
                map.put("id", biddingEntity.getId());
                map.put("projectDate", biddingEntity.getProjectDate());
                map.put("projectName", biddingEntity.getProjectName());
                map.put("projectStatus", biddingEntity.getProjectStatus());
                map.put("taskStatus", biddingEntity.getTaskStatus());
                map.put("taskFinishStatus", biddingEntity.getTaskFinishDate());
                map.put("bidder", biddingEntity.getBidder());
                map.put("railwayAdministrationId", biddingEntity.getRailwayAdministrationId());
                map.put("preSaleId", biddingEntity.getPreSaleId());
                secondTreeNodeUtil.setA_attr(map);
                secondTreeNodeUtil.setChildren(thirdTreeNodeUtils);
                secondTreeNodeUtils.add(secondTreeNodeUtil);

                TreeNodeUtil thirdTreeNodeUtil1 = new TreeNodeUtil();
                thirdTreeNodeUtil1.setId(3L);
                thirdTreeNodeUtil1.setLabel("输入文件");
                TreeNodeUtil thirdTreeNodeUtil2 = new TreeNodeUtil();
                thirdTreeNodeUtil2.setId(3L);
                thirdTreeNodeUtil2.setLabel("输出文件");
                thirdTreeNodeUtils.add(thirdTreeNodeUtil1);
                thirdTreeNodeUtils.add(thirdTreeNodeUtil2);
            }
        }
        return firstTreeNodeUtils;
    }
}
