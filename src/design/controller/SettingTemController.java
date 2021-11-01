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

import design.controllerObject.SettingTem;
import design.dao.SettingTemDao;
import design.dto.MyResponse;
import design.portWrite.Com2Writer;
import design.service.PortService;
import design.util.Util;

@RequestMapping(value = "settingTem")
@Controller
public class SettingTemController {
	
	@Autowired
	SettingTemDao dao;
	
	@Autowired
	DataFrameController dfController;
	
	@RequestMapping(value = "/getSettingTem.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse getSettingTem() {
		MyResponse myResponse = new MyResponse();
		SettingTem sTem = new SettingTem();
		List<Map<String,Object>> sMaps = dao.getSettingTem();
		for (Map<String, Object> map : sMaps) {
			sTem.settingTemNum = (int) map.get("settingTemNum");
		}
		
		myResponse.data = sTem;
		return myResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateSettingTem.action", method = RequestMethod.POST)
	@ResponseBody
	public MyResponse updateSettingTem(@RequestBody Map<String, Object> queryMap) {
		MyResponse response = new MyResponse();
		SettingTem sTem = new SettingTem();
		sTem.settingTemId = 1;
		sTem.settingTemNum = (int) queryMap.get("settingTemNum");

		int count = dao.updateSettingTem(sTem);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		response.data = result;

		// 设置温度帧全部字段
		String whole;
		// 需要进行校验的载荷部分
		String payLoad;

		// 信息头
		String header = "FFFF";
		// 帧长
		String flength = "0B";

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
		String functionNum = "04";

		// 数据部分
		String data;
		// 读取用户设置的温度信息
		String dataS = String.valueOf((int) queryMap.get("settingTemNum"));
		int dataInt = Integer.parseInt(dataS);
		if(dataInt < 0) {
			dataInt = dataInt * (-1);
			String BTem = Integer.toBinaryString(dataInt);
			char[] BTemC = BTem.toCharArray();
			while(BTemC.length < 6) {
				BTem = "0" + BTem;
				BTemC = BTem.toCharArray();
			}
			BTem = "1" + BTem + "0";
			dataS = Util.bstr2Hstr(BTem);
		}else {
			String BTem = Integer.toBinaryString(dataInt);
			char[] BTemC = BTem.toCharArray();
			while(BTemC.length < 6) {
				BTem = "0" + BTem;
				BTemC = BTem.toCharArray();
			}
			BTem = "0" + BTem + "0";
			dataS = Util.bstr2Hstr(BTem);
		}
		char[] dataC = dataS.toCharArray();
		while (dataC.length < 2) {
			dataS = "0" + dataS;
			dataC = dataS.toCharArray();
		}
		data = dataS;
		System.out.println("！！！！！！！！！控制温度大小：    " + dataS);

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
		// 确定设置温度帧全部字段
		whole = header + payLoad + crc + finishFlag;

		System.out.println("设置温度帧：" + whole);

		// 向板子发三次，实现可靠传输
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));

		return response;
	}
	
}
