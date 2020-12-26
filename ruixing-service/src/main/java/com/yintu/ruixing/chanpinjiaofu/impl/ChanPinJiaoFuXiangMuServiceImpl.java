package com.yintu.ruixing.chanpinjiaofu.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yintu.ruixing.chanpinjiaofu.*;
import com.yintu.ruixing.common.AuditTotalVo;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.ExportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.master.chanpinjiaofu.*;
import com.yintu.ruixing.master.common.MessageDao;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/7/1 13:44
 * @Version 1.0
 * 需求:产品交付
 */
@Service
@Transactional
public class ChanPinJiaoFuXiangMuServiceImpl implements ChanPinJiaoFuXiangMuService {
    @Autowired
    private ChanPinJiaoFuXiangMuDao chanPinJiaoFuXiangMuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ChanPinJiaoFuRecordMessageDao chanPinJiaoFuRecordMessageDao;

    @Autowired
    private ChanPinJiaoFuXiangMuFileDao chanPinJiaoFuXiangMuFileDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChanPinJiaoFuWenTiDao chanPinJiaoFuWenTiDao;

    @Autowired
    private AuditConfigurationService auditConfigurationService;

    @Autowired
    private AuditConfigurationUserService auditConfigurationUserService;

    @Autowired
    private ChanPinJiaoFuFileAuditorDao chanPinJiaoFuFileAuditorDao;

    @Autowired
    private RoleService roleService;

    @Override
    public List<AuditTotalVo> findByCPJFFileExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose) {
        return chanPinJiaoFuXiangMuFileDao.findByCPJFFileExample(search, userId, auditStatus, auditorId, activate, isDispose);
    }

    @Override
    public List<AuditTotalVo> findByCPJFXiangMuExample(String search, Integer userId, Short auditStatus, Integer auditorId, Short activate, Short isDispose) {
        return chanPinJiaoFuXiangMuDao.findByCPJFXiangMuExample(search, userId, auditStatus, auditorId, activate, isDispose);
    }

    @Override
    public List<AuditConfigurationEntity> findFileAudit(short i, short i1, short i2) {
        return auditConfigurationService.findAudit(i, i1, i2);
    }

    @Override
    public List<TreeNodeUtil> findTree() {
        List<RoleEntity> roleEntities = roleService.findAll();
        List<TreeNodeUtil> firstTreeNodeUtils = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            List<UserEntity> userEntities = roleService.findUsersByIds(roleEntity.getId());
            List<TreeNodeUtil> secondTreeNodeUtils = new ArrayList<>();
            TreeNodeUtil firstTreeNodeUtil = new TreeNodeUtil();
            firstTreeNodeUtil.setId(roleEntity.getId());
            firstTreeNodeUtil.setLabel(roleEntity.getName());
            firstTreeNodeUtil.setChildren(secondTreeNodeUtils);
            firstTreeNodeUtils.add(firstTreeNodeUtil);
            for (UserEntity userEntity : userEntities) {
                TreeNodeUtil secondTreeNodeUtil = new TreeNodeUtil();
                secondTreeNodeUtil.setId(userEntity.getId());
                secondTreeNodeUtil.setLabel(userEntity.getTrueName());
                secondTreeNodeUtil.setA_attr(BeanUtil.beanToMap(roleEntity));
                secondTreeNodeUtils.add(secondTreeNodeUtil);
            }
        }
        return firstTreeNodeUtils;
    }

    @Override
    public List<AuditConfigurationEntity> findXMAudit(short i, short i1, short i2) {
        return auditConfigurationService.findAudit(i, i1, i2);
    }

    @Override
    public List<UserEntity> findZhuanJiaoAuditorName(Integer objectid, Integer objectType) {
        List<UserEntity> userEntities = new ArrayList<>();
        boolean flag;
        String username = null;
        List<UserEntity> userEntitiess = userService.findByTruename(username);
        List<Integer> userid = chanPinJiaoFuFileAuditorDao.findUserIdByids(objectid, objectType);
        for (int i = 0; i < userEntitiess.size(); i++) {
            flag = false;
            for (int i1 = 0; i1 < userid.size(); i1++) {
                if (userid.get(i1) == userEntitiess.get(i).getId().intValue()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                userEntities.add(userEntitiess.get(i));
            }
        }
        return userEntities;
    }

    @Override
    public List<ChanPinJiaoFuFileAuditorEntity> findAuditorDatas(Integer objectid, Integer objectType, Integer auditorid, Integer sort, Short activate) {
        return chanPinJiaoFuFileAuditorDao.findAuditorDatas(objectid, objectType, auditorid, sort, activate);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangList(String choiceTing, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findJiaoFuQingKuangList(choiceTing);
    }


    @Override
    public void editXiaoXiById(MessageEntity messageEntity) {
        messageEntity.setStatus((short) 2);
        messageDao.updateByPrimaryKeySelective(messageEntity);
    }

    @Override
    public List<MessageEntity> findXiaoXi(Integer senderid) {
        Integer type = 2;
        return messageDao.findXiaoXi(senderid, type);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findAllXiangMu() {
        return chanPinJiaoFuXiangMuDao.findAllXiangMu();
    }

    @Override
    public void addXiaoXi(MessageEntity messageEntity) {
        /*messageEntity.setContext("项目待发货，请及时联系顾客确认供货计划！");
        messageEntity.setType((short)2);
        messageEntity.setStatus((short)1);
        messageEntity.setCreatedDate(new Date());*/
        messageDao.insertSelective(messageEntity);
    }

    @Override
    public List<UserEntity> findAllAuditorNameById(Integer id) {
        Integer[] ids = chanPinJiaoFuXiangMuDao.findAllAuditorNameById(id);
        List<UserEntity> userEntity = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            UserEntity userEntities = userDao.selectByPrimaryKey((long) ids[i]);
            System.out.println(userEntities);
            userEntity.add(userEntities);
        }
        return userEntity;
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findJiaoFuQingKuangLists(String choiceTing, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findJiaoFuQingKuangLists(choiceTing);
    }

    @Override
    public Map<String, Object> findJiaoFuQingKuangNumberAll() {
        Map<String, Object> map = new HashMap<>();
        List<String> zhengZaiZhiXing = chanPinJiaoFuXiangMuDao.findZhengZaiZhiXing();
        zhengZaiZhiXing.add("zhengZaiZhiXing");
        List<String> meiWanChengFaHuo = chanPinJiaoFuXiangMuDao.findFaHuo();
        meiWanChengFaHuo.add("meiWanChengFaHuo");
        List<String> meiWanChengQianShou = chanPinJiaoFuXiangMuDao.findQianShou();
        meiWanChengQianShou.add("meiWanChengQianShou");
        List<String> meiWanChengYanGong = chanPinJiaoFuXiangMuDao.meiWanChengYanGong();
        meiWanChengYanGong.add("meiWanChengYanGong");
        List<String> zanBuFaHuo = chanPinJiaoFuXiangMuDao.zanBuFaHuo();
        zanBuFaHuo.add("zanBuFaHuo");
        List<String> luXuFaHuo = chanPinJiaoFuXiangMuDao.luXuFaHuo();
        luXuFaHuo.add("luXuFaHuo");
        List<String> wuXuFaHuo = chanPinJiaoFuXiangMuDao.wuXuFaHuo();
        wuXuFaHuo.add("wuXuFaHuo");
        List<String> daiQianShu = chanPinJiaoFuXiangMuDao.daiQianShu();
        daiQianShu.add("daiQianShu");
        List<String> daiYanGong = chanPinJiaoFuXiangMuDao.daiYanGong();
        daiYanGong.add("daiYanGong");
        List<String> overQianShouMoney = chanPinJiaoFuXiangMuDao.overQianShouMoney();
        overQianShouMoney.add("overQianShouMoney");
        List<String> overYanGongMoney = chanPinJiaoFuXiangMuDao.overYanGongMoney();
        overYanGongMoney.add("overYanGongMoney");
        List<String> wenTingDoing = chanPinJiaoFuWenTiDao.wenTingDoingNumber();
        wenTingDoing.add("wenTingDoing");


        map.put("zhengZaiZhiXing", zhengZaiZhiXing);
        map.put("meiWanChengQianShou", meiWanChengQianShou);
        map.put("meiWanChengFaHuo", meiWanChengFaHuo);
        map.put("meiWanChengYanGong", meiWanChengYanGong);
        map.put("zanBuFaHuo", zanBuFaHuo);
        map.put("luXuFaHuo", luXuFaHuo);
        map.put("wuXuFaHuo", wuXuFaHuo);
        map.put("daiQianShu", daiQianShu);
        map.put("daiYanGong", daiYanGong);
        map.put("overQianShouMoney", overQianShouMoney);
        map.put("overYanGongMoney", overYanGongMoney);
        map.put("wenTingDoing", wenTingDoing);
        return map;
    }


    /////////////////////////项目交付状态小模块/////////////////////////////////////
    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer page, Integer size, Integer uid) {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = chanPinJiaoFuXiangMuDao.findXiangMuByIds(stateid);
        if (!xiangMuEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
                Integer id = chanPinJiaoFuXiangMuEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 1, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    chanPinJiaoFuXiangMuEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuEntityList;
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size, Integer uid) {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = chanPinJiaoFuXiangMuDao.findXiangMuData(xiangMuBianHao, xiangMuName);
        if (!xiangMuEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
                Integer id = chanPinJiaoFuXiangMuEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 1, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    chanPinJiaoFuXiangMuEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuEntityList;
    }

    @Override
    public void deletXiangMuFileByIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            chanPinJiaoFuXiangMuFileDao.deleteByPrimaryKey(ids[i]);
        }
    }

    @Override
    public void deletXiangMuFileById(Integer id) {
        chanPinJiaoFuXiangMuFileDao.deleteByPrimaryKey(id);
    }

    @Override
    public ChanPinJiaoFuXiangMuFileEntity findById(Integer id) {
        return chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void exportFile(ServletOutputStream outputStream, Integer[] ids) throws IOException {
        //excel标题
        String title = "产品交付项目列表";
        //excel表名
        String[] headers = {"序号", "项目编号", "项目名称", "项目状态", "销售需求状态", "发货状态", "签收状态", "是否验工", "验工状态", "是否需要现场服务", "创建时间"};
        //获取数据
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuDao.selectByCondition(ids);
        chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuEntities.stream()
                .sorted(Comparator.comparing(ChanPinJiaoFuXiangMuEntity::getId).reversed())
                .collect(Collectors.toList());
        //excel元素
        Integer j = 0;
        String[][] content = new String[chanPinJiaoFuXiangMuEntities.size()][headers.length];
        for (int i = 0; i < chanPinJiaoFuXiangMuEntities.size(); i++) {
            j++;
            ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity = chanPinJiaoFuXiangMuEntities.get(i);
            content[i][0] = j.toString();
            content[i][1] = chanPinJiaoFuXiangMuEntity.getXiangmuBianhao();
            content[i][2] = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1) {
                content[i][3] = "正在执行";
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2) {
                content[i][3] = "仅剩尾款";
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                content[i][3] = "项目关闭";
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 1) {
                content[i][4] = "前续未办理";
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 2) {
                content[i][4] = "全部品类/数量已下需求";
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 3) {
                content[i][4] = "部分品类/数量已下需求";
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 1) {
                content[i][5] = "项目停滞暂不发货";
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 2) {
                content[i][5] = "暂未开始发货";
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 3) {
                content[i][5] = "陆续发货中";
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 4) {
                content[i][5] = "工程结束不需发货";
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 5) {
                content[i][5] = "当前订单完成";
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 1) {
                content[i][6] = "前续未办理";
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 2) {
                content[i][6] = "公司暂存不签收";
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 3) {
                content[i][6] = "已转储待签收";
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 4) {
                content[i][6] = "完成签收";
            }
            if (chanPinJiaoFuXiangMuEntity.getIsnotYanGong() == 0) {
                content[i][7] = "否";
            }
            if (chanPinJiaoFuXiangMuEntity.getIsnotYanGong() == 1) {
                content[i][7] = "是";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 1) {
                content[i][8] = "是否需要验工";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 2) {
                content[i][8] = "前续未办理";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 3) {
                content[i][8] = "待验工";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 4) {
                content[i][8] = "顾客要求暂缓验工";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 5) {
                content[i][8] = "完成部分验工";
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 6) {
                content[i][8] = "完成验工";
            }
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 0) {
                content[i][9] = "否";
            }
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {
                content[i][9] = "是";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            content[i][10] = sdf.format(chanPinJiaoFuXiangMuEntity.getCreateTime());
        }
        //创建HSSFWorkbook
        XSSFWorkbook wb = ExportExcelUtil.getXSSFWorkbook(title, headers, content);
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer id, Integer uId, String username, Integer[] auditorid, Integer[] sort) {
        ChanPinJiaoFuXiangMuFileEntity fileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
        Integer xmId = chanPinJiaoFuXiangMuFileEntity.getXmId();//新增文件的项目id
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        chanPinJiaoFuXiangMuFileEntity.setUpdatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setUpdatename(username);
        if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 2 && chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2) {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(2);
        }
        chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(chanPinJiaoFuXiangMuFileEntity);
        //添加审查人
        if (chanPinJiaoFuXiangMuFileEntity.getFileType()==2&&chanPinJiaoFuXiangMuFileEntity.getFabuType()==2) {
            List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList=chanPinJiaoFuFileAuditorDao.findAuditorDatas(id,2,null,null,null);
            if (!auditorEntityList.isEmpty()) {
                //删除中间表的审查人id
                chanPinJiaoFuXiangMuDao.deletAuditor(id);
            }
            if (auditorid.length == 0 && sort.length == 0) {//使用审批流 来进行审批
                List<AuditConfigurationEntity> audit = auditConfigurationService.findAudit((short) 6, (short) 1, (short) 1);//查询审批流程
                List<AuditConfigurationUserEntity> userEntities = auditConfigurationUserService.findByauditConfigurationId(audit.get(0).getId());//查询审批流的人员
                for (AuditConfigurationUserEntity userEntity : userEntities) {
                    if (userEntity.getUserId() != uId.longValue()) {//排除自己审批
                        //添加审批人
                        ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                        chanPinJiaoFuFileAuditorEntity.setObjectId(id);//添加文件id
                        chanPinJiaoFuFileAuditorEntity.setObjecttype(2);//文件
                        chanPinJiaoFuFileAuditorEntity.setAuditorId(userEntity.getUserId().intValue());//添加审核人
                        chanPinJiaoFuFileAuditorEntity.setDoname(username);
                        chanPinJiaoFuFileAuditorEntity.setIsDispose((short)0);
                        chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                        chanPinJiaoFuFileAuditorEntity.setSort(userEntity.getSort());//添加批次
                        chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                        if (userEntity.getSort() == 1) {
                            chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                            //给第一批审核人 发消息
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(username);//创建人
                            messageEntity.setCreateTime(nowTime);//创建时间
                            messageEntity.setType((short) 2);
                            messageEntity.setMessageType((short) 2);
                            messageEntity.setFileId(id);
                            messageEntity.setSenderId(uId);
                            messageEntity.setReceiverId(userEntity.getUserId().intValue());
                            messageEntity.setStatus((short) 1);
                            messageEntity.setTitle("文件");
                            messageEntity.setContext("“" + chanPinJiaoFuXiangMuFileEntity.getFileName() + "”文件需要您审核,请查看");
                            messageService.sendMessage(messageEntity);
                        } else {
                            chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                        }
                        chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                    }
                }
            } else {//没有设置审批流  用选择的人进行审批
                if (auditorid.length != 0 && sort.length != 0 && auditorid.length == sort.length) {
                    for (int i = 0; i < auditorid.length; i++) {
                        if (auditorid[i] != uId) {
                            //添加审批人
                            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                            chanPinJiaoFuFileAuditorEntity.setObjectId(id);//添加文件id
                            chanPinJiaoFuFileAuditorEntity.setObjecttype(2);//文件
                            chanPinJiaoFuFileAuditorEntity.setAuditorId(auditorid[i]);//添加审核人
                            chanPinJiaoFuFileAuditorEntity.setDoname(username);
                            chanPinJiaoFuFileAuditorEntity.setIsDispose((short) 0);
                            chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                            chanPinJiaoFuFileAuditorEntity.setSort(sort[i]);//添加批次
                            chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                            if (sort[i] == 1) {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                                //给第一批审核人 发消息
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(username);//创建人
                                messageEntity.setCreateTime(nowTime);//创建时间
                                messageEntity.setType((short) 2);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setFileId(id);
                                messageEntity.setSenderId(uId);
                                messageEntity.setReceiverId(auditorid[i]);
                                messageEntity.setStatus((short) 1);
                                messageEntity.setTitle("项目");
                                messageEntity.setContext("“" + chanPinJiaoFuXiangMuFileEntity.getFileName() + "”文件需要您审核,请查看");
                                messageService.sendMessage(messageEntity);
                            } else {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                            }
                            chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                        }
                    }
                }
            }
        }


     /*   if (uids != null) {
            for (Integer uid : uids) {
                ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(id);
                chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
                chanPinJiaoFuFileAuditorEntity.setObjectType(2);
                chanPinJiaoFuFileAuditorEntity.setDoName(username);
                chanPinJiaoFuFileAuditorEntity.setDoTime(new Date());
                chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setContext("“" + chanPinJiaoFuXiangMuFileEntity.getFileName() + "”文件需要您审核,请查看！");
                messageEntity.setType((short) 2);
                messageEntity.setProjectId(xmId);
                messageEntity.setMessageType((short) 2);
                messageEntity.setFileId(id);
                messageEntity.setSenderId(uId);
                messageEntity.setReceiverId(uid);
                messageEntity.setStatus((short) 1);
                messageService.sendMessage(messageEntity);
            }
        }*/


        Integer aa = 0;
        if (!fileEntity.getFileName().equals(chanPinJiaoFuXiangMuFileEntity.getFileName())) {
            sb.append("文件名改为" + chanPinJiaoFuXiangMuFileEntity.getFileName());
            aa++;
        }
        if (fileEntity.getAuditorState() == null || fileEntity.getAuditorState() != chanPinJiaoFuXiangMuFileEntity.getAuditorState()) {
            if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == null) {
                sb.append(" ");
            } else {
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 1) {
                    sb.append("文件审核状态更改为  待审核,  ");
                    aa++;
                }
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 2) {
                    sb.append("文件审核状态更改为  已审核未通过, ");
                    aa++;
                }
                if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 3) {
                    sb.append("文件审核状态更改为  已审核已通过,");
                    aa++;
                }
            }
        }
        if (fileEntity.getFileType() != chanPinJiaoFuXiangMuFileEntity.getFileType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 1) {
                sb.append("文件类型更改为  输入文件, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
                sb.append("文件类型更改为  输出文件, ");
                aa++;
            }
        }
        if (fileEntity.getFabuType() != chanPinJiaoFuXiangMuFileEntity.getFabuType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 1) {
                sb.append("发布状态更改为  录入, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2) {
                sb.append("发布状态更改为  发布, ");
                aa++;
            }
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        Integer typenum = 2;
        Integer mid = id;
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, String username, Integer uId, Integer[] auditorid, Integer[] sort) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuFileEntity.setCreatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setCreatename(username);
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2 && chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(1);
        } else {
            chanPinJiaoFuXiangMuFileEntity.setAuditorState(null);
        }
        if (chanPinJiaoFuXiangMuFileEntity.getRemarks().equals("")) {
            chanPinJiaoFuXiangMuFileEntity.setRemarks("无");
        }
        String fileName = chanPinJiaoFuXiangMuFileEntity.getFileName();
        chanPinJiaoFuXiangMuFileDao.insertSelective(chanPinJiaoFuXiangMuFileEntity);
        Integer fid = chanPinJiaoFuXiangMuFileEntity.getId();//新增文件id
        Integer typenum = 2;
        String context = "新增“" + fileName + "”文件";
        chanPinJiaoFuRecordMessageDao.addRecordMessage(fid, typenum, nowTime, username, context);
        Integer xmId = chanPinJiaoFuXiangMuFileEntity.getXmId();//新增文件的项目id

        if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2 && chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
            if (auditorid.length == 0 && sort.length == 0) {//使用审批流 来进行审批
                List<AuditConfigurationEntity> audit = auditConfigurationService.findAudit((short) 6, (short) 1, (short) 1);//查询审批流程
                List<AuditConfigurationUserEntity> userEntities = auditConfigurationUserService.findByauditConfigurationId(audit.get(0).getId());//查询审批流的人员
                for (AuditConfigurationUserEntity userEntity : userEntities) {
                    if (userEntity.getUserId() != uId.longValue()) {//排除自己审批
                        //添加审批人
                        ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                        chanPinJiaoFuFileAuditorEntity.setObjectId(fid);//添加文件id
                        chanPinJiaoFuFileAuditorEntity.setObjecttype(2);//文件
                        chanPinJiaoFuFileAuditorEntity.setAuditorId(userEntity.getUserId().intValue());//添加审核人
                        chanPinJiaoFuFileAuditorEntity.setDoname(username);
                        chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                        chanPinJiaoFuFileAuditorEntity.setIsDispose((short) 0);
                        chanPinJiaoFuFileAuditorEntity.setSort(userEntity.getSort());//添加批次
                        chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                        if (userEntity.getSort() == 1) {
                            chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                            //给第一批审核人 发消息
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(username);//创建人
                            messageEntity.setCreateTime(nowTime);//创建时间
                            messageEntity.setType((short) 2);
                            messageEntity.setMessageType((short) 2);
                            messageEntity.setFileId(fid);
                            messageEntity.setSenderId(uId);
                            messageEntity.setReceiverId(userEntity.getUserId().intValue());
                            messageEntity.setStatus((short) 1);
                            messageEntity.setTitle("文件");
                            messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看");
                            messageService.sendMessage(messageEntity);
                        } else {
                            chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                        }
                        chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                    }
                }
            } else {//没有设置审批流  用选择的人进行审批
                if (auditorid.length != 0 && sort.length != 0 && auditorid.length == sort.length) {
                    for (int i = 0; i < auditorid.length; i++) {
                        if (auditorid[i] != uId) {
                            //添加审批人
                            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                            chanPinJiaoFuFileAuditorEntity.setObjectId(fid);//添加文件id
                            chanPinJiaoFuFileAuditorEntity.setObjecttype(2);//文件
                            chanPinJiaoFuFileAuditorEntity.setAuditorId(auditorid[i]);//添加审核人
                            chanPinJiaoFuFileAuditorEntity.setDoname(username);
                            chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                            chanPinJiaoFuFileAuditorEntity.setIsDispose((short) 0);
                            chanPinJiaoFuFileAuditorEntity.setSort(sort[i]);//添加批次
                            chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                            if (sort[i] == 1) {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                                //给第一批审核人 发消息
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(username);//创建人
                                messageEntity.setCreateTime(nowTime);//创建时间
                                messageEntity.setType((short) 2);
                                messageEntity.setMessageType((short) 2);
                                messageEntity.setFileId(fid);
                                messageEntity.setSenderId(uId);
                                messageEntity.setReceiverId(auditorid[i]);
                                messageEntity.setStatus((short) 1);
                                messageEntity.setTitle("项目");
                                messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看");
                                messageService.sendMessage(messageEntity);
                            } else {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                            }
                            chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                        }
                    }
                }
            }
        }




        /*for (Integer uid : uids) {
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
            chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(fid);
            chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
            chanPinJiaoFuFileAuditorEntity.setObjectType(2);
            chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
            //添加一条消息到消息表
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(nowTime);//创建时间
            messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看！");
            messageEntity.setType((short) 2);
            messageEntity.setMessageType((short) 2);
            messageEntity.setProjectId(xmId);
            messageEntity.setFileId(fid);
            messageEntity.setSenderId(uId);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
        }*/
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuById(Integer id, Integer uid) {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = chanPinJiaoFuXiangMuDao.findXiangMuById(id);
        if (!xiangMuEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
                if (chanPinJiaoFuXiangMuEntity != null) {
                    List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 1, uid, null, (short) 1);
                    if (!auditorEntityList.isEmpty()) {
                        chanPinJiaoFuXiangMuEntity.setAuditorEntityList(auditorEntityList);
                    }
                }
            }
        }
        return chanPinJiaoFuXiangMuDao.findXiangMuById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page, Integer size, Integer uid) {
        List<ChanPinJiaoFuXiangMuEntity> xiangMuEntityList = chanPinJiaoFuXiangMuDao.findAll();
        if (!xiangMuEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : xiangMuEntityList) {
                Integer id = chanPinJiaoFuXiangMuEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 1, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    chanPinJiaoFuXiangMuEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuEntityList;
    }

    @Override
    public void deletXiagMuById(Integer id) {
        chanPinJiaoFuXiangMuDao.deletXiagMuById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFile(Integer id) {
        return chanPinJiaoFuXiangMuFileDao.findFile(id);
    }

    @Override
    public List<ChanPinJiaoFuRecordMessageEntity> findFileReordById(Integer id) {
        return chanPinJiaoFuRecordMessageDao.findFileReordById(id);
    }

    @Override
    public List<ChanPinJiaoFuRecordMessageEntity> findReordById(Integer id) {
        return chanPinJiaoFuRecordMessageDao.findReordById(id);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFilee(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuChuFilee(xmid, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(Integer xmid, Integer page, Integer size, Integer uid) {
        List<ChanPinJiaoFuXiangMuFileEntity> xiangMuFileEntityList = chanPinJiaoFuXiangMuFileDao.findShuChuFile(xmid, uid);//不用审核的
        if (!xiangMuFileEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuFileEntity fileEntity : xiangMuFileEntityList) {
                Integer id = fileEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 2, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    fileEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuFileEntityList;
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFilee(Integer xmid, Integer page, Integer size, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findShuRuFilee(xmid, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(Integer xmid, Integer page, Integer size, Integer uid) {
        List<ChanPinJiaoFuXiangMuFileEntity> xiangMuFileEntityList = chanPinJiaoFuXiangMuFileDao.findShuRuFile(xmid, uid);//不用审核的
        if (!xiangMuFileEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuFileEntity fileEntity : xiangMuFileEntityList) {
                Integer id = fileEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 2, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    fileEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuFileEntityList;
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomethingg(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid) {
        return chanPinJiaoFuXiangMuFileDao.findFileBySomethingg(xmid, filetype, filename, uid);//需要审核的
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(Integer xmid, Integer page, Integer size, Integer filetype, String filename, Integer uid) {
        List<ChanPinJiaoFuXiangMuFileEntity> xiangMuFileEntityList = chanPinJiaoFuXiangMuFileDao.findFileBySomething(xmid, filetype, filename, uid);//不用审核的
        if (!xiangMuFileEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuFileEntity fileEntity : xiangMuFileEntityList) {
                Integer id = fileEntity.getId();
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 2, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    fileEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuFileEntityList;//不用审核的
    }

    @Override
    public List<ChanPinJiaoFuFileAuditorEntity> findXMByXmid(Integer xmid) {
        return chanPinJiaoFuXiangMuDao.findXMByXmid(xmid);
    }

    @Override
    public long findProjectSum() {
        return chanPinJiaoFuXiangMuDao.selectProjectSum();
    }

    @Override
    public void editAuditorByWJId(Integer id, Short isPass, Integer passUserId,
                                  String username, Integer receiverid,
                                  String accessoryName, String accessoryPath, String context) {
        Date nowTime = new Date();
        if (isPass == null)
            throw new BaseRuntimeException("审核状态不能为空");
        if (isPass != 3 && isPass != 4 && isPass != 5) {
            throw new BaseRuntimeException("此文件审核状态有误");
        }
        ChanPinJiaoFuXiangMuFileEntity xiangMuFileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);//需要审核的项目文件数据
        String fileName = xiangMuFileEntity.getFileName();//文件名
        if (xiangMuFileEntity != null) {//首先审核的文件必须存在
            //查询当前人是否有审核权限
            List<ChanPinJiaoFuFileAuditorEntity> auditorEntities = this.findAuditorDatas(id, 2, receiverid, null, (short) 1);
            if (auditorEntities.isEmpty()) {
                throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
            }
            //查询此批人是否已经审批
            List<ChanPinJiaoFuFileAuditorEntity> auditorEntities1 = this.findAuditorDatas(id, 2, null, null, (short) 1);
            if (auditorEntities1.isEmpty()) {
                throw new BaseRuntimeException("此文件已审核，无需重复审核");
            }
            //然后更改此批人的审核状态
            for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntities1) {
                auditorEntity.setActivate((short) 0);
                auditorEntity.setAuditFinishTime(nowTime);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
            }
            //添加附件
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = auditorEntities.get(0);
            chanPinJiaoFuFileAuditorEntity.setAccessoryName(accessoryName);
            chanPinJiaoFuFileAuditorEntity.setAccessoryPath(accessoryPath);
            chanPinJiaoFuFileAuditorEntity.setContext(context);
            chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(chanPinJiaoFuFileAuditorEntity);

            Integer sort = auditorEntities.get(0).getSort();//取出里边的任意顺序，进行下一批人审批
            Integer id1 = auditorEntities.get(0).getId();
            if (isPass == 5) {//转交别人审核
                if (passUserId == null) {
                    throw new BaseRuntimeException("转交人id不能为空");
                }
                //新增转交审批人数据
                ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity.setObjectId(id);//添加文件id
                auditorEntity.setObjecttype(2);//文件
                auditorEntity.setAuditorId(passUserId);//添加审核人
                auditorEntity.setDoname(username);
                auditorEntity.setDotime(nowTime);
                auditorEntity.setSort(sort);//添加批次
                auditorEntity.setAuditStatus((short) 2);
                auditorEntity.setActivate((short) 1);
                auditorEntity.setIsDispose((short) 0);
                chanPinJiaoFuFileAuditorDao.insertSelective(auditorEntity);
                //更改此审核数据的状态
                ChanPinJiaoFuFileAuditorEntity auditorEntity1 = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity1.setAuditStatus((short) 5);
                auditorEntity1.setActivate((short) 0);
                auditorEntity1.setIsDispose((short) 1);
                auditorEntity1.setAuditFinishTime(new Date());
                auditorEntity1.setId(id1);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity1);
                //给转交人发消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setType((short) 2);
                messageEntity.setMessageType((short) 2);
                messageEntity.setFileId(id);
                messageEntity.setSenderId(receiverid);
                messageEntity.setReceiverId(passUserId);
                messageEntity.setStatus((short) 1);
                messageEntity.setTitle("文件");
                messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看");
                messageService.sendMessage(messageEntity);
                //新增查看消息
                ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                recordMessageEntity.setTypeid(id);
                recordMessageEntity.setOperatorname(username);
                recordMessageEntity.setOperatortime(nowTime);
                recordMessageEntity.setTypenum(2);
                recordMessageEntity.setContext(username + "转交了此文件让" + userDao.findTureNameById(passUserId) + "来审核");
                chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
            }
            if (isPass == 3) {//审核通过
                //先判断 下一批人是否存在
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = this.findAuditorDatas(id, 2, null, sort + 1, null);
                if (auditorEntityList.isEmpty()) {//说明没有人下批人审批   审批结束
                    //更改文件审核状态   审核通过
                    ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
                    fileEntity.setAuditorState(3);
                    fileEntity.setAuditFinishTime(nowTime);
                    fileEntity.setUpdatetime(nowTime);
                    fileEntity.setUpdatename(username);
                    fileEntity.setId(id);
                    chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
                    //更改此审核数据的状态
                    ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                    auditorEntity.setAuditStatus((short) 3);
                    auditorEntity.setActivate((short) 0);
                    auditorEntity.setIsDispose((short) 1);
                    auditorEntity.setAuditFinishTime(new Date());
                    auditorEntity.setId(id1);
                    chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                    //给发起人 发送消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(username);//创建人
                    messageEntity.setCreateTime(nowTime);//创建时间
                    messageEntity.setType((short) 2);
                    messageEntity.setMessageType((short) 2);
                    messageEntity.setFileId(id);
                    messageEntity.setSenderId(receiverid);
                    messageEntity.setReceiverId(fileEntity.getUid());
                    messageEntity.setStatus((short) 1);
                    messageEntity.setTitle("项目");
                    messageEntity.setContext("“" + fileName + "”文件已通过审核,请您知晓！");
                    messageService.sendMessage(messageEntity);
                    //添加日志记录
                    ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                    recordMessageEntity.setTypeid(id);
                    recordMessageEntity.setOperatorname(username);
                    recordMessageEntity.setOperatortime(nowTime);
                    recordMessageEntity.setTypenum(2);
                    recordMessageEntity.setContext("“" + fileName + "”文件已通过审核！");
                    chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
                } else {//说明审核未结束
                    //更改文件审核状态  审核正在进行中
                    ChanPinJiaoFuXiangMuFileEntity xiangMuEntity1 = new ChanPinJiaoFuXiangMuFileEntity();
                    xiangMuEntity1.setAuditorState(2);
                    xiangMuEntity1.setId(id);
                    chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(xiangMuEntity1);

                    List<ChanPinJiaoFuFileAuditorEntity> auditorEntities2 = this.findAuditorDatas(id, 2, null, sort, null);
                    for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntities2) {
                        auditorEntity.setActivate((short) 0);//当前人不在审核中
                        auditorEntity.setIsDispose((short) 1);//已处理
                        auditorEntity.setAuditStatus((short) 3);
                        auditorEntity.setAuditFinishTime(new Date());
                        chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                    }
                    for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntityList) {
                        auditorEntity.setActivate((short) 1);
                        auditorEntity.setIsDispose((short) 0);
                        chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                        //发送审核消息
                        if (receiverid != auditorEntity.getAuditorId()) {//不要给自己发消息
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(username);//创建人
                            messageEntity.setCreateTime(nowTime);//创建时间
                            messageEntity.setType((short) 2);
                            messageEntity.setMessageType((short) 2);
                            messageEntity.setFileId(id);
                            messageEntity.setSenderId(receiverid);
                            messageEntity.setReceiverId(auditorEntity.getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageEntity.setTitle("文件");
                            messageEntity.setContext("“" + fileName + "”文件需要您审核,请查看");
                            messageService.sendMessage(messageEntity);
                        }
                    }
                    //添加日志记录
                    ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                    recordMessageEntity.setTypeid(id);
                    recordMessageEntity.setOperatorname(username);
                    recordMessageEntity.setOperatortime(nowTime);
                    recordMessageEntity.setTypenum(2);
                    recordMessageEntity.setContext("“" + username + "”审核了此文件");
                    chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
                }
            }
            if (isPass == 4) {//到此人审核被拒绝
                //更改文件审核状态  审核未通过
                ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
                fileEntity.setAuditorState(4);
                fileEntity.setAuditFinishTime(nowTime);
                fileEntity.setUpdatetime(nowTime);
                fileEntity.setUpdatename(username);
                fileEntity.setId(id);
                chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
                //更改此审核数据的状态
                ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity.setAuditStatus((short) 4);
                auditorEntity.setActivate((short) 0);
                auditorEntity.setIsDispose((short) 1);
                auditorEntity.setAuditFinishTime(new Date());
                auditorEntity.setId(id1);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                //给发起人 发送消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setType((short) 2);
                messageEntity.setMessageType((short) 2);
                messageEntity.setFileId(id);
                messageEntity.setSenderId(receiverid);
                messageEntity.setReceiverId(fileEntity.getUid());
                messageEntity.setStatus((short) 1);
                messageEntity.setTitle("文件");
                messageEntity.setContext("“" + fileName + "”文件未通过审核,请您查看！");
                messageService.sendMessage(messageEntity);
                //添加日志记录
                ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                recordMessageEntity.setTypeid(id);
                recordMessageEntity.setOperatorname(username);
                recordMessageEntity.setOperatortime(nowTime);
                recordMessageEntity.setTypenum(2);
                recordMessageEntity.setContext("“" + fileName + "”文件未通过审核！");
                chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
            }
        }





       /* Date nowTime = new Date();
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 0) {//审核未通过
            ChanPinJiaoFuXiangMuFileEntity onefileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
            ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(3);
            chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
            //新增消息  提醒发送者查看审核未通过的文件
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setTitle("文件");
            messageEntity.setCreateBy(username);
            messageEntity.setCreateTime(nowTime);
            messageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核未通过,请您查看！");
            messageEntity.setType((short) 2);
            messageEntity.setMessageType((short) 2);
            messageEntity.setFileId(id);
            messageEntity.setReceiverId(senderId);
            messageEntity.setSenderId(receiverid);
            messageEntity.setStatus((short) 1);
            messageDao.insertSelective(messageEntity);
            //新增查看消息
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            String reason = chanPinJiaoFuFileAuditorEntity.getReason();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核未通过,原因是:" + reason);
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        if (chanPinJiaoFuFileAuditorEntity.getIsPass() == 1) {//审核通过
            ChanPinJiaoFuXiangMuFileEntity fileEntity = new ChanPinJiaoFuXiangMuFileEntity();
            fileEntity.setId(id);
            fileEntity.setAuditorState(2);
            chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(fileEntity);
            //新增查看消息
            ChanPinJiaoFuXiangMuFileEntity onefileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
            ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(nowTime);
            recordMessageEntity.setTypenum(2);
            recordMessageEntity.setContext("“" + onefileEntity.getFileName() + "”文件审核已通过！");
            chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
        }
        chanPinJiaoFuFileAuditorEntity.setDoTime(nowTime);
        chanPinJiaoFuFileAuditorEntity.setDoName(username);
        chanPinJiaoFuXiangMuDao.editAuditorByXMId(chanPinJiaoFuFileAuditorEntity);*/
    }

    @Override
    public void editAuditorByXMId(Integer id, Short isPass, Integer passUserId,
                                  String username, Integer receiverid,
                                  String accessoryName, String accessoryPath, String context) {
        Date nowTime = new Date();
        if (isPass == null)
            throw new BaseRuntimeException("审核状态不能为空");
        if (isPass != 3 && isPass != 4 && isPass != 5) {
            throw new BaseRuntimeException("此项目审核状态有误");
        }
        ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);//需要审核的项目数据
        String xiangmuName = xiangMuEntity.getXiangmuName();//项目名
        if (xiangMuEntity != null) {//首先审核的项目必须存在
            //查询当前人是否有审核权限
            List<ChanPinJiaoFuFileAuditorEntity> auditorEntities = this.findAuditorDatas(id, 1, receiverid, null, (short) 1);
            if (auditorEntities.isEmpty()) {
                throw new BaseRuntimeException("你无权审核此文件或已被其他人审批");
            }
            //查询此批人是否已经审批
            List<ChanPinJiaoFuFileAuditorEntity> auditorEntities1 = this.findAuditorDatas(id, 1, null, null, (short) 1);
            if (auditorEntities1.isEmpty()) {
                throw new BaseRuntimeException("此文件已审核，无需重复审核");
            }
            //然后更改此批人的审核状态
            for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntities1) {
                auditorEntity.setActivate((short) 0);
                auditorEntity.setAuditFinishTime(nowTime);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
            }
            //添加附件
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = auditorEntities.get(0);
            chanPinJiaoFuFileAuditorEntity.setAccessoryName(accessoryName);
            chanPinJiaoFuFileAuditorEntity.setAccessoryPath(accessoryPath);
            chanPinJiaoFuFileAuditorEntity.setContext(context);
            chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(chanPinJiaoFuFileAuditorEntity);

            Integer sort = auditorEntities.get(0).getSort();//取出里边的任意顺序，进行下一批人审批
            Integer id1 = auditorEntities.get(0).getId();
            if (isPass == 5) {//转交别人审核
                if (passUserId == null) {
                    throw new BaseRuntimeException("转交人id不能为空");
                }
                //新增转交审批人数据
                ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity.setObjectId(id);//添加项目id
                auditorEntity.setObjecttype(1);//项目
                auditorEntity.setAuditorId(passUserId);//添加审核人
                auditorEntity.setDoname(username);
                auditorEntity.setDotime(nowTime);
                auditorEntity.setSort(sort);//添加批次
                auditorEntity.setAuditStatus((short) 2);
                auditorEntity.setActivate((short) 1);
                auditorEntity.setIsDispose((short) 0);
                chanPinJiaoFuFileAuditorDao.insertSelective(auditorEntity);
                //更改此审核数据的状态
                ChanPinJiaoFuFileAuditorEntity auditorEntity1 = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity1.setAuditStatus((short) 5);
                auditorEntity1.setActivate((short) 0);
                auditorEntity1.setIsDispose((short) 1);
                auditorEntity1.setAuditFinishTime(new Date());
                auditorEntity1.setId(id1);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity1);
                //给转交人发消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setType((short) 2);
                messageEntity.setMessageType((short) 3);
                messageEntity.setProjectId(id);
                messageEntity.setSenderId(receiverid);
                messageEntity.setReceiverId(passUserId);
                messageEntity.setStatus((short) 1);
                messageEntity.setTitle("项目");
                messageEntity.setContext("“" + xiangmuName + "”项目需要您审核,请查看");
                messageService.sendMessage(messageEntity);
                //新增查看消息
                ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                recordMessageEntity.setTypeid(id);
                recordMessageEntity.setOperatorname(username);
                recordMessageEntity.setOperatortime(nowTime);
                recordMessageEntity.setTypenum(1);
                recordMessageEntity.setContext(username + "转交了此项目让" + userDao.findTureNameById(passUserId) + "来审核");
                chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
            }
            if (isPass == 3) {//审核通过
                //先判断 下一批人是否存在
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = this.findAuditorDatas(id, 1, null, sort + 1, null);
                if (auditorEntityList.isEmpty()) {//说明没有人下批人审批   审批结束
                    //更改项目审核状态   审核通过
                    ChanPinJiaoFuXiangMuEntity xiangMuEntity1 = new ChanPinJiaoFuXiangMuEntity();
                    xiangMuEntity1.setAuditorstate(3);
                    xiangMuEntity1.setAuditFinishTime(nowTime);
                    xiangMuEntity1.setUpdateTime(nowTime);
                    xiangMuEntity1.setOperatorName(username);
                    xiangMuEntity1.setId(id);
                    chanPinJiaoFuXiangMuDao.editXiangMuById(xiangMuEntity1);
                    //更改此审核数据的状态
                    ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                    auditorEntity.setAuditStatus((short) 3);
                    auditorEntity.setActivate((short) 0);
                    auditorEntity.setIsDispose((short) 1);
                    auditorEntity.setAuditFinishTime(new Date());
                    auditorEntity.setId(id1);
                    chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                    //给发起人 发送消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(username);//创建人
                    messageEntity.setCreateTime(nowTime);//创建时间
                    messageEntity.setType((short) 2);
                    messageEntity.setMessageType((short) 3);
                    messageEntity.setProjectId(id);
                    messageEntity.setSenderId(receiverid);
                    messageEntity.setReceiverId(xiangMuEntity.getUserid());
                    messageEntity.setStatus((short) 1);
                    messageEntity.setTitle("项目");
                    messageEntity.setContext("“" + xiangmuName + "”项目已通过审核,请您知晓！");
                    messageService.sendMessage(messageEntity);
                    //添加日志记录
                    ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                    recordMessageEntity.setTypeid(id);
                    recordMessageEntity.setOperatorname(username);
                    recordMessageEntity.setOperatortime(nowTime);
                    recordMessageEntity.setTypenum(1);
                    recordMessageEntity.setContext("“" + xiangmuName + "”项目已通过审核！");
                    chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
                } else {//说明审核未结束
                    //更改文件审核状态  审核正在进行中
                    ChanPinJiaoFuXiangMuEntity xiangMuEntity1 = new ChanPinJiaoFuXiangMuEntity();
                    xiangMuEntity1.setAuditorstate(2);
                    xiangMuEntity1.setId(id);
                    chanPinJiaoFuXiangMuDao.editXiangMuById(xiangMuEntity1);

                    List<ChanPinJiaoFuFileAuditorEntity> auditorEntities2 = this.findAuditorDatas(id, 1, null, sort, null);
                    for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntities2) {
                        auditorEntity.setActivate((short) 0);
                        auditorEntity.setIsDispose((short) 1);
                        auditorEntity.setAuditStatus((short) 3);
                        auditorEntity.setAuditFinishTime(new Date());
                        chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                    }
                    for (ChanPinJiaoFuFileAuditorEntity auditorEntity : auditorEntityList) {
                        auditorEntity.setActivate((short) 1);
                        auditorEntity.setIsDispose((short) 0);
                        chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                        //发送审核消息
                        if (receiverid != auditorEntity.getAuditorId()) {//不要给自己发消息
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setCreateBy(username);//创建人
                            messageEntity.setCreateTime(nowTime);//创建时间
                            messageEntity.setType((short) 2);
                            messageEntity.setMessageType((short) 3);
                            messageEntity.setProjectId(id);
                            messageEntity.setSenderId(receiverid);
                            messageEntity.setReceiverId(auditorEntity.getAuditorId());
                            messageEntity.setStatus((short) 1);
                            messageEntity.setTitle("项目");
                            messageEntity.setContext("“" + xiangmuName + "”项目需要您审核,请查看");
                            messageService.sendMessage(messageEntity);
                        }
                    }
                    //添加日志记录
                    ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                    recordMessageEntity.setTypeid(id);
                    recordMessageEntity.setOperatorname(username);
                    recordMessageEntity.setOperatortime(nowTime);
                    recordMessageEntity.setTypenum(1);
                    recordMessageEntity.setContext("“" + username + "”审核了此项目");
                    chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);

                }
            }
            if (isPass == 4) {//到此人审核被拒绝
                //更改文件审核状态  审核未通过
                ChanPinJiaoFuXiangMuEntity xiangMuEntity1 = new ChanPinJiaoFuXiangMuEntity();
                xiangMuEntity1.setAuditorstate(4);
                xiangMuEntity1.setAuditFinishTime(nowTime);
                xiangMuEntity1.setUpdateTime(nowTime);
                xiangMuEntity1.setOperatorName(username);
                xiangMuEntity1.setId(id);
                chanPinJiaoFuXiangMuDao.editXiangMuById(xiangMuEntity1);
                //更改此审核数据的状态
                ChanPinJiaoFuFileAuditorEntity auditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                auditorEntity.setAuditStatus((short) 4);
                auditorEntity.setActivate((short) 0);
                auditorEntity.setIsDispose((short) 1);
                auditorEntity.setAuditFinishTime(new Date());
                auditorEntity.setId(id1);
                chanPinJiaoFuFileAuditorDao.updateByPrimaryKeySelective(auditorEntity);
                //给发起人 发送消息
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setType((short) 2);
                messageEntity.setMessageType((short) 3);
                messageEntity.setProjectId(id);
                messageEntity.setSenderId(receiverid);
                messageEntity.setReceiverId(xiangMuEntity.getUserid());
                messageEntity.setStatus((short) 1);
                messageEntity.setTitle("项目");
                messageEntity.setContext("“" + xiangmuName + "”项目未通过审核,请您查看！");
                messageService.sendMessage(messageEntity);
                //添加日志记录
                ChanPinJiaoFuRecordMessageEntity recordMessageEntity = new ChanPinJiaoFuRecordMessageEntity();
                recordMessageEntity.setTypeid(id);
                recordMessageEntity.setOperatorname(username);
                recordMessageEntity.setOperatortime(nowTime);
                recordMessageEntity.setTypenum(1);
                recordMessageEntity.setContext("“" + xiangmuName + "”项目未通过审核！");
                chanPinJiaoFuRecordMessageDao.insertSelective(recordMessageEntity);
            }
        }
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileById(Integer id, Integer uid) {
        List<ChanPinJiaoFuXiangMuFileEntity> xiangMuFileEntityList = chanPinJiaoFuXiangMuFileDao.findFileById(id);
        if (!xiangMuFileEntityList.isEmpty()) {
            for (ChanPinJiaoFuXiangMuFileEntity fileEntity : xiangMuFileEntityList) {
                List<ChanPinJiaoFuFileAuditorEntity> auditorEntityList = chanPinJiaoFuFileAuditorDao.findAuditorDatas(id, 2, uid, null, (short) 1);
                if (!auditorEntityList.isEmpty()) {
                    fileEntity.setAuditorEntityList(auditorEntityList);
                }
            }
        }
        return xiangMuFileEntityList;
    }

    @Override
    public void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer id, Integer senderid, Integer[] auditorid, Integer[] sort) {
        ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        //添加审查人
        if (chanPinJiaoFuXiangMuEntity.getXiangmuState() != xiangMuEntity.getXiangmuState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() != 1)
                if (auditorid.length == 0 && sort.length == 0) {//使用审批流 来进行审批
                    List<AuditConfigurationEntity> audit = auditConfigurationService.findAudit((short) 5, (short) 1, (short) 1);//查询审批流程
                    List<AuditConfigurationUserEntity> userEntities = auditConfigurationUserService.findByauditConfigurationId(audit.get(0).getId());//查询审批流的人员
                    for (AuditConfigurationUserEntity userEntity : userEntities) {
                        if (userEntity.getUserId() != senderid.longValue()) {//排除自己审批
                            //添加审批人
                            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                            chanPinJiaoFuFileAuditorEntity.setObjectId(id);//添加项目id
                            chanPinJiaoFuFileAuditorEntity.setObjecttype(1);//项目
                            chanPinJiaoFuFileAuditorEntity.setAuditorId(userEntity.getUserId().intValue());//添加审核人
                            chanPinJiaoFuFileAuditorEntity.setDoname(username);
                            chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                            chanPinJiaoFuFileAuditorEntity.setIsDispose((short) 0);
                            chanPinJiaoFuFileAuditorEntity.setSort(userEntity.getSort());//添加批次
                            chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                            if (userEntity.getSort() == 1) {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                                //给第一批审核人 发消息
                                MessageEntity messageEntity = new MessageEntity();
                                messageEntity.setCreateBy(username);//创建人
                                messageEntity.setCreateTime(nowTime);//创建时间
                                messageEntity.setType((short) 2);
                                messageEntity.setMessageType((short) 3);
                                messageEntity.setProjectId(id);
                                messageEntity.setSenderId(senderid);
                                messageEntity.setReceiverId(userEntity.getUserId().intValue());
                                messageEntity.setStatus((short) 1);
                                messageEntity.setTitle("项目");
                                messageEntity.setContext("“" + chanPinJiaoFuXiangMuEntity.getXiangmuName() + "”项目需要您审核,请查看");
                                messageService.sendMessage(messageEntity);
                            } else {
                                chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                            }
                            chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                        }
                    }
                } else {//没有设置审批流  用选择的人进行审批
                    if (auditorid.length != 0 && sort.length != 0 && auditorid.length == sort.length) {
                        for (int i = 0; i < auditorid.length; i++) {
                            if (auditorid[i] != senderid) {
                                //添加审批人
                                ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
                                chanPinJiaoFuFileAuditorEntity.setObjectId(id);//添加项目id
                                chanPinJiaoFuFileAuditorEntity.setObjecttype(1);//项目
                                chanPinJiaoFuFileAuditorEntity.setAuditorId(auditorid[i]);//添加审核人
                                chanPinJiaoFuFileAuditorEntity.setDoname(username);
                                chanPinJiaoFuFileAuditorEntity.setDotime(nowTime);
                                chanPinJiaoFuFileAuditorEntity.setIsDispose((short) 0);
                                chanPinJiaoFuFileAuditorEntity.setSort(sort[i]);//添加批次
                                chanPinJiaoFuFileAuditorEntity.setAuditStatus((short) 2);
                                if (sort[i] == 1) {
                                    chanPinJiaoFuFileAuditorEntity.setActivate((short) 1);
                                    //给第一批审核人 发消息
                                    MessageEntity messageEntity = new MessageEntity();
                                    messageEntity.setCreateBy(username);//创建人
                                    messageEntity.setCreateTime(nowTime);//创建时间
                                    messageEntity.setType((short) 2);
                                    messageEntity.setMessageType((short) 3);
                                    messageEntity.setProjectId(id);
                                    messageEntity.setSenderId(senderid);
                                    messageEntity.setReceiverId(auditorid[i]);
                                    messageEntity.setStatus((short) 1);
                                    messageEntity.setTitle("项目");
                                    messageEntity.setContext("“" + chanPinJiaoFuXiangMuEntity.getXiangmuName() + "”项目需要您审核,请查看");
                                    messageService.sendMessage(messageEntity);
                                } else {
                                    chanPinJiaoFuFileAuditorEntity.setActivate((short) 0);
                                }
                                chanPinJiaoFuFileAuditorDao.insertSelective(chanPinJiaoFuFileAuditorEntity);
                            }
                        }
                    }
                }
        }
        chanPinJiaoFuXiangMuEntity.setAuditorstate(2);//审核状态  审核中
        chanPinJiaoFuXiangMuEntity.setUpdateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        Integer typenum = 1;
        Integer mid = id;
        chanPinJiaoFuXiangMuDao.editXiangMuById(chanPinJiaoFuXiangMuEntity);
        Integer aa = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (xiangMuEntity.getXiangmuState() != chanPinJiaoFuXiangMuEntity.getXiangmuState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1) {
                sb.append(" 项目状态改为“正在执行”,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2) {
                sb.append(" 项目状态改为“仅剩尾款”,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                sb.append(" 项目状态改为“项目关闭”,");
                aa++;
            }
        }
        if (!xiangMuEntity.getXiangmuName().equals(chanPinJiaoFuXiangMuEntity.getXiangmuName())) {
            String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            sb.append(" 项目名改为" + xiangmuName + ",");
            aa++;
        }
        if (xiangMuEntity.getXiaoshouState() != chanPinJiaoFuXiangMuEntity.getXiaoshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 1) {
                sb.append(" 销售需求状态改为 前续未办理,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 2) {
                sb.append(" 销售需求状态改为 全部品类/数量已下需求,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 3) {
                sb.append(" 销售需求状态改为 部分品类/数量已下需求,");
                aa++;
            }
        }
        if (xiangMuEntity.getFahuoState() != chanPinJiaoFuXiangMuEntity.getFahuoState()) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 1) {
                sb.append(" 发货状态改为 项目停滞暂不发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 2) {
                sb.append(" 发货状态改为 暂未开始发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 3) {
                sb.append(" 发货状态改为 陆续发货中,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 4) {
                sb.append(" 发货状态改为 工程结束不需发货,");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 5) {
                sb.append(" 发货状态改为 当前订单完成,");
                aa++;
            }
        }
        if (xiangMuEntity.getYangongState() != chanPinJiaoFuXiangMuEntity.getYangongState()) {
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 1) {
                sb.append(" 验工状态改为  是否需要验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 2) {
                sb.append(" 验工状态改为  前续未办理, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 3) {
                sb.append(" 验工状态改为  待验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 4) {
                sb.append(" 验工状态改为  顾客要求暂缓验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 5) {
                sb.append(" 验工状态改为  完成部分验工, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 6) {
                sb.append(" 验工状态改为  完成验工, ");
                aa++;
            }
        }
        if (xiangMuEntity.getQianshouState() != chanPinJiaoFuXiangMuEntity.getQianshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 1) {
                sb.append(" 签收状态改为  前续未办理, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 2) {
                sb.append(" 签收状态改为  公司暂存不签收, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 3) {
                sb.append(" 签收状态改为  已转储待签收, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 4) {
                sb.append(" 签收状态改为  完成签收, ");
                aa++;
            }
        }
        if (xiangMuEntity.getXianchangfuwu() != chanPinJiaoFuXiangMuEntity.getXianchangfuwu()) {
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {
                sb.append(" 是否需要现场服务改为 是, ");
                aa++;
            }
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 0) {
                sb.append(" 是否需要现场服务改为 否, ");
                aa++;
            }
        }
        if (xiangMuEntity.getRemarks() == null) {
            if (chanPinJiaoFuXiangMuEntity.getRemarks() != null) {
                sb.append("新增备注" + chanPinJiaoFuXiangMuEntity.getRemarks() + ",");
                aa++;
            }
        }
        if (xiangMuEntity.getRemarks() != null && xiangMuEntity.getRemarks() != chanPinJiaoFuXiangMuEntity.getRemarks()) {
            sb.append("备注改为" + chanPinJiaoFuXiangMuEntity.getRemarks() + ",");
            aa++;
        }
        if (xiangMuEntity.getFahuoTixingTime() == null) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoTixingTime() != null) {
                Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
                String format = sdf.format(fahuoTixingTime);
                sb.append("新增发货时间" + format + ",");
                aa++;
            }
        }
        if (xiangMuEntity.getFahuoTixingTime() != null && xiangMuEntity.getFahuoTixingTime().getTime() != chanPinJiaoFuXiangMuEntity.getFahuoTixingTime().getTime()) {
            Date fahuoTixingTime = chanPinJiaoFuXiangMuEntity.getFahuoTixingTime();
            String format = sdf.format(fahuoTixingTime);
            sb.append("发货时间改为" + format + ",");
            aa++;
        }
        if (aa == 0) {
            sb.append("没有修改任何状态");
        }
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer senderid) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuEntity.setCreateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setUserid(senderid);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
        Integer typenum = 1;
        String context = "新增“" + xiangmuName + "”项目 , 项目状态为“正在执行”";
        chanPinJiaoFuXiangMuDao.addXiangMu(chanPinJiaoFuXiangMuEntity);
        Integer mid = chanPinJiaoFuXiangMuEntity.getId();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
        List<Integer> uids = new ArrayList<>();
        if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {//“是否需要现场服务”为真
            //获取所有的人员id  发消息给所有人
            String truename = null;
            List<UserEntity> userEntitiess = userService.findByTruename(truename);
            for (UserEntity entitiess : userEntitiess) {
                Integer id = entitiess.getId().intValue();
                uids.add(id);
            }
            for (Integer uid : uids) {
                //添加一条消息到消息表
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setContext("“" + xiangmuName + "”项目需现场服务，请关注项目进展情况，及时安排现场服务计划！");
                messageEntity.setType((short) 3);
                messageEntity.setStatus((short) 1);
                messageEntity.setCreateBy(username);//创建人
                messageEntity.setCreateTime(nowTime);//创建时间
                messageEntity.setMessageType((short) 1);
                messageEntity.setProjectId(mid);
                messageEntity.setSenderId(senderid);
                messageEntity.setReceiverId(uid);
                messageService.sendMessage(messageEntity);
            }
        }
    }

    //展示树形结构
    @Override
    public List<TreeNodeUtil> findSanJiShu() {
        List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities = chanPinJiaoFuXiangMuDao.findSanJiShu();
        List<TreeNodeUtil> treeNodeUtils = new ArrayList<>();
        for (ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity : chanPinJiaoFuXiangMuEntities) {
            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();

            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1 || chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2 || chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                //第一级
                Integer xmid = chanPinJiaoFuXiangMuEntity.getId();
                treeNodeUtil.setValue(xmid.toString());
                treeNodeUtil.setId((long) chanPinJiaoFuXiangMuEntity.getId());
                treeNodeUtil.setLabel(chanPinJiaoFuXiangMuEntity.getXiangmuState().toString());
                List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities1 = chanPinJiaoFuXiangMuDao.findDiErJi(chanPinJiaoFuXiangMuEntity.getXiangmuState());
                List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
                for (ChanPinJiaoFuXiangMuEntity pinJiaoFuXiangMuEntity : chanPinJiaoFuXiangMuEntities1) {
                    //第二级
                    TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                    //Map<String,Object> map=new HashMap();
                    Integer id = pinJiaoFuXiangMuEntity.getId() + 1111;
                    Integer idd = pinJiaoFuXiangMuEntity.getId();
                    treeNodeUtil1.setValue(id.toString());
                    treeNodeUtil1.setId((long) pinJiaoFuXiangMuEntity.getId());
                    treeNodeUtil1.setLabel(pinJiaoFuXiangMuEntity.getXiangmuBianhao());
                    // map.put("xiangmu",chanPinJiaoFuXiangMuDao.findOneXiangMU(pinJiaoFuXiangMuEntity.getId()));
                    //treeNodeUtil1.setLi_attr(map);
                    treeNodeUtilss.add(treeNodeUtil1);
                    treeNodeUtil.setChildren(treeNodeUtilss);
                    //第三级
                    Integer aa = id + 2222;
                    Integer aaa = id + 3333;
                    List<TreeNodeUtil> treeNodeUtilss2 = new ArrayList<>();
                    List<TreeNodeUtil> treeNodeUtilss3 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil2.setId((long) 1);
                    treeNodeUtil2.setIcon(idd.toString());//父id
                    treeNodeUtil2.setLabel("输入文件");
                    treeNodeUtil2.setValue(aa.toString());
                    treeNodeUtil2.setChildren(treeNodeUtilss3);
                    treeNodeUtil3.setId((long) 2);
                    treeNodeUtil3.setIcon(idd.toString());//父id
                    treeNodeUtil3.setLabel("输出文件");
                    treeNodeUtil3.setValue(aaa.toString());
                    treeNodeUtil3.setChildren(treeNodeUtilss3);
                    treeNodeUtilss2.add(treeNodeUtil2);
                    treeNodeUtilss2.add(treeNodeUtil3);
                    treeNodeUtil1.setChildren(treeNodeUtilss2);
                }
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        Integer a = 0;
        Integer b = 0;
        Integer c = 0;
        for (TreeNodeUtil nodeUtil : treeNodeUtils) {
            if (nodeUtil.getLabel().equals("1")) {
                a++;
            }
            if (nodeUtil.getLabel().equals("2")) {
                b++;
            }
            if (nodeUtil.getLabel().equals("3")) {
                c++;
            }
        }

        if (a == 0) {
            TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
            treeNodeUtil1.setLabel("正在执行");
            treeNodeUtils.add(0, treeNodeUtil1);
        }
        if (b == 0) {
            TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
            treeNodeUtil2.setLabel("仅剩尾款");
            treeNodeUtils.add(1, treeNodeUtil2);
        }
        if (c == 0) {
            TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
            treeNodeUtil3.setLabel("项目关闭");
            treeNodeUtils.add(2, treeNodeUtil3);
        }
        return treeNodeUtils;
    }

}




