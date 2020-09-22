package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.anzhuangtiaoshi.AnZhuangTiaoShiWorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Mr.liu
 * @Date 2020/7/27 19:38
 * @Version 1.0
 * 需求:安装调试现场作业
 */
@Service
@Transactional
public class AnZhuangTiaoShiWorksServiceImpl implements AnZhuangTiaoShiWorksService {
    @Autowired
    private AnZhuangTiaoShiWorksCheZhanDao anZhuangTiaoShiWorksCheZhanDao;

    @Autowired
    private AnZhuangTiaoShiCheZhanDao anZhuangTiaoShiCheZhanDao;

    @Autowired
    private AnZhuangTiaoShiWorksDingDao anZhuangTiaoShiWorksDingDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameTotalDao anZhuangTiaoShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorksFileDao anZhuangTiaoShiWorksFileDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao;

    @Autowired
    private AnZhuangTiaoShiWorkNameLibraryDao anZhuangTiaoShiWorkNameLibraryDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseDao anZhuangTiaoShiXiangMuServiceChooseDao;


    @Override
    public JSONObject findAllCheZhanDatasByXDid(Integer id, Integer worksid) {
        JSONObject js = new JSONObject();
        //获取表头数据
        List<WorksDingTitleEntity> worksDingTitleEntityList = new ArrayList<>();
        List<Integer> wnlId = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findAllwnlidByWorksid(worksid);
        Integer k = 0;
        if (wnlId.size() > 1) {
            for (Integer wnlid : wnlId) {
                k++;
                String workname = anZhuangTiaoShiWorkNameLibraryDao.findWorkNameByid(wnlid);
                WorksDingTitleEntity worksDingTitleEntity = new WorksDingTitleEntity();
                worksDingTitleEntity.setId(k);
                worksDingTitleEntity.setAworkName(workname);
                worksDingTitleEntity.setBworkDoerAndTime("作业人及标记时间");
                worksDingTitleEntity.setCworkBiaoShi("作业项标识");
                worksDingTitleEntity.setDworkState("作业项状态");
                worksDingTitleEntityList.add(worksDingTitleEntity);
            }
        }
        js.put("title", worksDingTitleEntityList);
        System.out.println(js);
        //获取数据
        List<WorksDingTitleDataEntity> worksDingTitleDataEntityList = new ArrayList<>();
        Integer a=-1;
        List<AnZhuangTiaoShiXiangMuServiceChooseEntity> xiangMuServiceChooseEntityList = anZhuangTiaoShiXiangMuServiceChooseDao.findCheZhanByXDid(id);
        for (AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity : xiangMuServiceChooseEntityList) {
            a++;
            WorksDingTitleDataEntity dingTitleDataEntity=new WorksDingTitleDataEntity();
            JSONObject jss = new JSONObject();
            String chezhanname = xiangMuServiceChooseEntity.getChezhanname();
            Integer czid = xiangMuServiceChooseEntity.getCzid();
            Integer j = 0;
            if (wnlId.size() > 1) {
                for (Integer wnlid : wnlId) {
                    List dataList = new ArrayList<>();
                    j++;
                    String workstate = null;
                    String workerName = null;
                    Date workTime = null;
                    String workStatee = null;
                    Integer workState1 = null;
                    Integer workState = anZhuangTiaoShiWorkNameLibraryShiWorkNameTotalDao.findWorkStateByIDS(worksid, wnlid);
                    if (workState == 1) {
                        workstate = "手动";
                    }
                    if (workState == 2) {
                        workstate = "自动";
                    }
                    AnZhuangTiaoShiWorksDingEntity worksDingEntity = anZhuangTiaoShiWorksDingDao.findDataByIDS(czid, worksid, wnlid);
                    if (worksDingEntity != null) {
                        workerName = worksDingEntity.getWorkerName();
                        workState1 = worksDingEntity.getWorkState();
                        if (workState1 == 1) {
                            workStatee = "已完成";
                        }
                        if (workState1 == 2) {
                            workStatee = "未完成";
                        }
                        workTime = worksDingEntity.getWorkTime();
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        String format = sdf.format(workTime);
                        String bworkDoerAndTime = workerName +" "+ format;
                        dataList.add(workState1);
                        dataList.add(bworkDoerAndTime);
                        dataList.add(workstate);
                        dataList.add(workStatee);
                    }
                    if (worksDingEntity == null) {
                        dataList.add(2);
                        dataList.add("");
                        dataList.add(workstate);
                        dataList.add("未完成");
                    }
                    jss.put(j.toString(),dataList);
                    dingTitleDataEntity.setCzName(chezhanname);
                    dingTitleDataEntity.setCzid(czid);
                    dingTitleDataEntity.setList(jss);
                }
            }
            worksDingTitleDataEntityList.add(a,dingTitleDataEntity);
        }
        js.put("tableData",worksDingTitleDataEntityList);
        return js;
    }

    @Override
    public void deletFileByIds(Integer[] ids) {
        anZhuangTiaoShiWorksFileDao.deletFileByIds(ids);
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findFileByNmae(Integer page, Integer size, Integer xdid, Integer filetype, String filename) {
        return anZhuangTiaoShiWorksFileDao.findFileByNmae(xdid, filename, filetype);
    }

    @Override
    public AnZhuangTiaoShiWorksFileEntity findById(Integer id) {
        return anZhuangTiaoShiWorksFileDao.selectByPrimaryKey(id);
    }

    @Override
    public void editFileById(AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity) {
        anZhuangTiaoShiWorksFileDao.updateByPrimaryKeySelective(anZhuangTiaoShiWorksFileEntity);
    }

    @Override
    public void addFile(AnZhuangTiaoShiWorksFileEntity anZhuangTiaoShiWorksFileEntity) {
        anZhuangTiaoShiWorksFileDao.insertSelective(anZhuangTiaoShiWorksFileEntity);
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findShuRuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiWorksFileDao.findShuRuFileByXid(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorksFileEntity> findShuChuFileByXid(Integer id, Integer page, Integer size) {
        return anZhuangTiaoShiWorksFileDao.findShuChuFileByXid(id);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameTotalEntity> findAllWorks() {
        return anZhuangTiaoShiWorkNameTotalDao.findAllWorks();
    }

    @Override
    public void addWorksDatas(AnZhuangTiaoShiWorksDingEntity anZhuangTiaoShiWorksDingEntity) {
        anZhuangTiaoShiWorksDingDao.insertSelective(anZhuangTiaoShiWorksDingEntity);
    }

    @Override
    public List<AnZhuangTiaoShiWorkNameLibraryEntity> findWorksDatasByCid(Integer cid, Integer page, Integer size) {
        return anZhuangTiaoShiWorksDingDao.findWorksDatasByCid(cid);
    }

    @Override
    public List<AnZhuangTiaoShiWorksCheZhanEntity> findCheZhanDatasByXid(Integer xid, Integer page, Integer size) {
        return anZhuangTiaoShiWorksDingDao.findCheZhanDatasByXid(xid);
    }

    @Override
    public List<AnZhuangTiaoShiCheZhanEntity> findCheZhanByXid(Integer xid) {
        return anZhuangTiaoShiCheZhanDao.findCheZhanByXid(xid);
    }

    @Override
    public void editWorksCheZhanByXid(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity) {
        anZhuangTiaoShiWorksCheZhanDao.updateByPrimaryKey(anZhuangTiaoShiWorksCheZhanEntity);
    }

    @Override
    public void addWorksCheZhan(AnZhuangTiaoShiWorksCheZhanEntity anZhuangTiaoShiWorksCheZhanEntity, String[] chezhanname) {
        for (int i = 0; i < chezhanname.length; i++) {
            anZhuangTiaoShiWorksCheZhanEntity.setCzName(chezhanname[i]);
            anZhuangTiaoShiWorksCheZhanDao.insertSelective(anZhuangTiaoShiWorksCheZhanEntity);
        }
    }
}
