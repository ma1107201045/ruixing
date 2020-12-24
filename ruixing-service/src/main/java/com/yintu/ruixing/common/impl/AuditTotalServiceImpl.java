package com.yintu.ruixing.common.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.AuditDto;
import com.yintu.ruixing.common.AuditTotalDto;
import com.yintu.ruixing.common.AuditTotalService;
import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.jiejuefangan.BiddingFileService;
import com.yintu.ruixing.jiejuefangan.DesignLiaisonFileService;
import com.yintu.ruixing.jiejuefangan.PreSaleFileService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import com.yintu.ruixing.zhishiguanli.ZhiShiGuanLiFileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: mlf
 * @Date: 2020/12/19 18:07
 * @Version: 1.0
 */
@Service
public class AuditTotalServiceImpl implements AuditTotalService {

    @Autowired
    private PreSaleFileService preSaleFileService;
    @Autowired
    private BiddingFileService biddingFileService;
    @Autowired
    private DesignLiaisonFileService designLiaisonFileService;
    @Autowired
    private ZhiShiGuanLiFileTypeService zhiShiGuanLiFileTypeService;

    @Override
    @Transactional
    public void audit(AuditDto auditDto) {
        switch (auditDto.getModuleType()) {//判断模块
            case 1:
                switch (auditDto.getType()) {//判断审核类型
                    case 1:
                    case 2:
                        preSaleFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                                auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                }
            case 2:
                switch (auditDto.getType()) {
                    case 1:
                    case 2:
                        biddingFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                                auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                }
            case 3:
                switch (auditDto.getType()) {
                    case 1:
                    case 2:
                        designLiaisonFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                                auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                }
            case 4:
                switch (auditDto.getType()) {
                    case 2:
                        zhiShiGuanLiFileTypeService.editAuditorByFileid(auditDto.getId(), auditDto.getIsPass(), auditDto.getPassUserId(),
                                auditDto.getTrueName(), auditDto.getLoginUserId(),
                                auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getContext());
                }
        }
    }

    @Override
    public List<UserEntity> findByOtherAuditors(AuditDto auditDto) {
        switch (auditDto.getModuleType()) {//判断模块
            case 1:
                switch (auditDto.getType()) {//判断审核类型
                    case 1:
                    case 2:
                        return preSaleFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());

                }
            case 2:
                switch (auditDto.getType()) {
                    case 1:
                    case 2:
                        return biddingFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());
                }

            case 3:
                switch (auditDto.getType()) {
                    case 1:
                    case 2:
                        return designLiaisonFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());
                }
            case 4:
                switch (auditDto.getType()) {
                    case 2:
                        zhiShiGuanLiFileTypeService.findZhuanJiaoAuditorName(auditDto.getId());
                }
            default:
                return new ArrayList<>();
        }

    }


    @Override
    public PageInfo<AuditTotalVo> findPage(int pageNum, int pageSize, AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos = new ArrayList<>();
        List<AuditTotalVo> preSaleAuditTotalVos = null;
        List<AuditTotalVo> biddingAuditTotalVos = null;
        List<AuditTotalVo> designLiaisonAuditTotalVos = null;
        List<AuditTotalVo> knowledgeManagementAuditTotalVos = null;//知识管理
        if (auditTotalDto.getModuleType() == null) {
            preSaleAuditTotalVos = this.findPreSale(auditTotalDto);
            biddingAuditTotalVos = this.findBidding(auditTotalDto);
            designLiaisonAuditTotalVos = this.findDesignLiaison(auditTotalDto);
            knowledgeManagementAuditTotalVos = this.findKnowledgeManagement(auditTotalDto);
        } else {
            switch (auditTotalDto.getModuleType()) {
                case 1:
                    preSaleAuditTotalVos = this.findPreSale(auditTotalDto);
                    break;
                case 2:
                    biddingAuditTotalVos = this.findBidding(auditTotalDto);
                    break;
                case 3:
                    designLiaisonAuditTotalVos = this.findDesignLiaison(auditTotalDto);
                    break;
                case 4:
                    knowledgeManagementAuditTotalVos = this.findKnowledgeManagement(auditTotalDto);
                    break;
            }
        }
        auditTotalVos.addAll(preSaleAuditTotalVos == null ? new ArrayList<>() : preSaleAuditTotalVos);
        auditTotalVos.addAll(biddingAuditTotalVos == null ? new ArrayList<>() : biddingAuditTotalVos);
        auditTotalVos.addAll(designLiaisonAuditTotalVos == null ? new ArrayList<>() : designLiaisonAuditTotalVos);
        auditTotalVos.addAll(knowledgeManagementAuditTotalVos == null ? new ArrayList<>() : knowledgeManagementAuditTotalVos);
        //统一按照发起时间排序（倒序）
        Page<AuditTotalVo> page = auditTotalVos.stream()
                .sorted(Comparator.comparing(AuditTotalVo::getInitiateTime).reversed())
                .skip(pageSize * (pageNum - 1))
                .limit(pageSize)
                .collect(Collectors.toCollection(() -> new Page<>(pageNum, pageSize)));
        page.setTotal(auditTotalVos.size());
        return page.toPageInfo();
    }


    @Override
    public List<AuditTotalVo> findPreSale(AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos;
        Short smallType = auditTotalDto.getSmallType();
        if (auditTotalDto.getBigType() == 1) {
            auditTotalVos = preSaleFileService.findByExample(auditTotalDto.getSearch(), null, null, auditTotalDto.getLoginUserId(), (short) 1, (short) 0);
        } else if (auditTotalDto.getBigType() == 2) {
            auditTotalVos = preSaleFileService.findByExample(auditTotalDto.getSearch(), null, smallType == null || smallType == 1 ? null : smallType, auditTotalDto.getLoginUserId(), null, (short) 1);
        } else {
            auditTotalVos = preSaleFileService.findByExample(auditTotalDto.getSearch(), auditTotalDto.getLoginUserId(), smallType == null || smallType == 1 ? null : smallType, null, null, null);
        }
        auditTotalVos.forEach(preSaleAuditTotalVo -> {
            preSaleAuditTotalVo.setModuleType((short) 1);
            preSaleAuditTotalVo.setType((short) 2);
        });
        return auditTotalVos;
    }

    @Override
    public List<AuditTotalVo> findBidding(AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos;
        Short smallType = auditTotalDto.getSmallType();
        if (auditTotalDto.getBigType() == 1) {
            auditTotalVos = biddingFileService.findByExample(auditTotalDto.getSearch(), null, null, auditTotalDto.getLoginUserId(), (short) 1, (short) 0);
        } else if (auditTotalDto.getBigType() == 2) {
            auditTotalVos = biddingFileService.findByExample(auditTotalDto.getSearch(), null, smallType == null || smallType == 1 ? null : auditTotalDto.getSmallType(), auditTotalDto.getLoginUserId(), null, (short) 1);
        } else {
            auditTotalVos = biddingFileService.findByExample(auditTotalDto.getSearch(), auditTotalDto.getLoginUserId(), smallType == null || smallType == 1 ? null : auditTotalDto.getSmallType(), null, null, null);
        }
        auditTotalVos.forEach(auditTotalVo -> {
            auditTotalVo.setModuleType((short) 2);
            auditTotalVo.setType((short) 2);
        });
        return auditTotalVos;
    }

    @Override
    public List<AuditTotalVo> findDesignLiaison(AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos;
        Short smallType = auditTotalDto.getSmallType();
        if (auditTotalDto.getBigType() == 1) {
            auditTotalVos = designLiaisonFileService.findByExample(auditTotalDto.getSearch(), null, null, auditTotalDto.getLoginUserId(), (short) 1, (short) 0);
        } else if (auditTotalDto.getBigType() == 2) {
            auditTotalVos = designLiaisonFileService.findByExample(auditTotalDto.getSearch(), null, smallType == null || smallType == 1 ? null : smallType, auditTotalDto.getLoginUserId(), null, (short) 1);
        } else {
            auditTotalVos = designLiaisonFileService.findByExample(auditTotalDto.getSearch(), auditTotalDto.getLoginUserId(), smallType == null || smallType == 1 ? null : smallType, null, null, null);
        }
        auditTotalVos.forEach(auditTotalVo -> {
            auditTotalVo.setModuleType((short) 3);
            auditTotalVo.setType((short) 2);
        });
        return auditTotalVos;
    }

    @Override
    public List<AuditTotalVo> findKnowledgeManagement(AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos;
        Short smallType = auditTotalDto.getSmallType();
        if (auditTotalDto.getBigType() == 1) {
            auditTotalVos = zhiShiGuanLiFileTypeService.findByZSGLExample(auditTotalDto.getSearch(), null, null, auditTotalDto.getLoginUserId(), (short) 1, (short) 0);
        } else if (auditTotalDto.getBigType() == 2) {
            auditTotalVos = zhiShiGuanLiFileTypeService.findByZSGLExample(auditTotalDto.getSearch(), null, smallType == null || smallType == 1 ? null : auditTotalDto.getSmallType(), auditTotalDto.getLoginUserId(), null, (short) 1);
        } else {
            auditTotalVos = zhiShiGuanLiFileTypeService.findByZSGLExample(auditTotalDto.getSearch(), auditTotalDto.getLoginUserId(), smallType == null || smallType == 1 ? null : auditTotalDto.getSmallType(), null, null, null);
        }
        auditTotalVos.forEach(auditTotalVo -> {
            auditTotalVo.setModuleType((short) 4);
            auditTotalVo.setType((short) 2);
        });
        return auditTotalVos;
    }
}
