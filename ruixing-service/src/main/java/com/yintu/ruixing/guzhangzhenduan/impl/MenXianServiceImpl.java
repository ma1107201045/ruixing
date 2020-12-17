package com.yintu.ruixing.guzhangzhenduan.impl;

import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.exception.BaseRuntimeException;
import com.yintu.ruixing.common.util.FileUtil;
import com.yintu.ruixing.common.util.ImportExcelUtil;
import com.yintu.ruixing.common.util.TreeNodeUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.master.guzhangzhenduan.MenXianDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:mlf
 * @date:2020/6/12 11:54
 */
@Service
@Transactional
public class MenXianServiceImpl implements MenXianService {
    @Autowired
    private MenXianDao menXianDao;
    @Autowired
    private QuDuanBaseService quDuanBaseService;
    @Autowired
    private QuDuanInfoPropertyService quDuanInfoPropertyService;
    @Autowired
    private QuDuanInfoServiceimpl quDuanInfoService;


    @Override
    public void add(MenXianEntity menXianEntity) {
        menXianDao.insertSelective(menXianEntity);
    }

    @Override
    public void remove(Integer id) {
        menXianDao.deleteByPrimaryKey(id);
    }

    @Override
    public void edit(MenXianEntity menXianEntity) {
        menXianDao.updateByPrimaryKeySelective(menXianEntity);
    }

    @Override
    public MenXianEntity findById(Integer id) {
        return menXianDao.selectByPrimaryKey(id);
    }

    @Override
    public List<MenXianEntity> findByCzIdAndQuduanIdAndPropertyId(Integer czId, Integer quanduanId, Integer propertyId) {
        return menXianDao.selectByCzIdAndQuduanIdAndPropertyId(czId, quanduanId, propertyId);
    }


    @Override
    public void removeBatch(List<MenXianEntity> menXianEntities) {
        menXianDao.deleteBatch(menXianEntities);
    }

    @Override
    public void addBatch(List<MenXianEntity> menXianEntities) {
        menXianDao.insertBatch(menXianEntities);
    }

    public boolean idIsInArr(int i) {
        int[] ids = new int[]{9, 10, 20, 21, 22, 23};
        for (int id : ids) {
            if (id == i)
                return true;
        }
        return false;
    }

    @Override
    public List<TreeNodeUtil> findPropertiesTree(Integer czId) {
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoService.findPropertiesByCzId(czId);
        Set<Integer> types = new HashSet<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            Short type = quDuanInfoPropertyEntity.getType();
            if (type != null && type != 1000)
                types.add(type.intValue());
        }

        List<TreeNodeUtil> treeNodeUtils = quDuanInfoService.findByTypess(types);
        for (TreeNodeUtil treeNodeUtil : treeNodeUtils) {
            List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities1 = quDuanInfoPropertyService.findByType(treeNodeUtil.getId().shortValue());
            List<TreeNodeUtil> trees = new ArrayList<>();
            for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities1) {
                if (!this.idIsInArr(quDuanInfoPropertyEntity.getId())) {
                    TreeNodeUtil tree = new TreeNodeUtil();
                    tree.setId(quDuanInfoPropertyEntity.getId().longValue());
                    tree.setLabel(quDuanInfoPropertyEntity.getName());
                    trees.add(tree);
                }
            }
            treeNodeUtil.setChildren(trees);
        }
        return treeNodeUtils;
    }


    @Override
    public JSONObject findByCzIdAndProperties(Integer czId, Integer[] properties) {
        JSONObject jo = new JSONObject();
        if (properties == null || properties.length == 0) {
            jo.put("title", new JSONArray());
            jo.put("data", new JSONArray());
            return jo;
        }
        //表头数组
        List<QuDuanInfoPropertyEntity> quDuanInfoPropertyEntities = quDuanInfoPropertyService.findByIds(properties);
        List<TreeNodeUtil> first = new ArrayList<>();
        for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
            List<TreeNodeUtil> second = new ArrayList<>();

            TreeNodeUtil treeNodeUtil = new TreeNodeUtil();
            treeNodeUtil.setLabel(quDuanInfoPropertyEntity.getName());
            treeNodeUtil.setChildren(second);

            switch (quDuanInfoPropertyEntity.getId()) {
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    List<TreeNodeUtil> third8 = new ArrayList<>();
                    List<TreeNodeUtil> third81 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil18 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil28 = new TreeNodeUtil();
                    treeNodeUtil18.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil18.setChildren(third8);
                    treeNodeUtil28.setLabel("备机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil28.setChildren(third81);

                    TreeNodeUtil a8 = new TreeNodeUtil();
                    a8.setLabel("量程下限");
                    a8.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b8 = new TreeNodeUtil();
                    b8.setLabel("量程上限");
                    b8.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil c8 = new TreeNodeUtil();
                    c8.setLabel("报警下限");
                    c8.setValue("c" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d8 = new TreeNodeUtil();
                    d8.setLabel("报警上限");
                    d8.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third8.add(a8);
                    third8.add(b8);
                    third8.add(c8);
                    third8.add(d8);

                    TreeNodeUtil a81 = new TreeNodeUtil();
                    a81.setLabel("量程下限");
                    a81.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b81 = new TreeNodeUtil();
                    b81.setLabel("量程上限");
                    b81.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil c81 = new TreeNodeUtil();
                    c81.setLabel("报警下限");
                    c81.setValue("c" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil d81 = new TreeNodeUtil();
                    d81.setLabel("报警上限");
                    d81.setValue("f" + quDuanInfoPropertyEntity.getId() + "1");
                    third81.add(a81);
                    third81.add(b81);
                    third81.add(c81);
                    third81.add(d81);
                    second.add(treeNodeUtil18);
                    second.add(treeNodeUtil28);
                    break;
                case 11:
                case 12:
                case 25://
                case 26:
                case 27:
                case 28:
                case 29://
                case 43:
                    List<TreeNodeUtil> third12 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil12 = new TreeNodeUtil();
                    treeNodeUtil12.setLabel(quDuanInfoPropertyEntity.getName());
                    treeNodeUtil12.setChildren(third12);
                    TreeNodeUtil a12 = new TreeNodeUtil();
                    a12.setLabel("量程下限");
                    a12.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b12 = new TreeNodeUtil();
                    b12.setLabel("量程上限");
                    b12.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil c12 = new TreeNodeUtil();
                    c12.setLabel("报警下限");
                    c12.setValue("c" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil f12 = new TreeNodeUtil();
                    f12.setLabel("报警上限");
                    f12.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third12.add(a12);
                    third12.add(b12);
                    third12.add(c12);
                    third12.add(f12);
                    second.add(treeNodeUtil12);
                    break;
                case 13:
                case 14:
                case 15:
                case 16:
                    List<TreeNodeUtil> third16 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil16 = new TreeNodeUtil();
                    treeNodeUtil16.setLabel(quDuanInfoPropertyEntity.getName());
                    treeNodeUtil16.setChildren(third16);
                    TreeNodeUtil a15 = new TreeNodeUtil();
                    a15.setLabel("量程下限");
                    a15.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b15 = new TreeNodeUtil();
                    b15.setLabel("量程上限");
                    b15.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d15 = new TreeNodeUtil();
                    d15.setLabel("报警下限(空闲)");
                    d15.setValue("d" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil e15 = new TreeNodeUtil();
                    e15.setLabel("报警下限(占用)");
                    e15.setValue("e" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil f15 = new TreeNodeUtil();
                    f15.setLabel("报警上限");
                    f15.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third16.add(a15);
                    third16.add(b15);
                    third16.add(d15);
                    third16.add(f15);
                    second.add(treeNodeUtil16);
                    break;
                case 17:
                    List<TreeNodeUtil> third17 = new ArrayList<>();
                    List<TreeNodeUtil> third171 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil117 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil217 = new TreeNodeUtil();
                    treeNodeUtil117.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil117.setChildren(third17);
                    treeNodeUtil217.setLabel("并机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil217.setChildren(third171);

                    TreeNodeUtil a17 = new TreeNodeUtil();
                    a17.setLabel("量程下限");
                    a17.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b17 = new TreeNodeUtil();
                    b17.setLabel("量程上限");
                    b17.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil c17 = new TreeNodeUtil();
                    c17.setLabel("报警下限");
                    c17.setValue("c" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil g17 = new TreeNodeUtil();
                    g17.setLabel("报警上限(占用)");
                    g17.setValue("g" + quDuanInfoPropertyEntity.getId());//a b c d e f g h
                    TreeNodeUtil h17 = new TreeNodeUtil();
                    h17.setLabel("报警上限(空闲)");
                    h17.setValue("h" + quDuanInfoPropertyEntity.getId());
                    third17.add(a17);
                    third17.add(b17);
                    third17.add(c17);
                    third17.add(g17);
                    third17.add(h17);

                    TreeNodeUtil a171 = new TreeNodeUtil();
                    a171.setLabel("量程下限");
                    a171.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b171 = new TreeNodeUtil();
                    b171.setLabel("量程上限");
                    b171.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil c171 = new TreeNodeUtil();
                    c171.setLabel("报警下限");
                    c171.setValue("c" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil g171 = new TreeNodeUtil();
                    g171.setLabel("报警上限(占用)");
                    g171.setValue("g" + quDuanInfoPropertyEntity.getId() + "1");//a b c d e f g h
                    TreeNodeUtil h171 = new TreeNodeUtil();
                    h171.setLabel("报警上限(空闲)");
                    h171.setValue("h" + quDuanInfoPropertyEntity.getId() + "1");
                    third171.add(a171);
                    third171.add(b171);
                    third171.add(c171);
                    third171.add(g171);
                    third171.add(h171);
                    second.add(treeNodeUtil117);
                    second.add(treeNodeUtil217);
                    break;
                case 18:
                    List<TreeNodeUtil> third18 = new ArrayList<>();
                    List<TreeNodeUtil> third181 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil118 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil218 = new TreeNodeUtil();
                    treeNodeUtil118.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil118.setChildren(third18);
                    treeNodeUtil218.setLabel("并机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil218.setChildren(third181);
                    TreeNodeUtil a18 = new TreeNodeUtil();
                    a18.setLabel("量程下限");
                    a18.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b18 = new TreeNodeUtil();
                    b18.setLabel("量程上限");
                    b18.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d18 = new TreeNodeUtil();
                    d18.setLabel("报警下限(空闲)");
                    d18.setValue("d" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil e18 = new TreeNodeUtil();
                    e18.setLabel("报警下限(占用)");
                    e18.setValue("e" + quDuanInfoPropertyEntity.getId());//a b c d e f g h
                    TreeNodeUtil g18 = new TreeNodeUtil();
                    g18.setLabel("报警上限(空闲)");
                    g18.setValue("g" + quDuanInfoPropertyEntity.getId());
                    third18.add(a18);
                    third18.add(b18);
                    third18.add(d18);
                    third18.add(e18);
                    third18.add(g18);

                    TreeNodeUtil a181 = new TreeNodeUtil();
                    a181.setLabel("量程下限");
                    a181.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b181 = new TreeNodeUtil();
                    b181.setLabel("量程上限");
                    b181.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil d181 = new TreeNodeUtil();
                    d181.setLabel("报警下限(空闲)");
                    d181.setValue("d" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil e181 = new TreeNodeUtil();
                    e181.setLabel("报警下限(占用)");
                    e181.setValue("e" + quDuanInfoPropertyEntity.getId() + "1");//a b c d e f g h
                    TreeNodeUtil g181 = new TreeNodeUtil();
                    g181.setLabel("报警上限(空闲)");
                    g181.setValue("g" + quDuanInfoPropertyEntity.getId() + "1");
                    third181.add(a181);
                    third181.add(b181);
                    third181.add(d181);
                    third181.add(e181);
                    third181.add(g181);
                    second.add(treeNodeUtil118);
                    second.add(treeNodeUtil218);
                    break;
                case 19:
                    List<TreeNodeUtil> third19 = new ArrayList<>();
                    List<TreeNodeUtil> third191 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil119 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil219 = new TreeNodeUtil();
                    treeNodeUtil119.setLabel("主机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil119.setChildren(third19);
                    treeNodeUtil219.setLabel("并机" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil219.setChildren(third191);
                    TreeNodeUtil a19 = new TreeNodeUtil();
                    a19.setLabel("量程下限");
                    a19.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b19 = new TreeNodeUtil();
                    b19.setLabel("量程上限");
                    b19.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d19 = new TreeNodeUtil();
                    d19.setLabel("报警下限(空闲)");
                    d19.setValue("d" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil e19 = new TreeNodeUtil();
                    e19.setLabel("报警下限(占用)");
                    e19.setValue("e" + quDuanInfoPropertyEntity.getId());//a b c d e f g h
                    TreeNodeUtil f18 = new TreeNodeUtil();
                    f18.setLabel("报警上限");
                    f18.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third19.add(a19);
                    third19.add(b19);
                    third19.add(d19);
                    third19.add(e19);
                    third19.add(f18);

                    TreeNodeUtil a191 = new TreeNodeUtil();
                    a191.setLabel("量程下限");
                    a191.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b191 = new TreeNodeUtil();
                    b191.setLabel("量程上限");
                    b191.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil d191 = new TreeNodeUtil();
                    d191.setLabel("报警下限(空闲)");
                    d191.setValue("d" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil e191 = new TreeNodeUtil();
                    e191.setLabel("报警下限(占用)");
                    e191.setValue("e" + quDuanInfoPropertyEntity.getId() + "1");//a b c d e f g h
                    TreeNodeUtil f191 = new TreeNodeUtil();
                    f191.setLabel("报警上限");
                    f191.setValue("f" + quDuanInfoPropertyEntity.getId() + "1");
                    third191.add(a191);
                    third191.add(b191);
                    third191.add(d191);
                    third191.add(e191);
                    third191.add(f191);
                    second.add(treeNodeUtil119);
                    second.add(treeNodeUtil219);
                    break;
                case 24:
                    List<TreeNodeUtil> third24 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil124 = new TreeNodeUtil();
                    treeNodeUtil124.setLabel(quDuanInfoPropertyEntity.getName());
                    treeNodeUtil124.setChildren(third24);
                    TreeNodeUtil a24 = new TreeNodeUtil();
                    a24.setLabel("量程下限");
                    a24.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b24 = new TreeNodeUtil();
                    b24.setLabel("量程上限");
                    b24.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d24 = new TreeNodeUtil();
                    d24.setLabel("报警下限(空闲)");
                    d24.setValue("d" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil f24 = new TreeNodeUtil();
                    f24.setLabel("报警上限");
                    f24.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third24.add(a24);
                    third24.add(b24);
                    third24.add(d24);
                    third24.add(f24);
                    second.add(treeNodeUtil124);
                    break;
//                    List<TreeNodeUtil> third29 = new ArrayList<>();
//                    TreeNodeUtil treeNodeUtil129 = new TreeNodeUtil();
//                    treeNodeUtil129.setLabel(quDuanInfoPropertyEntity.getName());
//                    treeNodeUtil129.setChildren(third29);
//                    TreeNodeUtil a29 = new TreeNodeUtil();
//                    a29.setLabel("量程下限");
//                    a29.setValue("a" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil b29 = new TreeNodeUtil();
//                    b29.setLabel("量程上限");
//                    b29.setValue("b" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil c29 = new TreeNodeUtil();
//                    c29.setLabel("报警下限");
//                    c29.setValue("c" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil f28 = new TreeNodeUtil();
//                    f28.setLabel("报警上限");
//                    f28.setValue("f" + quDuanInfoPropertyEntity.getId());
//                    third29.add(a29);
//                    third29.add(b29);
//                    third29.add(c29);
//                    third29.add(f28);
//                    second.add(treeNodeUtil129);
//                    break;
                case 30:
                case 31:
                case 32:
                case 33:
                    List<TreeNodeUtil> third33 = new ArrayList<>();
                    List<TreeNodeUtil> third331 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil133 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil233 = new TreeNodeUtil();
                    treeNodeUtil133.setLabel("主信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil133.setChildren(third33);
                    treeNodeUtil233.setLabel("调信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil233.setChildren(third331);

                    TreeNodeUtil a33 = new TreeNodeUtil();
                    a33.setLabel("量程下限");
                    a33.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b33 = new TreeNodeUtil();
                    b33.setLabel("量程上限");
                    b33.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil c33 = new TreeNodeUtil();
                    c33.setLabel("报警下限");
                    c33.setValue("c" + quDuanInfoPropertyEntity.getId());//a b c d e f g h
                    TreeNodeUtil f33 = new TreeNodeUtil();
                    f33.setLabel("报警上限");
                    f33.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third33.add(a33);
                    third33.add(b33);
                    third33.add(c33);
                    third33.add(f33);

                    TreeNodeUtil a331 = new TreeNodeUtil();
                    a331.setLabel("量程下限");
                    a331.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b331 = new TreeNodeUtil();
                    b331.setLabel("量程上限");
                    b331.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil c331 = new TreeNodeUtil();
                    c331.setLabel("报警下限");
                    c331.setValue("c" + quDuanInfoPropertyEntity.getId() + "1");//a b c d e f g h
                    TreeNodeUtil f331 = new TreeNodeUtil();
                    f331.setLabel("报警上限");
                    f331.setValue("f" + quDuanInfoPropertyEntity.getId() + "1");
                    third331.add(a331);
                    third331.add(b331);
                    third331.add(c331);
                    third331.add(f331);

                    second.add(treeNodeUtil133);
                    second.add(treeNodeUtil233);
                    break;
                case 34:
                case 35:
                case 36:
                case 37:
                    List<TreeNodeUtil> third37 = new ArrayList<>();
                    List<TreeNodeUtil> third371 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil137 = new TreeNodeUtil();
                    TreeNodeUtil treeNodeUtil237 = new TreeNodeUtil();
                    treeNodeUtil137.setLabel("主信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil137.setChildren(third37);
                    treeNodeUtil237.setLabel("调信号" + quDuanInfoPropertyEntity.getName());
                    treeNodeUtil237.setChildren(third371);

                    TreeNodeUtil a37 = new TreeNodeUtil();
                    a37.setLabel("量程下限");
                    a37.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b37 = new TreeNodeUtil();
                    b37.setLabel("量程上限");
                    b37.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d37 = new TreeNodeUtil();
                    d37.setLabel("报警下限(空闲)");
                    d37.setValue("d" + quDuanInfoPropertyEntity.getId());//a b c d e f g h
                    TreeNodeUtil e37 = new TreeNodeUtil();
                    e37.setLabel("报警下限(占用)");
                    e37.setValue("e" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil f37 = new TreeNodeUtil();
                    f37.setLabel("报警上限");
                    f37.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third37.add(a37);
                    third37.add(b37);
                    third37.add(d37);
                    third37.add(e37);
                    third37.add(f37);

                    TreeNodeUtil a371 = new TreeNodeUtil();
                    a371.setLabel("量程下限");
                    a371.setValue("a" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil b371 = new TreeNodeUtil();
                    b371.setLabel("量程上限");
                    b371.setValue("b" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil d371 = new TreeNodeUtil();
                    d371.setLabel("报警下限(空闲)");
                    d371.setValue("d" + quDuanInfoPropertyEntity.getId() + "1");//a b c d e f g h
                    TreeNodeUtil e371 = new TreeNodeUtil();
                    e371.setLabel("报警下限(占用)");
                    e371.setValue("e" + quDuanInfoPropertyEntity.getId() + "1");
                    TreeNodeUtil f371 = new TreeNodeUtil();
                    f371.setLabel("报警上限");
                    f371.setValue("f" + quDuanInfoPropertyEntity.getId() + "1");
                    third371.add(a371);
                    third371.add(b371);
                    third371.add(d371);
                    third371.add(e371);
                    third371.add(f371);
                    second.add(treeNodeUtil137);
                    second.add(treeNodeUtil237);
                    break;
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                    List<TreeNodeUtil> third42 = new ArrayList<>();
                    TreeNodeUtil treeNodeUtil142 = new TreeNodeUtil();
                    treeNodeUtil142.setLabel(quDuanInfoPropertyEntity.getName());
                    treeNodeUtil142.setChildren(third42);
                    TreeNodeUtil a42 = new TreeNodeUtil();
                    a42.setLabel("量程下限");
                    a42.setValue("a" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil b42 = new TreeNodeUtil();
                    b42.setLabel("量程上限");
                    b42.setValue("b" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil d42 = new TreeNodeUtil();
                    d42.setLabel("报警下限(空闲)");
                    d42.setValue("d" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil e42 = new TreeNodeUtil();
                    e42.setLabel("报警下限(占用)");
                    e42.setValue("e" + quDuanInfoPropertyEntity.getId());
                    TreeNodeUtil f42 = new TreeNodeUtil();
                    f42.setLabel("报警上限");
                    f42.setValue("f" + quDuanInfoPropertyEntity.getId());
                    third42.add(a42);
                    third42.add(b42);
                    third42.add(d42);
                    third42.add(e42);
                    third42.add(f42);
                    second.add(treeNodeUtil142);
                    break;

            }
            first.add(treeNodeUtil);
        }
        jo.put("title", first);

        //表头对应数据数组
        List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(czId, null, null, null);
        List<JSONObject> list = new ArrayList<>();
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {
            List<MenXianEntity> menXianEntities = this.findByCzIdAndQuduanIdAndPropertyId(czId, quDuanBaseEntity.getQdid(), null);
            JSONObject jos = new JSONObject();
            jos.put("quDuanYunYingName", quDuanBaseEntity.getQuduanyunyingName());
            for (QuDuanInfoPropertyEntity quDuanInfoPropertyEntity : quDuanInfoPropertyEntities) {
                Integer propertyId = quDuanInfoPropertyEntity.getId();
                List<MenXianEntity> result = menXianEntities.stream().filter(menXianEntity -> propertyId.equals(menXianEntity.getPropertyId())).collect(Collectors.toList());
                switch (propertyId) {
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        MenXianEntity menXianEntity81 = result.get(0);
                        MenXianEntity menXianEntity82 = result.get(1);
                        jos.put("a" + propertyId, menXianEntity81.getLcLower());
                        jos.put("b" + propertyId, menXianEntity81.getLcSuperior());
                        jos.put("c" + propertyId, menXianEntity81.getAlarmLower());
                        jos.put("f" + propertyId, menXianEntity81.getAlarmSuperior());
                        jos.put("a" + propertyId + "1", menXianEntity82.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity82.getLcSuperior());
                        jos.put("c" + propertyId + "1", menXianEntity82.getAlarmLower());
                        jos.put("f" + propertyId + "1", menXianEntity82.getAlarmSuperior());
                        break;
                    case 11:
                    case 12:
                    case 25://
                    case 26:
                    case 27:
                    case 28:
                    case 29://
                    case 43:
                        MenXianEntity menXianEntity43 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity43.getLcLower());
                        jos.put("b" + propertyId, menXianEntity43.getLcSuperior());
                        jos.put("c" + propertyId, menXianEntity43.getAlarmLower());
                        jos.put("f" + propertyId, menXianEntity43.getAlarmSuperior());
                        break;
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        MenXianEntity menXianEntity16 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity16.getLcLower());
                        jos.put("b" + propertyId, menXianEntity16.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity16.getAlarmLowerK());
                        jos.put("e" + propertyId, menXianEntity16.getAlarmLowerZ());
                        jos.put("f" + propertyId, menXianEntity16.getAlarmSuperior());
                        break;
                    case 17:
                        MenXianEntity menXianEntity171 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity171.getLcLower());
                        jos.put("b" + propertyId, menXianEntity171.getLcSuperior());
                        jos.put("c" + propertyId, menXianEntity171.getAlarmLower());
                        jos.put("h" + propertyId, menXianEntity171.getAlarmSuperiorZ());
                        jos.put("g" + propertyId, menXianEntity171.getAlarmSuperiorK());

                        MenXianEntity menXianEntity172 = result.get(1);
                        jos.put("a" + propertyId + "1", menXianEntity172.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity172.getLcSuperior());
                        jos.put("c" + propertyId + "1", menXianEntity172.getAlarmLower());
                        jos.put("h" + propertyId + "1", menXianEntity172.getAlarmSuperiorZ());
                        jos.put("g" + propertyId + "1", menXianEntity172.getAlarmSuperiorK());
                        break;
                    case 18:
                        MenXianEntity menXianEntity181 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity181.getLcLower());
                        jos.put("b" + propertyId, menXianEntity181.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity181.getAlarmLowerK());
                        jos.put("e" + propertyId, menXianEntity181.getAlarmLowerZ());
                        jos.put("g" + propertyId, menXianEntity181.getAlarmSuperiorK());

                        MenXianEntity menXianEntity182 = result.get(1);
                        jos.put("a" + propertyId + "1", menXianEntity182.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity182.getLcSuperior());
                        jos.put("d" + propertyId + "1", menXianEntity182.getAlarmLowerK());
                        jos.put("e" + propertyId + "1", menXianEntity182.getAlarmLowerZ());
                        jos.put("g" + propertyId + "1", menXianEntity182.getAlarmSuperiorK());
                        break;
                    case 19:
                        MenXianEntity menXianEntity191 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity191.getLcLower());
                        jos.put("b" + propertyId, menXianEntity191.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity191.getAlarmLowerK());
                        jos.put("e" + propertyId, menXianEntity191.getAlarmLowerZ());
                        jos.put("f" + propertyId, menXianEntity191.getAlarmSuperior());

                        MenXianEntity menXianEntity192 = result.get(1);
                        jos.put("a" + propertyId + "1", menXianEntity192.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity192.getLcSuperior());
                        jos.put("d" + propertyId + "1", menXianEntity192.getAlarmLowerK());
                        jos.put("e" + propertyId + "1", menXianEntity192.getAlarmLowerZ());
                        jos.put("f" + propertyId + "1", menXianEntity192.getAlarmSuperior());
                        break;
                    case 24:
                        MenXianEntity menXianEntity24 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity24.getLcLower());
                        jos.put("b" + propertyId, menXianEntity24.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity24.getAlarmLowerK());
                        jos.put("f" + propertyId, menXianEntity24.getAlarmSuperior());
                        break;
//                    List<TreeNodeUtil> third29 = new ArrayList<>();
//                    TreeNodeUtil treeNodeUtil129 = new TreeNodeUtil();
//                    treeNodeUtil129.setLabel(quDuanInfoPropertyEntity.getName());
//                    treeNodeUtil129.setChildren(third29);
//                    TreeNodeUtil a29 = new TreeNodeUtil();
//                    a29.setLabel("量程下限");
//                    a29.setValue("a" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil b29 = new TreeNodeUtil();
//                    b29.setLabel("量程上限");
//                    b29.setValue("b" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil c29 = new TreeNodeUtil();
//                    c29.setLabel("报警下限");
//                    c29.setValue("c" + quDuanInfoPropertyEntity.getId());
//                    TreeNodeUtil f28 = new TreeNodeUtil();
//                    f28.setLabel("报警上限");
//                    f28.setValue("f" + quDuanInfoPropertyEntity.getId());
//                    third29.add(a29);
//                    third29.add(b29);
//                    third29.add(c29);
//                    third29.add(f28);
//                    second.add(treeNodeUtil129);
//                    break;
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                        MenXianEntity menXianEntity331 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity331.getLcLower());
                        jos.put("b" + propertyId, menXianEntity331.getLcSuperior());
                        jos.put("c" + propertyId, menXianEntity331.getAlarmLower());
                        jos.put("f" + propertyId, menXianEntity331.getAlarmLower());

                        MenXianEntity menXianEntity332 = result.get(1);
                        jos.put("a" + propertyId + "1", menXianEntity332.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity332.getLcSuperior());
                        jos.put("c" + propertyId + "1", menXianEntity332.getAlarmLower());
                        jos.put("f" + propertyId + "1", menXianEntity332.getAlarmLower());

                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        MenXianEntity menXianEntity371 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity371.getLcLower());
                        jos.put("b" + propertyId, menXianEntity371.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity371.getAlarmLowerK());
                        jos.put("e" + propertyId, menXianEntity371.getAlarmLowerZ());
                        jos.put("f" + propertyId, menXianEntity371.getAlarmSuperior());
                        MenXianEntity menXianEntity372 = result.get(1);
                        jos.put("a" + propertyId + "1", menXianEntity372.getLcLower());
                        jos.put("b" + propertyId + "1", menXianEntity372.getLcSuperior());
                        jos.put("d" + propertyId + "1", menXianEntity372.getAlarmLowerK());
                        jos.put("e" + propertyId + "1", menXianEntity372.getAlarmLowerZ());
                        jos.put("f" + propertyId + "1", menXianEntity371.getAlarmSuperior());
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                        MenXianEntity menXianEntity42 = result.get(0);
                        jos.put("a" + propertyId, menXianEntity42.getLcLower());
                        jos.put("b" + propertyId, menXianEntity42.getLcSuperior());
                        jos.put("d" + propertyId, menXianEntity42.getAlarmLowerK());
                        jos.put("e" + propertyId, menXianEntity42.getAlarmLowerZ());
                        jos.put("f" + propertyId, menXianEntity42.getAlarmSuperior());
                }
            }
            list.add(jos);
        }
        jo.put("data", list);

        return jo;
    }


    @Override
    public String[][] importFile(InputStream inputStream, String fileName) throws IOException {
        //excel标题
        String[][] content;
        if ("xls".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getHSSFDatas(new HSSFWorkbook(inputStream));
        } else if ("xlsx".equals(FileUtil.getExtensionName(fileName))) {
            content = ImportExcelUtil.getXSSFDatas(new XSSFWorkbook(inputStream));
        } else {
            throw new BaseRuntimeException("文件格式有误");
        }
        return content;
    }

    @Override
    public void importData(String[][] context, String loginUserName) {
        List<MenXianEntity> menXianEntities = new ArrayList<>();
        for (String[] strings : context) {
            JSONArray jsonArray = (JSONArray) JSONArray.toJSON(strings);
            Integer cid = jsonArray.getInteger(0);
            Integer qid = jsonArray.getInteger(2);
            List<QuDuanBaseEntity> quDuanBaseEntities = quDuanBaseService.findByCzIdAndQdId(cid, qid, null, null);
            if (!quDuanBaseEntities.isEmpty()) {
                //（主机/备机）功出电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 4);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity4 = new MenXianEntity();
                    menXianEntity4.setCzId(cid);
                    menXianEntity4.setQuduanId(qid);
                    menXianEntity4.setPropertyId(4);
                    menXianEntity4.setLevel(1 + j);
                    menXianEntity4.setLcLower(jsonArray.getString(4));
                    menXianEntity4.setLcSuperior(jsonArray.getString(5));
                    menXianEntity4.setAlarmLower(jsonArray.getString(6));
                    menXianEntity4.setAlarmSuperior(jsonArray.getString(8));
                    menXianEntities.add(menXianEntity4);
                }
                //主机功出电流mA
                //备机功出电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 5);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity5 = new MenXianEntity();
                    menXianEntity5.setCzId(cid);
                    menXianEntity5.setQuduanId(qid);
                    menXianEntity5.setPropertyId(5);
                    menXianEntity5.setLevel(1 + j);
                    menXianEntity5.setLcLower(jsonArray.getString(9 + j * 5));
                    menXianEntity5.setLcSuperior(jsonArray.getString(10 + j * 5));
                    menXianEntity5.setAlarmLower(jsonArray.getString(11 + j * 5));
                    menXianEntity5.setAlarmSuperior(jsonArray.getString(13 + j * 5));
                    menXianEntities.add(menXianEntity5);
                }

                //（主机/备机）上边频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 6);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity6 = new MenXianEntity();
                    menXianEntity6.setCzId(cid);
                    menXianEntity6.setQuduanId(qid);
                    menXianEntity6.setPropertyId(6);
                    menXianEntity6.setLevel(1 + j);
                    menXianEntity6.setLcLower(jsonArray.getString(19));
                    menXianEntity6.setLcSuperior(jsonArray.getString(20));
                    menXianEntity6.setAlarmLower(jsonArray.getString(21));
                    menXianEntity6.setAlarmSuperior(jsonArray.getString(23));
                    menXianEntities.add(menXianEntity6);
                }
                //（主机/备机）下边频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 7);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity7 = new MenXianEntity();
                    menXianEntity7.setCzId(cid);
                    menXianEntity7.setQuduanId(qid);
                    menXianEntity7.setPropertyId(7);
                    menXianEntity7.setLevel(1 + j);
                    menXianEntity7.setLcLower(jsonArray.getString(24));
                    menXianEntity7.setLcSuperior(jsonArray.getString(25));
                    menXianEntity7.setAlarmLower(jsonArray.getString(26));
                    menXianEntity7.setAlarmSuperior(jsonArray.getString(28));
                    menXianEntities.add(menXianEntity7);
                }
                //（主机/备机）发送低频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 8);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity8 = new MenXianEntity();
                    menXianEntity8.setCzId(cid);
                    menXianEntity8.setQuduanId(qid);
                    menXianEntity8.setPropertyId(8);
                    menXianEntity8.setLevel(1 + j);
                    menXianEntity8.setLcLower(jsonArray.getString(29));
                    menXianEntity8.setLcSuperior(jsonArray.getString(30));
                    menXianEntity8.setAlarmLower(jsonArray.getString(31));
                    menXianEntity8.setAlarmSuperior(jsonArray.getString(33));
                    menXianEntities.add(menXianEntity8);
                }
                //送端电缆侧电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 11);
                MenXianEntity menXianEntity11 = new MenXianEntity();
                menXianEntity11.setCzId(cid);
                menXianEntity11.setQuduanId(qid);
                menXianEntity11.setPropertyId(11);
                menXianEntity11.setLevel(1);
                menXianEntity11.setLcLower(jsonArray.getString(34));
                menXianEntity11.setLcSuperior(jsonArray.getString(35));
                menXianEntity11.setAlarmLower(jsonArray.getString(36));
                menXianEntity11.setAlarmSuperior(jsonArray.getString(38));
                menXianEntities.add(menXianEntity11);


                //送端电缆侧电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 12);
                MenXianEntity menXianEntity12 = new MenXianEntity();
                menXianEntity12.setCzId(cid);
                menXianEntity12.setQuduanId(qid);
                menXianEntity12.setPropertyId(12);
                menXianEntity12.setLevel(1);
                menXianEntity12.setLcLower(jsonArray.getString(39));
                menXianEntity12.setLcSuperior(jsonArray.getString(40));
                menXianEntity12.setAlarmLower(jsonArray.getString(41));
                menXianEntity12.setAlarmSuperior(jsonArray.getString(43));
                menXianEntities.add(menXianEntity12);

                //受端电缆侧主电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 13);
                MenXianEntity menXianEntity13 = new MenXianEntity();
                menXianEntity13.setCzId(cid);
                menXianEntity13.setQuduanId(qid);
                menXianEntity13.setPropertyId(13);
                menXianEntity13.setLevel(1);
                menXianEntity13.setLcLower(jsonArray.getString(44));
                menXianEntity13.setLcSuperior(jsonArray.getString(45));
                menXianEntity13.setAlarmLowerK(jsonArray.getString(46));
                menXianEntity13.setAlarmLowerZ(jsonArray.getString(47));
                menXianEntity13.setAlarmSuperior(jsonArray.getString(48));
                menXianEntities.add(menXianEntity13);

                //受端电缆侧调电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 14);
                MenXianEntity menXianEntity14 = new MenXianEntity();
                menXianEntity14.setCzId(cid);
                menXianEntity14.setQuduanId(qid);
                menXianEntity14.setPropertyId(14);
                menXianEntity14.setLevel(1);
                menXianEntity14.setLcLower(jsonArray.getString(49));
                menXianEntity14.setLcSuperior(jsonArray.getString(50));
                menXianEntity14.setAlarmLowerK(jsonArray.getString(51));
                menXianEntity14.setAlarmLowerZ(jsonArray.getString(52));
                menXianEntity14.setAlarmSuperior(jsonArray.getString(53));
                menXianEntities.add(menXianEntity14);

                //受端电缆侧电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 15);
                MenXianEntity menXianEntity15 = new MenXianEntity();
                menXianEntity15.setCzId(cid);
                menXianEntity15.setQuduanId(qid);
                menXianEntity15.setPropertyId(15);
                menXianEntity15.setLevel(1);
                menXianEntity15.setLcLower(jsonArray.getString(54));
                menXianEntity15.setLcSuperior(jsonArray.getString(55));
                menXianEntity15.setAlarmLowerK(jsonArray.getString(56));
                menXianEntity15.setAlarmLowerZ(jsonArray.getString(57));
                menXianEntity15.setAlarmSuperior(jsonArray.getString(58));
                menXianEntities.add(menXianEntity15);


                //轨入电压V
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 16);
                MenXianEntity menXianEntity16 = new MenXianEntity();
                menXianEntity16.setCzId(cid);
                menXianEntity16.setQuduanId(qid);
                menXianEntity16.setPropertyId(16);
                menXianEntity16.setLevel(1);
                menXianEntity16.setLcLower(jsonArray.getString(59));
                menXianEntity16.setLcSuperior(jsonArray.getString(60));
                menXianEntity16.setAlarmLowerK(jsonArray.getString(61));
                menXianEntity16.setAlarmLowerZ(jsonArray.getString(62));
                menXianEntity16.setAlarmSuperior(jsonArray.getString(63));
                menXianEntities.add(menXianEntity16);


                //（主机/并机）主接入电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 17);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity17 = new MenXianEntity();
                    menXianEntity17.setCzId(cid);
                    menXianEntity17.setQuduanId(qid);
                    menXianEntity17.setPropertyId(17);
                    menXianEntity17.setLevel(1 + j);
                    menXianEntity17.setLcLower(jsonArray.getString(64));
                    menXianEntity17.setLcSuperior(jsonArray.getString(65));
                    menXianEntity17.setAlarmLower(jsonArray.getString(66));
                    menXianEntity17.setAlarmSuperiorK(jsonArray.getString(67));
                    menXianEntity17.setAlarmSuperiorZ(jsonArray.getString(68));
                    menXianEntities.add(menXianEntity17);
                }
                //（主机/并机）调接入电压mV
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 18);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity18 = new MenXianEntity();
                    menXianEntity18.setCzId(cid);
                    menXianEntity18.setQuduanId(qid);
                    menXianEntity18.setPropertyId(18);
                    menXianEntity18.setLevel(1 + j);
                    menXianEntity18.setLcLower(jsonArray.getString(69));
                    menXianEntity18.setLcSuperior(jsonArray.getString(70));
                    menXianEntity18.setAlarmLowerK(jsonArray.getString(71));
                    menXianEntity18.setAlarmLowerZ(jsonArray.getString(72));
                    menXianEntity18.setAlarmSuperiorK(jsonArray.getString(73));
                    menXianEntities.add(menXianEntity18);
                }
                //（主机/并机）接收低频Hz
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 19);
                for (int j = 0; j < 2; j++) {
                    MenXianEntity menXianEntity19 = new MenXianEntity();
                    menXianEntity19.setCzId(cid);
                    menXianEntity19.setQuduanId(qid);
                    menXianEntity19.setPropertyId(19);
                    menXianEntity19.setLevel(1 + j);
                    menXianEntity19.setLcLower(jsonArray.getString(74));
                    menXianEntity19.setLcSuperior(jsonArray.getString(75));
                    menXianEntity19.setAlarmLowerK(jsonArray.getString(76));
                    menXianEntity19.setAlarmLowerZ(jsonArray.getString(77));
                    menXianEntity19.setAlarmSuperior(jsonArray.getString(78));
                    menXianEntities.add(menXianEntity19);
                }


                //FBP电缆电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 24);
                MenXianEntity menXianEntity24 = new MenXianEntity();
                menXianEntity24.setCzId(cid);
                menXianEntity24.setQuduanId(qid);
                menXianEntity24.setPropertyId(24);
                menXianEntity24.setLevel(1);
                menXianEntity24.setLcLower(jsonArray.getString(79));
                menXianEntity24.setLcSuperior(jsonArray.getString(80));
                menXianEntity24.setAlarmLowerK(jsonArray.getString(81));
                menXianEntity24.setAlarmSuperior(jsonArray.getString(83));
                menXianEntities.add(menXianEntity24);

                //FBP电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 25);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 26);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 27);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 28);
                for (int j = 0; j < 4; j++) {
                    MenXianEntity menXianEntity25 = new MenXianEntity();
                    menXianEntity25.setCzId(cid);
                    menXianEntity25.setQuduanId(qid);
                    menXianEntity25.setPropertyId(25 + j);
                    menXianEntity25.setLevel(1);
                    menXianEntity25.setLcLower(jsonArray.getString(84));
                    menXianEntity25.setLcSuperior(jsonArray.getString(85));
                    menXianEntity25.setAlarmLower(jsonArray.getString(86));
                    menXianEntity25.setAlarmSuperior(jsonArray.getString(88));
                    menXianEntities.add(menXianEntity25);
                }
                //FBP温度℃
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 29);
                MenXianEntity menXianEntity29 = new MenXianEntity();
                menXianEntity29.setCzId(cid);
                menXianEntity29.setQuduanId(qid);
                menXianEntity29.setPropertyId(29);
                menXianEntity29.setLevel(1);
                menXianEntity29.setLcLower(jsonArray.getString(89));
                menXianEntity29.setLcSuperior(jsonArray.getString(90));
                menXianEntity29.setAlarmLower(jsonArray.getString(91));
                menXianEntity29.setAlarmSuperior(jsonArray.getString(93));
                menXianEntities.add(menXianEntity29);


                //主信号FBA电流A（长内/长外/短内/短外）
                //调信号FBA电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 30);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 31);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 32);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 33);
                for (int z = 0; z < 2; z++) {
                    for (int j = 0; j < 4; j++) {
                        MenXianEntity menXianEntity30 = new MenXianEntity();
                        menXianEntity30.setCzId(cid);
                        menXianEntity30.setQuduanId(qid);
                        menXianEntity30.setPropertyId(30 + j);
                        menXianEntity30.setLevel(1 + z);
                        menXianEntity30.setLcLower(jsonArray.getString(94 + z * 5));
                        menXianEntity30.setLcSuperior(jsonArray.getString(95 + z * 5));
                        menXianEntity30.setAlarmLower(jsonArray.getString(96 + z * 5));
                        menXianEntity30.setAlarmSuperior(jsonArray.getString(98 + z * 5));
                        menXianEntities.add(menXianEntity30);
                    }
                }

                //主信号JBA电流A（长内/长外/短内/短外）
                //调信号JBA电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 34);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 35);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 36);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 37);
                for (int z = 0; z < 2; z++) {
                    for (int j = 0; j < 4; j++) {
                        MenXianEntity menXianEntity34 = new MenXianEntity();
                        menXianEntity34.setCzId(cid);
                        menXianEntity34.setQuduanId(qid);
                        menXianEntity34.setPropertyId(34 + j);
                        menXianEntity34.setLevel(1 + z);
                        menXianEntity34.setLcLower(jsonArray.getString(104 + z * 5));
                        menXianEntity34.setLcSuperior(jsonArray.getString(105 + z * 5));
                        menXianEntity34.setAlarmLowerK(jsonArray.getString(106 + z * 5));
                        menXianEntity34.setAlarmLowerZ(jsonArray.getString(107 + z * 5));
                        menXianEntity34.setAlarmSuperior(jsonArray.getString(108 + z * 5));
                        menXianEntities.add(menXianEntity34);
                    }
                }


                //JBP电缆电流mA
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 38);
                MenXianEntity menXianEntity38 = new MenXianEntity();
                menXianEntity38.setCzId(cid);
                menXianEntity38.setQuduanId(qid);
                menXianEntity38.setPropertyId(38);
                menXianEntity38.setLevel(1);
                menXianEntity38.setLcLower(jsonArray.getString(114));
                menXianEntity38.setLcSuperior(jsonArray.getString(115));
                menXianEntity38.setAlarmLowerK(jsonArray.getString(116));
                menXianEntity38.setAlarmLowerZ(jsonArray.getString(117));
                menXianEntity38.setAlarmSuperior(jsonArray.getString(118));
                menXianEntities.add(menXianEntity38);

                //JBP电流A（长内/长外/短内/短外）
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 39);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 40);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 41);
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 42);
                for (int j = 0; j < 4; j++) {
                    MenXianEntity menXianEntity39 = new MenXianEntity();
                    menXianEntity39.setCzId(cid);
                    menXianEntity39.setQuduanId(qid);
                    menXianEntity39.setPropertyId(39 + j);
                    menXianEntity39.setLevel(1);
                    menXianEntity39.setLcLower(jsonArray.getString(119));
                    menXianEntity39.setLcSuperior(jsonArray.getString(120));
                    menXianEntity39.setAlarmLowerK(jsonArray.getString(121));
                    menXianEntity39.setAlarmLowerZ(jsonArray.getString(122));
                    menXianEntity39.setAlarmSuperior(jsonArray.getString(123));
                    menXianEntities.add(menXianEntity39);
                }

                //JBP温度℃
                //this.removeByCzIdAndQuduanIdAndPropertyId(cid, qid, 43);
                MenXianEntity menXianEntity43 = new MenXianEntity();
                menXianEntity43.setCzId(cid);
                menXianEntity43.setQuduanId(qid);
                menXianEntity43.setPropertyId(43);
                menXianEntity43.setLevel(1);
                menXianEntity43.setLcLower(jsonArray.getString(124));
                menXianEntity43.setLcSuperior(jsonArray.getString(125));
                menXianEntity43.setAlarmLower(jsonArray.getString(126));
                menXianEntity43.setAlarmSuperior(jsonArray.getString(128));
                menXianEntities.add(menXianEntity43);

            }
        }
        if (!menXianEntities.isEmpty()) {
            this.removeBatch(menXianEntities);
            this.addBatch(menXianEntities);
        }

    }


    @Override
    public void templateFile(OutputStream outputStream) throws IOException {
        //excel标题
        String title = "门限参数模板";
        File file = SystemUtil.getOsInfo().isWindows() ? cn.hutool.core.io.FileUtil.file("C:\\data\\ruixing\\templates\\" + title + ".xlsx")
                : cn.hutool.core.io.FileUtil.file("/data/ruixing/templates/" + title + ".xlsx");
        InputStream is = cn.hutool.core.io.FileUtil.getInputStream(file);
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b, 0, b.length)) != -1) {
            outputStream.write(b, 0, len);
        }
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public String findMinNumber(Integer czid, Integer qdid, Integer mid) {
        return menXianDao.findMinNumber(czid, qdid, mid);
    }

    @Override
    public String findMaxNumber(Integer czid, Integer qdid, Integer mid) {
        return menXianDao.findMaxNumber(czid, qdid, mid);
    }
}
