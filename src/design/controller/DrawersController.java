package design.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import design.controllerObject.Drawers;
import design.dao.DrawersDao;
import design.dto.MyResponse;
import design.portWrite.Com2Writer;
import design.service.PortService;
import design.util.Util;

@RequestMapping(value = "drawers")
@Controller
public class DrawersController {
	
	@Autowired
	DrawersDao dao;
	
	@Autowired
	DataFrameController dfController;
	
	@RequestMapping(value = "/getDrawers.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse getDrawers()
	{
		MyResponse myResponse = new MyResponse();
		Drawers drawers = new Drawers();
		List<Map<String,Object>> dataFramePartList =  dao.getDrawers();
		for (Map<String, Object> map : dataFramePartList) {
			drawers.drawer1 = (int) map.get("drawer1");
			drawers.drawer2 = (int) map.get("drawer2");
			drawers.drawer3 = (int) map.get("drawer3");
			drawers.drawer4 = (int) map.get("drawer4"); 
			drawers.drawer5 = (int) map.get("drawer5");
			drawers.drawer6 = (int) map.get("drawer6");
			drawers.drawer7 = (int) map.get("drawer7");
			drawers.drawer8 = (int) map.get("drawer8");
			drawers.drawer9 = (int) map.get("drawer9");
			drawers.drawer10 = (int) map.get("drawer10");
		}
		myResponse.data = drawers;
		return myResponse;
	}
	
	/**
	 * 根据用户需求更改帧字段
	 * 
	 * @param queryMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateDawers.action", method = RequestMethod.POST)
	@ResponseBody
	public MyResponse updateDawers(@RequestBody Map<String, Object> queryMap) {
		System.out.println("更改抽屉状态");
		MyResponse response = new MyResponse();
		Drawers drawers = new Drawers();
		drawers.drawer1 = (int) queryMap.get("drawer1");
		drawers.drawer2 = (int) queryMap.get("drawer2");
		drawers.drawer3 = (int) queryMap.get("drawer3");
		drawers.drawer4 = (int) queryMap.get("drawer4");
		drawers.drawer5 = (int) queryMap.get("drawer5");
		drawers.drawer6 = (int) queryMap.get("drawer6");
		drawers.drawer7 = (int) queryMap.get("drawer7");
		drawers.drawer8 = (int) queryMap.get("drawer8");
		drawers.drawer9 = (int) queryMap.get("drawer9");
		drawers.drawer10 = (int) queryMap.get("drawer10");
		drawers.seq = 1;

		System.out.println("都让开！！！！！！！！！！！！！1");
		System.out.println(drawers.toString());
		
		
		int count = dao.updateDawers(drawers);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		response.data = result;

		// 开锁帧全部字段
		String whole;
		// 需要进行校验的载荷部分
		String payLoad;

		// 信息头
		String header = "FFFF";
		// 帧长
		String flength = "0C";

		// 帧号
		String seq;
		String chseqS = Util.decToHex(PortService.frameSeq);
		PortService.frameSeq = (PortService.frameSeq + 1) % 256;
		char[] chseqC = chseqS.toCharArray();
		while (chseqC.length < 2) {
			chseqS = "0" + chseqS;
			chseqC = chseqS.toCharArray();
		}
		seq = chseqS;

		// 设备地址
		String deAddress;
		Map<String, Object> sqlResult = (Map<String, Object>) dfController.getDeAddress().data;
		System.out.println("设备地址：    " + (int) sqlResult.get("deviceAddr"));
		String chDeAddrS = String.valueOf((int) sqlResult.get("deviceAddr"));
		char[] chDeAddrC = chDeAddrS.toCharArray();
		while (chDeAddrC.length < 2) {
			chDeAddrS = "0" + chDeAddrS;
			chDeAddrC = chDeAddrS.toCharArray();
		}
		deAddress = chDeAddrS;

		// 功能号
		String functionNum = "03";

		// 数据部分
		String data;
		String dataS;
		String drawer1S = String.valueOf((int) queryMap.get("drawer1"));
		String drawer2S = String.valueOf((int) queryMap.get("drawer2"));
		String drawer3S = String.valueOf((int) queryMap.get("drawer3"));
		String drawer4S = String.valueOf((int) queryMap.get("drawer4"));
		String drawer5S = String.valueOf((int) queryMap.get("drawer5"));
		String drawer6S = String.valueOf((int) queryMap.get("drawer6"));
		String drawer7S = String.valueOf((int) queryMap.get("drawer7"));
		String drawer8S = String.valueOf((int) queryMap.get("drawer8"));
		String drawer9S = String.valueOf((int) queryMap.get("drawer9"));
		String drawer10S = String.valueOf((int) queryMap.get("drawer10"));
		dataS = drawer8S + drawer7S + drawer6S + drawer5S 
				+ drawer4S + drawer3S + drawer2S + drawer1S
				+ "0" + "0" + "0" + "0"
				+ "0" + "0" + drawer10S + drawer9S;
		dataS = Util.decToHex(Integer.parseInt(dataS, 2));
		char[] dataC = dataS.toCharArray();
		while(dataC.length < 4) {
			dataS = "0" + dataS;
			dataC = dataS.toCharArray();
		}
		data = dataS;

		// 载荷确认
		payLoad = flength + seq + deAddress + functionNum + data;

		// 生成crc校验码
		String crc;
		String crcS = Util.byte2HexStr(Util.CRC16(Util.hexStr2Byte(payLoad)));
		char[] crcC = crcS.toCharArray();
		while(crcC.length < 4) {
			crcS = "0" + crcS;
			crcC = crcS.toCharArray();
		}
		crc = crcS;

		// 结束标志
		String finishFlag = "FFF7";
		// 确定开锁帧全部字段
		whole = header + payLoad + crc + finishFlag;

		System.out.println("开锁帧：" + whole);

		// 向板子发五次，实现可靠传输
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));

		return response;
	}
}
