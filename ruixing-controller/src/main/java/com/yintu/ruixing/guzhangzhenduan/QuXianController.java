package com.yintu.ruixing.guzhangzhenduan;

import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.QuDuanBaseEntity;
import com.yintu.ruixing.guzhangzhenduan.QuDuanShuXingEntity;
import com.yintu.ruixing.guzhangzhenduan.quduanEntity;
import com.yintu.ruixing.guzhangzhenduan.QuXianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:lcy
 * @date:2020-06-11 17
 * 曲线相关
 */
@RestController
@RequestMapping("/quXianAll")
public class QuXianController {
    @Autowired
    private QuXianService quXianService;

    @Autowired
    private CheZhanService cheZhanService;

    //日报表   --> 待定
    //根据所选日期  获得对应的24个时间点  然后根据时间点和传来的的字段名字 来获取对应的数据
    @GetMapping("/findQuDuanDataByTime1")
    public Map<String, Object> findQuDuanDataByTime1(@RequestParam("time") Date time, @RequestParam("name") String name) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<QuDuanBaseEntity> quDuanBaseEntities = quXianService.findQuDuanDataByTime1(time); //根据传来的时间 获取查询出来的区段信息
        for (QuDuanBaseEntity quDuanBaseEntity : quDuanBaseEntities) {//遍历区段
            Date time1 = quDuanBaseEntity.getTime();//得到查询的时间
            // System.out.println(time1);
            String hh = new SimpleDateFormat("HH").format(time1);//把时间变成小时
            list.add(hh);//把时间变成小时  存到list里面
            Collections.sort(list);//排序时间  从小到大
            String format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(time1);//把时间转换格式
            Integer date = quXianService.findQuDuanDataByTime2(format, name);//根据时间查询区段的所有数据信息
            list1.add(date);
        }
        map.put("hours", list);
        map.put("shuzi", list1);
        return ResponseDataUtil.ok("查询数据成功", map);
    }

    //根据车站id   获取车站下 的所有区段
    @GetMapping("/findQuDuanById/{id}")
    public Map<String, Object> findQuDuanById(@PathVariable Integer id) {
        List<String> quDuanBaseEntities = quXianService.findQuDuanById(id);
        return ResponseDataUtil.ok("查询区段成功", quDuanBaseEntities);
    }

    //根据车站id   获取车站下的所有电码化区段
    @GetMapping("/findDMHQuDuanById/{id}")
    public Map<String, Object> findDMHQuDuanById(@PathVariable Integer id) {
        List<String> quDuanBaseEntities = quXianService.findDMHQuDuanById(id);
        return ResponseDataUtil.ok("查询区段成功", quDuanBaseEntities);
    }

    //获取区段的属性名
    @GetMapping("/shuXingMing")
    public Map<String, Object> shuXingMing() {
        List<QuDuanShuXingEntity> quDuanShuXingEntities = quXianService.shuXingMing();
        return ResponseDataUtil.ok("查询区段名成功", quDuanShuXingEntities);
    }


    //电码化日曲线
    //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findDMHQuDuanDayData")
    public Map<String, Object> findDMHQuDuanDayData(@RequestParam("dayTime") Date dayTime,
                                                    @RequestParam("shuxingId") Integer[] shuxingId,
                                                    @RequestParam("quduanName") String[] quduanName,
                                                    Integer czid) throws Exception {
        String tableName = StringUtil.getTableName(czid, dayTime);
        if (shuxingId.length == 0 || quduanName.length == 0 || dayTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();//48个时间集合
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();
            JSONObject js = new JSONObject();
            long statrtime = dayTime.getTime() / 1000;
            long endtime = statrtime + 86399;
            System.out.println("statrtimerrrrrrrrrrr" + statrtime);
            for (int i = 0; i < 86399; i++) {
                long onetime = statrtime + i;
                long value = onetime * 1000L;
                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                String time2 = format1.format(new Date(value));
                list.add(time2);
                timelist.add(onetime);
            }
            js.put("shijian", list);
            System.out.println("");
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            Integer k = 0;
            for (int i = 0; i < sqlname.size(); i++) {
                k++;
                System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                String shuxingname = sqlname.get(i);
                System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                String quduanname = quduanName[i];
                Integer qdid = quXianService.findQDid(quduanname,czid);
                List<quduanEntity> date = quXianService.findOneQuDuanDatas(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
                System.out.println("1234" + date);
                if (date.size() == timelist.size()) {
                    for (int i1 = 0; i1 < date.size(); i1++) {
                        if (k == 1) {
                            listt1.add(i1, date.get(i1).getName());
                        }
                        if (k == 2) {
                            listt2.add(i1, date.get(i1).getName());
                        }
                        if (k == 3) {
                            listt3.add(i1, date.get(i1).getName());
                        }
                        if (k == 4) {
                            listt4.add(i1, date.get(i1).getName());
                        }
                        if (k == 5) {
                            listt5.add(i1, date.get(i1).getName());
                        }
                    }
                } else {
                    Integer l = 0;
                    for (int i1 = 0; i1 < timelist.size(); i1++) {
                        Integer p = 0;
                        for (int i2 = 0; i2 < date.size(); i2++) {
                            if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                l = 0;
                                p++;
                            } else {
                                l = 1;
                                break;
                            }
                        }
                        if (k == 1) {
                            if (l == 0) {
                                listt1.add(i1, null);
                            } else {
                                listt1.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 2) {
                            if (l == 0) {
                                listt2.add(i1, null);
                            } else {
                                listt2.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 3) {
                            if (l == 0) {
                                listt3.add(i1, null);
                            } else {
                                listt3.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 4) {
                            if (l == 0) {
                                listt4.add(i1, null);
                            } else {
                                listt4.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 5) {
                            if (l == 0) {
                                listt5.add(i1, null);
                            } else {
                                listt5.add(i1, date.get(p).getName());
                            }
                        }
                    }
                }
                if (k == 1) {
                    js.put("shuju" + k.toString(), listt1);
                    System.out.println("shujasdaalda" + listt1);
                    js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                }
                if (k == 2) {
                    js.put("shuju" + k.toString(), listt2);
                    js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                }
                if (k == 3) {
                    js.put("shuju" + k.toString(), listt3);
                    js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                }
                if (k == 4) {
                    js.put("shuju" + k.toString(), listt4);
                    js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                }
                if (k == 5) {
                    js.put("shuju" + k.toString(), listt5);
                    js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                }
            }
            //System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }


    //电码化实时曲线
    @GetMapping("/findDMHQuDuanShiShiData")
    public Map<String, Object> findDMHQuDuanShiShiData(@RequestParam("shuxingId") Integer[] shuxingId,
                                                       @RequestParam("quduanName") String[] quduanName,
                                                       Integer czid, Integer times) throws Exception {
        Date today = new Date();
        String tableName = StringUtil.getTableName(czid, today);
        if (shuxingId.length == 0 || quduanName.length == 0) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            JSONObject js = new JSONObject();
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            long cZid = czid.longValue();
            if (cheZhanService.findCzStutrs(cZid, false)) {
                List aa=new ArrayList<>();
                JSONObject ss=new JSONObject();
                ss.put("data",aa);
                return ss;
            } else {
                if (times == 1) {
                    List<Long> timelist = new ArrayList<>();
                    long statrtime = today.getTime() / 1000;
                    long endtime = statrtime + 1800;
                    System.out.println("statrtimerrrrrrrrrrr" + statrtime);
                    for (int i = 0; i < 1800; i++) {
                        long onetime = statrtime + i;
                        long value = onetime * 1000L;
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                        String time2 = format1.format(new Date(value));
                        list.add(time2);
                        timelist.add(onetime);
                    }
                    Integer k = 0;
                    for (int i = 0; i < sqlname.size(); i++) {
                        k++;
                        System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                        String shuxingname = sqlname.get(i);
                        System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                        String quduanname = quduanName[i];
                        Integer qdid = quXianService.findQDid(quduanname,czid);
                        List<quduanEntity> date = quXianService.findQuDuanDayData(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
                        System.out.println("1234" + date);
                        if (date.size() == timelist.size()) {
                            for (int i1 = 0; i1 < date.size(); i1++) {
                                if (k == 1) {
                                    listt1.add(i1, date.get(i1).getName());
                                }
                                if (k == 2) {
                                    listt2.add(i1, date.get(i1).getName());
                                }
                                if (k == 3) {
                                    listt3.add(i1, date.get(i1).getName());
                                }
                                if (k == 4) {
                                    listt4.add(i1, date.get(i1).getName());
                                }
                                if (k == 5) {
                                    listt5.add(i1, date.get(i1).getName());
                                }
                            }
                        } else {
                            Integer l = 0;
                            for (int i1 = 0; i1 < timelist.size(); i1++) {
                                Integer p = 0;
                                for (int i2 = 0; i2 < date.size(); i2++) {
                                    if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                        l = 0;
                                        p++;
                                    } else {
                                        l = 1;
                                        break;
                                    }
                                }
                                if (k == 1) {
                                    if (l == 0) {
                                        listt1.add(i1, null);
                                    } else {
                                        listt1.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 2) {
                                    if (l == 0) {
                                        listt2.add(i1, null);
                                    } else {
                                        listt2.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 3) {
                                    if (l == 0) {
                                        listt3.add(i1, null);
                                    } else {
                                        listt3.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 4) {
                                    if (l == 0) {
                                        listt4.add(i1, null);
                                    } else {
                                        listt4.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 5) {
                                    if (l == 0) {
                                        listt5.add(i1, null);
                                    } else {
                                        listt5.add(i1, date.get(p).getName());
                                    }
                                }
                            }
                        }
                        js.put("shijian", list);
                        System.out.println("~~~~~~~~~~~~~~~=" + list);
                        if (k == 1) {
                            js.put("shuju" + k.toString(), listt1);
                            js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                        }
                        if (k == 2) {
                            js.put("shuju" + k.toString(), listt2);
                            js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                        }
                        if (k == 3) {
                            js.put("shuju" + k.toString(), listt3);
                            js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                        }
                        if (k == 4) {
                            js.put("shuju" + k.toString(), listt4);
                            js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                        }
                        if (k == 5) {
                            js.put("shuju" + k.toString(), listt5);
                            js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                        }
                    }
                } else if (times != 1) {
                    Integer k = 0;
                    for (int i = 0; i < sqlname.size(); i++) {
                        k++;
                        System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                        String shuxingname = sqlname.get(i);
                        System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                        String quduanname = quduanName[i];
                        Integer qdid = quXianService.findQDid(quduanname,czid);
                        List<quduanEntity> date = quXianService.findDMHQuDuanShiShiData(shuxingname, quduanname, qdid, tableName);
                        for (quduanEntity quduanEntity : date) {
                            long createtime = quduanEntity.getCreatetime();
                            long value = createtime * 1000L;
                            SimpleDateFormat format12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time = format12.format(new Date(value));
                            list.add(time);
                            System.out.println("1234" + date);
                            if (k == 1) {
                                listt1.add(quduanEntity.getName());
                            }
                            if (k == 2) {
                                listt2.add(quduanEntity.getName());
                            }
                            if (k == 3) {
                                listt3.add(quduanEntity.getName());
                            }
                            if (k == 4) {
                                listt4.add(quduanEntity.getName());
                            }
                            if (k == 5) {
                                listt5.add(quduanEntity.getName());
                            }
                        }
                        js.put("shijian", list);
                        System.out.println("~~~~~~~~~~~~~~~=" + list);
                        if (k == 1) {
                            js.put("shuju" + k.toString(), listt1);
                            js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                        }
                        if (k == 2) {
                            js.put("shuju" + k.toString(), listt2);
                            js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                        }
                        if (k == 3) {
                            js.put("shuju" + k.toString(), listt3);
                            js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                        }
                        if (k == 4) {
                            js.put("shuju" + k.toString(), listt4);
                            js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                        }
                        if (k == 5) {
                            js.put("shuju" + k.toString(), listt5);
                            js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                        }
                    }
                }
            }
            System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }

    //电码化的辅助曲线
    //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findDMHQuDuanData")
    public Map<String, Object> findDMHQuDuanData(@RequestParam("startTime") Date startTime,
                                                 @RequestParam("endTime") Date endTime,
                                                 @RequestParam("shuxingId") Integer[] shuxingId,
                                                 @RequestParam("quduanName") String[] quduanName,
                                                 Integer czid) throws Exception {
        String tableName = StringUtil.getTableName(czid, startTime);
        if (shuxingId.length == 0 || quduanName.length == 0 || startTime == null || endTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();
            JSONObject js = new JSONObject();
            long time = endTime.getTime() / 1000 - startTime.getTime() / 1000;//得到这两个时间差 单位是秒
            long starttimea = startTime.getTime();
            for (long i = 0; i <= time; i++) {
                long starttimee = starttimea + i * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time1 = format.format(new Date(starttimee));
                list.add(time1);
                timelist.add(starttimee / 1000);
            }
            System.out.println("timelist==" + timelist);
            js.put("shijian", list);
            long starttime = startTime.getTime() / 1000;
            long endtime = endTime.getTime() / 1000;
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            System.out.println("%%%%%" + name);
            System.out.println("11111" + sqlname);
            Integer k = 0;
            for (int i = 0; i < sqlname.size(); i++) {
                k++;
                System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                String shuxingname = sqlname.get(i);
                System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                String quduanname = quduanName[i];
                Integer qdid = quXianService.findQDid(quduanname,czid);
                List<quduanEntity> date = quXianService.findDMHQuDuanData(starttime, endtime, shuxingname, quduanname, qdid, tableName);
                System.out.println("1234" + date);
                if (date.size() == timelist.size()) {
                    for (int i1 = 0; i1 < date.size(); i1++) {
                        if (k == 1) {
                            listt1.add(i1, date.get(i1).getName());
                        }
                        if (k == 2) {
                            listt2.add(i1, date.get(i1).getName());
                        }
                        if (k == 3) {
                            listt3.add(i1, date.get(i1).getName());
                        }
                        if (k == 4) {
                            listt4.add(i1, date.get(i1).getName());
                        }
                        if (k == 5) {
                            listt5.add(i1, date.get(i1).getName());
                        }
                    }
                } else {
                    Integer l = 0;
                    for (int i1 = 0; i1 < timelist.size(); i1++) {
                        Integer p = 0;
                        for (int i2 = 0; i2 < date.size(); i2++) {
                            if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                l = 0;
                                p++;
                            } else {
                                l = 1;
                                break;
                            }
                        }
                        if (k == 1) {
                            if (l == 0) {
                                listt1.add(i1, null);
                            } else {
                                listt1.add(i1, date.get(p).getName());
                                // System.out.println("666666666666666666+="+date.get(i1).getName());
                            }
                        }
                        if (k == 2) {
                            if (l == 0) {
                                listt2.add(i1, null);
                            } else {
                                listt2.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 3) {
                            if (l == 0) {
                                listt3.add(i1, null);
                            } else {
                                listt3.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 4) {
                            if (l == 0) {
                                listt4.add(i1, null);
                            } else {
                                listt4.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 5) {
                            if (l == 0) {
                                listt5.add(i1, null);
                            } else {
                                listt5.add(i1, date.get(p).getName());
                            }
                        }
                    }
                }
                if (k == 1) {
                    js.put("shuju" + k.toString(), listt1);
                    js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                }
                if (k == 2) {
                    js.put("shuju" + k.toString(), listt2);
                    js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                }
                if (k == 3) {
                    js.put("shuju" + k.toString(), listt3);
                    js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                }
                if (k == 4) {
                    js.put("shuju" + k.toString(), listt4);
                    js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                }
                if (k == 5) {
                    js.put("shuju" + k.toString(), listt5);
                    js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                }
            }
            System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }


    //区段实时曲线
    @GetMapping("/findQuDuanShiShiData")
    public Map<String, Object> findQuDuanShiShiData(@RequestParam("shuxingId") Integer[] shuxingId,
                                                    @RequestParam("quduanName") String[] quduanName,
                                                    Integer czid, Integer times) throws Exception {
        Date today = new Date();
        String tableName = StringUtil.getTableName(czid, today);
        if (shuxingId.length == 0 || quduanName.length == 0) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            JSONObject js = new JSONObject();
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            long cZid = czid.longValue();
            if (cheZhanService.findCzStutrs(cZid, false)==false) {
                List aa=new ArrayList<>();
                JSONObject ss=new JSONObject();
                ss.put("data",aa);
                return ss;
            } else {
                if (times == 1) {
                    List<Long> timelist = new ArrayList<>();
                    long statrtime = today.getTime() / 1000;
                    long endtime = statrtime + 1800;
                    System.out.println("statrtimerrrrrrrrrrr" + statrtime);
                    for (int i = 0; i < 1800; i++) {
                        long onetime = statrtime + i;
                        long value = onetime * 1000L;
                        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                        String time2 = format1.format(new Date(value));
                        list.add(time2);
                        timelist.add(onetime);
                    }
                    Integer k = 0;
                    for (int i = 0; i < sqlname.size(); i++) {
                        k++;
                        System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                        String shuxingname = sqlname.get(i);
                        System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                        String quduanname = quduanName[i];
                        Integer qdid = quXianService.findQDid(quduanname,czid);
                        List<quduanEntity> date = quXianService.findQuDuanDayData(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
                        System.out.println("1234" + date);
                        if (date.size() == timelist.size()) {
                            for (int i1 = 0; i1 < date.size(); i1++) {
                                if (k == 1) {
                                    listt1.add(i1, date.get(i1).getName());
                                }
                                if (k == 2) {
                                    listt2.add(i1, date.get(i1).getName());
                                }
                                if (k == 3) {
                                    listt3.add(i1, date.get(i1).getName());
                                }
                                if (k == 4) {
                                    listt4.add(i1, date.get(i1).getName());
                                }
                                if (k == 5) {
                                    listt5.add(i1, date.get(i1).getName());
                                }
                            }
                        } else {
                            Integer l = 0;
                            for (int i1 = 0; i1 < timelist.size(); i1++) {
                                Integer p = 0;
                                for (int i2 = 0; i2 < date.size(); i2++) {
                                    if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                        l = 0;
                                        p++;
                                    } else {
                                        l = 1;
                                        break;
                                    }
                                }
                                if (k == 1) {
                                    if (l == 0) {
                                        listt1.add(i1, null);
                                    } else {
                                        listt1.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 2) {
                                    if (l == 0) {
                                        listt2.add(i1, null);
                                    } else {
                                        listt2.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 3) {
                                    if (l == 0) {
                                        listt3.add(i1, null);
                                    } else {
                                        listt3.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 4) {
                                    if (l == 0) {
                                        listt4.add(i1, null);
                                    } else {
                                        listt4.add(i1, date.get(p).getName());
                                    }
                                }
                                if (k == 5) {
                                    if (l == 0) {
                                        listt5.add(i1, null);
                                    } else {
                                        listt5.add(i1, date.get(p).getName());
                                    }
                                }
                            }
                        }
                        js.put("shijian", list);
                        System.out.println("~~~~~~~~~~~~~~~=" + list);
                        if (k == 1) {
                            js.put("shuju" + k.toString(), listt1);
                            js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                        }
                        if (k == 2) {
                            js.put("shuju" + k.toString(), listt2);
                            js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                        }
                        if (k == 3) {
                            js.put("shuju" + k.toString(), listt3);
                            js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                        }
                        if (k == 4) {
                            js.put("shuju" + k.toString(), listt4);
                            js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                        }
                        if (k == 5) {
                            js.put("shuju" + k.toString(), listt5);
                            js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                        }
                    }
                } else if (times != 1) {
                    Integer k = 0;
                    for (int i = 0; i < sqlname.size(); i++) {
                        k++;
                        System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                        String shuxingname = sqlname.get(i);
                        System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                        String quduanname = quduanName[i];
                        Integer qdid = quXianService.findQDid(quduanname,czid);
                        List<quduanEntity> date = quXianService.findQuDuanShiShiData(shuxingname, quduanname, qdid, tableName);
                        for (quduanEntity quduanEntity : date) {
                            long createtime = quduanEntity.getCreatetime();
                            long value = createtime * 1000L;
                            SimpleDateFormat format12 = new SimpleDateFormat("HH:mm:ss");
                            String time = format12.format(new Date(value));
                            list.add(time);
                            System.out.println("1234" + date);
                            if (k == 1) {
                                listt1.add(quduanEntity.getName());
                            }
                            if (k == 2) {
                                listt2.add(quduanEntity.getName());
                            }
                            if (k == 3) {
                                listt3.add(quduanEntity.getName());
                            }
                            if (k == 4) {
                                listt4.add(quduanEntity.getName());
                            }
                            if (k == 5) {
                                listt5.add(quduanEntity.getName());
                            }
                        }
                        js.put("shijian", list);
                        System.out.println("~~~~~~~~~~~~~~~=" + list);
                        if (k == 1) {
                            js.put("shuju" + k.toString(), listt1);
                            js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                        }
                        if (k == 2) {
                            js.put("shuju" + k.toString(), listt2);
                            js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                        }
                        if (k == 3) {
                            js.put("shuju" + k.toString(), listt3);
                            js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                        }
                        if (k == 4) {
                            js.put("shuju" + k.toString(), listt4);
                            js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                        }
                        if (k == 5) {
                            js.put("shuju" + k.toString(), listt5);
                            js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                        }
                    }
                }
                System.out.println("jsssssssssssssss" + js);
                return ResponseDataUtil.ok("查询数据成功", js);
            }
        }
    }


    //区段日曲线
    //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findQuDuanDayData")
    public Map<String, Object> findQuDuanDayData(@RequestParam("dayTime") Date dayTime,
                                                 @RequestParam("shuxingId") Integer[] shuxingId,
                                                 @RequestParam("quduanName") String[] quduanName,
                                                 Integer czid) throws Exception {
        String tableName = StringUtil.getTableName(czid, dayTime);
        if (shuxingId.length == 0 || quduanName.length == 0 || dayTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();//48个时间集合
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();
            JSONObject js = new JSONObject();
            long statrtime = dayTime.getTime() / 1000;
            long endtime = statrtime + 86399;
            System.out.println("statrtimerrrrrrrrrrr" + statrtime);
            for (int i = 0; i < 86399; i++) {
                long onetime = statrtime + i;
                long value = onetime * 1000L;
                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
                String time2 = format1.format(new Date(value));
                list.add(time2);
                timelist.add(onetime);
            }
            js.put("shijian", list);
            System.out.println("");
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            Integer k = 0;
            for (int i = 0; i < sqlname.size(); i++) {
                k++;
                System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                String shuxingname = sqlname.get(i);
                System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                String quduanname = quduanName[i];
                Integer qdid = quXianService.findQDid(quduanname,czid);
                List<quduanEntity> date = quXianService.findQuDuanDayData(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
                System.out.println("1234" + date);
                if (date.size() == timelist.size()) {
                    for (int i1 = 0; i1 < date.size(); i1++) {
                        if (k == 1) {
                            listt1.add(i1, date.get(i1).getName());
                        }
                        if (k == 2) {
                            listt2.add(i1, date.get(i1).getName());
                        }
                        if (k == 3) {
                            listt3.add(i1, date.get(i1).getName());
                        }
                        if (k == 4) {
                            listt4.add(i1, date.get(i1).getName());
                        }
                        if (k == 5) {
                            listt5.add(i1, date.get(i1).getName());
                        }
                    }
                } else {
                    Integer l = 0;
                    for (int i1 = 0; i1 < timelist.size(); i1++) {
                        Integer p = 0;
                        for (int i2 = 0; i2 < date.size(); i2++) {
                            if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                l = 0;
                                p++;
                            } else {
                                l = 1;
                                break;
                            }
                        }
                        if (k == 1) {
                            if (l == 0) {
                                listt1.add(i1, null);
                            } else {
                                listt1.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 2) {
                            if (l == 0) {
                                listt2.add(i1, null);
                            } else {
                                listt2.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 3) {
                            if (l == 0) {
                                listt3.add(i1, null);
                            } else {
                                listt3.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 4) {
                            if (l == 0) {
                                listt4.add(i1, null);
                            } else {
                                listt4.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 5) {
                            if (l == 0) {
                                listt5.add(i1, null);
                            } else {
                                listt5.add(i1, date.get(p).getName());
                            }
                        }
                    }
                }
                if (k == 1) {
                    js.put("shuju" + k.toString(), listt1);
                    System.out.println("shujasdaalda" + listt1);
                    js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                }
                if (k == 2) {
                    js.put("shuju" + k.toString(), listt2);
                    js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                }
                if (k == 3) {
                    js.put("shuju" + k.toString(), listt3);
                    js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                }
                if (k == 4) {
                    js.put("shuju" + k.toString(), listt4);
                    js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                }
                if (k == 5) {
                    js.put("shuju" + k.toString(), listt5);
                    js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                }
            }
            //System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }


    //区段辅助曲线
    //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findQuDuanData")
    public Map<String, Object> findQuDuanData(@RequestParam("startTime") Date startTime,
                                              @RequestParam("endTime") Date endTime,
                                              @RequestParam("shuxingId") Integer[] shuxingId,
                                              @RequestParam("quduanName") String[] quduanName,
                                              Integer czid) throws Exception {
        String tableName = StringUtil.getTableName(czid, startTime);
        if (shuxingId.length == 0 || quduanName.length == 0 || startTime == null || endTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();
            JSONObject js = new JSONObject();
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            System.out.println("%%%%%" + name);
            System.out.println("11111" + sqlname);
            long time = endTime.getTime() / 1000 - startTime.getTime() / 1000;//得到这两个时间差 单位是秒
            long starttimea = startTime.getTime();
            for (long i = 0; i <= time; i++) {
                long starttimee = starttimea + i * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time1 = format.format(new Date(starttimee));
                list.add(time1);
                timelist.add(starttimee / 1000);
            }
            System.out.println("timelist==" + timelist);
            js.put("shijian", list);
            long starttime = startTime.getTime() / 1000;
            long endtime = endTime.getTime() / 1000;

            Integer k = 0;
            for (int i = 0; i < sqlname.size(); i++) {
                k++;
                System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                String shuxingname = sqlname.get(i);
                System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                String quduanname = quduanName[i];
                Integer qdid = quXianService.findQDid(quduanname,czid);
                List<quduanEntity> date = quXianService.findQuDuanDatas(starttime, endtime, shuxingname, quduanname, qdid, tableName);
                System.out.println("1234" + date);
                if (date.size() == timelist.size()) {
                    for (int i1 = 0; i1 < date.size(); i1++) {
                        if (k == 1) {
                            listt1.add(i1, date.get(i1).getName());
                        }
                        if (k == 2) {
                            listt2.add(i1, date.get(i1).getName());
                        }
                        if (k == 3) {
                            listt3.add(i1, date.get(i1).getName());
                        }
                        if (k == 4) {
                            listt4.add(i1, date.get(i1).getName());
                        }
                        if (k == 5) {
                            listt5.add(i1, date.get(i1).getName());
                        }
                    }
                } else {
                    Integer l = 0;
                    for (int i1 = 0; i1 < timelist.size(); i1++) {
                        Integer p = 0;
                        for (int i2 = 0; i2 < date.size(); i2++) {
                            if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                l = 0;
                                p++;
                            } else {
                                l = 1;
                                break;
                            }
                        }
                        if (k == 1) {
                            if (l == 0) {
                                listt1.add(i1, null);
                            } else {
                                listt1.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 2) {
                            if (l == 0) {
                                listt2.add(i1, null);
                            } else {
                                listt2.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 3) {
                            if (l == 0) {
                                listt3.add(i1, null);
                            } else {
                                listt3.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 4) {
                            if (l == 0) {
                                listt4.add(i1, null);
                            } else {
                                listt4.add(i1, date.get(p).getName());
                            }
                        }
                        if (k == 5) {
                            if (l == 0) {
                                listt5.add(i1, null);
                            } else {
                                listt5.add(i1, date.get(p).getName());
                            }
                        }
                    }
                }
                if (k == 1) {
                    js.put("shuju" + k.toString(), listt1);
                    js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                }
                if (k == 2) {
                    js.put("shuju" + k.toString(), listt2);
                    js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                }
                if (k == 3) {
                    js.put("shuju" + k.toString(), listt3);
                    js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                }
                if (k == 4) {
                    js.put("shuju" + k.toString(), listt4);
                    js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                }
                if (k == 5) {
                    js.put("shuju" + k.toString(), listt5);
                    js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                }
            }
            System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }

    public static void main(String[] args) {

        Date nowDay = new Date();
        long time2 = nowDay.getTime() / 1000;

        // System.out.println(time2);
        // 1599235200 2020-09-05 00:00:00      1599321600 2020-09-06 00:00:00    1599321599  12020-09-05 23:59:59
        // 1601169272  2020-09-27 09:14:32    1601169349  2020-09-27 09:15:49
        // 1601105466
        //1596509890 2020-08-04 10:58:10    1596519568  2020-08-04 13:39:28
        long value = (1601169349) * 1000L;//1595303879  2020-07-21 11:57:59     1595304033  2020-07-21 12:00:33
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date(value));
        System.out.println("1" + time);


        Date d = new Date("2020/08/04 10:58:10 ");
        SimpleDateFormat starttime1 = new SimpleDateFormat("HH:mm:ss");
        String format1 = starttime1.format(d);
        Date date = new Date();
        long time1 = date.getTime();
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        String time12 = format2.format(new Date(time1));
        System.out.println("2" + time1);
        System.out.println("3" + time12);
        System.out.println("4" + format1);
        System.out.println("5" + d.getTime());
        System.out.println("6" + d.getTime() + 1000);
        System.out.println("7" + format.format(new Date(d.getTime() + 1000)));

    }



/*

    //电码化日曲线
    //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findDMHQuDuanDayData")
    public Map<String, Object> findDMHQuDuanDayData(@RequestParam("dayTime") Date dayTime,
                                                 @RequestParam("shuxingId") Integer[] shuxingId,
                                                 @RequestParam("quduanName") String[] quduanName,
                                                 Integer czid) throws Exception {
        String tableName = StringUtil.getTableName(czid, dayTime);
        if (shuxingId.length == 0 || quduanName.length == 0 || dayTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();//48个时间集合
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            JSONObject js = new JSONObject();
            List<Long> times = new ArrayList<>();//48个秒数集合
            long daytimes = dayTime.getTime() / 1000;
            for (int i = 0; i < 48; i++) {
                long onetime = daytimes + 1800 * i;
                times.add(onetime);
                long value = onetime * 1000L;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time2 = format1.format(new Date(value));
                list.add(time2);
            }
            js.put("shijian", list);
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            Integer k = 0;
            for (int i = 0; i < sqlname.size(); i++) {
                k++;
                System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                String shuxingname = sqlname.get(i);
                System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                String quduanname = quduanName[i];
                Integer qdid = quXianService.findQDid(quduanname);
                for (Long time : times) {
                    BigDecimal date = quXianService.findOneQuDuanDatas(time, shuxingname, quduanname, qdid, tableName);
                    System.out.println("1234" + date);
                    if (k == 1) {
                        listt1.add(date);
                    }
                    if (k == 2) {
                        listt2.add(date);
                    }
                    if (k == 3) {
                        listt3.add(date);
                    }
                    if (k == 4) {
                        listt4.add(date);
                    }
                    if (k == 5) {
                        listt5.add(date);
                    }
                    if (k == 1) {
                        js.put("shuju" + k.toString(), listt1);
                        js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
                    }
                    if (k == 2) {
                        js.put("shuju" + k.toString(), listt2);
                        js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
                    }
                    if (k == 3) {
                        js.put("shuju" + k.toString(), listt3);
                        js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
                    }
                    if (k == 4) {
                        js.put("shuju" + k.toString(), listt4);
                        js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
                    }
                    if (k == 5) {
                        js.put("shuju" + k.toString(), listt5);
                        js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
                    }
                }
            }
            System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }
*/






   /* public static void main(String[] args) throws Exception {
        Date day = new Date();


        Date d = new Date("2020/08/19 ");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = df.format(d);
        System.out.println("ssssss" + s);
        System.out.println("ssssss111+" + day.getTime() / 1000);

        List<Long> times = new ArrayList<>();
        long time = d.getTime() / 1000;
        for (int i = 0; i < 48; i++) {
            long time1 = time + 1800 * i;
            times.add(time1);
        }


        //1597820525, 1597822325, 1597824125, 1597825925, 1597827725, 1597829525, 1597831325, 1597833125, 1597834925, 1597836725, 1597838525, 1597840325, 1597842125, 1597843925, 1597845725, 1597847525, 1597849325, 1597851125, 1597852925, 1597854725, 1597856525, 1597858325, 1597860125, 1597861925, 1597863725, 1597865525, 1597867325, 1597869125, 1597870925, 1597872725, 1597874525, 1597876325, 1597878125, 1597879925, 1597881725, 1597883525, 1597885325, 1597887125, 1597888925, 1597890725, 1597892525, 1597894325, 1597896125, 1597897925, 1597899725, 1597901525, 1597903325, 1597905125
        // [1597820991, 1597822791, 1597824591, 1597826391, 1597828191, 1597829991, 1597831791, 1597833591, 1597835391, 1597837191, 1597838991, 1597840791, 1597842591, 1597844391, 1597846191, 1597847991, 1597849791, 1597851591, 1597853391, 1597855191, 1597856991, 1597858791, 1597860591, 1597862391, 1597864191, 1597865991, 1597867791, 1597869591, 1597871391, 1597873191, 1597874991, 1597876791, 1597878591, 1597880391, 1597882191, 1597883991, 1597885791, 1597887591, 1597889391, 1597891191, 1597892991, 1597894791, 1597896591, 1597898391, 1597900191, 1597901991, 1597903791, 1597905591]
        System.out.println("afvadfa++" + times);
        System.out.println("afvadfa++" + times.size());


        long value = 1597835583 * 1000L;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = format1.format(new Date(value));
        System.out.println("1==" + time2);


//afvadfa++[1597766400, 1597768200, 1597770000, 1597771800, 1597773600, 1597775400, 1597777200, 1597779000, 1597780800, 1597782600, 1597784400, 1597786200, 1597788000, 1597789800, 1597791600, 1597793400, 1597795200, 1597797000, 1597798800, 1597800600, 1597802400, 1597804200, 1597806000, 1597807800, 1597809600, 1597811400, 1597813200, 1597815000, 1597816800, 1597818600, 1597820400, 1597822200, 1597824000, 1597825800, 1597827600, 1597829400, 1597831200, 1597833000, 1597834800, 1597836600, 1597838400, 1597840200, 1597842000, 1597843800, 1597845600, 1597847400, 1597849200, 1597851000]


        Date date = df.parse(s);
        System.out.println("date" + date);
        ArrayList<String> dates = new ArrayList<String>();
        dates.add(s);
        for (int i = 0; i < 23; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, -1);
            date = cal.getTime();
            String s1 = format.format(date);
            dates.add(s1);
        }
        System.out.println(dates);

    }

*/
    /*
     //根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findQuDuanData")
    public Map<String, Object> findQuDuanData(@RequestParam("startTime") Date startTime,
                                              @RequestParam("endTime") Date endTime,
                                              @RequestParam("shuxingId") Integer[] shuxingId,
                                              @RequestParam("quduanName") String[] quduanName,
                                             Integer czid) throws Exception {
        Date today =new Date();
        String tableName = StringUtil.getTableName(czid, today);
        if (shuxingId.length == 0 || quduanName.length == 0 || startTime == null || endTime == null) {
            return ResponseDataUtil.error("请选择正确的数据");
        } else {
            List<String> list = new ArrayList<>();
            List<BigDecimal> listt1 = new ArrayList<>();
            List<BigDecimal> listt2 = new ArrayList<>();
            List<BigDecimal> listt3 = new ArrayList<>();
            List<BigDecimal> listt4 = new ArrayList<>();
            List<BigDecimal> listt5 = new ArrayList<>();
            List<Long> timelist = new ArrayList<>();
            JSONObject js = new JSONObject();
            long time = endTime.getTime() / 1000 - startTime.getTime() / 1000;//得到这两个时间差 单位是秒
            long starttimea = startTime.getTime();
            for (long i = 0; i <= time; i++) {
                long starttimee = starttimea + i * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time1 = format.format(new Date(starttimee));
                list.add(time1);
                timelist.add(starttimee / 1000);
            }
            System.out.println("timelist==" + timelist);
            js.put("shijian", list);
            long starttime = startTime.getTime() / 1000;
            long endtime = endTime.getTime() / 1000;
            List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
            List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
            System.out.println("%%%%%"+name);
            System.out.println("11111" + sqlname);
            Integer k = 0;
            if (sqlname.size() != 1) {
                for (int i = 0; i < sqlname.size(); i++) {
                    k++;
                    System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
                    String shuxingname = sqlname.get(i);
                    System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
                    String quduanname = quduanName[i];
                    Integer qdid = quXianService.findQDid(quduanname);
                    List<quduanEntity> date = quXianService.findQuDuanDatas(starttime, endtime, shuxingname, quduanname, qdid,tableName);
                    System.out.println("1234" + date);
                    if (date.size() == timelist.size()) {
                        for (int i1 = 0; i1 < date.size(); i1++) {
                            if (k == 1) {
                                listt1.add(i1, date.get(i1).getName());
                            }
                            if (k == 2) {
                                listt2.add(i1, date.get(i1).getName());
                            }
                            if (k == 3) {
                                listt3.add(i1, date.get(i1).getName());
                            }
                            if (k == 4) {
                                listt4.add(i1, date.get(i1).getName());
                            }
                            if (k == 5) {
                                listt5.add(i1, date.get(i1).getName());
                            }
                        }
                    } else {
                        Integer l = 0;
                        for (int i1 = 0; i1 < timelist.size(); i1++) {
                            Integer p = 0;
                            for (int i2 = 0; i2 < date.size(); i2++) {
                                if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                    // System.out.println("11+="+timelist.get(i1));
                                    //System.out.println("112+="+date.get(i2).getCreatetime());
                                    l = 0;
                                    p++;
                                } else {
                                    l = 1;
                                    break;
                                }
                            }
                            if (k == 1) {
                                if (l == 0) {
                                    listt1.add(i1, null);
                                } else {
                                    listt1.add(i1, date.get(p).getName());
                                    // System.out.println("666666666666666666+="+date.get(i1).getName());
                                }
                            }
                            if (k == 2) {
                                if (l == 0) {
                                    listt2.add(i1, null);
                                } else {
                                    listt2.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 3) {
                                if (l == 0) {
                                    listt3.add(i1, null);
                                } else {
                                    listt3.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 4) {
                                if (l == 0) {
                                    listt4.add(i1, null);
                                } else {
                                    listt4.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 5) {
                                if (l == 0) {
                                    listt5.add(i1, null);
                                } else {
                                    listt5.add(i1, date.get(p).getName());
                                }
                            }
                        }
                    }
                    if (k == 1) {
                        js.put("shuju" + k.toString(), listt1);
                        js.put("mingzi" + k.toString(), quduanName[0] + name.get(i));
                    }
                    if (k == 2) {
                        js.put("shuju" + k.toString(), listt2);
                        js.put("mingzi" + k.toString(), quduanName[1] + name.get(i));
                    }
                    if (k == 3) {
                        js.put("shuju" + k.toString(), listt3);
                        js.put("mingzi" + k.toString(), quduanName[2] + name.get(i));
                    }
                    if (k == 4) {
                        js.put("shuju" + k.toString(), listt4);
                        js.put("mingzi" + k.toString(), quduanName[3] + name.get(i));
                    }
                    if (k == 5) {
                        js.put("shuju" + k.toString(), listt5);
                        js.put("mingzi" + k.toString(), quduanName[4] + name.get(i));
                    }
                }
            } else {
                for (int i = 0; i < quduanName.length; i++) {
                    k++;
                    System.out.println("222222" + sqlname.get(0));//获得每一个属性名
                    String shuxingname = sqlname.get(0);
                    System.out.println("123=" + quduanName[i]);//获得每一个区段名
                    String quduanname = quduanName[i];
                    Integer qdid = quXianService.findQDid(quduanname);
                    List<quduanEntity> date = quXianService.findQuDuanDatas(starttime, endtime, shuxingname, quduanname, qdid,tableName);
                    System.out.println("1234" + date);
                    if (date.size() == timelist.size()) {
                        for (int i1 = 0; i1 < date.size(); i1++) {
                            if (k == 1) {
                                listt1.add(i1, date.get(i1).getName());
                            }
                            if (k == 2) {
                                listt2.add(i1, date.get(i1).getName());
                            }
                            if (k == 3) {
                                listt3.add(i1, date.get(i1).getName());
                            }
                            if (k == 4) {
                                listt4.add(i1, date.get(i1).getName());
                            }
                            if (k == 5) {
                                listt5.add(i1, date.get(i1).getName());
                            }
                        }
                    } else {
                        Integer l = 0;
                        for (int i1 = 0; i1 < timelist.size(); i1++) {
                            Integer p = 0;
                            for (int i2 = 0; i2 < date.size(); i2++) {
                                if (timelist.get(i1) != (long) date.get(i2).getCreatetime()) {
                                    // System.out.println("11+="+timelist.get(i1));
                                    //System.out.println("112+="+date.get(i2).getCreatetime());
                                    l = 0;
                                    p++;
                                } else {
                                    l = 1;
                                    break;
                                }
                            }
                            if (k == 1) {
                                if (l == 0) {
                                    listt1.add(i1, null);
                                } else {
                                    listt1.add(i1, date.get(p).getName());
                                    // System.out.println("666666666666666666+="+date.get(i1).getName());
                                }
                            }
                            if (k == 2) {
                                if (l == 0) {
                                    listt2.add(i1, null);
                                } else {
                                    listt2.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 3) {
                                if (l == 0) {
                                    listt3.add(i1, null);
                                } else {
                                    listt3.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 4) {
                                if (l == 0) {
                                    listt4.add(i1, null);
                                } else {
                                    listt4.add(i1, date.get(p).getName());
                                }
                            }
                            if (k == 5) {
                                if (l == 0) {
                                    listt5.add(i1, null);
                                } else {
                                    listt5.add(i1, date.get(p).getName());
                                }
                            }
                        }
                    }
                    if (k == 1) {
                        js.put("shuju" + k.toString(), listt1);
                        js.put("mingzi" + k.toString(), quduanName[0] + name.get(0));
                    }
                    if (k == 2) {
                        js.put("shuju" + k.toString(), listt2);
                        js.put("mingzi" + k.toString(), quduanName[1] + name.get(0));
                    }
                    if (k == 3) {
                        js.put("shuju" + k.toString(), listt3);
                        js.put("mingzi" + k.toString(), quduanName[2] + name.get(0));
                    }
                    if (k == 4) {
                        js.put("shuju" + k.toString(), listt4);
                        js.put("mingzi" + k.toString(), quduanName[3] + name.get(0));
                    }
                    if (k == 5) {
                        js.put("shuju" + k.toString(), listt5);
                        js.put("mingzi" + k.toString(), quduanName[4] + name.get(0));
                    }
                }
            }
            System.out.println("jsssssssssssssss" + js);
            return ResponseDataUtil.ok("查询数据成功", js);
        }
    }


     */

    /*//根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
    @GetMapping("/findQuDuanData")
    public Map<String, Object> findQuDuanData(@RequestParam("startTime") Date startTime,
                                              @RequestParam("endTime") Date endTime,
                                              @RequestParam("shuxingId") Integer[] shuxingId,
                                              @RequestParam("quduanName") String[] quduanName) throws Exception {
        List<String> list = new ArrayList<>();
        List<Integer> listdata1 = new ArrayList<>();
        List<Integer> listdata2 = new ArrayList<>();
        List<Integer> listdata3 = new ArrayList<>();
        List<Integer> listdata4 = new ArrayList<>();
        List<Integer> listdata5 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        JSONObject js = new JSONObject();
        List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
        System.out.println("11111" + sqlname);
        List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
        SimpleDateFormat starttime11 = new SimpleDateFormat("HH:mm:ss");
        long time = endTime.getTime() / 1000 - startTime.getTime() / 1000;//得到这两个时间差 单位是秒
        long starttimea = startTime.getTime();
        if (sqlname.size() != 1) {
            for (long i = 0; i <= time; i++) {
                long starttimee = starttimea + i * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time1 = format.format(new Date(starttimee));
                list.add(time1);
                for (int l = 0; l < sqlname.size(); l++) {
                    long starttimeee = starttimee / 1000;
                    System.out.println("222222" + sqlname.get(l));//获得每一个属性名
                    String shuxingname = sqlname.get(l);
                    System.out.println("123=" + quduanName[l]);//获得每一个区段名
                    String quduanname = quduanName[l];
                    Integer qdid = quXianService.findQDid(quduanname);
                    Integer date = quXianService.findQuDuanData(starttimeee, shuxingname, quduanname, qdid);
                    if (l == 0) {
                        listdata1.add(date);
                    }
                    if (l == 1) {
                        listdata2.add(date);
                    }
                    if (l == 2) {
                        listdata3.add(date);
                    }
                    if (l == 3) {
                        listdata4.add(date);
                    }
                    if (l == 4) {
                        listdata5.add(date);
                    }
                }
            }
            if (listdata1.size() != 0) {
                js.put("shuju1", listdata1);
                js.put("mingzi1", name.get(0) + quduanName[0]);
                System.out.println(name.get(0) + quduanName[0]);
            }
            if (listdata2.size() != 0) {
                js.put("shuju2", listdata2);
                js.put("mingzi2", name.get(1) + quduanName[1]);
                System.out.println(name.get(1) + quduanName[1]);
            }
            if (listdata3.size() != 0) {
                js.put("shuju3", listdata3);
                js.put("mingzi3", name.get(2) + quduanName[2]);
                System.out.println(name.get(2) + quduanName[2]);
            }
            if (listdata4.size() != 0) {
                js.put("shuju4", listdata4);
                js.put("mingzi4", name.get(3) + quduanName[3]);
                System.out.println(name.get(3) + quduanName[3]);
            }
            if (listdata5.size() != 0) {
                js.put("shuju5", listdata5);
                js.put("mingzi5", name.get(4) + quduanName[4]);
                System.out.println(name.get(4) + quduanName[4]);
            }
        } else {
            for (long i = 0; i <= time; i++) {
                long starttimee = starttimea + i * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time1 = format.format(new Date(starttimee));
                list.add(time1);
                for (int l = 0; l < quduanName.length; l++) {
                    long starttimeee = starttimee / 1000;
                    System.out.println("222222" + sqlname.get(0));//获得每一个属性名
                    String shuxingname = sqlname.get(0);
                    System.out.println("123=" + quduanName[l]);//获得每一个区段名
                    String quduanname = quduanName[l];
                    Integer qdid = quXianService.findQDid(quduanname);
                    Integer date = quXianService.findQuDuanData(starttimeee, shuxingname, quduanname, qdid);
                    if (l == 0) {
                        listdata1.add(date);
                    }
                    if (l == 1) {
                        listdata2.add(date);
                    }
                    if (l == 2) {
                        listdata3.add(date);
                    }
                    if (l == 3) {
                        listdata4.add(date);
                    }
                    if (l == 4) {
                        listdata5.add(date);
                    }
                }
            }
            if (listdata1.size() != 0) {
                js.put("shuju1", listdata1);
                js.put("mingzi1", name.get(0) + quduanName[0]);
                System.out.println(name.get(0) + quduanName[0]);
            }
            if (listdata2.size() != 0) {
                js.put("shuju2", listdata2);
                js.put("mingzi2", name.get(0) + quduanName[1]);
                System.out.println(name.get(0) + quduanName[1]);
            }
            if (listdata3.size() != 0) {
                js.put("shuju3", listdata3);
                js.put("mingzi3", name.get(0) + quduanName[2]);
                System.out.println(name.get(0) + quduanName[2]);
            }
            if (listdata4.size() != 0) {
                js.put("shuju4", listdata4);
                js.put("mingzi4", name.get(0) + quduanName[3]);
                System.out.println(name.get(0) + quduanName[3]);
            }
            if (listdata5.size() != 0) {
                js.put("shuju5", listdata5);
                js.put("mingzi5", name.get(0) + quduanName[4]);
                System.out.println(name.get(0) + quduanName[4]);
            }
        }
        js.put("shijian", list);
        return ResponseDataUtil.ok("查询数据成功", js);
    }
*/




    /* //根据车站cid查询 对应的设备
    @GetMapping("/findSheBeiByCid/{id}")
    public Map<String, Object> findSheBeiByCid(@PathVariable Integer id) {
        List<SheBeiEntity> sheBeiEntities = quXianService.findSheBeiByCid(id);
        return ResponseDataUtil.ok("查询设备成功", sheBeiEntities);
    }*/

    //根据sid查询对应的区段
    /*@GetMapping("/findQuDuanById/{id}")
    public Map<String, Object> findQuDuanById(@PathVariable Integer id) {
        List<QuDuanBaseEntity> quDuanBaseEntities = quXianService.findQuDuanById(id);
        return ResponseDataUtil.ok("查询区段信息成功", quDuanBaseEntities);
    }*/


   /* //根据所选日期  获取对应的数据
    @GetMapping("/findQuDuanDataByTime")
    public Map<String, Object> findQuDuanDataByTime(@RequestParam("time") Date time) {
        List<QuDuanInfoEntity> quDuanInfoEntities = quXianService.findQuDuanDataByTime(time);
        System.out.println("riqi" + quDuanInfoEntities);
        return ResponseDataUtil.ok("查询数据成功", quDuanInfoEntities);
    }*/





/*
    List<String> list = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();
    JSONObject js=new JSONObject();
    SimpleDateFormat  starttime11 = new SimpleDateFormat("HH:mm:ss");
    String starttime1 = starttime11.format(startTime);
    String endtime1 = starttime11.format(endTime);
    //String starttime1 = new SimpleDateFormat("HH:mm:ss").format(startTime);//把开始时间转换格式
    // String endtime1 = new SimpleDateFormat("HH:mm:ss").format(endTime);//把结束时间转换格式
    long time = endTime.getTime()/1000 - startTime.getTime()/1000;//得到这两个时间差 单位是秒
    Integer j = 0;
        list.add(starttime1);
    long starttimea = startTime.getTime();
        for (long i = 1; i < time; i++) {
        long starttimee = starttimea+i*1000;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss") ;
        String time1 = format.format(new Date(starttimee)) ;
        //j++;
        list.add(time1);

    }
        list.add(endtime1);
        map.put("shijian", list);
        js.put("shijian", list);
    //String starttime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startTime);//把开始时间转换格式
    long starttime = startTime.getTime()/1000;
    //System.out.println("starttime"+starttime);
    //String endtime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endTime);//把结束时间转换格式
    long endtime = endTime.getTime()/1000;
    //System.out.println("endtime"+endtime);
    List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
    List<String>name=quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
        System.out.println("11111" + sqlname);
    //String[] name=new String[sqlname.size()];
    Integer k = 0;
        for (int i = 0; i < sqlname.size(); i++) {
        k++;
        System.out.println("222222" + sqlname.get(i));//获得每一个属性名
        String shuxingname = sqlname.get(i);
        System.out.println("123=" + quduanName[i]);//获得每一个区段名
        String quduanname = quduanName[i];
        Integer qdid=quXianService.findQDid(quduanname);
        List<Integer> date = quXianService.findQuDuanData(starttime, endtime, shuxingname, quduanname,qdid);
        map.put("shuju" + k.toString(), date);
        js.put("shuju" + k.toString(), date);
        js.put("mingzi" + k.toString(), name.get(i));
    }
        return ResponseDataUtil.ok("查询数据成功", js);


    */


}
