package com.yintu.ruixing.guzhangzhenduan;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.common.result.Result;
import com.yintu.ruixing.common.util.FileUploadUtil;
import com.yintu.ruixing.common.util.POIUtils;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.guzhangzhenduan.*;
import com.yintu.ruixing.guzhangzhenduan.DataStatsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:lcy
 * @date:2020-05-21 11
 * 数据统计
 */
@RestController
@RequestMapping("/dataStats")
public class DataStatsController {
    @Autowired
    private DataStatsService dataStatsService;

    //模板下载
    @GetMapping("/quduandownloads")
    public void quduandownloads(HttpServletResponse response) throws IOException {
        //String filePath ="C:\\data\\ruixing\\templates";
        String fileName = "区段信息配置模板.xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        FileUploadUtil.getTemplateFile(response.getOutputStream(), fileName);
    }


    //区段的Excel数据预览
    @PostMapping("/QuDuanYuLan")
    @ResponseBody
    public Map<String, Object> QuDuanYuLan(@RequestParam("file") MultipartFile excelFile) {
        List<String[]> datas = new ArrayList<>();
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    if (!strings[0].equals("") || !strings[1].equals("") || !strings[2].equals("") || !strings[3].equals("") || !strings[4].equals("") || !strings[5].equals("")) {
                        if (strings[5].equals("下行") || strings[5].equals("上行") ||
                                strings[5].equals("——") || strings[5].equals("") ||
                                strings[6].equals("接近") || strings[6].equals("离去") ||
                                strings[6].equals("——") || strings[6].equals("")) {
                            String[] strings1 = strings;
                            datas.add(strings1);
                        } else {
                            return ResponseDataUtil.error("请选择正确的Excel数据");
                        }
                    }
                }
            }
        } catch (IOException e) {
            return ResponseDataUtil.error("文件上传失败");
        }
        return ResponseDataUtil.ok("文件上传成功", datas);
    }


    //将上传的区段数据保存到数据库
    @PostMapping("/uploadQuDuanData")
    public Map<String, Object> uploadQuDuanData(@RequestBody JSONObject quDuanDatas) {
        JSONArray quDuanDatas1 = quDuanDatas.getJSONArray("quDuanDatas");
        Integer j = 0;
        List<Integer> number = new ArrayList<>();
        List<String[]> list = quDuanDatas1.toJavaList(String[].class);
        Integer qq=0;
        for (String[] stringss : list) {
            j++;
            String czid = stringss[0];
            String qdid = stringss[7];
            List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByIds(Integer.parseInt(czid), Integer.parseInt(qdid));//根据车站czid 和区段qdid 查询对应的区段数据
            if (quDuanBaseEntityList.size() != 0) {
                number.add(j);
            }
        }
        if (number.size() == 0) {
            for (String[] strings : list) {
                System.out.println("qqqqqqq"+qq++);
                System.out.println("qqqqqqq"+qq +"+"+strings);
                String czid1 = strings[0];
                String czname = strings[1];
                String line = strings[2];
                String leftright = strings[3];
                String ofxianduan = strings[4];
                String xingbie = strings[5];
                String type = strings[6];
                System.out.println("11111111111");
                String qdid1 = strings[7];
                String zongheid = strings[8];
                String quduanshejiname = strings[9];
                String qudunyunyingname = strings[10];
                String quduanlength = strings[11];
                String carrier = strings[12];
                String diduantype = strings[13];
                String xianluqingkuang = strings[14];
                System.out.println("2222222222222");
                String bianjie = strings[15];
                String fenjiedianwhere = strings[16];
                String zhanqufenjie = strings[17];
                String jinzhanxinhaojiname = strings[18];
                String xinhaojiorbiaozhiming = strings[19];
                String xinhaojizhanpaiwhere = strings[20];
                String xinhaojiewhere = strings[21];
                String zuocejueyuantype = strings[22];
                String youcejueyuantype = strings[23];
                System.out.println("33333333333333333");
                String zhengxianhoufangquduanid = strings[24];
                String zhengxianqianfangquduanid = strings[25];
                String daochaguanlianquduan1id = strings[26];
                String daochaguanlianquduan2id = strings[27];
                String dianmahuaguidao = strings[28];
                String guineidizhi = strings[29];
                System.out.println("44444444444444444");
                Long cid = dataStatsService.findchezhanid(Long.parseLong(czid1));//根据车站专用czid  查询对应的id
                System.out.println("55555555555555555");
                Integer lastParentid = dataStatsService.findLastParentid();
                QuDuanBaseEntity quDuanBaseEntity = new QuDuanBaseEntity();
                quDuanBaseEntity.setCzid(Integer.parseInt(czid1));
                quDuanBaseEntity.setParentId(lastParentid);
                System.out.println("666666666666666666");
                Integer xdid = dataStatsService.findxianduanid(Long.parseLong(czid1));
                quDuanBaseEntity.setXid(xdid);
                quDuanBaseEntity.setCid(Integer.parseInt(cid.toString()));
                quDuanBaseEntity.setQdid(Integer.parseInt(qdid1));
                quDuanBaseEntity.setLine(line);
                System.out.println("77777777777777777");
                quDuanBaseEntity.setOfXianDuan(ofxianduan);
                quDuanBaseEntity.setLeftRight(leftright);
                if (xingbie.equals("上行")) {
                    quDuanBaseEntity.setXingBie(1);
                }
                if (xingbie.equals("下行")) {
                    quDuanBaseEntity.setXingBie(2);
                }
                if (xingbie.equals("——") || xingbie.equals("")) {
                    quDuanBaseEntity.setXingBie(0);
                }
                if (type.equals("接近")) {
                    quDuanBaseEntity.setType(1);
                }
                if (type.equals("离去")) {
                    quDuanBaseEntity.setType(2);
                }
                if (type.equals("——") || type.equals("")) {
                    quDuanBaseEntity.setType(0);
                }
                quDuanBaseEntity.setZongheId(zongheid);
                quDuanBaseEntity.setQuduanshejiName(quduanshejiname);
                quDuanBaseEntity.setQuduanyunyingName(qudunyunyingname);
                if (quduanlength.equals("") || quduanlength.equals("——")) {
                    quDuanBaseEntity.setQuduanLength(null);
                } else {
                    quDuanBaseEntity.setQuduanLength(Integer.parseInt(quduanlength));
                }
                System.out.println("888888888888888888");
                quDuanBaseEntity.setCarrier(carrier);
                quDuanBaseEntity.setDiduanType(diduantype);
                quDuanBaseEntity.setXianluqingkuang(xianluqingkuang);
                if (bianjie.equals("") || bianjie.equals("否")) {
                    quDuanBaseEntity.setBianjie(0);
                } else {
                    quDuanBaseEntity.setBianjie(1);
                }
                quDuanBaseEntity.setFenjiedianWhere(fenjiedianwhere);
                System.out.println("99999999999999999");
                if (zhanqufenjie.equals("") || zhanqufenjie.equals("否")) {
                    quDuanBaseEntity.setZhanqufenjie(0);
                } else {
                    quDuanBaseEntity.setZhanqufenjie(1);
                }
                quDuanBaseEntity.setJinzhanxinhaojiName(jinzhanxinhaojiname);
                quDuanBaseEntity.setXinhaojiorbiaozhiming(xinhaojiorbiaozhiming);
                quDuanBaseEntity.setXinhaobiaozhipaiWhere(xinhaojizhanpaiwhere);
                quDuanBaseEntity.setXinhaojiWhere(xinhaojiewhere);
                quDuanBaseEntity.setZuocejueyuanType(zuocejueyuantype);
                quDuanBaseEntity.setYoucejueyuanType(youcejueyuantype);
                System.out.println("111111112222222222222");
                quDuanBaseEntity.setZhengxianhoufangquduanId(zhengxianhoufangquduanid);
                quDuanBaseEntity.setZhengxianqianfangquduanId(zhengxianqianfangquduanid);
                System.out.println("123");
                quDuanBaseEntity.setDaochaguanlianquduan1Id(daochaguanlianquduan1id);
                quDuanBaseEntity.setDaochaguanlianquduan2Id(daochaguanlianquduan2id);
                System.out.println("456");
                quDuanBaseEntity.setDianmahuaguihao(dianmahuaguidao);
                if (guineidizhi.equals("")) {
                    quDuanBaseEntity.setGuineidizhi(null);
                } else {
                    quDuanBaseEntity.setGuineidizhi(Integer.parseInt(guineidizhi));
                }
                dataStatsService.addQuDuan(quDuanBaseEntity);
            }
        } else {
            return ResponseDataUtil.error("请检查第" + number + "行的数据，有重复");
        }
        return ResponseDataUtil.ok("数据上传成功");
    }

    //导入区段的Excel模板数据
    @PostMapping("/QuDuabUpLoad")
    @ResponseBody
    public Map<String, Object> QuDuabUpLoad(@RequestParam("file") MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    if (!strings[0].equals("") || !strings[1].equals("") || !strings[2].equals("") || !strings[3].equals("") || !strings[4].equals("") || !strings[5].equals("")) {
                        String czid = strings[0];
                        String czname = strings[1];
                        String line = strings[2];
                        String leftright = strings[3];
                        String ofxianduan = strings[4];
                        String xingbie = strings[5];
                        String type = strings[6];
                        //System.out.println("11111111111");
                        String qdid = strings[7];
                        String zongheid = strings[8];
                        String quduanshejiname = strings[9];
                        String qudunyunyingname = strings[10];
                        String quduanlength = strings[11];
                        String carrier = strings[12];
                        String diduantype = strings[13];
                        String xianluqingkuang = strings[14];
                        //System.out.println("2222222222222");
                        String bianjie = strings[15];
                        String fenjiedianwhere = strings[16];
                        String zhanqufenjie = strings[17];
                        String jinzhanxinhaojiname = strings[18];
                        String xinhaojiorbiaozhiming = strings[19];
                        String xinhaojizhanpaiwhere = strings[20];
                        String xinhaojiewhere = strings[21];
                        String zuocejueyuantype = strings[22];
                        String youcejueyuantype = strings[23];
                        //System.out.println("33333333333333333");
                        String zhengxianhoufangquduanid = strings[24];
                        String zhengxianqianfangquduanid = strings[25];
                        String daochaguanlianquduan1id = strings[26];
                        String daochaguanlianquduan2id = strings[27];
                        String dianmahuaguidao = strings[28];
                        String guineidizhi = strings[29];
                        //System.out.println("44444444444444444");
                        Long cid = dataStatsService.findchezhanid(Long.parseLong(czid));//根据车站专用czid  查询对应的id
                        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByQuDuanYunYingName(qudunyunyingname);//根据车站cid  查询对应的区段数据
                        if (quDuanBaseEntityList.size() == 0) {
                            //System.out.println("55555555555555555");
                            Integer lastParentid = dataStatsService.findLastParentid();
                            QuDuanBaseEntity quDuanBaseEntity = new QuDuanBaseEntity();
                            quDuanBaseEntity.setCzid(Integer.parseInt(czid));
                            quDuanBaseEntity.setParentId(lastParentid);
                            //System.out.println("666666666666666666");
                            Integer xdid = dataStatsService.findxianduanid(Long.parseLong(czid));
                            quDuanBaseEntity.setXid(xdid);
                            quDuanBaseEntity.setCid(Integer.parseInt(cid.toString()));
                            quDuanBaseEntity.setQdid(Integer.parseInt(qdid));
                            quDuanBaseEntity.setLine(line);
                            //System.out.println("77777777777777777");
                            quDuanBaseEntity.setOfXianDuan(ofxianduan);
                            quDuanBaseEntity.setLeftRight(leftright);
                            if (xingbie.equals("上行")) {
                                quDuanBaseEntity.setXingBie(1);
                            }
                            if (xingbie.equals("下行")) {
                                quDuanBaseEntity.setXingBie(2);
                            }
                            if (xingbie.equals("——") || xingbie.equals("")) {
                                quDuanBaseEntity.setXingBie(0);
                            }
                            if (type.equals("接近")) {
                                quDuanBaseEntity.setType(1);
                            }
                            if (type.equals("离去")) {
                                quDuanBaseEntity.setType(2);
                            }
                            if (type.equals("——") || type.equals("")) {
                                quDuanBaseEntity.setType(0);
                            }
                            quDuanBaseEntity.setZongheId(zongheid);
                            quDuanBaseEntity.setQuduanshejiName(quduanshejiname);
                            quDuanBaseEntity.setQuduanyunyingName(qudunyunyingname);
                            if (quduanlength.equals("") || quduanlength.equals("——")) {
                                quDuanBaseEntity.setQuduanLength(null);
                            } else {
                                quDuanBaseEntity.setQuduanLength(Integer.parseInt(quduanlength));
                            }
                            //System.out.println("888888888888888888");
                            quDuanBaseEntity.setCarrier(carrier);
                            quDuanBaseEntity.setDiduanType(diduantype);
                            quDuanBaseEntity.setXianluqingkuang(xianluqingkuang);
                            if (bianjie.equals("") || bianjie.equals("否")) {
                                quDuanBaseEntity.setBianjie(0);
                            } else {
                                quDuanBaseEntity.setBianjie(1);
                            }
                            quDuanBaseEntity.setFenjiedianWhere(fenjiedianwhere);
                            // System.out.println("99999999999999999");
                            if (zhanqufenjie.equals("") || zhanqufenjie.equals("否")) {
                                quDuanBaseEntity.setZhanqufenjie(0);
                            } else {
                                quDuanBaseEntity.setZhanqufenjie(1);
                            }
                            quDuanBaseEntity.setJinzhanxinhaojiName(jinzhanxinhaojiname);
                            quDuanBaseEntity.setXinhaojiorbiaozhiming(xinhaojiorbiaozhiming);
                            quDuanBaseEntity.setXinhaobiaozhipaiWhere(xinhaojizhanpaiwhere);
                            quDuanBaseEntity.setXinhaojiWhere(xinhaojiewhere);
                            quDuanBaseEntity.setZuocejueyuanType(zuocejueyuantype);
                            quDuanBaseEntity.setYoucejueyuanType(youcejueyuantype);
                            //System.out.println("111111112222222222222");
                            quDuanBaseEntity.setZhengxianhoufangquduanId(zhengxianhoufangquduanid);
                            quDuanBaseEntity.setZhengxianqianfangquduanId(zhengxianqianfangquduanid);
                            //System.out.println("123");
                            quDuanBaseEntity.setDaochaguanlianquduan1Id(daochaguanlianquduan1id);
                            quDuanBaseEntity.setDaochaguanlianquduan2Id(daochaguanlianquduan2id);
                            System.out.println("456");
                            quDuanBaseEntity.setDianmahuaguihao(dianmahuaguidao);
                            if (guineidizhi.equals("")) {
                                quDuanBaseEntity.setGuineidizhi(null);
                            } else {
                                quDuanBaseEntity.setGuineidizhi(Integer.parseInt(guineidizhi));
                            }

                            dataStatsService.addQuDuan(quDuanBaseEntity);
                        }
                    }
                }
            }
        } catch (IOException e) {
            return ResponseDataUtil.error("文件上传失败");
        }
        return ResponseDataUtil.ok("文件上传成功");
    }

    //模板下载
    @GetMapping("/downloads")
    public void downloadFile(HttpServletResponse response) throws IOException {
        //String filePath ="C:\\data\\ruixing\\templates";
        String fileName = "车站信息配置模板.xlsx";
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        FileUploadUtil.getTemplateFile(response.getOutputStream(), fileName);
    }

    //车站的Excel数据预览
    @PostMapping("/CheZhanDatasYuLan")
    @ResponseBody
    public Map<String, Object> CheZhanDatasYuLan(@RequestParam("file") MultipartFile excelFile) {
        List<String[]> datas = new ArrayList<>();
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    if (!strings[0].equals("") || !strings[1].equals("") || !strings[2].equals("") || !strings[3].equals("") || !strings[4].equals("") || !strings[5].equals("")) {
                       /* System.out.println("1111"+Integer.valueOf(strings[0]).getClass());
                        System.out.println("1112"+strings[1].getClass().getTypeName());
                        System.out.println("1113"+Integer.valueOf(strings[2]).getClass().getTypeName());
                        System.out.println("1114"+strings[3].getClass().getTypeName());
                        System.out.println("1115"+Integer.valueOf(strings[4]).getClass().getTypeName());
                        System.out.println("1116"+strings[5].getClass().getTypeName());
                        System.out.println("1117"+strings[6].getClass().getTypeName());
                        System.out.println("1118"+Integer.valueOf(strings[7]).getClass().getTypeName());
                        System.out.println("1119"+Integer.valueOf(strings[11]).getClass().getTypeName());
                        System.out.println("11112"+Integer.valueOf(strings[12]).getClass().getTypeName());
                        System.out.println("11113"+Integer.valueOf(strings[13]).getClass().getTypeName());
                        System.out.println("11114"+Integer.valueOf(strings[14]).getClass().getTypeName());*/
                        /*if (Integer.valueOf(strings[0]).getClass().getTypeName().equals("java.lang.Integer")
                                && strings[1].getClass().getTypeName().equals("java.lang.String")
                                && Integer.valueOf(strings[2]).getClass().getTypeName().equals("java.lang.Integer")
                                && strings[3].getClass().getTypeName().equals("java.lang.String")
                                && Integer.valueOf(strings[4]).getClass().getTypeName().equals("java.lang.Integer")
                                && strings[5].getClass().getTypeName().equals("java.lang.String")
                                && strings[6].getClass().getTypeName().equals("java.lang.String")
                                && Integer.valueOf(strings[7]).getClass().getTypeName().equals("java.lang.Integer")
                                && Integer.valueOf(strings[11]).getClass().getTypeName().equals("java.lang.Integer")
                                && Integer.valueOf(strings[12]).getClass().getTypeName().equals("java.lang.Integer")
                                && Integer.valueOf(strings[13]).getClass().getTypeName().equals("java.lang.Integer")) {
                            String[] strings1 = strings;
                            datas.add(strings1);
                        } else {
                            return ResponseDataUtil.error("请选择正确的Excel数据");
                        }*/

                        String[] strings1 = strings;
                        datas.add(strings1);
                    }
                }
            }
        } catch (IOException e) {
            return ResponseDataUtil.error("文件上传失败");
        }
        return ResponseDataUtil.ok("文件上传成功", datas);
    }

    //上传车站的数据到数据库
    @PostMapping("/uploadCheZhanData")
    public Map<String, Object> uploadCheZhanData(@RequestBody JSONObject quDuanDatas) {
        JSONArray quDuanDatas1 = quDuanDatas.getJSONArray("quDuanDatas");
        Integer j = 0;
        List<Integer> number = new ArrayList<>();
        List<String[]> list = quDuanDatas1.toJavaList(String[].class);
        for (String[] stringss : list) {
            j++;
            String xdid = stringss[4];
            String czid = stringss[7];
            List<CheZhanEntity> cheZhanEntityList1 = dataStatsService.findCheZhanByIds(Integer.parseInt(czid), Integer.parseInt(xdid));//根据车站czid 和区段qdid 查询对应的车站数据
            if (cheZhanEntityList1.size() != 0) {
                number.add(j);
            }
        }
        if (number.size() == 0) {
            for (String[] strings : list) {
                String tljId = strings[0];
                String tljName = strings[1];
                List<TieLuJuEntity> tljname = dataStatsService.findAllTieLuJuByName(tljName);//查询铁路局表中是否有此铁路局
                System.out.println("铁路局个数" + tljname.size());
                TieLuJuEntity luJuEntity = new TieLuJuEntity();
                if (tljname.size() == 0) {//没有此铁路局
                    luJuEntity.setTljId(Long.parseLong(tljId));
                    luJuEntity.setTljName(tljName);
                    dataStatsService.addTieLuJU(luJuEntity);
                }
                String dwdid = strings[2];
                String dwdname = strings[3];
                List<DianWuDuanEntity> dianWuDuanEntityList = dataStatsService.findDianWuDuanBydid(Long.parseLong(dwdid));//查询电务段表中是否有此电务段
                System.out.println("电务段个数" + dianWuDuanEntityList.size());
                if (dianWuDuanEntityList.size() == 0) {//没有此电务段
                    Long tljid = dataStatsService.findTLJid(Long.parseLong(tljId));//获取上个铁路局的id
                    DianWuDuanEntity duanEntity = new DianWuDuanEntity();
                    duanEntity.setTljDwdId(tljid);
                    duanEntity.setDwdName(dwdname);
                    duanEntity.setDwdId(Long.parseLong(dwdid));
                    duanEntity.setTljId(Long.parseLong(strings[0]));
                    dataStatsService.addDianWuDuan(duanEntity);
                }
                String xdid = strings[4];
                String xdname = strings[5];
                String xdzgname = strings[6];
                List<XianDuanEntity> xianDuanEntityList = dataStatsService.findAllXianDuanByDwdid(Long.parseLong(dwdid));
                System.out.println("线段个数" + xianDuanEntityList.size());
                if (xianDuanEntityList.size() == 0) {
                    Long dwdid1 = dataStatsService.findDWDid(Long.parseLong(dwdid));
                    XianDuanEntity xianDuanEntity1 = new XianDuanEntity();
                    xianDuanEntity1.setDwdXdId(dwdid1);
                    xianDuanEntity1.setXdName(xdname);
                    xianDuanEntity1.setXdId(Long.parseLong(xdid));
                    xianDuanEntity1.setDwdId(Long.parseLong(strings[2]));
                    xianDuanEntity1.setXdZgName(xdzgname);
                    dataStatsService.addXianDuan(xianDuanEntity1);
                }
                String czid = strings[7];
                String czname = strings[8];
                String tuzhongjiancheng = strings[9];
                String cztype = strings[10];
                String tongxinbianmaguidaonumber = strings[11];
                String tongxinbianmazhanneinumber = strings[12];
                String jidianbianmaonetoonenumber = strings[13];
                String jidianbianmaNtOnenumber = strings[14];
                String jidianbianmaNtoOneshebeinumber = strings[15];
                String tongxinbianmazhanneidianmahuanumber = strings[16];
                String jidianNtoOneDianMaHuaNumber = strings[17];
                String jidianJiashiGuidaoNumber = strings[18];
                String jidianJiashiDianmahuaNumber = strings[19];
                String jiDianDianMaHuaNumber = strings[20];
                String yuliushebei1 = strings[21];
                String yuliushebei2 = strings[22];
                String yuliushebei3 = strings[23];
                String qujianBisaiType = strings[24];
                String isnoDuantou = strings[25];
                String linzhan1id = strings[26];
                String linzhan1name = strings[27];
                String linzhan1LineType = strings[28];
                String linzhan1OfXianduan = strings[29];
                String linzhan1benDWD = strings[30];
                String linzhan2id = strings[31];
                String linzhan2name = strings[32];
                String linzhan2LineType = strings[33];
                String linzhan2OfXianduan = strings[34];
                String linzhan2benDWD = strings[35];
                String linzhan3id = strings[36];
                String linzhan3name = strings[37];
                String linzhan3LineType = strings[38];
                String linzhan3OfXianduan = strings[39];
                String linzhan3benDWD = strings[40];
                String linzhan4id = strings[41];
                String linzhan4name = strings[42];
                String linzhan4LineType = strings[43];
                String linzhan4OfXianduan = strings[44];
                String linzhan4benDWD = strings[45];
                String linzhan5id = strings[46];
                String linzhan5name = strings[47];
                String linzhan5LineType = strings[48];
                String linzhan5OfXianduan = strings[49];
                String linzhan5benDWD = strings[50];
                String linzhan6id = strings[51];
                String linzhan6name = strings[52];
                String linzhan6LineType = strings[53];
                String linzhan6OfXianduan = strings[54];
                String linzhan6benDWD = strings[55];
                List<CheZhanEntity> cheZhanEntityList = dataStatsService.findallChezhanByName(czname);
                System.out.println("车站个数" + cheZhanEntityList.size());
                if (cheZhanEntityList.size() == 0) {
                    Long xianduanid = dataStatsService.findXDid(Long.parseLong(dwdid));
                    CheZhanEntity cheZhan = new CheZhanEntity();
                    cheZhan.setXdCzId(xianduanid);
                    cheZhan.setCzName(czname);
                    cheZhan.setCzId(Long.parseLong(czid));
                    cheZhan.setXdId(Long.parseLong(xdid));
                    cheZhan.setCzNameJianCheng(tuzhongjiancheng);
                    cheZhan.setCzType(cztype);
                    cheZhan.setTongxinbianmaguidaonumber(Integer.parseInt(tongxinbianmaguidaonumber));
                    cheZhan.setTongxinbianmazhanneioneguidaonumber(Integer.parseInt(tongxinbianmazhanneinumber));
                    cheZhan.setJidianonetooneguidaonumber(Integer.parseInt(jidianbianmaonetoonenumber));
                    cheZhan.setJidianntooneguidaonumber(Integer.parseInt(jidianbianmaNtOnenumber));
                    cheZhan.setJidianntooneshebeinumber(Integer.parseInt(jidianbianmaNtoOneshebeinumber));
                    cheZhan.setTongxinbianmadianmahuashebeinumber(Integer.parseInt(tongxinbianmazhanneidianmahuanumber));
                    cheZhan.setJidianntoonedianmahuashebeinumber(Integer.parseInt(jidianNtoOneDianMaHuaNumber));
                    cheZhan.setJidianjiashiguidaonumber(Integer.parseInt(jidianJiashiGuidaoNumber));
                    cheZhan.setJidianjiashidianmahuashebeinumber(Integer.parseInt(jidianJiashiDianmahuaNumber));
                    cheZhan.setJiDianDianMaHuaNumber(Integer.parseInt(jiDianDianMaHuaNumber));
                    cheZhan.setYuliushebei1(yuliushebei1);
                    cheZhan.setYuliushebei2(yuliushebei2);
                    cheZhan.setYuliushebei3(yuliushebei3);
                    cheZhan.setQujianbisaitype(qujianBisaiType);
                    if (isnoDuantou.equals("是")) {
                        cheZhan.setCzDuanTou(1);
                    } else {
                        cheZhan.setCzDuanTou(0);
                    }
                    if (linzhan1id.equals("——")) {
                        cheZhan.setLinzhan1id(null);
                    } else {
                        cheZhan.setLinzhan1id(Integer.parseInt(linzhan1id));
                    }
                    cheZhan.setLinzhan1name(linzhan1name);
                    cheZhan.setLinzhan1linetype(linzhan1LineType);
                    cheZhan.setLinzhan1ofxianduan(linzhan1OfXianduan);
                    cheZhan.setLinzhan1isnobendwd(linzhan1benDWD);

                    if (linzhan2id.equals("——")) {
                        cheZhan.setLinzhan2id(null);
                    } else {
                        cheZhan.setLinzhan2id(Integer.parseInt(linzhan2id));
                    }
                    cheZhan.setLinzhan2name(linzhan2name);
                    cheZhan.setLinzhan2linetype(linzhan2LineType);
                    cheZhan.setLinzhan2ofxianduan(linzhan2OfXianduan);
                    cheZhan.setLinzhan2isnobendwd(linzhan2benDWD);

                    if (linzhan3id.equals("——")) {
                        cheZhan.setLinzhan3id(null);
                    } else {
                        cheZhan.setLinzhan3id(Integer.parseInt(linzhan3id));
                    }
                    cheZhan.setLinzhan3name(linzhan3name);
                    cheZhan.setLinzhan3linetype(linzhan3LineType);
                    cheZhan.setLinzhan3ofxianduan(linzhan3OfXianduan);
                    cheZhan.setLinzhan3isnobendwd(linzhan3benDWD);

                    if (linzhan4id.equals("——")) {
                        cheZhan.setLinzhan4id(null);
                    } else {
                        cheZhan.setLinzhan4id(Integer.parseInt(linzhan4id));
                    }
                    cheZhan.setLinzhan4name(linzhan4name);
                    cheZhan.setLinzhan4linetype(linzhan4LineType);
                    cheZhan.setLinzhan4ofxianduan(linzhan4OfXianduan);
                    cheZhan.setLinzhan4isnobendwd(linzhan4benDWD);

                    if (linzhan5id.equals("——")) {
                        cheZhan.setLinzhan5id(null);
                    } else {
                        cheZhan.setLinzhan5id(Integer.parseInt(linzhan5id));
                    }
                    cheZhan.setLinzhan5name(linzhan5name);
                    cheZhan.setLinzhan5linetype(linzhan5LineType);
                    cheZhan.setLinzhan5ofxianduan(linzhan5OfXianduan);
                    cheZhan.setLinzhan5isnobendwd(linzhan5benDWD);

                    if (linzhan6id.equals("——")) {
                        cheZhan.setLinzhan6id(null);
                    } else {
                        cheZhan.setLinzhan6id(Integer.parseInt(linzhan6id));
                    }
                    cheZhan.setLinzhan6name(linzhan6name);
                    cheZhan.setLinzhan6linetype(linzhan6LineType);
                    cheZhan.setLinzhan6ofxianduan(linzhan6OfXianduan);
                    cheZhan.setLinzhan6isnobendwd(linzhan6benDWD);
                    dataStatsService.addCheZhan(cheZhan);
                }
            }
        } else {
            return ResponseDataUtil.error("请检查第" + number + "行的数据，车站有重复");
        }
        return ResponseDataUtil.ok("数据上传成功");
    }

    //导入Excel模板数据
    @PostMapping("/uploads")
    @ResponseBody
    public Map<String, Object> uploads(@RequestParam("file") MultipartFile excelFile) {
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    if (!strings[0].equals("") || !strings[1].equals("") || !strings[2].equals("") || !strings[3].equals("") || !strings[4].equals("") || !strings[5].equals("")) {
                        String tljId = strings[0];
                        String tljName = strings[1];
                        List<TieLuJuEntity> tljname = dataStatsService.findAllTieLuJuByName(tljName);//查询铁路局表中是否有此铁路局
                        System.out.println("铁路局个数" + tljname.size());
                        TieLuJuEntity luJuEntity = new TieLuJuEntity();
                        if (tljname.size() == 0) {//没有此铁路局
                            luJuEntity.setTljId(Long.parseLong(tljId));
                            luJuEntity.setTljName(tljName);
                            dataStatsService.addTieLuJU(luJuEntity);
                        }
                        String dwdid = strings[2];
                        String dwdname = strings[3];
                        List<DianWuDuanEntity> dianWuDuanEntityList = dataStatsService.findDianWuDuanBydid(Long.parseLong(dwdid));//查询电务段表中是否有此电务段
                        System.out.println("电务段个数" + dianWuDuanEntityList.size());
                        if (dianWuDuanEntityList.size() == 0) {//没有此电务段
                            Long tljid = dataStatsService.findTLJid(Long.parseLong(tljId));//获取上个铁路局的id
                            DianWuDuanEntity duanEntity = new DianWuDuanEntity();
                            duanEntity.setTljDwdId(tljid);
                            duanEntity.setDwdName(dwdname);
                            duanEntity.setDwdId(Long.parseLong(dwdid));
                            duanEntity.setTljId(Long.parseLong(strings[0]));
                            dataStatsService.addDianWuDuan(duanEntity);
                        }
                        String xdid = strings[4];
                        String xdname = strings[5];
                        String xdzgname = strings[6];
                        List<XianDuanEntity> xianDuanEntityList = dataStatsService.findAllXianDuanByDwdid(Long.parseLong(dwdid));
                        System.out.println("线段个数" + xianDuanEntityList.size());
                        if (xianDuanEntityList.size() == 0) {
                            Long dwdid1 = dataStatsService.findDWDid(Long.parseLong(dwdid));
                            XianDuanEntity xianDuanEntity1 = new XianDuanEntity();
                            xianDuanEntity1.setDwdXdId(dwdid1);
                            xianDuanEntity1.setXdName(xdname);
                            xianDuanEntity1.setXdId(Long.parseLong(xdid));
                            xianDuanEntity1.setDwdId(Long.parseLong(strings[2]));
                            xianDuanEntity1.setXdZgName(xdzgname);
                            dataStatsService.addXianDuan(xianDuanEntity1);
                        }
                        String czid = strings[7];
                        String czname = strings[8];
                        String tuzhongjiancheng = strings[9];
                        String cztype = strings[10];
                        String tongxinbianmaguidaonumber = strings[11];
                        String tongxinbianmazhanneinumber = strings[12];
                        String jidianbianmaonetoonenumber = strings[13];
                        String jidianbianmaNtOnenumber = strings[14];
                        String jidianbianmaNtoOneshebeinumber = strings[15];
                        String tongxinbianmazhanneidianmahuanumber = strings[16];
                        String jidianNtoOneDianMaHuaNumber = strings[17];
                        String jidianJiashiGuidaoNumber = strings[18];
                        String jidianJiashiDianmahuaNumber = strings[19];
                        String yuliushebei1 = strings[20];
                        String yuliushebei2 = strings[21];
                        String yuliushebei3 = strings[22];
                        String qujianBisaiType = strings[23];
                        String isnoDuantou = strings[24];
                        String linzhan1id = strings[25];
                        String linzhan1name = strings[26];
                        String linzhan1LineType = strings[27];
                        String linzhan1OfXianduan = strings[28];
                        String linzhan1benDWD = strings[29];
                        String linzhan2id = strings[30];
                        String linzhan2name = strings[31];
                        String linzhan2LineType = strings[32];
                        String linzhan2OfXianduan = strings[33];
                        String linzhan2benDWD = strings[34];
                        String linzhan3id = strings[35];
                        String linzhan3name = strings[36];
                        String linzhan3LineType = strings[37];
                        String linzhan3OfXianduan = strings[38];
                        String linzhan3benDWD = strings[39];
                        String linzhan4id = strings[40];
                        String linzhan4name = strings[41];
                        String linzhan4LineType = strings[42];
                        String linzhan4OfXianduan = strings[43];
                        String linzhan4benDWD = strings[44];
                        String linzhan5id = strings[45];
                        String linzhan5name = strings[46];
                        String linzhan5LineType = strings[47];
                        String linzhan5OfXianduan = strings[48];
                        String linzhan5benDWD = strings[49];
                        String linzhan6id = strings[50];
                        String linzhan6name = strings[51];
                        String linzhan6LineType = strings[52];
                        String linzhan6OfXianduan = strings[53];
                        String linzhan6benDWD = strings[54];
                        List<CheZhanEntity> cheZhanEntityList = dataStatsService.findallChezhanByName(czname);
                        System.out.println("车站个数" + cheZhanEntityList.size());
                        if (cheZhanEntityList.size() == 0) {
                            Long xianduanid = dataStatsService.findXDid(Long.parseLong(dwdid));
                            CheZhanEntity cheZhan = new CheZhanEntity();
                            cheZhan.setXdCzId(xianduanid);
                            cheZhan.setCzName(czname);
                            cheZhan.setCzId(Long.parseLong(czid));
                            cheZhan.setXdId(Long.parseLong(xdid));
                            cheZhan.setCzNameJianCheng(tuzhongjiancheng);
                            cheZhan.setCzType(cztype);
                            cheZhan.setTongxinbianmaguidaonumber(Integer.parseInt(tongxinbianmaguidaonumber));
                            cheZhan.setTongxinbianmazhanneioneguidaonumber(Integer.parseInt(tongxinbianmazhanneinumber));
                            cheZhan.setJidianonetooneguidaonumber(Integer.parseInt(jidianbianmaonetoonenumber));
                            cheZhan.setJidianntooneguidaonumber(Integer.parseInt(jidianbianmaNtOnenumber));
                            cheZhan.setJidianntooneshebeinumber(Integer.parseInt(jidianbianmaNtoOneshebeinumber));
                            cheZhan.setTongxinbianmadianmahuashebeinumber(Integer.parseInt(tongxinbianmazhanneidianmahuanumber));
                            cheZhan.setJidianntoonedianmahuashebeinumber(Integer.parseInt(jidianNtoOneDianMaHuaNumber));
                            cheZhan.setJidianjiashiguidaonumber(Integer.parseInt(jidianJiashiGuidaoNumber));
                            cheZhan.setJidianjiashidianmahuashebeinumber(Integer.parseInt(jidianJiashiDianmahuaNumber));
                            cheZhan.setYuliushebei1(yuliushebei1);
                            cheZhan.setYuliushebei2(yuliushebei2);
                            cheZhan.setYuliushebei3(yuliushebei3);
                            cheZhan.setQujianbisaitype(qujianBisaiType);
                            if (isnoDuantou.equals("是")) {
                                cheZhan.setCzDuanTou(1);
                            } else {
                                cheZhan.setCzDuanTou(0);
                            }
                            if (linzhan1id.equals("——")) {
                                cheZhan.setLinzhan1id(null);
                            } else {
                                cheZhan.setLinzhan1id(Integer.parseInt(linzhan1id));
                            }
                            cheZhan.setLinzhan1name(linzhan1name);
                            cheZhan.setLinzhan1linetype(linzhan1LineType);
                            cheZhan.setLinzhan1ofxianduan(linzhan1OfXianduan);
                            cheZhan.setLinzhan1isnobendwd(linzhan1benDWD);

                            if (linzhan2id.equals("——")) {
                                cheZhan.setLinzhan2id(null);
                            } else {
                                cheZhan.setLinzhan2id(Integer.parseInt(linzhan2id));
                            }
                            cheZhan.setLinzhan2name(linzhan2name);
                            cheZhan.setLinzhan2linetype(linzhan2LineType);
                            cheZhan.setLinzhan2ofxianduan(linzhan2OfXianduan);
                            cheZhan.setLinzhan2isnobendwd(linzhan2benDWD);

                            if (linzhan3id.equals("——")) {
                                cheZhan.setLinzhan3id(null);
                            } else {
                                cheZhan.setLinzhan3id(Integer.parseInt(linzhan3id));
                            }
                            cheZhan.setLinzhan3name(linzhan3name);
                            cheZhan.setLinzhan3linetype(linzhan3LineType);
                            cheZhan.setLinzhan3ofxianduan(linzhan3OfXianduan);
                            cheZhan.setLinzhan3isnobendwd(linzhan3benDWD);

                            if (linzhan4id.equals("——")) {
                                cheZhan.setLinzhan4id(null);
                            } else {
                                cheZhan.setLinzhan4id(Integer.parseInt(linzhan4id));
                            }
                            cheZhan.setLinzhan4name(linzhan4name);
                            cheZhan.setLinzhan4linetype(linzhan4LineType);
                            cheZhan.setLinzhan4ofxianduan(linzhan4OfXianduan);
                            cheZhan.setLinzhan4isnobendwd(linzhan4benDWD);

                            if (linzhan5id.equals("——")) {
                                cheZhan.setLinzhan5id(null);
                            } else {
                                cheZhan.setLinzhan5id(Integer.parseInt(linzhan5id));
                            }
                            cheZhan.setLinzhan5name(linzhan5name);
                            cheZhan.setLinzhan5linetype(linzhan5LineType);
                            cheZhan.setLinzhan5ofxianduan(linzhan5OfXianduan);
                            cheZhan.setLinzhan5isnobendwd(linzhan5benDWD);

                            if (linzhan6id.equals("——")) {
                                cheZhan.setLinzhan6id(null);
                            } else {
                                cheZhan.setLinzhan6id(Integer.parseInt(linzhan6id));
                            }
                            cheZhan.setLinzhan6name(linzhan6name);
                            cheZhan.setLinzhan6linetype(linzhan6LineType);
                            cheZhan.setLinzhan6ofxianduan(linzhan6OfXianduan);
                            cheZhan.setLinzhan6isnobendwd(linzhan6benDWD);
                            dataStatsService.addCheZhan(cheZhan);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseDataUtil.error("文件上传失败");
        }
        return ResponseDataUtil.ok("文件上传成功");
    }


    //根据车站id 判断是否有站内数据
    @GetMapping("/findNumBycid/{id}")
    public Map<String,Object>findNumBycid(@PathVariable Integer id){
        Integer number=dataStatsService.findNumBycid(id);
        return ResponseDataUtil.ok("查询成功",number);
    }
    //查询所有车站信息
    @GetMapping("/findAll")
    public Result findAll() {
        List<DataStatsEntity> list = dataStatsService.findAll();
        return new Result(true, "查询数据成功", list);

    }

    //分页查询
    @GetMapping("/findPage/{page}/{size}")
    public PageInfo<DataStatsEntity> findPage(@PathVariable Integer page, @PathVariable Integer size) {
        PageInfo<DataStatsEntity> pageInfo = dataStatsService.findPage(page, size);
        return pageInfo;
    }

    //批量删除车站
    @DeleteMapping("/delCheZhanListById/{ids}")
    public Map<String, Object> delCheZhanListById(@PathVariable int[] ids) {
        dataStatsService.delCheZhanListById(ids);
        return ResponseDataUtil.ok("批量删除车站信息成功");
    }

    //查询所有的车站  展示在列表
    @RequestMapping("/findAllCheZhan")
    public Map<String, Object> findAllCheZhan(@RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(value = "size", required = false) Integer size) {

        PageHelper.startPage(page, size);
        List<DataStatsEntity> dataStatEntities = dataStatsService.findAllCheZhan(page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<>(dataStatEntities);
        return ResponseDataUtil.ok("查询所有车站信息成功", pageInfo);
    }

    //根据铁路局id  查询此铁路局下的所有车站
    @GetMapping("/findTieLuJuById/{tid}")
    public Map<String, Object> findTieLuJuById(@PathVariable Long tid,
                                               @RequestParam(value = "page", required = false) Integer
                                                       page, @RequestParam(value = "size", required = false) Integer size) {
        //JSONObject jo = new JSONObject();
        //List<DataStatsEntity> tieLuJuEntity = dataStatsService.findTieLuJuById(tid, page, size);
        //jo.put("tieLuJuEntity", tieLuJuEntity);
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all = dataStatsService.findTieLuJuById(tid, page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<>(all);
        //jo.put("pageInfo", pageInfo);
        //System.out.println("分页为" + pageInfo);
        return ResponseDataUtil.ok("查询铁路局成功", pageInfo);
    }

    //根据电务段id  查询此电务段下的所有车站
    @GetMapping("/findDianWuDuanCheZhanById/{did}")
    public Map<String, Object> findDianWuDuanById(@PathVariable Long did,
                                                  @RequestParam(value = "page", required = false) Integer
                                                          page, @RequestParam(value = "size", required = false) Integer size) {

        PageHelper.startPage(page, size);
        List<DataStatsEntity> all = dataStatsService.findDianWuDuanCheZhanById(did, page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<>(all);
        return ResponseDataUtil.ok("查询铁路局成功", pageInfo);
    }

    //根据线段id  查询此线段下的所有车站
    @GetMapping("/findXianDuanCheZhanById/{xid}")
    public Map<String, Object> findXianDuanCheZhanById(@PathVariable Long xid,
                                                       @RequestParam(value = "page", required = false) Integer
                                                               page, @RequestParam(value = "size", required = false) Integer size) {

        PageHelper.startPage(page, size);
        List<DataStatsEntity> all = dataStatsService.findXianDuanCheZhanById(xid, page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<>(all);
        return ResponseDataUtil.ok("查询铁路局成功", pageInfo);
    }

    //根据车站id  查询车站信息
    @GetMapping("/findCheZhanById/{cid}")
    public Map<String, Object> findCheZhanById(@PathVariable Long cid,
                                               @RequestParam(value = "page", required = false) Integer page,
                                               @RequestParam(value = "size", required = false) Integer size) {
        PageHelper.startPage(page, size);
        List<DataStatsEntity> all = dataStatsService.findCheZhanById(cid, page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<>(all);
        return ResponseDataUtil.ok("查询铁路局成功", pageInfo);
    }


    //根据线段id更改状态
    @PutMapping("/editStateByXid/{xid}")
    public Map<String, Object> editStateByXid(@PathVariable Integer xid, XianDuanEntity xianDuanEntity) {
        dataStatsService.editStateByXid(xianDuanEntity);
        return ResponseDataUtil.ok("更改线段状态成功");
    }

    //根据车站id更改状态
    @PutMapping("/editStateByCid/{cid}")
    public Map<String, Object> editStateByCid(@PathVariable Integer cid, CheZhanEntity cheZhanEntity) {
        dataStatsService.editStateByCid(cheZhanEntity);
        return ResponseDataUtil.ok("更改车站状态成功");
    }

    //根据车站id更改站内电码化的状态
    @PutMapping("/editDMHStateByCid/{cid}")
    public Map<String, Object> editDMHStateByCid(@PathVariable Integer cid, CheZhanEntity cheZhanEntity) {
        dataStatsService.editDMHStaCteByCid(cheZhanEntity);
        return ResponseDataUtil.ok("更改站内电码化状态成功");
    }

    //根据线段id 清除json  和更改状态
    @PutMapping("/qingChuaByXid/{xid}")
    public Map<String, Object> qingChuaByXid(@PathVariable Integer xid, XianDuanEntity xianDuanEntity) {
        dataStatsService.qingChuaByXid(xianDuanEntity);
        return ResponseDataUtil.ok("清除线段成功");
    }

    //根据车站id  清除json  和更改状态
    @PutMapping("/qingChuaByCid/{cid}")
    public Map<String, Object> qingChuaByCid(@PathVariable Integer cid, CheZhanEntity cheZhanEntity) {
        dataStatsService.qingChuaByCid(cheZhanEntity);
        return ResponseDataUtil.ok("清除车站成功");
    }

    //根据车站id  清除站内电码化的 json  和更改状态
    @PutMapping("/qingChuaDMHByCid/{cid}")
    public Map<String, Object> qingChuaDMHByCid(@PathVariable Integer cid, CheZhanEntity cheZhanEntity) {
        dataStatsService.qingChuaDMHByCid(cheZhanEntity);
        return ResponseDataUtil.ok("清除站内电码化成功");
    }

    //根据车站id  查询对应的电码化数据
    @GetMapping("/findDianMaHuaByCid/{cid}")
    public Map<String, Object> findDianMaHuaByCid(@PathVariable Integer cid) {
        List<QuDuanBaseEntity> dianMaHuaList = dataStatsService.findDianMaHuaByCid(cid);
        if (dianMaHuaList.size() == 0) {
            return ResponseDataUtil.ok("此车站无电码化");
        } else {
            return ResponseDataUtil.ok("查询电码化成功", dianMaHuaList);
        }
    }

    //新增线段配置 根据线段xid 查询此线段下的所有车站数据
    @GetMapping("/findSomeCheZhanByXid/{xid}")
    public Map<String, Object> findSomeCheZhanByXid(@PathVariable Integer xid) {
        JSONObject js = new JSONObject();
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findSomeCheZhanByXid(xid);
        //根据线段xid 查询此线段下的头车站id和尾车站id
        Integer firstCZid = dataStatsService.findFirstCZid(xid);
        List<CheZhanEntity> startCheZhanEntities = dataStatsService.findStartCheZhan(firstCZid);
        Integer endCZid = dataStatsService.findEndCZid(xid);
        List<CheZhanEntity> endCheZhanEntities = dataStatsService.findEndCheZhan(endCZid);
        js.put("chezhan", cheZhanEntities);
        js.put("startCheZhan", startCheZhanEntities);
        js.put("endCheZhan", endCheZhanEntities);
        return ResponseDataUtil.ok("查询对应车站数据成功", js);
    }

    //新增车站配置  根据车站cid  查询此车站下的所有区段数据
    @GetMapping("/findSomeQuDuanByCid/{cid}")
    public Map<String, Object> findSomeQuDuanByCid(@PathVariable Long cid) {
        JSONObject js = new JSONObject();
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByCid(cid);
        js.put("quDuan", quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询对应的区段数据成功", js);
    }

    //根据线段xid 查询此线段下的线段配置json数据
    @GetMapping("/findXDJsonByXid/{xid}")
    public Map<String, Object> findXDJsonByXid(@PathVariable Integer xid) {
        String xdJson = dataStatsService.findXDJsonByXid(xid);
        //js.put("xdJson",xdJson);
        return ResponseDataUtil.ok("查询线段的json数据成功", xdJson);
    }

    //根据车站cid 查询此车站下的区段配置json数据
    @GetMapping("/findQDJsonByCid/{cid}")
    public Map<String, Object> findQDJsonByCid(@PathVariable Integer cid) {
        String qdJson = dataStatsService.findQDJsonByCid(cid);
        return ResponseDataUtil.ok("查询区段的json数据成功", qdJson);
    }

    //根据车站cid 查询此车站内电码化的区段配置json数据
    @GetMapping("/findDMHJsonByCid/{cid}")
    public Map<String, Object> findDMHJsonByCid(@PathVariable Integer cid) {
        String dmhJson = dataStatsService.findDMHJsonByCid(cid);
        return ResponseDataUtil.ok("查询区段的json数据成功", dmhJson);
    }


    //查询所有的铁路局的名字  和 id
    @GetMapping("/findAllTieLuJu")
    public Map<String, Object> findAllTieLuJu() {
        List<TieLuJuEntity> tieLuJuEntities = dataStatsService.findAllTieLuJu();
        return ResponseDataUtil.ok("查询铁路局成功", tieLuJuEntities);
    }

    //根据铁路局的id  查询对应的电务段
    @GetMapping("/findDianWuDuanByTid/{tid}")
    public Map<String, Object> findDianWuDuanByTid(@PathVariable Integer tid) {
        List<DianWuDuanEntity> dianWuDuanEntities = dataStatsService.findDianWuDuanByTid(tid);
        return ResponseDataUtil.ok("查询电务段成功", dianWuDuanEntities);
    }

    //根据电务段的id  查找线段的名字和id
    @GetMapping("/findXianDuanByDid/{did}")
    public Map<String, Object> findXianDuanByDid(@PathVariable Integer did) {
        List<XianDuanEntity> xianDuanEntities = dataStatsService.findXianDuanByDid(did);
        return ResponseDataUtil.ok("查询线段成功", xianDuanEntities);
    }

    //根据线段的id  查找车站的名字和id
    @GetMapping("/findCheZhanByXid/{xid}")
    public Map<String, Object> findCheZhanByXid(@PathVariable Integer xid) {
        List<CheZhanEntity> cheZhanEntities = dataStatsService.findCheZhanByXid(xid);
        return ResponseDataUtil.ok("查询车站成功", cheZhanEntities);
    }


    //查找所有的电务段名和id
    @GetMapping("/findAllDianWuDuan")
    public Map<String,Object>findAllDianWuDuan(){
        List<DianWuDuanEntity> duanEntityList=dataStatsService.findAllDianWuDuan();
        return ResponseDataUtil.ok("查询电务段成功",duanEntityList);
    }



    //查询站外所有的区段信息  排除电码化
    @GetMapping("/findAllQuDuan")
    public Map<String, Object> findAllQuDuan(@RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "size", required = false) Integer size) {

        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findAllQuDuan(page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntities);
        return ResponseDataUtil.ok("查询所有站外区段成功", quDuanBaseEntityPageInfo);
    }

    //查询所有的电码化区段
    @GetMapping("/findAllDianMaHua")
    public Map<String, Object> findAllDianMaHua(@RequestParam(value = "page", required = false) Integer page,
                                                @RequestParam(value = "size", required = false) Integer size) {
        JSONObject jo = new JSONObject();
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findAllDianMaHua(page, size);
        jo.put("quDuanBaseEntities", quDuanBaseEntities);
        PageInfo<QuDuanBaseEntity> pageInfo = new PageInfo<>(quDuanBaseEntities);
        jo.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询电码化成功", jo);
    }

    //根据车站的cid  查找本车站下的所有站外的区段
    @GetMapping("/findAllQuDuanByCid/{cid}")
    public Map<String, Object> findAllQuDuanByCid(@PathVariable Integer cid,
                                                  @RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "size", required = false) Integer size) {
        JSONObject jo = new JSONObject();
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findAllQuDuanByCid(cid, page, size);
        jo.put("quDuanBaseEntities", quDuanBaseEntities);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntities);
        jo.put("quDuanBaseEntityPageInfo", quDuanBaseEntityPageInfo);
        return ResponseDataUtil.ok("查询所有站外区段成功", quDuanBaseEntityPageInfo);
    }

    //根据车站的cid  查询所有的电码化区段

    @GetMapping("/findAllDianMaHuaByCid/{cid}")
    public Map<String, Object> findAllDianMaHuaByCid(@PathVariable Integer cid,
                                                     @RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        JSONObject jo = new JSONObject();
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntities = dataStatsService.findAllDianMaHuaByCid(cid, page, size);
        jo.put("quDuanBaseEntities", quDuanBaseEntities);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntities);
        jo.put("quDuanBaseEntityPageInfo", quDuanBaseEntityPageInfo);
        return ResponseDataUtil.ok("查询所有站外区段成功", quDuanBaseEntityPageInfo);
    }

    //新增站外区段  包括电码化
    @PostMapping("/addQuDuan")
    public Map<String, Object> addQuDuan(QuDuanBaseEntity quDuanBaseEntity) {
        dataStatsService.addQuDuan(quDuanBaseEntity);
        return ResponseDataUtil.ok("添加站外区段数据成功");
    }

    //根据区段id编辑区段信息  包括电码化
    @PutMapping("/editQuDuanById/{id}")
    public Map<String, Object> editQuDuanById(@PathVariable Integer id, QuDuanBaseEntity quDuanBaseEntity) {
        dataStatsService.editQuDuanById(quDuanBaseEntity);
        return ResponseDataUtil.ok("修改区段信息成功");
    }

    //根据区段id删除区段信息
    @DeleteMapping("/deletQuDuanById/{id}")
    public Map<String, Object> deletQuDuanById(@PathVariable Integer id) {
        dataStatsService.deletQuDuanById(id);
        return ResponseDataUtil.ok("删除区段数据成功");
    }

    //根据id批量删除区段数据
    @DeleteMapping("/deletQuDuanByIds/{ids}")
    public Map<String, Object> deletQuDuanByIds(@PathVariable Integer[] ids) {
        dataStatsService.deletQuDuanByIds(ids);
        return ResponseDataUtil.ok("批量删除成功");
    }

    //根据铁路局tid 查询此铁路局下所有的区段
    @GetMapping("/findQuDuanByTid/{tid}")
    public Map<String, Object> findQuDuanByTid(@PathVariable Integer tid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByTid(tid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据电务段Did 查询此电务段下所有的区段
    @GetMapping("/findQuDuanByDid/{did}")
    public Map<String, Object> findQuDuanByDid(@PathVariable Integer did, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByDid(did, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据线段Xid 查询此线段下所有的区段
    @GetMapping("/findQuDuanByXid/{xid}")
    public Map<String, Object> findQuDuanByXid(@PathVariable Integer xid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanByXid(xid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据车站Cid 查询此车站下所有的区段
    @GetMapping("/findQuDuanBycid/{cid}")
    public Map<String, Object> findQuDuanBycid(@PathVariable Integer cid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findQuDuanBycid(cid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }


    //根据铁路局tid 查询此铁路局下所有的电码化区段
    @GetMapping("/findDianMaHuaByTid/{tid}")
    public Map<String, Object> findDianMaHuaByTid(@PathVariable Integer tid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findDianMaHuaByTid(tid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据电务段Did 查询此电务段下所有的电码化区段
    @GetMapping("/findDianMaHuaByDid/{did}")
    public Map<String, Object> findDianMaHuaByDid(@PathVariable Integer did, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findDianMaHuaByDid(did, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据线段Xid 查询此线段下所有的电码化区段
    @GetMapping("/findDianMaHuaByXid/{xid}")
    public Map<String, Object> findDianMaHuaByXid(@PathVariable Integer xid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findDianMaHuaByXid(xid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }

    //根据车站Cid 查询此车站下所有的电码化区段
    @GetMapping("/findDianMaHuaBycid/{cid}")
    public Map<String, Object> findDianMaHuaBycid(@PathVariable Integer cid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<QuDuanBaseEntity> quDuanBaseEntityList = dataStatsService.findDianMaHuaBycid(cid, page, size);
        PageInfo<QuDuanBaseEntity> quDuanBaseEntityPageInfo = new PageInfo<>(quDuanBaseEntityList);
        return ResponseDataUtil.ok("查询铁路局下所有的区段数据成功", quDuanBaseEntityPageInfo);
    }


























/*
    //根据id查询铁路局下的电务段
    @PostMapping("/findDianWuDuanById/{tid}/{did}")
    public Map<String, Object> findDianWuDuanById(@PathVariable Long tid, @PathVariable Long did,
                                                  @RequestParam Integer page, @RequestParam Integer size) {

        List<DataStatsEntity> dataStatEntities = dataStatsService.findDianWuDuanById(tid, did, page, size);
        List<DataStatsEntity> list = new ArrayList<>();
        for (DataStatsEntity dataStat : dataStatEntities) {
            if (dataStat.getTid() == tid && dataStat.getDid() == did) {
                list.add(dataStat);
                System.out.println("查询结果是" + list);
            }
        }
        JSONObject jo = new JSONObject();
        jo.put("list", list);
        PageHelper.startPage(page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(list);
        System.out.println("pageInfo" + pageInfo);
        jo.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询电务段信息成功", jo);
    }

    //根据id查询铁路局下的电务段下的线段
    @PostMapping("/findXianDuanById/{tid}/{did}/{xid}")
    public Map<String, Object> findXianDuanById(@PathVariable Long tid, @PathVariable Long did, @PathVariable Long xid,
                                                @RequestParam Integer page, @RequestParam Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsService.findXianDuanById(tid, did, xid, page, size);
        List<DataStatsEntity> list = new ArrayList<>();
        for (DataStatsEntity dataStat : dataStatEntities) {
            if (dataStat.getTid() == tid && dataStat.getDid() == did && dataStat.getXid() == xid) {
                list.add(dataStat);
            }
        }
        JSONObject jo = new JSONObject();
        jo.put("list", list);
        PageHelper.startPage(page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(list);
        System.out.println("pageInfo" + pageInfo);
        jo.put("pageInfo", pageInfo);
        return ResponseDataUtil.ok("查询线段信息成功", jo);
    }

    //根据id查询铁路局下的电务段下的线段的车站
    @PostMapping("/findCheZhanById/{tid}/{did}/{xid}/{cid}")
    public Map<String, Object> findCheZhanById(@PathVariable Long tid, @PathVariable Long did, @PathVariable Long xid, @PathVariable Long cid,
                                               @RequestParam Integer page, @RequestParam Integer size) {
        List<DataStatsEntity> dataStatEntities = dataStatsService.findCheZhanById(tid, did, xid, cid, page, size);
        JSONObject jo = new JSONObject();
        jo.put("dataStatEntities", dataStatEntities);
        PageHelper.startPage(page, size);
        PageInfo<DataStatsEntity> pageInfo = new PageInfo<DataStatsEntity>(dataStatEntities);
        jo.put("pageInfo", pageInfo);
        System.out.println("分页为" + pageInfo);
        return ResponseDataUtil.ok("查询车站信息成功", jo);
    }*/

}
