package com.yintu.ruixing.anzhuangtiaoshi.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.anzhuangtiaoshi.*;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.guzhangzhenduan.CheZhanService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Mr.liu
 * @Date 2020/9/14 16:36
 * @Version 1.0
 * 需求:安装调试 项目
 */
@Service
@Transactional
public class AnZhuangTiaoShiXiangMuServiceChooseServiceImpl implements AnZhuangTiaoShiXiangMuServiceChooseService {
    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseDao anZhuangTiaoShiXiangMuServiceChooseDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuDao anZhuangTiaoShiXiangMuDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusChooseDao anZhuangTiaoShiXiangMuServiceStatusChooseDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceStatusDao anZhuangTiaoShiXiangMuServiceStatusDao;

    @Autowired
    private AnZhuangTiaoShiXiangMuServiceChooseService anZhuangTiaoShiXiangMuServiceChooseService;
    @Autowired
    private CheZhanService cheZhanService;

    @Override
    public void addXiangMuServiceChooseEntity(AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity) {
        anZhuangTiaoShiXiangMuServiceChooseDao.insertSelective(xiangMuServiceChooseEntity);
    }

    @Override
    public void addXiangMu(AnZhuangTiaoShiXiangMuEntity xiangMuEntity) {
        anZhuangTiaoShiXiangMuDao.addSanJiShuXiangMu(xiangMuEntity);
    }

    @Override
    public AnZhuangTiaoShiXiangMuServiceStatusEntity findServiceStatusById(Integer id) {
        return anZhuangTiaoShiXiangMuServiceStatusDao.selectByPrimaryKey(id);
    }

    @Override
    public JSONArray findStatusByCzId(Integer czId) {
        JSONArray ja = new JSONArray();
        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> anZhuangTiaoShiXiangMuServiceStatusEntities = anZhuangTiaoShiXiangMuServiceStatusDao.findAllServiceStatus();
        for (AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity : anZhuangTiaoShiXiangMuServiceStatusEntities) {
            Integer id = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> serviceChooseEntities = anZhuangTiaoShiXiangMuServiceChooseDao.findAllChoidBySeridAndCzId(id, czId);
            if (!serviceChooseEntities.isEmpty()) {
                Integer timeType = anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype();
                JSONObject jo;
                if (timeType != null) {//单选
                    jo = (JSONObject) JSONObject.toJSON(serviceChooseEntities.get(0));
                    jo.put("isNotFinish", jo.getInteger("isNotFinish") == 1);
                    jo.put("serviceName", anZhuangTiaoShiXiangMuServiceStatusEntity.getServicename());

                } else {
                    jo = new JSONObject();//多选
                    jo.put("serviceName", anZhuangTiaoShiXiangMuServiceStatusEntity.getServicename());
                    JSONArray jsonArray = new JSONArray();
                    for (AnZhuangTiaoShiXiangMuServiceChooseEntity serviceChooseEntity : serviceChooseEntities) {
                        AnZhuangTiaoShiXiangMuServiceStatusChooseEntity anZhuangTiaoShiXiangMuServiceStatusChooseEntity = anZhuangTiaoShiXiangMuServiceStatusChooseDao.selectByPrimaryKey(serviceChooseEntity.getChoid());
                        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(serviceChooseEntity);
                        jsonObject.put("isnot", jsonObject.getInteger("isnot") == 1);
                        jsonObject.put("serviceName", anZhuangTiaoShiXiangMuServiceStatusChooseEntity.getName());
                        jsonArray.add(jsonObject);
                    }
                    jo.put("list", jsonArray);
                }
                ja.add(jo);
            }
        }
        return ja;
    }

    @Override
    public void addXiangMuServiceChoose(JSONArray cheZhanDatas, String username, Integer senderid) {
        Date today = new Date();
        for (Object chezhandata : cheZhanDatas) {
            Map<String, Object> chezhandat = (Map<String, Object>) chezhandata;
            Map<String, Object> label = (Map<String, Object>) chezhandat.get("addForm");
            Map<String, Object> tljName = (Map<String, Object>) label.get("tljName");
            Map<String, Object> dwdName = (Map<String, Object>) label.get("dwdName");
            Map<String, Object> xdName = (Map<String, Object>) label.get("xdName");
            Map<String, Object> czName = (Map<String, Object>) label.get("chezhanname");
            List statusdata = (List) label.get("statusdata");

            String tljname = (String) tljName.get("label");
            String dwdname = (String) dwdName.get("label");
            String xdname = (String) xdName.get("label");
            Integer xdid = (Integer) xdName.get("value");
            String czname = (String) czName.get("label");
            Integer czid = (Integer) czName.get("value");
            Long xianduanTime = (Long) label.get("xianduantime");
            Integer worksid = (Integer) label.get("worksid");
            String xdFenlei = (String) label.get("xdFenlei");
            String guanlianxiangmu = (String) label.get("guanlianxiangmu");
            String xdType = (String) label.get("xdType");
            Date xianduantime = new Date(xianduanTime);
            List<AnZhuangTiaoShiXiangMuEntity> anZhuangTiaoShiXiangMuEntities = anZhuangTiaoShiXiangMuDao.findByXdId(xdid);
            if (anZhuangTiaoShiXiangMuEntities.isEmpty()) {//如果线段不存在添加数据，否侧无需重复添加线段数据
                //新增项目
                AnZhuangTiaoShiXiangMuEntity xiangMuEntity = new AnZhuangTiaoShiXiangMuEntity();
                xiangMuEntity.setTljName(tljname);
                xiangMuEntity.setDwdName(dwdname);
                xiangMuEntity.setXdId(xdid);
                xiangMuEntity.setXdName(xdname);
                xiangMuEntity.setXianduantime(xianduantime);
                xiangMuEntity.setXdFenlei(Integer.parseInt(xdFenlei));
                xiangMuEntity.setWorksid(worksid);
                xiangMuEntity.setXdType(xdType);
                xiangMuEntity.setGuanlianxiangmu(guanlianxiangmu);
                xiangMuEntity.setCreatename(username);
                xiangMuEntity.setCreatetime(today);
                xiangMuEntity.setUpdatename(username);
                xiangMuEntity.setUpdatetime(today);

                anZhuangTiaoShiXiangMuServiceChooseService.addXiangMu(xiangMuEntity);
            }

            List<AnZhuangTiaoShiXiangMuServiceChooseEntity> anZhuangTiaoShiXiangMuServiceChooseEntities = anZhuangTiaoShiXiangMuServiceChooseDao.findAllByCZid(czid);
            if (!anZhuangTiaoShiXiangMuServiceChooseEntities.isEmpty())
                throw new BaseRuntimeException("此线段下车站已有数据，请选择其他车站");
            for (Object statusdatum : statusdata) {
                Map<String, Object> statusdatu = (Map<String, Object>) statusdatum;
                Integer titleid = (Integer) statusdatu.get("id");//服务状态标识id
                String servicename = (String) statusdatu.get("servicename");//服务状态标识名
                AnZhuangTiaoShiXiangMuServiceStatusEntity serviceStatusEntity = anZhuangTiaoShiXiangMuServiceChooseService.findServiceStatusById(titleid);
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 2) {//有状态标识  且有计划和实际开始结束时间
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");
                    Date planStartTime = null;
                    Date planEndTime = null;
                    if (isNotFinish != null && isNotFinish == 0) {
                        Long planStartTimeTimestamp = (Long) statusdatu.get("planStartTime");
                        Long planEndTimeTimestamp = (Long) statusdatu.get("planEndTime");
                        planStartTime = new Date(planStartTimeTimestamp);
                        planEndTime = new Date(planEndTimeTimestamp);
                    }
                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xdid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(2);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setPlanStartTime(planStartTime);
                    xiangMuServiceChooseEntity.setPlanEndTime(planEndTime);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    xiangMuServiceChooseEntity.setUpdatename(username);
                    xiangMuServiceChooseEntity.setUpdatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 1) {//有状态标识  没有计划和实际开始结束时间
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");
                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xdid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(1);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    xiangMuServiceChooseEntity.setUpdatename(username);
                    xiangMuServiceChooseEntity.setUpdatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);

                }
                if (serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == 3) {
                    Integer isNotFinish = (Integer) statusdatu.get("isNotFinish");
                    Date planOpenTime = null;
                    if (isNotFinish != null && isNotFinish == 0) {
                        Long planOpenTimeTimeTimestamp = (Long) statusdatu.get("planOpenTime");
                        planOpenTime = new Date(planOpenTimeTimeTimestamp);
                    }
                    AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    xiangMuServiceChooseEntity.setXdid(xdid);
                    xiangMuServiceChooseEntity.setSerid(titleid);
                    xiangMuServiceChooseEntity.setCzid(czid);
                    xiangMuServiceChooseEntity.setChezhanname(czname);
                    xiangMuServiceChooseEntity.setTypetime(3);
                    xiangMuServiceChooseEntity.setIsNotFinish(isNotFinish);
                    xiangMuServiceChooseEntity.setPlanOpenTime(planOpenTime);
                    xiangMuServiceChooseEntity.setCreatename(username);
                    xiangMuServiceChooseEntity.setCreatetime(today);
                    xiangMuServiceChooseEntity.setUpdatename(username);
                    xiangMuServiceChooseEntity.setUpdatetime(today);
                    anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
                }
                if (!serviceStatusEntity.getChoose().equals("是否") && serviceStatusEntity.getTimetype() == null) {
                    List list = (List) statusdatu.get("list");
                    for (Object chroose : list) {
                        Map<String, Object> chroosee = (Map<String, Object>) chroose;
                        Integer chrooseid = (Integer) chroosee.get("id");
                        Boolean isnot = (Boolean) chroosee.get("isNotChoose");
                        AnZhuangTiaoShiXiangMuServiceChooseEntity xiangMuServiceChooseEntity = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                        xiangMuServiceChooseEntity.setXdid(xdid);
                        xiangMuServiceChooseEntity.setSerid(titleid);
                        xiangMuServiceChooseEntity.setCzid(czid);
                        xiangMuServiceChooseEntity.setChezhanname(czname);
                        xiangMuServiceChooseEntity.setTypetime(null);
                        xiangMuServiceChooseEntity.setIsnot(isnot ? 1 : 0);
                        xiangMuServiceChooseEntity.setChoid(chrooseid);
                        xiangMuServiceChooseEntity.setCreatename(username);
                        xiangMuServiceChooseEntity.setCreatetime(today);
                        xiangMuServiceChooseEntity.setUpdatename(username);
                        xiangMuServiceChooseEntity.setUpdatetime(today);
                        anZhuangTiaoShiXiangMuServiceChooseService.addXiangMuServiceChooseEntity(xiangMuServiceChooseEntity);
                    }
                }
            }
        }
    }

    @Override
    public void removeByCzId(Integer[] czIds) {
        for (Integer czId : czIds) {
            anZhuangTiaoShiXiangMuServiceChooseDao.deleteByCzId(czId);
        }
    }

    @Override
    public void editByCzId(Map<String, Object> cheZhanData, String username, Integer senderid) {
        JSONObject jo = (JSONObject) JSONObject.toJSON(cheZhanData);
        Integer xdId = jo.getInteger("xdId");
        Integer czId = jo.getInteger("czId");
        this.removeByCzId(new Integer[]{czId});//删除所有的
        JSONArray ja = jo.getJSONArray("statusdata");
        for (Object obj : ja) {
            JSONObject jsonObject = (JSONObject) obj;
            Integer typetime = jsonObject.getInteger("typetime");
            if (typetime == null) {
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (Object obj1 : jsonArray) {
                    JSONObject a = (JSONObject) obj1;
                    AnZhuangTiaoShiXiangMuServiceChooseEntity muchSelect = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                    muchSelect.setCreatename(username);
                    muchSelect.setCreatetime(new Date());
                    muchSelect.setUpdatename(username);
                    muchSelect.setUpdatetime(new Date());
                    muchSelect.setXdid(xdId);
                    muchSelect.setCzid(czId);
                    muchSelect.setSerid(a.getInteger("serid"));
                    muchSelect.setChoid(a.getInteger("choid"));
                    muchSelect.setChezhanname(a.getString("chezhanname"));
                    muchSelect.setIsnot(a.getBoolean("isnot") ? 1 : 0);
                    this.addXiangMuServiceChooseEntity(muchSelect);
                }
            } else {
                AnZhuangTiaoShiXiangMuServiceChooseEntity oneSelect = new AnZhuangTiaoShiXiangMuServiceChooseEntity();
                oneSelect.setCreatename(username);
                oneSelect.setCreatetime(new Date());
                oneSelect.setUpdatename(username);
                oneSelect.setUpdatetime(new Date());
                oneSelect.setXdid(xdId);
                oneSelect.setCzid(czId);
                oneSelect.setSerid(jsonObject.getInteger("serid"));
                oneSelect.setChezhanname(jsonObject.getString("chezhanname"));
                oneSelect.setTypetime(jsonObject.getInteger("typetime"));
                oneSelect.setIsNotFinish(jsonObject.getBoolean("isNotFinish") ? 1 : 0);
                oneSelect.setPlanStartTime(jsonObject.getDate("planStartTime"));
                oneSelect.setPlanEndTime(jsonObject.getDate("planEndTime"));
                Date actualStartTime = jsonObject.getDate("actualStartTime");
                Date actualEndTime = jsonObject.getDate("actualEndTime");
                if (actualStartTime != null && actualEndTime != null)
                    oneSelect.setIsNotFinish(1);
                oneSelect.setActualStartTime(actualStartTime);
                oneSelect.setActualEndTime(actualEndTime);
                oneSelect.setPlanOpenTime(jsonObject.getDate("planOpenTime"));
                Date actualOpenTime = jsonObject.getDate("actualOpenTime");
                if (actualOpenTime != null) {
                    oneSelect.setIsNotFinish(1);
                }
                oneSelect.setActualOpenTime(actualOpenTime);
                this.addXiangMuServiceChooseEntity(oneSelect);
            }

        }
    }


    @Override
    public JSONObject findAllByXdId(Integer pageNumber, Integer pageSize, Integer xdId, String czName) {
        JSONObject titleAndData = new JSONObject();

        List<AnZhuangTiaoShiXiangMuServiceStatusEntity> anZhuangTiaoShiXiangMuServiceStatusEntities = anZhuangTiaoShiXiangMuServiceStatusDao.findAllServiceStatus();
        titleAndData.put("title", anZhuangTiaoShiXiangMuServiceStatusEntities);
        List<AnZhuangTiaoShiXiangMuEntity> anZhuangTiaoShiXiangMuEntities = anZhuangTiaoShiXiangMuDao.findByXdId(xdId);
        AnZhuangTiaoShiXiangMuEntity anZhuangTiaoShiXiangMuEntity = anZhuangTiaoShiXiangMuEntities.get(0);

        Page<Object> page = PageHelper.startPage(pageNumber, pageSize, "axsc.id DESC");
        List<Integer> czIds = anZhuangTiaoShiXiangMuServiceChooseDao.findCZidByXDid(xdId, czName);
        JSONArray ja = new JSONArray();
        for (Integer czId : czIds) {
            JSONObject jo = new JSONObject(true);
            jo.put("xdId", xdId);
            jo.put("czId", czId);
            jo.put("czName", cheZhanService.findByCheZhanId(czId.longValue()).getCzName());
            jo.put("xdType", anZhuangTiaoShiXiangMuEntity.getXdType());
            for (AnZhuangTiaoShiXiangMuServiceStatusEntity anZhuangTiaoShiXiangMuServiceStatusEntity : anZhuangTiaoShiXiangMuServiceStatusEntities) {
                Integer serid = anZhuangTiaoShiXiangMuServiceStatusEntity.getId();
                Integer timetype = anZhuangTiaoShiXiangMuServiceStatusEntity.getTimetype();
                List<AnZhuangTiaoShiXiangMuServiceChooseEntity> anZhuangTiaoShiXiangMuServiceChooseEntities = anZhuangTiaoShiXiangMuServiceChooseDao.findServiceChoose(serid, xdId, czId);
                JSONArray jsonArray = new JSONArray();
                if (timetype == null) { //多选
                    JSONObject jsonObject = new JSONObject(true);
                    jsonObject.put("xdid", xdId);
                    jsonObject.put("servicename", anZhuangTiaoShiXiangMuServiceStatusEntity.getServicename());
                    if (anZhuangTiaoShiXiangMuServiceChooseEntities.isEmpty())
                        jsonObject.put("list", anZhuangTiaoShiXiangMuServiceChooseEntities);
                    else {
                        List<AnZhuangTiaoShiXiangMuServiceStatusChooseEntity> anZhuangTiaoShiXiangMuServiceStatusChooseEntities = anZhuangTiaoShiXiangMuServiceStatusChooseDao.findOneChooseBySidid(serid);
                        JSONArray jsonArray1 = (JSONArray) JSONArray.toJSON(anZhuangTiaoShiXiangMuServiceStatusChooseEntities);
                        for (Object o : jsonArray1) {
                            JSONObject j = (JSONObject) o;
                            j.put("isNotChoose", false);
                            for (AnZhuangTiaoShiXiangMuServiceChooseEntity anZhuangTiaoShiXiangMuServiceChooseEntity : anZhuangTiaoShiXiangMuServiceChooseEntities) {
                                Integer id = j.getInteger("id");
                                Integer choid = anZhuangTiaoShiXiangMuServiceChooseEntity.getChoid();
                                Integer isNot = anZhuangTiaoShiXiangMuServiceChooseEntity.getIsnot();
                                if (id.equals(choid) && isNot != null && isNot == 1) {
                                    j.put("isNotChoose", true);
                                    break;
                                }
                            }
                        }
                        jsonObject.put("list", jsonArray1);
                    }
                    jsonArray.add(jsonObject);
                } else {  //单选
                    JSONObject oneSelect;
                    if (anZhuangTiaoShiXiangMuServiceChooseEntities.isEmpty())
                        oneSelect = new JSONObject();
                    else {
                        AnZhuangTiaoShiXiangMuServiceChooseEntity anZhuangTiaoShiXiangMuServiceChooseEntity = anZhuangTiaoShiXiangMuServiceChooseEntities.get(0);
                        Date planStartTime = anZhuangTiaoShiXiangMuServiceChooseEntity.getPlanStartTime();
                        Date planEndTime = anZhuangTiaoShiXiangMuServiceChooseEntity.getPlanEndTime();
                        if (planStartTime != null && planEndTime != null) {
                            int PlanToalTime = (int) ((planEndTime.getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                            int PlanOneTime = (int) ((new Date().getTime() - planStartTime.getTime()) / (1000 * 3600 * 24)) + 1;
                            anZhuangTiaoShiXiangMuServiceChooseEntity.setPlanToalTime(PlanToalTime);
                            anZhuangTiaoShiXiangMuServiceChooseEntity.setPlanOneTime(PlanOneTime);
                        }
                        oneSelect = (JSONObject) JSONObject.toJSON(anZhuangTiaoShiXiangMuServiceChooseEntity);
                    }
                    jsonArray.add(oneSelect);
                }
                jo.put(serid.toString(), jsonArray);
            }
            ja.add(jo);
        }

        page.clear();
        page.addAll(ja);
        PageInfo<Object> pageInfo = new PageInfo<>(page);
        titleAndData.put("tableData", pageInfo);
        return titleAndData;
    }


}
