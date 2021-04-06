package com.yintu.ruixing.guzhangzhenduan;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.yintu.ruixing.common.util.ResponseDataUtil;
import com.yintu.ruixing.common.util.StringUtil;
import com.yintu.ruixing.guzhangzhenduan.QuDuanShuXingEntity;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	@Autowired
	private MenXianService menXianService;

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
		return ResponseDataUtil.ok("查询属性名成功", quDuanShuXingEntities);
	}

	//获取区段的属性名
	@GetMapping("/kaiguanliang")
	public Map<String, Object> kaiguanliang() {
		List<QuDuanShuXingEntity> quDuanShuXingEntities = quXianService.kaiguanliang();
		return ResponseDataUtil.ok("查询开关量成功", quDuanShuXingEntities);
	}


	//电码化实时曲线  没用
	@GetMapping("/findDMHQuDuanShiShiData")
	public Map<String, Object> findDMHQuDuanShiShiData(@RequestParam("shuxingId") Integer[] shuxingId,
													   @RequestParam("quduanName") String[] quduanName,
													   @RequestParam("types") Integer[] types,
													   @RequestParam("mids") Integer[] mids,
													   Integer czid, Integer times) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, today);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			JSONObject js = new JSONObject();
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
			long cZid = czid.longValue();
			if (cheZhanService.findCzStutrs(cZid, false)) {
				List aa = new ArrayList<>();
				JSONObject ss = new JSONObject();
				ss.put("data", aa);
				return ss;
			} else {
				Integer k = 0;
				for (int i = 0; i < sqlname.size(); i++) {
					List<Object> listt1 = new ArrayList<>();
					k++;
					System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
					String shuxingname = sqlname.get(i);
					System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
					String quduanname = quduanName[i];
					Integer qdid = quXianService.findQDid(quduanname, czid);
					Integer type = types[i];//区段类型
					Integer mid = mids[i];//本区段对应的最大值和最小值id
					String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
					System.out.println("maxNumbers=" + maxNumbers);
					String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
					System.out.println("maxNumbers_k=" + maxNumbers_k);
					String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
					System.out.println("maxNumbers_z=" + maxNumbers_z);
					String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
					System.out.println("minNumbers=" + minNumbers);
					String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
					System.out.println("minNumbers_k=" + minNumbers_k);
					String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
					System.out.println("minNumbers_z=" + minNumbers_z);
					List<quduanEntity> date = quXianService.findDMHQuDuanShiShiData(shuxingname, quduanname, qdid, tableName);
					for (quduanEntity quduanEntity : date) {
						List<Object> list1 = new ArrayList();
						Integer createtime = quduanEntity.getCreatetime();
						BigDecimal numValue = quduanEntity.getNumValue();
						list1.add(createtime * 1000L);
						list1.add(numValue);
						listt1.add(list1);
					}
					if (k == 1) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 2) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 3) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 4) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 5) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 6) {

						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));

						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 7) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 8) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
				}
			}
			return ResponseDataUtil.ok("查询数据成功", js);
		}
	}

	//电码化日曲线  没用
	//根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
	@GetMapping("/findDMHQuDuanDayData")
	public Map<String, Object> findDMHQuDuanDayData(@RequestParam("dayTime") Date dayTime,
													@RequestParam("shuxingId") Integer[] shuxingId,
													@RequestParam("quduanName") String[] quduanName,
													@RequestParam("types") Integer[] types,
													@RequestParam("mids") Integer[] mids,
													Integer czid) throws Exception {
		String tableName = StringUtil.getTableName(czid, dayTime);
		if (shuxingId.length == 0 || quduanName.length == 0 || dayTime == null) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			JSONObject js = new JSONObject();
			Long endtime = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
			Date today = new Date();
			Date nowday = simpleDateFormat.parse(simpleDateFormat.format(today));//当前的年月日
			long todaytime = today.getTime() / 1000;//当前时间转换为秒
			long nowdaytime = nowday.getTime() / 1000;//当前年月日转化为秒
			long statrtime = dayTime.getTime() / 1000;
			if (nowdaytime != statrtime) {
				endtime = statrtime + 86399;
			} else {
				endtime = todaytime;
			}
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
			Integer k = 0;
			for (int i = 0; i < sqlname.size(); i++) {
				List<Object> listt1 = new ArrayList<>();
				k++;
				System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
				String shuxingname = sqlname.get(i);
				System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
				String quduanname = quduanName[i];
				Integer qdid = quXianService.findQDid(quduanname, czid);
				Integer type = types[i];//区段类型
				Integer mid = mids[i];//本区段对应的最大值和最小值id
				String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
				String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
				String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
				String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
				String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
				String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
				List<quduanEntity> date = quXianService.findOneQuDuanDatas(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
				// System.out.println("1234" + date);
				for (quduanEntity quduanEntity : date) {
					List<Object> list1 = new ArrayList();
					Integer createtime = quduanEntity.getCreatetime();
					BigDecimal numValue = quduanEntity.getNumValue();
					list1.add(createtime * 1000L);
					list1.add(numValue);
					listt1.add(list1);
				}
				if (k == 1) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 2) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 3) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 4) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 5) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 6) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 7) {

					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));

					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 8) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}

			}
			return ResponseDataUtil.ok("查询数据成功", js);
		}
	}

	//区段实时曲线  没用
	@GetMapping("/findQuDuanShiShiData")
	public Map<String, Object> findQuDuanShiShiData(@RequestParam("shuxingId") Integer[] shuxingId,
													@RequestParam("quduanName") String[] quduanName,
													@RequestParam("types") Integer[] types,
													@RequestParam("mids") Integer[] mids,
													Integer czid, Integer times) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, today);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			JSONObject js = new JSONObject();
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
			long cZid = czid.longValue();
			if (!cheZhanService.findCzStutrs(cZid, false)) {
				List aa = new ArrayList<>();
				JSONObject ss = new JSONObject();
				ss.put("data", aa);
				return ss;
			} else {
				Integer k = 0;
				for (int i = 0; i < sqlname.size(); i++) {
					List<Object> listt1 = new ArrayList<>();
					k++;
					System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
					String shuxingname = sqlname.get(i);
					System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
					String quduanname = quduanName[i];
					Integer qdid = quXianService.findQDid(quduanname, czid);
					Integer type = types[i];//区段类型
					Integer mid = mids[i];//本区段对应的最大值和最小值id
					String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
					String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
					String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
					String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
					String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
					String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
					List<quduanEntity> date = quXianService.findQuDuanShiShiData(shuxingname, quduanname, qdid, tableName);
					for (quduanEntity quduanEntity : date) {
						List<Object> list1 = new ArrayList();
						Integer createtime = quduanEntity.getCreatetime();
						BigDecimal numValue = quduanEntity.getNumValue();
						list1.add(createtime * 1000L);
						list1.add(numValue);
						listt1.add(list1);
					}
					if (k == 1) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 2) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 3) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 4) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 5) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 6) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 7) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
					if (k == 8) {
						js.put("shuju" + k.toString(), listt1);
						js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
						if (type == 0 || type == 1) {
							if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
								js.put("max" + k.toString(), maxNumbers);
							}
							if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
								js.put("max_k" + k.toString(), maxNumbers_k);
							}
							if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
								js.put("max_z" + k.toString(), maxNumbers_z);
							}
							if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
								js.put("min" + k.toString(), minNumbers);
							}
							if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
								js.put("min_k" + k.toString(), minNumbers_k);
							}
							if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
								js.put("min_z" + k.toString(), minNumbers_z);
							}
						}
					}
				}
				return ResponseDataUtil.ok("查询数据成功", js);
			}
		}
	}

	//区段日曲线  没用
	//根据传进来的区段id 和本区段所选择的属性id  包括传进来的日期获取对应的数据
	@GetMapping("/findQuDuanDayData")
	public Map<String, Object> findQuDuanDayData(@RequestParam("dayTime") Date dayTime,
												 @RequestParam("shuxingId") Integer[] shuxingId,
												 @RequestParam("quduanName") String[] quduanName,
												 @RequestParam("types") Integer[] types,
												 @RequestParam("mids") Integer[] mids,
												 Integer czid) throws Exception {
		String tableName = StringUtil.getTableName(czid, dayTime);
		if (shuxingId.length == 0 || quduanName.length == 0 || dayTime == null) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {

			JSONObject js = new JSONObject();
			Long endtime = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
			Date today = new Date();
			Date nowday = simpleDateFormat.parse(simpleDateFormat.format(today));//当前的年月日
			long todaytime = today.getTime() / 1000;//当前时间转换为秒
			long nowdaytime = nowday.getTime() / 1000;//当前年月日转化为秒
			long statrtime = dayTime.getTime() / 1000;
			if (nowdaytime != statrtime) {
				endtime = statrtime + 86399;
			} else {
				endtime = todaytime;
			}
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
			Integer k = 0;
			for (int i = 0; i < sqlname.size(); i++) {
				List<Object> listt1 = new ArrayList<>();
				k++;
				System.out.println("22222211111" + sqlname.get(i));//获得每一个属性名
				String shuxingname = sqlname.get(i);
				System.out.println("1231111=" + quduanName[i]);//获得每一个区段名
				String quduanname = quduanName[i];
				Integer qdid = quXianService.findQDid(quduanname, czid);
				System.out.println("qdidddddddd" + qdid);
				Integer type = types[i];//区段类型
				Integer mid = mids[i];//本区段对应的最大值和最小值id
				String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
				System.out.println("maxNumbers=" + maxNumbers);
				String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
				System.out.println("maxNumbers_k=" + maxNumbers_k);
				String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
				System.out.println("maxNumbers_z=" + maxNumbers_z);
				String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
				System.out.println("minNumbers=" + minNumbers);
				String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
				System.out.println("minNumbers_k=" + minNumbers_k);
				String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
				System.out.println("minNumbers_z=" + minNumbers_z);
				List<quduanEntity> date = quXianService.findQuDuanDayData(statrtime, endtime, shuxingname, quduanname, qdid, tableName);
				// System.out.println("1234" + date);

				for (quduanEntity quduanEntity : date) {
					List<Object> list1 = new ArrayList();
					Integer createtime = quduanEntity.getCreatetime();
					BigDecimal numValue = quduanEntity.getNumValue();
					list1.add(createtime * 1000L);
					list1.add(numValue);
					listt1.add(list1);
				}
				if (k == 1) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 2) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 3) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 4) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 5) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 6) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 7) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 8) {
					js.put("shuju" + k.toString(), listt1);
					js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
			}
			//System.out.println("jsssssssssssssss" + js);
			// return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
			return ResponseDataUtil.ok("查询数据成功", js);
		}
	}


	//查看门限参数
	@GetMapping("/findMenXianDatas")
	public Map<String, Object> findMenXianDatas(Integer czid, Integer[] shuxingId, String[] quduanName, Integer[] types, Integer[] mids) {
		JSONObject jo = menXianService.findMenXianDatas(czid, shuxingId, quduanName, types, mids);
		return ResponseDataUtil.ok("查询门限列表成功", jo);
	}

	//电码化实时曲线
	@GetMapping("/findDMHShiShiDatas")
	public Map<String, Object> findDMHShiShiDatas(@RequestParam("shuxingId") Integer[] shuxingId,
												  @RequestParam("quduanName") String[] quduanName,
												  @RequestParam("types") Integer[] types,
												  @RequestParam("mids") Integer[] mids,
												  @RequestParam("qdids") Integer[] qdids,
												  Integer czid, Integer times,
												  String Time) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, today);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			if (times == -1) {
				JSONObject js = new JSONObject();
				List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
				List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
				long cZid = czid.longValue();
				if (!cheZhanService.findCzStutrs(cZid, false)) {
					List aa = new ArrayList<>();
					JSONObject ss = new JSONObject();
					ss.put("data", aa);
					return ss;
				} else {
					Map quDuanSXEntityList = new HashMap();
					List<Object> listtt1 = new ArrayList<>();
					//对区段id数组去重
					LinkedHashSet<Object> temp = new LinkedHashSet<>();
					for (int i = 0; i < qdids.length; i++) {
						temp.add(qdids[i]);
					}
					Object[] objects = temp.toArray();
					int length = objects.length;
					System.out.println("ssssss" + length);
					//查询有多少个不同的区段
					Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
					int i1 = collect.get(objects[0]).intValue();
					int i2 = 0;
					int i3 = 0;
					System.out.println("i11111" + i1);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < i1; i++) {
						sb.append(sqlname.get(i) + ",");
					}
					System.out.println("sbsbsb" + sb);
					Integer qdiddd = qdids[i1 - 1];
					LinkedHashMap datee = quXianService.findDMHShiShiData(sb, qdiddd, tableName);
					quDuanSXEntityList.putAll(datee);
					if (length > 1) {
						i2 = collect.get(objects[1]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1;
						for (int i = 0; i < i2; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						System.out.println("sbsbsb" + sbb);
						Integer qdidd = qdids[i1];
						LinkedHashMap dateee = quXianService.findDMHShiShiData(sbb, qdidd, tableName);
						quDuanSXEntityList.putAll(dateee);
					}
					if (length > 2) {
						i3 = collect.get(objects[2]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1 + i2;
						for (int i = 0; i < i3; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						Integer qdidddd = qdids[i1 + i2];
						LinkedHashMap dateee = quXianService.findDMHShiShiData(sbb, qdidddd, tableName);
						quDuanSXEntityList.putAll(dateee);
					}
					System.out.println(quDuanSXEntityList);
					for (String ss : sqlname) {
						for (Object s : quDuanSXEntityList.keySet()) {
							if (ss.equals(s)) {
								List<Object> list1 = new ArrayList();
								Object createtime = quDuanSXEntityList.get("createtime");
								Object oneData = quDuanSXEntityList.get(ss);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt1.add(list1);
								// System.out.println("oneData : " + oneData + " createtime : " + createtime);
							}
						}
					}

					Integer k = 0;
					Integer kf = -1;
					for (int i = 0; i < sqlname.size(); i++) {
						List<Object> listt1 = new ArrayList<>();
						kf++;
						k++;
						Object ooo = listtt1.get(kf);
						listt1.add(ooo);
						Integer qdid = qdids[kf];
						Integer type = types[i];//区段类型
						Integer mid = mids[i];//本区段对应的最大值和最小值id
						String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
						String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
						String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
						String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
						String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
						String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
						if (k == 1) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 2) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 3) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 4) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 5) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 6) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 7) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
						if (k == 8) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
							}
						}
					}
					// return ResponseDataUtil.ok("查询数据成功", js);
					return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
				}
			} else {
				Integer creatTime = Integer.parseInt(Time);
				Integer intValue = Integer.valueOf(Time).intValue();
				System.out.println("intValue=" + intValue);
				JSONObject js = new JSONObject();
				List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
				List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
				long cZid = czid.longValue();
				if (!cheZhanService.findCzStutrs(cZid, false)) {
					List aa = new ArrayList<>();
					JSONObject ss = new JSONObject();
					ss.put("data", aa);
					return ss;
				} else {
					Map quDuanSXEntityList = new HashMap();
					List<Object> listtt1 = new ArrayList<>();
					//对区段id数组去重
					LinkedHashSet<Object> temp = new LinkedHashSet<>();
					for (int i = 0; i < qdids.length; i++) {
						temp.add(qdids[i]);
					}
					Object[] objects = temp.toArray();
					int length = objects.length;
					System.out.println("ssssss" + length);
					//查询有多少个不同的区段
					Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
					int i1 = collect.get(objects[0]).intValue();
					int i2 = 0;
					int i3 = 0;
					System.out.println("i11111" + i1);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < i1; i++) {
						sb.append(sqlname.get(i) + ",");
					}
					System.out.println("sbsbsb" + sb);
					Integer qdiddd = qdids[i1 - 1];
					LinkedHashMap datee = quXianService.findDMHShiShiDataTime(sb, qdiddd, tableName, creatTime);
					System.out.println("dateeeee=" + datee);
					if (datee != null) {
						quDuanSXEntityList.putAll(datee);
					}
					if (length > 1) {
						i2 = collect.get(objects[1]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1;
						for (int i = 0; i < i2; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						System.out.println("sbsbsb" + sbb);
						Integer qdidd = qdids[i1];
						LinkedHashMap dateee = quXianService.findDMHShiShiDataTime(sbb, qdidd, tableName, creatTime);
						System.out.println("datee2222=" + dateee);
						if (dateee != null) {
							quDuanSXEntityList.putAll(dateee);
						}
					}
					if (length > 2) {
						i3 = collect.get(objects[2]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1 + i2;
						for (int i = 0; i < i3; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						Integer qdidddd = qdids[i1 + i2];
						LinkedHashMap dateee = quXianService.findDMHShiShiDataTime(sbb, qdidddd, tableName, creatTime);
						System.out.println("dateee3333=" + dateee);
						if (dateee != null) {
							quDuanSXEntityList.putAll(dateee);
						}
					}
					System.out.println("quDuanSXEntityList=" + quDuanSXEntityList);
					for (String ss : sqlname) {
						if (quDuanSXEntityList.size() != 0)
							for (Object s : quDuanSXEntityList.keySet()) {
								if (ss.equals(s)) {
									List<Object> list1 = new ArrayList();
									Object createtime = quDuanSXEntityList.get("createtime");
									Object oneData = quDuanSXEntityList.get(ss);
									list1.add((Long.valueOf(createtime.toString())) * 1000L);
									list1.add(oneData);
									listtt1.add(list1);
									// System.out.println("oneData : " + oneData + " createtime : " + createtime);
								}
							}
					}

					Integer k = 0;
					Integer kf = -1;
					for (int i = 0; i < sqlname.size(); i++) {
						List<Object> listt1 = new ArrayList<>();
						kf++;
						k++;
						if (listtt1.size() != 0) {
							Object ooo = listtt1.get(kf);
							listt1.add(ooo);
							Integer qdid = qdids[kf];
							Integer type = types[i];//区段类型
							Integer mid = mids[i];//本区段对应的最大值和最小值id
							String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
							String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
							String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
							String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
							String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
							String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
							String upLimit = quXianService.findUpLimitNumber(czid, qdid, mid, type);
							String lowLimit = quXianService.findLowLimitNumber(czid, qdid, mid, type);
							if (k == 1) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 2) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 3) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 4) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 5) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 6) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 7) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 8) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
						}
					}
					return ResponseDataUtil.ok("查询数据成功", js);
					// return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
				}
			}
		}
	}


	//电码化日曲线
	@GetMapping("/findDMHDayDatas")
	public Map<String, Object> findDMHQuDuanDayDatas(@RequestParam("dayTime") Date dayTime,
													 @RequestParam("shuxingId") Integer[] shuxingId,
													 @RequestParam("quduanName") String[] quduanName,
													 @RequestParam("types") Integer[] types,
													 @RequestParam("mids") Integer[] mids,
													 @RequestParam("qdids") Integer[] qdids,
													 Integer czid) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, dayTime);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			JSONObject js = new JSONObject();
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名

			Long endtime = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
			Date nowday = simpleDateFormat.parse(simpleDateFormat.format(today));//当前的年月日
			long todaytime = today.getTime() / 1000;//当前时间转换为秒
			long nowdaytime = nowday.getTime() / 1000;//当前年月日转化为秒
			long statrtime = dayTime.getTime() / 1000;
			if (nowdaytime != statrtime) {
				endtime = statrtime + 86399;
			} else {
				endtime = todaytime;
			}
			List<Object> listtt1 = new ArrayList<>();
			List<Object> listtt2 = new ArrayList<>();
			List<Object> listtt3 = new ArrayList<>();
			List<Object> datelist1 = new ArrayList<>();
			List<Object> datelist2 = new ArrayList<>();
			List<Object> datelist3 = new ArrayList<>();
			List<Object> datelist4 = new ArrayList<>();
			List<Object> datelist5 = new ArrayList<>();
			List<Object> datelist6 = new ArrayList<>();
			List<Object> datelist7 = new ArrayList<>();
			List<Object> datelist8 = new ArrayList<>();
			//对区段id数组去重
			LinkedHashSet<Object> temp = new LinkedHashSet<>();
			for (int i = 0; i < qdids.length; i++) {
				temp.add(qdids[i]);
			}
			Object[] objects = temp.toArray();
			int length = objects.length;
			System.out.println("ssssss" + length);
			//查询有多少个不同的区段
			Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			int i1 = collect.get(objects[0]).intValue();
			int i2 = 0;
			int i3 = 0;
			System.out.println("i11111" + i1);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < i1; i++) {
				sb.append(sqlname.get(i) + ",");
			}
			System.out.println("sbsbsb" + sb);
			Integer qdiddd = qdids[i1 - 1];
			List<LinkedHashMap> datee = quXianService.findDMHDayData(sb, statrtime, endtime, qdiddd, tableName);
			for (LinkedHashMap linkedHashMap : datee) {
				for (Object o : linkedHashMap.keySet()) {
					for (String s : sqlname) {
						if (s.equals(o)) {
							List<Object> list1 = new ArrayList();
							Object createtime = linkedHashMap.get("createtime");
							Object oneData = linkedHashMap.get(s);
							list1.add((Long.valueOf(createtime.toString())) * 1000L);
							list1.add(oneData);
							listtt1.add(list1);
						}
					}
				}
			}
			// quDuanSXEntityList.addAll(datee);
			if (length > 1) {
				i2 = collect.get(objects[1]).intValue();
				StringBuilder sbb = new StringBuilder();
				int num = i1;
				for (int i = 0; i < i2; i++) {
					sbb.append(sqlname.get(num++) + ",");
				}
				System.out.println("sbsbsb" + sbb);
				Integer qdidd = qdids[i1];
				List<LinkedHashMap> dateee = quXianService.findDMHDayData(sbb, statrtime, endtime, qdidd, tableName);
				for (LinkedHashMap linkedHashMap : dateee) {
					for (Object o : linkedHashMap.keySet()) {
						for (String s : sqlname) {
							if (s.equals(o)) {
								List<Object> list1 = new ArrayList();
								Object createtime = linkedHashMap.get("createtime");
								Object oneData = linkedHashMap.get(s);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt2.add(list1);
							}
						}
					}
				}
			}
			if (length > 2) {
				i3 = collect.get(objects[2]).intValue();
				StringBuilder sbb = new StringBuilder();
				int num = i1 + i2;
				for (int i = 0; i < i3; i++) {
					sbb.append(sqlname.get(num++) + ",");
				}
				Integer qdidd = qdids[i1 + i2];
				List<LinkedHashMap> dateee = quXianService.findDMHDayData(sbb, statrtime, endtime, qdidd, tableName);
				for (LinkedHashMap linkedHashMap : dateee) {
					for (Object o : linkedHashMap.keySet()) {
						for (String s : sqlname) {
							if (s.equals(o)) {
								List<Object> list1 = new ArrayList();
								Object createtime = linkedHashMap.get("createtime");
								Object oneData = linkedHashMap.get(s);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt3.add(list1);
							}
						}
					}
				}
			}
			Integer k = 0;
			Integer kf = -1;
			//第一个区段
			if (i1 == 1) {
				datelist1 = listtt1;
			}
			if (i1 == 2) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 2 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 2 == 1)
						datelist2.add(listtt1.get(i4));
				}
			}
			if (i1 == 3) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 3 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 3 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 3 == 2)
						datelist3.add(listtt1.get(i4));
				}
			}
			if (i1 == 4) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 4 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 4 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 4 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 4 == 3)
						datelist4.add(listtt1.get(i4));
				}
			}
			if (i1 == 5) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 5 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 5 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 5 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 5 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 5 == 4)
						datelist5.add(listtt1.get(i4));
				}
			}
			if (i1 == 6) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 6 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 6 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 6 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 6 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 6 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 6 == 5)
						datelist6.add(listtt1.get(i4));
				}
			}
			if (i1 == 7) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 7 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 7 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 7 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 7 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 7 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 7 == 5)
						datelist6.add(listtt1.get(i4));
					if (i4 % 7 == 6)
						datelist7.add(listtt1.get(i4));
				}
			}
			if (i1 == 8) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 8 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 8 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 8 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 8 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 8 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 8 == 5)
						datelist6.add(listtt1.get(i4));
					if (i4 % 8 == 6)
						datelist7.add(listtt1.get(i4));
					if (i4 % 8 == 7)
						datelist8.add(listtt1.get(i4));
				}
			}

			//第二个区段
			if (i2 != 0 && i2 == 1) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist2 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist3 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist4 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist5 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist6 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist7 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist8 = listtt2;
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 2) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist3.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist4.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist6.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist7.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 3) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist4.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {

						if (i4 % 3 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist6.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist6.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist7.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 4) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist6.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist6.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist7.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 5) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist6.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist6.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist7.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 6) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 6 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 6 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 6 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 6 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 6 == 4)
							datelist6.add(listtt2.get(i4));
						if (i4 % 6 == 5)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 6 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 6 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 6 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 6 == 3)
							datelist6.add(listtt2.get(i4));
						if (i4 % 6 == 4)
							datelist7.add(listtt2.get(i4));
						if (i4 % 6 == 5)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 7) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 7 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 7 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 7 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 7 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 7 == 4)
							datelist6.add(listtt2.get(i4));
						if (i4 % 7 == 5)
							datelist7.add(listtt2.get(i4));
						if (i4 % 7 == 6)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}

			//第三个区段
			if (i3 != 0 && i3 == 1) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist3 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist4 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist5 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist6 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist7 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist8 = listtt3;
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 2) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist4.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist5.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist6.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist7.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 3) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist5.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist6.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist6.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist7.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 4) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist6.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist6.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist7.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 5) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 5 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 5 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 5 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 5 == 3)
							datelist6.add(listtt3.get(i4));
						if (i4 % 5 == 4)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 5 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 5 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 5 == 2)
							datelist6.add(listtt3.get(i4));
						if (i4 % 5 == 3)
							datelist7.add(listtt3.get(i4));
						if (i4 % 5 == 4)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 6) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 6 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 6 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 6 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 6 == 3)
							datelist6.add(listtt3.get(i4));
						if (i4 % 6 == 4)
							datelist7.add(listtt3.get(i4));
						if (i4 % 6 == 5)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}

			for (int i = 0; i < sqlname.size(); i++) {
				kf++;
				k++;
				Integer qdid = qdids[kf];
				Integer type = types[i];//区段类型
				Integer mid = mids[i];//本区段对应的最大值和最小值id
				String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
				String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
				String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
				String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
				String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
				String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
				if (k == 1) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist1.size(); i4++) {
							List oneList = (List) datelist1.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist1);
					}
					js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist1);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 2) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist2.size(); i4++) {
							List oneList = (List) datelist2.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist2);
					}
					js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist2);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 3) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist3.size(); i4++) {
							List oneList = (List) datelist3.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist3);
					}
					js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist3);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 4) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist4.size(); i4++) {
							List oneList = (List) datelist4.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist4);
					}
					js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist4);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 5) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist5.size(); i4++) {
							List oneList = (List) datelist5.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist5);
					}
					js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist5);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 6) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist6.size(); i4++) {
							List oneList = (List) datelist6.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist6);
					}
					js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist6);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 7) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist7.size(); i4++) {
							List oneList = (List) datelist7.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist7);
					}
					js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist7);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 8) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist8.size(); i4++) {
							List oneList = (List) datelist8.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist8);
					}
					js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist8);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
			}
			// return ResponseDataUtil.ok("查询数据成功", js);
			return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
		}
	}


	//区段日曲线
	@GetMapping("/findQuDuanDayDatas")
	public Map<String, Object> findQuDuanDayDatas(@RequestParam("dayTime") Date dayTime,
												  @RequestParam("shuxingId") Integer[] shuxingId,
												  @RequestParam("quduanName") String[] quduanName,
												  @RequestParam("types") Integer[] types,
												  @RequestParam("mids") Integer[] mids,
												  @RequestParam("qdids") Integer[] qdids,
												  Integer czid) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, dayTime);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			JSONObject js = new JSONObject();
			List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
			List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名

			Long endtime = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
			Date nowday = simpleDateFormat.parse(simpleDateFormat.format(today));//当前的年月日
			long todaytime = today.getTime() / 1000;//当前时间转换为秒
			long nowdaytime = nowday.getTime() / 1000;//当前年月日转化为秒
			long statrtime = dayTime.getTime() / 1000;
			if (nowdaytime != statrtime) {
				endtime = statrtime + 86399;
			} else {
				endtime = todaytime;
			}
			List<Object> listtt1 = new ArrayList<>();
			List<Object> listtt2 = new ArrayList<>();
			List<Object> listtt3 = new ArrayList<>();
			List<Object> datelist1 = new ArrayList<>();
			List<Object> datelist2 = new ArrayList<>();
			List<Object> datelist3 = new ArrayList<>();
			List<Object> datelist4 = new ArrayList<>();
			List<Object> datelist5 = new ArrayList<>();
			List<Object> datelist6 = new ArrayList<>();
			List<Object> datelist7 = new ArrayList<>();
			List<Object> datelist8 = new ArrayList<>();
			//对区段id数组去重
			LinkedHashSet<Object> temp = new LinkedHashSet<>();
			for (int i = 0; i < qdids.length; i++) {
				temp.add(qdids[i]);
			}
			Object[] objects = temp.toArray();
			int length = objects.length;
			System.out.println("ssssss" + length);
			//查询有多少个不同的区段
			Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			int i1 = collect.get(objects[0]).intValue();
			int i2 = 0;
			int i3 = 0;
			System.out.println("i11111" + i1);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < i1; i++) {
				sb.append(sqlname.get(i) + ",");
			}
			System.out.println("sbsbsb" + sb);
			Integer qdiddd = qdids[i1 - 1];
			List<LinkedHashMap> datee = quXianService.findQuDuanDayDataa(sb, statrtime, endtime, qdiddd, tableName);
			//  System.out.println("asddadada0"+datee);
			for (LinkedHashMap linkedHashMap : datee) {
				for (Object o : linkedHashMap.keySet()) {
					for (String s : sqlname) {
						if (s.equals(o)) {
							List<Object> list1 = new ArrayList();
							Object createtime = linkedHashMap.get("createtime");
							Object oneData = linkedHashMap.get(s);
							list1.add((Long.valueOf(createtime.toString())) * 1000L);
							list1.add(oneData);
							listtt1.add(list1);
						}
					}
				}
			}
			// quDuanSXEntityList.addAll(datee);
			if (length > 1) {
				i2 = collect.get(objects[1]).intValue();
				System.out.println("i2i2i22i2i2i=" + i2);
				StringBuilder sbb = new StringBuilder();
				int num = i1;
				for (int i = 0; i < i2; i++) {
					sbb.append(sqlname.get(num++) + ",");
				}
				System.out.println("sbsbsb" + sbb);
				Integer qdidd = qdids[i1];
				List<LinkedHashMap> dateee = quXianService.findQuDuanDayDataa(sbb, statrtime, endtime, qdidd, tableName);
				for (LinkedHashMap linkedHashMap : dateee) {
					for (Object o : linkedHashMap.keySet()) {
						for (String s : sqlname) {
							if (s.equals(o)) {
								List<Object> list1 = new ArrayList();
								Object createtime = linkedHashMap.get("createtime");
								Object oneData = linkedHashMap.get(s);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt2.add(list1);
							}
						}
					}
				}
			}
			if (length > 2) {
				i3 = collect.get(objects[2]).intValue();
				System.out.println("i3i3i3i33i3i=" + i3);
				StringBuilder sbb = new StringBuilder();
				int num = i1 + i2;
				for (int i = 0; i < i3; i++) {
					sbb.append(sqlname.get(num++) + ",");
				}
				Integer qdidd = qdids[i1 + i2];
				List<LinkedHashMap> dateee = quXianService.findQuDuanDayDataa(sbb, statrtime, endtime, qdidd, tableName);
				for (LinkedHashMap linkedHashMap : dateee) {
					for (Object o : linkedHashMap.keySet()) {
						for (String s : sqlname) {
							if (s.equals(o)) {
								List<Object> list1 = new ArrayList();
								Object createtime = linkedHashMap.get("createtime");
								Object oneData = linkedHashMap.get(s);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt3.add(list1);
							}
						}
					}
				}
			}
			Integer k = 0;
			Integer kf = -1;
			//第一个区段
			if (i1 == 1) {
				datelist1 = listtt1;
			}
			if (i1 == 2) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 2 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 2 == 1)
						datelist2.add(listtt1.get(i4));
				}
			}
			if (i1 == 3) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 3 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 3 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 3 == 2)
						datelist3.add(listtt1.get(i4));
				}
			}
			if (i1 == 4) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 4 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 4 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 4 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 4 == 3)
						datelist4.add(listtt1.get(i4));
				}
			}
			if (i1 == 5) {
				for (int i4 = 0; i4 < listtt1.size(); i4++) {
					if (i4 % 5 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 5 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 5 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 5 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 5 == 4)
						datelist5.add(listtt1.get(i4));
				}
			}
			if (i1 == 6) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 6 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 6 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 6 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 6 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 6 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 6 == 5)
						datelist6.add(listtt1.get(i4));
				}
			}
			if (i1 == 7) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 7 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 7 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 7 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 7 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 7 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 7 == 5)
						datelist6.add(listtt1.get(i4));
					if (i4 % 7 == 6)
						datelist7.add(listtt1.get(i4));
				}
			}
			if (i1 == 8) {
				for (int i4 = 1; i4 < listtt1.size(); i4++) {
					if (i4 % 8 == 0)
						datelist1.add(listtt1.get(i4));
					if (i4 % 8 == 1)
						datelist2.add(listtt1.get(i4));
					if (i4 % 8 == 2)
						datelist3.add(listtt1.get(i4));
					if (i4 % 8 == 3)
						datelist4.add(listtt1.get(i4));
					if (i4 % 8 == 4)
						datelist5.add(listtt1.get(i4));
					if (i4 % 8 == 5)
						datelist6.add(listtt1.get(i4));
					if (i4 % 8 == 6)
						datelist7.add(listtt1.get(i4));
					if (i4 % 8 == 7)
						datelist8.add(listtt1.get(i4));
				}
			}

			//第二个区段
			if (i2 != 0 && i2 == 1) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist2 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist3 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist4 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist5 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist6 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist7 = listtt2;
					}
					aa = 1;
				}
				if (aa == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						datelist8 = listtt2;
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 2) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist3.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist4.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist6.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 2 == 0)
							datelist7.add(listtt2.get(i4));
						if (i4 % 2 == 1)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 3) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist4.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {

						if (i4 % 3 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist6.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 3 == 0)
							datelist6.add(listtt2.get(i4));
						if (i4 % 3 == 1)
							datelist7.add(listtt2.get(i4));
						if (i4 % 3 == 2)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 4) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist5.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist6.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 4 == 0)
							datelist5.add(listtt2.get(i4));
						if (i4 % 4 == 1)
							datelist6.add(listtt2.get(i4));
						if (i4 % 4 == 2)
							datelist7.add(listtt2.get(i4));
						if (i4 % 4 == 3)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 5) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist6.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist6.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 5 == 0)
							datelist4.add(listtt2.get(i4));
						if (i4 % 5 == 1)
							datelist5.add(listtt2.get(i4));
						if (i4 % 5 == 2)
							datelist6.add(listtt2.get(i4));
						if (i4 % 5 == 3)
							datelist7.add(listtt2.get(i4));
						if (i4 % 5 == 4)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 6) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 6 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 6 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 6 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 6 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 6 == 4)
							datelist6.add(listtt2.get(i4));
						if (i4 % 6 == 5)
							datelist7.add(listtt2.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 6 == 0)
							datelist3.add(listtt2.get(i4));
						if (i4 % 6 == 1)
							datelist4.add(listtt2.get(i4));
						if (i4 % 6 == 2)
							datelist5.add(listtt2.get(i4));
						if (i4 % 6 == 3)
							datelist6.add(listtt2.get(i4));
						if (i4 % 6 == 4)
							datelist7.add(listtt2.get(i4));
						if (i4 % 6 == 5)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}
			if (i2 != 0 && i2 == 7) {
				Integer aa = 0;
				if (aa == 0 && datelist2.size() == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt2.size(); i4++) {
						if (i4 % 7 == 0)
							datelist2.add(listtt2.get(i4));
						if (i4 % 7 == 1)
							datelist3.add(listtt2.get(i4));
						if (i4 % 7 == 2)
							datelist4.add(listtt2.get(i4));
						if (i4 % 7 == 3)
							datelist5.add(listtt2.get(i4));
						if (i4 % 7 == 4)
							datelist6.add(listtt2.get(i4));
						if (i4 % 7 == 5)
							datelist7.add(listtt2.get(i4));
						if (i4 % 7 == 6)
							datelist8.add(listtt2.get(i4));
					}
					aa = 1;
				}
			}

			//第三个区段
			if (i3 != 0 && i3 == 1) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist3 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist4 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist5 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist6 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist7 = listtt3;
					}
					aa = 1;
				}
				if (aa == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						datelist8 = listtt3;
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 2) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist4.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist5.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist6.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 2 == 0)
							datelist7.add(listtt3.get(i4));
						if (i4 % 2 == 1)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 3) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist5.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist6.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 3 == 0)
							datelist6.add(listtt3.get(i4));
						if (i4 % 3 == 1)
							datelist7.add(listtt3.get(i4));
						if (i4 % 3 == 2)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 4) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist6.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist6.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 4 == 0)
							datelist5.add(listtt3.get(i4));
						if (i4 % 4 == 1)
							datelist6.add(listtt3.get(i4));
						if (i4 % 4 == 2)
							datelist7.add(listtt3.get(i4));
						if (i4 % 4 == 3)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 5) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 5 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 5 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 5 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 5 == 3)
							datelist6.add(listtt3.get(i4));
						if (i4 % 5 == 4)
							datelist7.add(listtt3.get(i4));
					}
					aa = 1;
				}
				if (aa == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 5 == 0)
							datelist4.add(listtt3.get(i4));
						if (i4 % 5 == 1)
							datelist5.add(listtt3.get(i4));
						if (i4 % 5 == 2)
							datelist6.add(listtt3.get(i4));
						if (i4 % 5 == 3)
							datelist7.add(listtt3.get(i4));
						if (i4 % 5 == 4)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}
			if (i3 != 0 && i3 == 6) {
				Integer aa = 0;
				if (aa == 0 && datelist3.size() == 0 && datelist4.size() == 0 && datelist5.size() == 0 && datelist6.size() == 0 && datelist7.size() == 0 && datelist8.size() == 0) {
					for (int i4 = 1; i4 < listtt3.size(); i4++) {
						if (i4 % 6 == 0)
							datelist3.add(listtt3.get(i4));
						if (i4 % 6 == 1)
							datelist4.add(listtt3.get(i4));
						if (i4 % 6 == 2)
							datelist5.add(listtt3.get(i4));
						if (i4 % 6 == 3)
							datelist6.add(listtt3.get(i4));
						if (i4 % 6 == 4)
							datelist7.add(listtt3.get(i4));
						if (i4 % 6 == 5)
							datelist8.add(listtt3.get(i4));
					}
					aa = 1;
				}
			}

			for (int i = 0; i < sqlname.size(); i++) {
				kf++;
				k++;
				Integer qdid = qdids[kf];
				Integer type = types[i];//区段类型
				Integer mid = mids[i];//本区段对应的最大值和最小值id
				String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
				String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
				String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
				String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
				String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
				String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
				if (k == 1) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist1.size(); i4++) {
							List oneList = (List) datelist1.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist1);
					}
					js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist1);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 2) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist2.size(); i4++) {
							List oneList = (List) datelist2.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist2);
					}
					js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist2);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 3) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist3.size(); i4++) {
							List oneList = (List) datelist3.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist3);
					}
					js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist3);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 4) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist4.size(); i4++) {
							List oneList = (List) datelist4.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist4);
					}
					js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist4);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 5) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist5.size(); i4++) {
							List oneList = (List) datelist5.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist5);
					}
					js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist5);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 6) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist6.size(); i4++) {
							List oneList = (List) datelist6.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist6);
					}
					js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist6);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 7) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist7.size(); i4++) {
							List oneList = (List) datelist7.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist7);
					}
					js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist7);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
				if (k == 8) {
					if (type == 2) {
						for (int i4 = 0; i4 < datelist8.size(); i4++) {
							List oneList = (List) datelist8.get(i4);
							oneList.add(1, 2);
						}
						js.put("shuju" + k.toString(), datelist8);
					}
					js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
					if (type == 0 || type == 1) {
						js.put("shuju" + k.toString(), datelist8);
						if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
							js.put("max" + k.toString(), maxNumbers);
						}
						if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
							js.put("max_k" + k.toString(), maxNumbers_k);
						}
						if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
							js.put("max_z" + k.toString(), maxNumbers_z);
						}
						if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
							js.put("min" + k.toString(), minNumbers);
						}
						if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
							js.put("min_k" + k.toString(), minNumbers_k);
						}
						if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
							js.put("min_z" + k.toString(), minNumbers_z);
						}
					}
				}
			}
			//  return ResponseDataUtil.ok("查询数据成功", js);
			return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
		}
	}


	//区段实时曲线
	@GetMapping("/findQuDuanShiShiDatas")
	public Map<String, Object> findQuDuanShiShiDatas(@RequestParam("shuxingId") Integer[] shuxingId,
													 @RequestParam("quduanName") String[] quduanName,
													 @RequestParam("types") Integer[] types,
													 @RequestParam("mids") Integer[] mids,
													 @RequestParam("qdids") Integer[] qdids,
													 Integer czid, Integer times,
													 String Time) throws Exception {
		Date today = new Date();
		String tableName = StringUtil.getTableName(czid, today);
		if (shuxingId.length == 0 || quduanName.length == 0) {
			return ResponseDataUtil.error("请选择正确的数据");
		} else {
			if (times == -1) {
				JSONObject js = new JSONObject();
				List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
				List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
				long cZid = czid.longValue();
				if (!cheZhanService.findCzStutrs(cZid, false)) {
					List aa = new ArrayList<>();
					JSONObject ss = new JSONObject();
					ss.put("data", aa);
					return ss;
				} else {
					Map quDuanSXEntityList = new HashMap();
					List<Object> listtt1 = new ArrayList<>();
					//对区段id数组去重
					LinkedHashSet<Object> temp = new LinkedHashSet<>();
					for (int i = 0; i < qdids.length; i++) {
						temp.add(qdids[i]);
					}
					Object[] objects = temp.toArray();
					int length = objects.length;
					System.out.println("ssssss" + length);
					//查询有多少个不同的区段
					Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
					int i1 = collect.get(objects[0]).intValue();
					int i2 = 0;
					int i3 = 0;
					System.out.println("i11111" + i1);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < i1; i++) {
						sb.append(sqlname.get(i) + ",");
					}
					System.out.println("sbsbsb" + sb);
					Integer qdiddd = qdids[i1 - 1];
					LinkedHashMap datee = quXianService.findQuDuanShiShiDataa(sb, qdiddd, tableName);
					quDuanSXEntityList.putAll(datee);
					if (length > 1) {
						i2 = collect.get(objects[1]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1;
						for (int i = 0; i < i2; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						System.out.println("sbsbsb" + sbb);
						Integer qdidd = qdids[i1];
						LinkedHashMap dateee = quXianService.findQuDuanShiShiDataa(sbb, qdidd, tableName);
						quDuanSXEntityList.putAll(dateee);
					}
					if (length > 2) {
						i3 = collect.get(objects[2]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1 + i2;
						for (int i = 0; i < i3; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						Integer qdidddd = qdids[i1 + i2];
						LinkedHashMap dateee = quXianService.findQuDuanShiShiDataa(sbb, qdidddd, tableName);
						quDuanSXEntityList.putAll(dateee);
					}
					System.out.println(quDuanSXEntityList);
					for (String ss : sqlname) {
						for (Object s : quDuanSXEntityList.keySet()) {
							if (ss.equals(s)) {
								List<Object> list1 = new ArrayList();
								Object createtime = quDuanSXEntityList.get("createtime");
								Object oneData = quDuanSXEntityList.get(ss);
								list1.add((Long.valueOf(createtime.toString())) * 1000L);
								list1.add(oneData);
								listtt1.add(list1);
								// System.out.println("oneData : " + oneData + " createtime : " + createtime);
							}
						}
					}

					Integer k = 0;
					Integer kf = -1;
					for (int i = 0; i < sqlname.size(); i++) {
						List<Object> listt1 = new ArrayList<>();
						kf++;
						k++;
						Object ooo = listtt1.get(kf);
						listt1.add(ooo);
						Integer qdid = qdids[kf];
						Integer type = types[i];//区段类型
						Integer mid = mids[i];//本区段对应的最大值和最小值id
						String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
						String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
						String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
						String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
						String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
						String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
						String upLimit = quXianService.findUpLimitNumber(czid, qdid, mid, type);
						String lowLimit = quXianService.findLowLimitNumber(czid, qdid, mid, type);

						if (k == 1) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 2) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 3) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 4) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 5) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 6) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 7) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
						if (k == 8) {
							if (type == 2) {
								for (int i4 = 0; i4 < listt1.size(); i4++) {
									List oneList = (List) listt1.get(i4);
									oneList.add(1, 2);
								}
								js.put("shuju" + k.toString(), listt1);
							}
							js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
							if (type == 0 || type == 1) {
								js.put("shuju" + k.toString(), listt1);
								if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
									js.put("max" + k.toString(), maxNumbers);
								}
								if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
									js.put("max_k" + k.toString(), maxNumbers_k);
								}
								if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
									js.put("max_z" + k.toString(), maxNumbers_z);
								}
								if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
									js.put("min" + k.toString(), minNumbers);
								}
								if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
									js.put("min_k" + k.toString(), minNumbers_k);
								}
								if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
									js.put("min_z" + k.toString(), minNumbers_z);
								}
								if (upLimit != null && !("——").equals(upLimit)) {
									js.put("upLimit" + k.toString(), upLimit);
								}
								if (lowLimit != null && !("——").equals(lowLimit)) {
									js.put("lowLimit" + k.toString(), lowLimit);
								}
							}
						}
					}
					return ResponseDataUtil.ok("查询数据成功", js);
					// return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
				}
			}
			else {
				Integer creatTime = Integer.parseInt(Time);
				Integer intValue = Integer.valueOf(Time).intValue();
				System.out.println("intValue=" + intValue);
				JSONObject js = new JSONObject();
				List<String> sqlname = quXianService.findShuXingName(shuxingId);//获取区段属性的英文名
				List<String> name = quXianService.findShuXingHanZiName(shuxingId);//获取区段属性的中文名
				long cZid = czid.longValue();
				if (!cheZhanService.findCzStutrs(cZid, false)) {
					List aa = new ArrayList<>();
					JSONObject ss = new JSONObject();
					ss.put("data", aa);
					return ss;
				} else {
					Map quDuanSXEntityList = new HashMap();
					List<Object> listtt1 = new ArrayList<>();
					//对区段id数组去重
					LinkedHashSet<Object> temp = new LinkedHashSet<>();
					for (int i = 0; i < qdids.length; i++) {
						temp.add(qdids[i]);
					}
					Object[] objects = temp.toArray();
					int length = objects.length;
					System.out.println("ssssss" + length);
					//查询有多少个不同的区段
					Map<Integer, Long> collect = Stream.of(qdids).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
					int i1 = collect.get(objects[0]).intValue();
					int i2 = 0;
					int i3 = 0;
					System.out.println("i11111" + i1);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < i1; i++) {
						sb.append(sqlname.get(i) + ",");
					}
					System.out.println("sbsbsb" + sb);
					Integer qdiddd = qdids[i1 - 1];
					LinkedHashMap datee = quXianService.findQuDuanShiShiDataaTime(sb, qdiddd, tableName, creatTime);
					System.out.println("dateeeee=" + datee);
					if (datee != null) {
						quDuanSXEntityList.putAll(datee);
					}
					if (length > 1) {
						i2 = collect.get(objects[1]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1;
						for (int i = 0; i < i2; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						System.out.println("sbsbsb" + sbb);
						Integer qdidd = qdids[i1];
						LinkedHashMap dateee = quXianService.findQuDuanShiShiDataaTime(sbb, qdidd, tableName, creatTime);
						System.out.println("datee2222=" + dateee);
						if (dateee != null) {
							quDuanSXEntityList.putAll(dateee);
						}
					}
					if (length > 2) {
						i3 = collect.get(objects[2]).intValue();
						StringBuilder sbb = new StringBuilder();
						int num = i1 + i2;
						for (int i = 0; i < i3; i++) {
							sbb.append(sqlname.get(num++) + ",");
						}
						Integer qdidddd = qdids[i1 + i2];
						LinkedHashMap dateee = quXianService.findQuDuanShiShiDataaTime(sbb, qdidddd, tableName, creatTime);
						System.out.println("dateee3333=" + dateee);
						if (dateee != null) {
							quDuanSXEntityList.putAll(dateee);
						}
					}
					System.out.println("quDuanSXEntityList=" + quDuanSXEntityList);
					for (String ss : sqlname) {
						if (quDuanSXEntityList.size() != 0)
							for (Object s : quDuanSXEntityList.keySet()) {
								if (ss.equals(s)) {
									List<Object> list1 = new ArrayList();
									Object createtime = quDuanSXEntityList.get("createtime");
									Object oneData = quDuanSXEntityList.get(ss);
									list1.add((Long.valueOf(createtime.toString())) * 1000L);
									list1.add(oneData);
									listtt1.add(list1);
									// System.out.println("oneData : " + oneData + " createtime : " + createtime);
								}
							}
					}

					Integer k = 0;
					Integer kf = -1;
					for (int i = 0; i < sqlname.size(); i++) {
						List<Object> listt1 = new ArrayList<>();
						kf++;
						k++;
						if (listtt1.size() != 0) {
							Object ooo = listtt1.get(kf);
							listt1.add(ooo);
							Integer qdid = qdids[kf];
							Integer type = types[i];//区段类型
							Integer mid = mids[i];//本区段对应的最大值和最小值id
							String maxNumbers = quXianService.findMaxNumber(czid, qdid, mid, type);
							String maxNumbers_k = quXianService.findMaxNumberK(czid, qdid, mid, type);
							String maxNumbers_z = quXianService.findMaxNumberZ(czid, qdid, mid, type);
							String minNumbers = quXianService.findMinNumber(czid, qdid, mid, type);
							String minNumbers_k = quXianService.findMinNumberK(czid, qdid, mid, type);
							String minNumbers_z = quXianService.findMinNumberZ(czid, qdid, mid, type);
							String upLimit = quXianService.findUpLimitNumber(czid, qdid, mid, type);
							String lowLimit = quXianService.findLowLimitNumber(czid, qdid, mid, type);
							if (k == 1) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[0] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 2) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[1] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 3) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[2] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 4) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[3] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 5) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[4] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 6) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[5] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 7) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[6] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
							if (k == 8) {
								if (type == 2) {
									for (int i4 = 0; i4 < listt1.size(); i4++) {
										List oneList = (List) listt1.get(i4);
										oneList.add(1, 2);
									}
									js.put("shuju" + k.toString(), listt1);
								}
								js.put("mingzi" + k.toString(), quduanName[7] + "—" + name.get(i));
								if (type == 0 || type == 1) {
									js.put("shuju" + k.toString(), listt1);
									if (maxNumbers != null && !("——").equals(maxNumbers) && !("0").equals(maxNumbers)) {
										js.put("max" + k.toString(), maxNumbers);
									}
									if (maxNumbers_k != null && !("——").equals(maxNumbers_k) && !("0").equals(maxNumbers_k)) {
										js.put("max_k" + k.toString(), maxNumbers_k);
									}
									if (maxNumbers_z != null && !("——").equals(maxNumbers_z) && !("0").equals(maxNumbers_z)) {
										js.put("max_z" + k.toString(), maxNumbers_z);
									}
									if (minNumbers != null && !("——").equals(minNumbers) && !("0").equals(minNumbers)) {
										js.put("min" + k.toString(), minNumbers);
									}
									if (minNumbers_k != null && !("——").equals(minNumbers_k) && !("0").equals(minNumbers_k)) {
										js.put("min_k" + k.toString(), minNumbers_k);
									}
									if (minNumbers_z != null && !("——").equals(minNumbers_z) && !("0").equals(minNumbers_z)) {
										js.put("min_z" + k.toString(), minNumbers_z);
									}
									if (upLimit != null && !("——").equals(upLimit)) {
										js.put("upLimit" + k.toString(), upLimit);
									}
									if (lowLimit != null && !("——").equals(lowLimit)) {
										js.put("lowLimit" + k.toString(), lowLimit);
									}
								}
							}
						}
					}
					return ResponseDataUtil.ok("查询数据成功", js);
					// return ResponseDataUtil.ok("查询数据成功", ZipUtil.gzip(js.toJSONString(), "utf-8"));
				}
			}
		}
	}

   /* public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        Integer[] aa = {1, 1, 2, 3, 1};
        List<Integer> resultList = new ArrayList<>(Arrays.asList(aa));
        TreeSet<Integer> hset = new TreeSet<Integer>(Arrays.asList(aa));
        Iterator i = hset.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
            list.add(i.next());
        }
        System.out.println(list);
        for (Object o : list) {

            Bag bag = new HashBag(resultList);
            int count = bag.getCount(o);
            System.out.println("sdasdad" + count);
        }









       *//* Map<Integer, Long> collect = Stream.of(aa).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("aaa1"+collect.get(collect.keySet()));
        System.out.println("aaa2"+collect.get(1));
        System.out.println("aaa3"+collect.get(2));
        System.out.println("aaa4"+collect.get(3));
        Collection<Long> values = collect.values();
        System.out.println("aaa"+values);*//*


    }*/


	/* public static void main(String[] args) throws Exception{
	 *//*SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        String time2 = format1.format(new Date());
        System.out.println("shijian="+time2);
        int date = format1.parse(time2).getDate();
        System.out.println("aaaaa"+date);
        int seconds = format1.parse(time2).getSeconds();
        System.out.println("bbbbb"+seconds);*//*

        long start = System.currentTimeMillis();
        List<String>list=new ArrayList<>();
        List<Long>timelist=new ArrayList<>();
        for (int i = 0; i <= 86399; i++) {
            long onetime = 1609516800 + i;
            long value = onetime * 1000L;
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            String time2 = format1.format(new Date(value));
            list.add(time2);
            timelist.add(onetime);
        }
        long end = System.currentTimeMillis();
        System.out.println("用时：" + (end - start) + "ms.");
    }*/
	public static void main(String[] args) {

		Date nowDay = new Date();
		long time2 = nowDay.getTime() / 1000;

		// 1609573237  1609573237 21-01-02 15:40:37
		//1610069150000
		//long value = (1610069150) * 1000L;//1595303879  2020-07-21 11:57:59     1595304033  2020-07-21 12:00:33
		long value = 1610899200000L;//1595303879  2020-07-21 11:57:59     1595304033  2020-07-21 12:00:33
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date(value));
		System.out.println("日期" + time);


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


		int num = 86399;
		String[] countArr = new String[num];
		System.out.println("1233333232" + countArr);


	}
}



