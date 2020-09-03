package com.yintu.ruixing.chanpinjiaofu.impl;

import com.yintu.ruixing.chanpinjiaofu.*;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.xitongguanli.UserDao;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<MessageEntity> findXiaoXi() {
        return messageDao.findXiaoXi();
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
        return map;
    }


    /////////////////////////项目交付状态小模块/////////////////////////////////////
    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuByIds(Integer stateid, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findXiangMuByIds(stateid);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findXiangMuData(String xiangMuBianHao, String xiangMuName, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findXiangMuData(xiangMuBianHao, xiangMuName);
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
    public void editXiangMuFileById(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer id, Integer[] uids, Integer uId, String username) {
        ChanPinJiaoFuXiangMuFileEntity fileEntity = chanPinJiaoFuXiangMuFileDao.selectByPrimaryKey(id);
        StringBuilder sb = new StringBuilder();
        //删除中间表的审查人id
        chanPinJiaoFuXiangMuDao.deletAuditor(id);
        //添加审查人
        for (Integer uid : uids) {
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
            chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(id);
            chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
            chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
        }
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuFileEntity.setUpdatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setUpdatename(username);
        chanPinJiaoFuXiangMuFileDao.updateByPrimaryKeySelective(chanPinJiaoFuXiangMuFileEntity);
        if (!fileEntity.getFileName().equals(chanPinJiaoFuXiangMuFileEntity.getFileName())) {
            sb.append("文件名改为" + chanPinJiaoFuXiangMuFileEntity.getFileName());
        }
        if (fileEntity.getAuditorState() != chanPinJiaoFuXiangMuFileEntity.getAuditorState()) {
            if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 1) {
                sb.append("文件审核状态更改为  待审核  ");
            }
            if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 2) {
                sb.append("文件审核状态更改为  已审核未通过 ");
            }
            if (chanPinJiaoFuXiangMuFileEntity.getAuditorState() == 3) {
                sb.append("文件审核状态更改为  已审核已通过");
            }
        }
        if (fileEntity.getFileType() != chanPinJiaoFuXiangMuFileEntity.getFileType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 1) {
                sb.append("文件类型更改为  输入文件 ");
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFileType() == 2) {
                sb.append("文件类型更改为  输出文件 ");
            }
        }
        if (fileEntity.getFabuType() != chanPinJiaoFuXiangMuFileEntity.getFabuType()) {
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 1) {
                sb.append("发布状态更改为  录入 ");
            }
            if (chanPinJiaoFuXiangMuFileEntity.getFabuType() == 2) {
                sb.append("发布状态更改为  发布 ");
            }
        } else {
            sb.append("没有修改任何状态");
        }
        Integer typenum = 2;
        Integer mid = id;
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMuFile(ChanPinJiaoFuXiangMuFileEntity chanPinJiaoFuXiangMuFileEntity, Integer[] uids, String username, Integer uId) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuFileEntity.setCreatetime(nowTime);
        chanPinJiaoFuXiangMuFileEntity.setCreatename(username);
        chanPinJiaoFuXiangMuFileEntity.setUid(uId);
        chanPinJiaoFuXiangMuFileEntity.setAuditorState(1);
        String fileName = chanPinJiaoFuXiangMuFileEntity.getFileName();
        chanPinJiaoFuXiangMuFileDao.insertSelective(chanPinJiaoFuXiangMuFileEntity);
        Integer mid = chanPinJiaoFuXiangMuFileEntity.getId();
        Integer typenum = 2;
        String context = "新增“" + fileName + "”文件";
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
        Integer xid = chanPinJiaoFuXiangMuFileEntity.getId();
        for (Integer uid : uids) {
            ChanPinJiaoFuFileAuditorEntity chanPinJiaoFuFileAuditorEntity = new ChanPinJiaoFuFileAuditorEntity();
            chanPinJiaoFuFileAuditorEntity.setChanPinJiaoFuFileId(xid);
            chanPinJiaoFuFileAuditorEntity.setAuditorId(uid);
            chanPinJiaoFuXiangMuDao.addAuditorName(chanPinJiaoFuFileAuditorEntity);
        }
    }

    @Override
    public List<ChanPinJiaoFuXiangMuEntity> findAll(Integer page, Integer size) {
        return chanPinJiaoFuXiangMuDao.findAll();
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
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuChuFile(Integer xmid, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuFileDao.findShuChuFile(xmid);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findShuRuFile(Integer xmid, Integer page, Integer size) {
        return chanPinJiaoFuXiangMuFileDao.findShuRuFile(xmid);
    }

    @Override
    public List<ChanPinJiaoFuXiangMuFileEntity> findFileBySomething(Integer xmid, Integer page, Integer size, Integer filetype, String filename) {
        return chanPinJiaoFuXiangMuFileDao.findFileBySomething(xmid, filetype, filename);
    }

    @Override
    public void editXiangMuById(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username, Integer id) {
        StringBuilder sb = new StringBuilder();
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuEntity.setUpdateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        Integer typenum = 1;
        Integer mid = id;
        ChanPinJiaoFuXiangMuEntity xiangMuEntity = chanPinJiaoFuXiangMuDao.selectByPrimaryKey(id);
        chanPinJiaoFuXiangMuDao.editXiangMuById(chanPinJiaoFuXiangMuEntity);
        if (xiangMuEntity.getXiangmuState() != chanPinJiaoFuXiangMuEntity.getXiangmuState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 1) {
                sb.append(" 项目状态改为“正在执行”");
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 2) {
                sb.append(" 项目状态改为“仅剩尾款”");
            }
            if (chanPinJiaoFuXiangMuEntity.getXiangmuState() == 3) {
                sb.append(" 项目状态改为“项目关闭”");
            }
        }
        if (!xiangMuEntity.getXiangmuName().equals(chanPinJiaoFuXiangMuEntity.getXiangmuName())) {
            String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
            sb.append(" 项目名改为" + xiangmuName);
        }
        if (xiangMuEntity.getXiaoshouState() != chanPinJiaoFuXiangMuEntity.getXiaoshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 1) {
                sb.append(" 销售需求状态改为:前续未办理");
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 2) {
                sb.append(" 销售需求状态改为:全部品类/数量已下需求");
            }
            if (chanPinJiaoFuXiangMuEntity.getXiaoshouState() == 3) {
                sb.append(" 销售需求状态改为:部分品类/数量已下需求");
            }
        }
        if (xiangMuEntity.getFahuoState() != chanPinJiaoFuXiangMuEntity.getFahuoState()) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 1) {
                sb.append(" 发货状态改为 项目停滞暂不发货");
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 2) {
                sb.append(" 发货状态改为 暂未开始发货");
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 3) {
                sb.append(" 发货状态改为 陆续发货中");
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 4) {
                sb.append(" 发货状态改为 工程结束不需发货");
            }
            if (chanPinJiaoFuXiangMuEntity.getFahuoState() == 5) {
                sb.append(" 发货状态改为 当前订单完成");
            }
        }
        if (xiangMuEntity.getYangongState() != chanPinJiaoFuXiangMuEntity.getYangongState()) {
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 1) {
                sb.append(" 验工状态改为  是否需要验工 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 2) {
                sb.append(" 验工状态改为  前续未办理 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 3) {
                sb.append(" 验工状态改为  待验工 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 4) {
                sb.append(" 验工状态改为  顾客要求暂缓验工 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 5) {
                sb.append(" 验工状态改为  完成部分验工 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getYangongState() == 6) {
                sb.append(" 验工状态改为  完成验工");
            }
        }
        if (xiangMuEntity.getQianshouState() != chanPinJiaoFuXiangMuEntity.getQianshouState()) {
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 1) {
                sb.append(" 签收状态改为  前续未办理 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 2) {
                sb.append(" 签收状态改为  公司暂存不签收 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 3) {
                sb.append(" 签收状态改为  已转储待签收 ");
            }
            if (chanPinJiaoFuXiangMuEntity.getQianshouState() == 4) {
                sb.append(" 签收状态改为  完成签收 ");
            }
        }
        if (xiangMuEntity.getXianchangfuwu() != chanPinJiaoFuXiangMuEntity.getXianchangfuwu()) {
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 1) {
                sb.append(" 是否需要现场服务改为 是");
            }
            if (chanPinJiaoFuXiangMuEntity.getXianchangfuwu() == 0) {
                sb.append(" 是否需要现场服务改为 否");
            }
        }
        if (xiangMuEntity.getRemarks() == null) {
            if (chanPinJiaoFuXiangMuEntity.getRemarks() != null) {
                sb.append("新增备注" + chanPinJiaoFuXiangMuEntity.getRemarks());
            }
        }
        if (xiangMuEntity.getRemarks() != null && xiangMuEntity.getRemarks() != chanPinJiaoFuXiangMuEntity.getRemarks()) {
            sb.append("备注改为" + chanPinJiaoFuXiangMuEntity.getRemarks());
        }
        if (xiangMuEntity.getFahuoTixingTime() == null) {
            if (chanPinJiaoFuXiangMuEntity.getFahuoTixingTime() != null) {
                sb.append("新增发货时间" + chanPinJiaoFuXiangMuEntity.getFahuoTixingTime());
            }
        }
        if (xiangMuEntity.getFahuoTixingTime() != null && xiangMuEntity.getFahuoTixingTime().getTime() != chanPinJiaoFuXiangMuEntity.getFahuoTixingTime().getTime()) {
            sb.append("发货时间改为" + chanPinJiaoFuXiangMuEntity.getFahuoTixingTime());
        } else {
            sb.append("没有修改任何状态");
        }
        String context = sb.toString();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
    }

    @Override
    public void addXiangMu(ChanPinJiaoFuXiangMuEntity chanPinJiaoFuXiangMuEntity, String username) {
        Date nowTime = new Date();
        chanPinJiaoFuXiangMuEntity.setCreateTime(nowTime);
        chanPinJiaoFuXiangMuEntity.setOperatorName(username);
        String xiangmuName = chanPinJiaoFuXiangMuEntity.getXiangmuName();
        Integer typenum = 1;
        String context = "新增“" + xiangmuName + "”项目";
        chanPinJiaoFuXiangMuDao.addXiangMu(chanPinJiaoFuXiangMuEntity);
        Integer mid = chanPinJiaoFuXiangMuEntity.getId();
        chanPinJiaoFuRecordMessageDao.addRecordMessage(mid, typenum, nowTime, username, context);
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
                treeNodeUtil.setId((long) chanPinJiaoFuXiangMuEntity.getId());
                treeNodeUtil.setLabel(chanPinJiaoFuXiangMuEntity.getXiangmuState().toString());
                List<ChanPinJiaoFuXiangMuEntity> chanPinJiaoFuXiangMuEntities1 = chanPinJiaoFuXiangMuDao.findDiErJi(chanPinJiaoFuXiangMuEntity.getXiangmuState());
                List<TreeNodeUtil> treeNodeUtilss = new ArrayList<>();
                for (ChanPinJiaoFuXiangMuEntity pinJiaoFuXiangMuEntity : chanPinJiaoFuXiangMuEntities1) {
                    //第二级
                    TreeNodeUtil treeNodeUtil1 = new TreeNodeUtil();
                    //Map<String,Object> map=new HashMap();
                    treeNodeUtil1.setId((long) pinJiaoFuXiangMuEntity.getId());
                    treeNodeUtil1.setLabel(pinJiaoFuXiangMuEntity.getXiangmuBianhao());
                    // map.put("xiangmu",chanPinJiaoFuXiangMuDao.findOneXiangMU(pinJiaoFuXiangMuEntity.getId()));
                    //treeNodeUtil1.setLi_attr(map);
                    treeNodeUtilss.add(treeNodeUtil1);
                    treeNodeUtil.setChildren(treeNodeUtilss);
                    //第三级
                    List<TreeNodeUtil> treeNodeUtilss2 = new ArrayList<>();
                    List<TreeNodeUtil> treeNodeUtilss3 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil2 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil3 = new TreeNodeUtil();
                    treeNodeUtil2.setId((long) 1);
                    treeNodeUtil2.setLabel("输入文件");
                    treeNodeUtil2.setChildren(treeNodeUtilss3);
                    treeNodeUtil3.setId((long) 2);
                    treeNodeUtil3.setLabel("输出文件");
                    treeNodeUtil3.setChildren(treeNodeUtilss3);
                    treeNodeUtilss2.add(treeNodeUtil2);
                    treeNodeUtilss2.add(treeNodeUtil3);
                    treeNodeUtil1.setChildren(treeNodeUtilss2);
                }
            }
            treeNodeUtils.add(treeNodeUtil);
        }
        return treeNodeUtils;
    }

}




