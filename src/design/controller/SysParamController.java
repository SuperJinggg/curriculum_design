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

import design.controllerObject.DeCoding;
import design.controllerObject.SysParam;
import design.dao.DeCodingDao;
import design.dao.SysParamDao;
import design.dto.MyResponse;
import design.portWrite.Com2Writer;
import design.service.PortService;
import design.util.Util;

@RequestMapping(value = "sysParam")
@Controller
public class SysParamController {
	
	@Autowired
	SysParamDao dao;
	
	@Autowired
	DataFrameController dfController;
	
	@Autowired
	DeCodingDao dcDao;
	
	@RequestMapping(value = "/getSysParam.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse getSysParam() {
		MyResponse myResponse = new MyResponse();
		SysParam sysParam = new SysParam();
		List<Map<String,Object>> spMaps = dao.getSysParam();
		for (Map<String, Object> map : spMaps) {
			sysParam.deviceCoding =  (String) map.get("deviceCoding");
			sysParam.deviceAddr = (int) map.get("deviceAddr");
			sysParam.stateUpInterval = (int) map.get("stateUpInterval");
			sysParam.startOutTime = (int) map.get("startOutTime");
			sysParam.settingTem = (int) map.get("settingTem");
			sysParam.TemCBias = (int) map.get("TemCBias");
		}
		
		myResponse.data = sysParam;
		return myResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateSysParam.action", method = RequestMethod.POST)
	@ResponseBody
	public MyResponse updateSysParam(@RequestBody Map<String, Object> queryMap) {
		MyResponse response = new MyResponse();
		SysParam sysParam = new SysParam();
		sysParam.sysParamId = 1;
		sysParam.deviceCoding = (String) queryMap.get("deviceCoding");
		sysParam.deviceAddr = (int) queryMap.get("deviceAddr");
		sysParam.stateUpInterval = (int) queryMap.get("stateUpInterval");
		sysParam.startOutTime = (int) queryMap.get("startOutTime");
		sysParam.settingTem = (int) queryMap.get("settingTem");
		sysParam.TemCBias = (int) queryMap.get("TemCBias");
		
		int count = dao.updateSysParam(sysParam);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		response.data = result;
		
		//更新用户设置的设备编址
		DeCoding deCoding = new DeCoding();
		deCoding.deviceCodingId = 1;
		deCoding.deviceCoding = (String) queryMap.get("deviceCoding");
		dcDao.updateDeviceCoding(deCoding);
		//更新用户设置设备地址
		deCoding.deviceAddress = sysParam.deviceAddr;
		dcDao.updateDeAddress(deCoding);

		// 设置温度帧全部字段
		String whole;
		// 需要进行校验的载荷部分
		String payLoad;

		// 信息头
		String header = "FFFF";
		// 帧长
		String flength = "1C";

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
		String chDeAddrS = Util.decToHex((int) sqlResult.get("deviceAddr"));
		char[] chDeAddrC = chDeAddrS.toCharArray();
		while (chDeAddrC.length < 2) {
			chDeAddrS = "0" + chDeAddrS;
			chDeAddrC = chDeAddrS.toCharArray();
		}
		deAddress = chDeAddrS;

		// 功能号
		String functionNum = "05";

		// 数据部分
		String data;
			//设备编码
		System.out.println("我是设备编码：" + (String) queryMap.get("deviceCoding"));
		String deviceCodingS = Util.longToHex(Long.decode((String) queryMap.get("deviceCoding")));
		System.out.println("我是十进制的设备编码：" + deviceCodingS);
		char[] deviceCodingC = deviceCodingS.toCharArray();
		while(deviceCodingC.length < 10) {
			deviceCodingS = "0" + deviceCodingS;
			deviceCodingC = deviceCodingS.toCharArray();
		}
			//设备地址
		String deviceAddrS = Util.decToHex((int) queryMap.get("deviceAddr"));
		char[] deviceAddrC = deviceAddrS.toCharArray();
		while(deviceAddrC.length < 2) {
			deviceAddrS = "0" + deviceAddrS;
			deviceAddrC = deviceAddrS.toCharArray();
		}
			//状态上传时延
		String stateUpIntervalS = Util.decToHex((int) queryMap.get("stateUpInterval"));
		char[] stateUpIntervalC = stateUpIntervalS.toCharArray();
		while(stateUpIntervalC.length < 2) {
			stateUpIntervalS = "0" + stateUpIntervalS;
			stateUpIntervalC = stateUpIntervalS.toCharArray();
		}
		   	//压缩机启动时延
		String startOutTimeS = Util.decToHex((int) queryMap.get("startOutTime"));
		char[] startOutTimeC = startOutTimeS.toCharArray();
		while(startOutTimeC.length < 2) {
			startOutTimeS = "0" + startOutTimeS;
			startOutTimeC = startOutTimeS.toCharArray();
		}
			//控制温度
		String settingTemS = String.valueOf((int) queryMap.get("settingTem"));
		int TemInt = Integer.parseInt(settingTemS);
		if(TemInt < 0) {
			TemInt = TemInt * (-1);
			String BTem = Integer.toBinaryString(TemInt);
			char[] BTemC = BTem.toCharArray();
			while(BTemC.length < 6) {
				BTem = "0" + BTem;
				BTemC = BTem.toCharArray();
			}
			BTem = "1" + BTem + "0";
			settingTemS = Util.bstr2Hstr(BTem);
		}else {
			String BTem = Integer.toBinaryString(TemInt);
			char[] BTemC = BTem.toCharArray();
			while(BTemC.length < 6) {
				BTem = "0" + BTem;
				BTemC = BTem.toCharArray();
			}
			BTem = "0" + BTem + "0";
			settingTemS = Util.bstr2Hstr(BTem);
		}
		char[] settingTemC = settingTemS.toCharArray();
		while(settingTemC.length < 2) {
			settingTemS = "0" + settingTemS;
			settingTemC = settingTemS.toCharArray();
		}
			//控制温度偏差
		String TemCBiasS = Util.decToHex((int) queryMap.get("TemCBias"));
		char[] TemCBiasC = TemCBiasS.toCharArray();
		while(TemCBiasC.length < 2) {
			TemCBiasS = "0" + TemCBiasS;
			TemCBiasC = TemCBiasS.toCharArray();
		}
		data = deviceCodingS + deviceAddrS + "00" + stateUpIntervalS + startOutTimeS
				+ "0000" + settingTemS + TemCBiasS + "FFFF" + "FFFF" + "00";

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
		// 设置参数帧全部字段
		whole = header + payLoad + crc + finishFlag;

		System.out.println("设置参数帧：" + whole);

		// 向板子发三次，实现可靠传输
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));

		return response;
	}

}
