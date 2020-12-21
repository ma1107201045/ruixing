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

    @Override
    @Transactional
    public void audit(AuditDto auditDto) {
        switch (auditDto.getModuleType()) {
            case 1:
                preSaleFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                        auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                break;
            case 2:
                biddingFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                        auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                break;
            case 3:
                designLiaisonFileService.audit(auditDto.getId(), auditDto.getIsPass(), auditDto.getContext(),
                        auditDto.getAccessoryName(), auditDto.getAccessoryPath(), auditDto.getPassUserId(), auditDto.getLoginUserId(), auditDto.getUserName(), auditDto.getTrueName());
                break;
        }
    }

    @Override
    public List<UserEntity> findByOtherAuditors(AuditDto auditDto) {
        switch (auditDto.getModuleType()) {
            case 1:
                return preSaleFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());
            case 2:
                return biddingFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());
            case 3:
                return designLiaisonFileService.findByOtherAuditors(auditDto.getId(), (long) auditDto.getLoginUserId());
        }
        return new ArrayList<>();
    }

    @Override
    public PageInfo<AuditTotalVo> findPage(int pageNum, int pageSize, AuditTotalDto auditTotalDto) {
        List<AuditTotalVo> auditTotalVos = new Page<>(pageNum, pageSize);
        List<AuditTotalVo> preSaleAuditTotalVos = null;
        List<AuditTotalVo> biddingAuditTotalVos = null;
        List<AuditTotalVo> designLiaisonAuditTotalVos = null;
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
            default:
                preSaleAuditTotalVos = this.findPreSale(auditTotalDto);
                biddingAuditTotalVos = this.findBidding(auditTotalDto);
                designLiaisonAuditTotalVos = this.findDesignLiaison(auditTotalDto);
        }
        auditTotalVos.addAll(preSaleAuditTotalVos == null ? new ArrayList<>() : preSaleAuditTotalVos);
        auditTotalVos.addAll(biddingAuditTotalVos == null ? new ArrayList<>() : biddingAuditTotalVos);
        auditTotalVos.addAll(designLiaisonAuditTotalVos == null ? new ArrayList<>() : designLiaisonAuditTotalVos);

        //统一按照发起时间排序（倒序）
        auditTotalVos = auditTotalVos.stream().sorted(Comparator.comparing(AuditTotalVo::getInitiateTime).reversed()).collect(Collectors.toList());
        Page<AuditTotalVo> page = (Page<AuditTotalVo>) auditTotalVos;
        return page.toPageInfo();
    }

    @Override
    public List<AuditTotalVo> findPreSale(AuditTotalDto auditTotalDto) {
        if (auditTotalDto.getBigType() == 1) {

        }
        return null;
    }

    @Override
    public List<AuditTotalVo> findBidding(AuditTotalDto auditTotalDto) {
        return null;
    }

    @Override
    public List<AuditTotalVo> findDesignLiaison(AuditTotalDto auditTotalDto) {
        return null;
    }


}
