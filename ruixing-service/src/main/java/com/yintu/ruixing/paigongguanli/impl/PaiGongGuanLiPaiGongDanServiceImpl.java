package com.yintu.ruixing.paigongguanli.impl;

import com.yintu.ruixing.common.MessageDao;
import com.yintu.ruixing.common.MessageEntity;
import com.yintu.ruixing.common.MessageService;
import com.yintu.ruixing.paigongguanli.*;
import com.yintu.ruixing.xitongguanli.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.util.*;

/**
 * @Author Mr.liu
 * @Date 2020/8/22 19:32
 * @Version 1.0
 * 需求: 派工单
 */
@Service
@Transactional
public class PaiGongGuanLiPaiGongDanServiceImpl implements PaiGongGuanLiPaiGongDanService {
    @Autowired
    private PaiGongGuanLiPaiGongDanDao paiGongGuanLiPaiGongDanDao;

    @Autowired
    private PaiGongGuanLiTaskDao paiGongGuanLiTaskDao;

    @Autowired
    private PaiGongGuanLiTaskUserDao paiGongGuanLiTaskUserDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PaiGongGuanLiRiQinDao paiGongGuanLiRiQinDao;

    @Autowired
    private PaiGongGuanLiPaiGongDanRecordMessageDao paiGongGuanLiPaiGongDanRecordMessageDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PaiGongGuanLiBusinessTypeDao paiGongGuanLiBusinessTypeDao;

    @Autowired
    private MessageDao messageDao;


    @Override
    public PaiGongGuanLiPaiGongDanEntity findPaiGongDanByid(Integer id) {
        return paiGongGuanLiPaiGongDanDao.selectByPrimaryKey(id);
    }

    @Override
    public List<MessageEntity> findXiaoXi(Integer senderid) {
        Integer type = 5;
        return messageDao.findXiaoXi(senderid, type);
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanRecordMessageEntity> findRecordMessageByid(Integer id) {
        return paiGongGuanLiPaiGongDanRecordMessageDao.findRecordMessageByid(id);
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findAllPaiGongDan() {
        return paiGongGuanLiPaiGongDanDao.findAllPaiGongDan();
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findBuinessById(Integer id) {
        return paiGongGuanLiBusinessTypeDao.findBuinessById(id);
    }

    @Override
    public List<PaiGongGuanLiBusinessTypeEntity> findAllBuiness() {
        return paiGongGuanLiBusinessTypeDao.findAllBuiness();
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findPaiGongDan(Integer page, Integer size, String paiGongNumber) {
        return paiGongGuanLiPaiGongDanDao.findPaiGongDan(paiGongNumber);
    }

    @Override
    public void doSomeThingg(Integer receiverid, Integer senderid, Integer id, Integer isNotRefuse, String reason, String username) {
        Date today = new Date();//获取今天日期
        if (isNotRefuse == 0) {//不同意完工
            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派工任务被" + username + "不同意完工,原因是：" + reason);
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(today);//创建时间
            messageEntity.setContext("有派单任务不同意完工,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(id);
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(receiverid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
            //更改派工单状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity = new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(id);
            paiGongDanEntity.setState(0);
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
        }
        if (isNotRefuse == 1) {//同意完工
            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派工任务被" + username + "同意完工");
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
            //更改派工单状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity = new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(id);
            paiGongDanEntity.setPaigongstate(6);
            paiGongDanEntity.setState(0);
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
        }
    }

    @Override
    public void doSomeThing(Integer receiverid, Integer senderid, Integer id, Integer isNotRefuse, String reason, String username) {
        Date today = new Date();//获取今天日期
        if (isNotRefuse == 0) {//拒绝派遣
            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派工任务被" + username + "拒绝,原因是：" + reason);
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(today);//创建时间
            messageEntity.setContext("有派单任务被拒绝派遣,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(id);
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(receiverid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);

            //更改派工单状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity = new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(id);
            paiGongDanEntity.setState(0);
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
        }
        if (isNotRefuse == 1) {//接受派遣
            //添加记录
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派工任务被" + username + "接受派遣");
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
            //更改派工单状态
            PaiGongGuanLiPaiGongDanEntity paiGongDanEntity = new PaiGongGuanLiPaiGongDanEntity();
            paiGongDanEntity.setId(id);
            paiGongDanEntity.setPaigongstate(3);
            paiGongDanEntity.setState(0);
            paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongDanEntity);
        }
    }

    @Override
    public void deletePaiGongDanByIds(Integer[] ids) {
        for (Integer id : ids) {
            paiGongGuanLiPaiGongDanDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public void editPaiGongDanById(Integer id, Integer senderid, Integer uid, PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username) {
        Date today = new Date();//获取今天日期
        PaiGongGuanLiPaiGongDanEntity paiGongDanEntity = paiGongGuanLiPaiGongDanDao.selectByPrimaryKey(id);
        StringBuilder sb = new StringBuilder();
        Integer aa = 0;
        if (paiGongGuanLiPaiGongDanEntity.getPaigongstate() == 5) {//任务被终止
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(today);//创建时间
            messageEntity.setContext("有派单任务被终止,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(id);
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
            //记录数据
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派单任务被" + username + "终止");
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);

        }
        if (paiGongGuanLiPaiGongDanEntity.getPaigongstate() == 6) {//完成申请
            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(today);//创建时间
            messageEntity.setContext("有派单任务已完成需要您审批,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(id);
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(uid);
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);
            //记录数据
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(id);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("此派单任务已被" + username + "完成");
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
            paiGongGuanLiPaiGongDanEntity.setPaigongstate(2);

        }
        paiGongGuanLiPaiGongDanEntity.setState(2);
        paiGongGuanLiPaiGongDanDao.updateByPrimaryKeySelective(paiGongGuanLiPaiGongDanEntity);
        PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
        recordMessageEntity.setTypeid(id);
        recordMessageEntity.setOperatorname(username);
        recordMessageEntity.setOperatortime(today);
        recordMessageEntity.setContext("编辑派工任务");
        recordMessageEntity.setTypenum(1);
        paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);
    }

    @Override
    public List<PaiGongGuanLiPaiGongDanEntity> findOnePaiGongDanByNum(String paiGongDanNum) {
        return paiGongGuanLiPaiGongDanDao.findOnePaiGongDanByNum(paiGongDanNum);
    }


    @Override
    public void addPaiGongDan(PaiGongGuanLiPaiGongDanEntity paiGongGuanLiPaiGongDanEntity, String username, Integer senderid) {
        Date today = new Date();//获取今天日期
        List<Integer> userlist = new ArrayList<>();//人员名id集合
        Integer maxTaskshuxingNum = null;
        Integer minTaskshuxingNum = null;
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要且紧急") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("重要不紧急")) {
            maxTaskshuxingNum = 90;
            minTaskshuxingNum = 80;
        }
        if (paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("紧急不重要") || paiGongGuanLiPaiGongDanEntity.getTaskshuxing().equals("不重要不紧急")) {
            maxTaskshuxingNum = 70;
            minTaskshuxingNum = 60;
        }
        if (paiGongGuanLiPaiGongDanEntity.getPaigongmode() == 1) { //手动派工
            paiGongGuanLiPaiGongDanEntity.setPaigongstate(1);
            paiGongGuanLiPaiGongDanEntity.setOperatorid(senderid);
            paiGongGuanLiPaiGongDanEntity.setState(1);
            paiGongGuanLiPaiGongDanDao.insertSelective(paiGongGuanLiPaiGongDanEntity);
            Integer pgid = paiGongGuanLiPaiGongDanEntity.getId();
            PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
            recordMessageEntity.setTypeid(pgid);
            recordMessageEntity.setOperatorname(username);
            recordMessageEntity.setOperatortime(today);
            recordMessageEntity.setContext("新增派工任务");
            recordMessageEntity.setTypenum(1);
            paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);

            //添加消息
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setCreateBy(username);//创建人
            messageEntity.setCreateTime(today);//创建时间
            messageEntity.setContext("有派单任务需要您处理,请查看！");
            messageEntity.setType((short) 5);
            messageEntity.setProjectId(pgid);
            messageEntity.setMessageType((short) 3);
            messageEntity.setSenderId(senderid);
            messageEntity.setReceiverId(paiGongGuanLiPaiGongDanEntity.getPaigongpeople());
            messageEntity.setStatus((short) 1);
            messageService.sendMessage(messageEntity);

        } else { //自动派工
            Integer paigongpeoplenumber = paiGongGuanLiPaiGongDanEntity.getPaigongpeoplenumber();//自动派工人数

            String xiangmutype = paiGongGuanLiPaiGongDanEntity.getXiangmutype();//项目类型
            String yewutype = paiGongGuanLiPaiGongDanEntity.getYewutype();//业务类别
            String chuchaitype = paiGongGuanLiPaiGongDanEntity.getChuchaitype();//出差类型
            String renwu = xiangmutype + yewutype + "——" + chuchaitype;
            Integer tid = paiGongGuanLiTaskDao.findTid(renwu);
            //根据任务id  和分值范围  查询对应的人员  并按照分值大小排列
            List<PaiGongGuanLiTaskUserEntity> userEntityList = paiGongGuanLiTaskUserDao.findUser(tid, maxTaskshuxingNum, minTaskshuxingNum);
            for (PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity : userEntityList) {//遍历每个人员
                Integer userid = paiGongGuanLiTaskUserEntity.getUid();//获取人员名id
                //Integer uid = userDao.findid(truename);
                String userdongtai = paiGongGuanLiRiQinDao.findUserDongTai(userid);//查询这个人的出差  请假情况
                if (!userdongtai.equals("请假") || !userdongtai.equals("出差")) {
                    List<PaiGongGuanLiPaiGongDanEntity> userList = paiGongGuanLiPaiGongDanDao.findUserByUserid(userid);//根据人员名id  查询是否派单 或者是否是改派人员
                    if (userList.size() == 0) { //说明这个人咩有出过差
                        userlist.add(userid); //把这个人放到list集合里
                    } else {//这个人出过差
                        for (PaiGongGuanLiPaiGongDanEntity gongGuanLiPaiGongDanEntity : userList) {//遍历这个人的出差情况  由时间从前往后排序
                            long Chuchaiendtime = gongGuanLiPaiGongDanEntity.getChuchaiendtime().getTime();//获得本次出差的结束时间
                            Integer paigongpeople = gongGuanLiPaiGongDanEntity.getPaigongpeople();//得到此人的id
                            if (Chuchaiendtime < paiGongGuanLiPaiGongDanEntity.getChuchaistarttime().getTime()) {//说明此人 能派遣
                                userlist.add(paigongpeople);
                            }
                        }
                    }
                }
            }

            List lianxu = new ArrayList();
            //遍历符合条件的人员userlist
            if (userlist.size() != 0) {
                long lianxutime = 0;
                for (int i = 0; i < userlist.size(); i++) {
                    Integer usernameid = userlist.get(i);//得到人员名
                    List<PaiGongGuanLiPaiGongDanEntity> userList = paiGongGuanLiPaiGongDanDao.findUserByUserid(usernameid);//根据人员名  查询此人的派工经历
                    if (userList.size() == 1) {
                        long endtime = userList.get(0).getChuchaiendtime().getTime();
                        long starttime = userList.get(0).getChuchaistarttime().getTime();
                        lianxutime = (endtime - starttime) / 86400000;
                    }
                    if (userList.size() > 1) {
                        long endtime1 = userList.get(0).getChuchaiendtime().getTime();
                        long starttime1 = userList.get(0).getChuchaistarttime().getTime();

                        long endtime2 = userList.get(1).getChuchaiendtime().getTime();
                        long starttime2 = userList.get(1).getChuchaistarttime().getTime();

                        long lianxutime1 = starttime1 - endtime2;
                        long dayNum = lianxutime1 / 86400000;//间隔天数
                        if (dayNum < 3) {
                            lianxutime = ((endtime1 - starttime1) + (endtime2 - starttime2)) / 86400000;
                        }
                        if (3 <= dayNum && dayNum < 6) {
                            lianxutime = (long) ((endtime1 - starttime1) + (endtime2 - starttime2) * 0.6) / 86400000;
                        }
                        if (6 <= dayNum && dayNum < 10) {
                            lianxutime = (long) ((endtime1 - starttime1) + (endtime2 - starttime2) * 0.3) / 86400000;
                        }
                        if (10 <= dayNum) {
                            lianxutime = (endtime1 - starttime1) / 86400000;
                        }
                    }
                    lianxu.add(lianxutime);
                }

                for (int i = 0; i < lianxu.size(); i++) {

                }

                int n = lianxu.size();
                HashMap map = new HashMap();
                for (int i = 0; i < lianxu.size(); i++) {
                    map.put(lianxu.get(i), i); //将值和下标存入Map
                }

                //排列
                List list = new ArrayList();
                Arrays.sort(lianxu.toArray()); //升序排列
                for (int i = 0; i < lianxu.toArray().length; i++) {
                    list.add(lianxu.toArray()[i]);
                }

                List list1 = new ArrayList();//获取时间从小到大排序的原始下标
                //查找原始下标
                for (int i = 0; i < n; i++) {
                    list1.add(map.get(lianxu.toArray()[i]));
                }
                for (int i = 0; i < paigongpeoplenumber; i++) {
                    int ii = list1.get(i).hashCode();
                    Integer usernameeid = userlist.get(ii);//得到人员名id
                    paiGongGuanLiPaiGongDanEntity.setPaigongpeople(usernameeid);
                    paiGongGuanLiPaiGongDanEntity.setPaigongstate(1);
                    paiGongGuanLiPaiGongDanEntity.setOperatorid(senderid);
                    paiGongGuanLiPaiGongDanEntity.setState(1);
                    paiGongGuanLiPaiGongDanDao.insertSelective(paiGongGuanLiPaiGongDanEntity);
                    Integer pgid = paiGongGuanLiPaiGongDanEntity.getId();
                    PaiGongGuanLiPaiGongDanRecordMessageEntity recordMessageEntity = new PaiGongGuanLiPaiGongDanRecordMessageEntity();
                    recordMessageEntity.setTypeid(pgid);
                    recordMessageEntity.setOperatorname("自动派工");
                    recordMessageEntity.setOperatortime(today);
                    recordMessageEntity.setContext("新增派工任务");
                    recordMessageEntity.setTypenum(1);
                    paiGongGuanLiPaiGongDanRecordMessageDao.insertSelective(recordMessageEntity);

                    //添加消息
                    MessageEntity messageEntity = new MessageEntity();
                    messageEntity.setCreateBy(username);//创建人
                    messageEntity.setCreateTime(today);//创建时间
                    messageEntity.setContext("有派单任务需要您处理,请查看！");
                    messageEntity.setType((short) 5);
                    messageEntity.setProjectId(pgid);
                    messageEntity.setMessageType((short) 3);
                    messageEntity.setSenderId(senderid);
                    messageEntity.setReceiverId(usernameeid);
                    messageEntity.setStatus((short) 1);
                    messageService.sendMessage(messageEntity);
                }

            }
        }
    }

    @Override
    public String findPaiGongDanNum(String suoxie) {
        return paiGongGuanLiPaiGongDanDao.findPaiGongDanNum(suoxie);
    }


    public static void main(String[] args) {
//        /*int[] a={1,2,9,6,5};
//        int temp =0;
//        for (int i = 0; i < a.length; i++) {
//            for (int i1 = i+1; i1 < a.length; i1++) {
//                if (a[i]>a[i1]){
//                     temp = a[i];
//
//                    a[i] = a[i1];
//
//                    a[i1] = temp;
//                }
//            }
//        }
//        System.out.println(temp);*/

//
//        List list12 = new ArrayList();
//        int[] arr = {23, 12, 48, 56, 45};
//        int n1 = arr.length;
//
//        HashMap map1 = new HashMap();
//        for (int i = 0; i < arr.length; i++) {
//            map1.put(arr[i], i); //将值和下标存入Map
//        }
//
//
//        int temp = -1;
//        for (int i = 0; i < arr.length; i++) {
//            for (int j = i + 1; j < arr.length; j++) {
//                if (arr[i] < arr[j]) {
//                    temp = arr[i];
//                    arr[i] = arr[j];
//                    arr[j] = temp;
//
//                }
//            }
//        }
//        System.out.println(Arrays.toString(arr));
//        //查找原始下标
//
//        for (int i = 0; i < n1; i++) {
//            //System.out.println(map.get(a[i]));
//            list12.add("666" + map1.get(arr[i]));
//        }

        ///////////////////////////////////

/*

        int n = 9;
        int[] a = {8, 5, 4, 6, 2, 1, 7, 9, 3};
        HashMap map = new HashMap();
        for (int i = 0; i < a.length; i++) {
            map.put(a[i], i); //将值和下标存入Map
        }

        //排列
        List list = new ArrayList();
        Arrays.sort(a); //升序排列
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }

        Collections.reverse(list); //逆序排列,变为降序
        for (int i = 0; i < list.size(); i++) {
            a[i] = (Integer) list.get(i);
        }

        List list1 = new ArrayList();
        //查找原始下标
        for (int i = 0; i < n; i++) {
            list1.add(map.get(a[i]));

        }
        System.out.println(list1);

*/


        int n = 9;
        Integer[] a = {8, 5, 4, 6, 1, 1, 7, 9, 3};
        System.out.println("vc" + a[5]);
        HashMap map = new HashMap();
        for (int i = 0; i < a.length; i++) {
            map.put(a[i], i); //将值和下标存入Map
        }

        //排列
        List list = new ArrayList();
        Arrays.sort(a); //升序排列
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }


        /*Collections.reverse(list); //逆序排列,变为降序
        for (int i = 0; i < list.size(); i++) {
            a[i] = (Integer) list.get(i);
        }
*/
        List list1 = new ArrayList();
        //查找原始下标
        for (int i = 0; i < n; i++) {
            list1.add(map.get(a[i]));
        }
        System.out.println(list1);

        Integer[] b = {8, 5, 4, 6, 1, 1, 7, 9, 3};
        for (int i = 0; i <= 3; i++) {
            int i1 = list1.get(i).hashCode();
            //System.out.println("i1"+i1);
            //System.out.println(list1.get(i));
            System.out.println(b[i1]);
        }


        //获取IP地址

        /*try {
            InetAddress myip = InetAddress.getLocalHost();

            System.out.println("你的IP地址是：" + myip.getHostAddress());

            System.out.println("主机名为：" + myip.getHostName() + "。");
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

}
