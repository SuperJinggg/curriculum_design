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


import design.dao.DataFrameDao;
import design.dto.MyResponse;
import design.entity.DataFrame;
import design.portWrite.Com2Writer;
import design.service.PortService;
import design.util.Util;

@RequestMapping(value = "dataFrame")
@Controller
public class DataFrameController {
	
	//自动注入
	@Autowired
	DataFrameDao dao;
	
	@Autowired
	CompressorController compressorController;
	
	/**
	 * 给前端发送所有有用帧字段
	 * @return
	 */
	@RequestMapping(value = "/getDataFrame.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse getDataFrame()
	{
		MyResponse myResponse = new MyResponse();
		DataFrame dataFrame = new DataFrame();
		List<Map<String,Object>> dataFramePartList =  dao.getDataFrame();
		for (Map<String, Object> map : dataFramePartList) {
			dataFrame.dataFrameId = 1;
			dataFrame.deviceCoding = (String) map.get("deviceCoding");
			dataFrame.deviceAddr = (int) map.get("deviceAddr");
			dataFrame.stateUpInterval = (int) map.get("stateUpInterval");
			dataFrame.startOutTime = (int) map.get("startOutTime");
			dataFrame.settingTem = Double.parseDouble((String) map.get("settingTem"));
			dataFrame.TemCBias = (int) map.get("TemCBias");
			dataFrame.deviceState = (int) map.get("deviceState");
			dataFrame.compreRunState = (int) map.get("compreRunState");
			dataFrame.percepTem = Double.parseDouble((String) map.get("percepTem"));
			dataFrame.drawer1 = (int) map.get("drawer1");
			dataFrame.drawer2 = (int) map.get("drawer2");
			dataFrame.drawer3 = (int) map.get("drawer3");
			dataFrame.drawer4 = (int) map.get("drawer4"); 
			dataFrame.drawer5 = (int) map.get("drawer5");
			dataFrame.drawer6 = (int) map.get("drawer6");
			dataFrame.drawer7 = (int) map.get("drawer7");
			dataFrame.drawer8 = (int) map.get("drawer8");
			dataFrame.drawer9 = (int) map.get("drawer9");
			dataFrame.drawer10 = (int) map.get("drawer10");
		}
		
		System.out.println("我是getDataFrame，我被调用了！！！！！！");
		
		//采集温度
		double collectTem = dataFrame.percepTem;
		//控制温度
		double controllerTem = dataFrame.settingTem;
		//压缩机状态
		int compreState = dataFrame.compreRunState;
		//控制温度偏差
		int TemControllerBias = dataFrame.TemCBias;
		//如果当前温度大于设定温度加温度控制偏差，但压缩机处于工作状态，什么也不干
		if(collectTem > (controllerTem + TemControllerBias)
				&& (compreState == 2)) {
			
		}//如果当前温度大于设定温度加温度控制偏差，但压缩机处于关闭状态，则发送打开压缩机的控制帧
		else if(collectTem > (controllerTem + TemControllerBias)
				&& (compreState == 0 || compreState == 1)){
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("compressorState", 1);
			compressorController.updateComState(queryMap);
		}//如果当前温度小于或等于设定温度，且压缩机处于停止状态，什么也不做
		else if(collectTem <= (controllerTem - TemControllerBias)
				&& compreState == 0) {
			
		}//如果当前温度小于或等于设定温度，但压缩机处于工作状态，则发送关闭压缩机的控制帧
		else if(collectTem <= (controllerTem - TemControllerBias)
				&& (compreState == 1 || compreState == 2)) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("compressorState", 0);
			compressorController.updateComState(queryMap);
		}//如果压缩机故障，啥也不做，板子自己处理
		else if(compreState == 3){
			
		}
		
		
		
		
		myResponse.data = dataFrame;
		return myResponse;
	}
	
	/**
	 * 根据用户需求更改帧字段
	 * @param queryMap
	 * @return
	 */
	@RequestMapping(value = "/updateDataFrame.action", method = RequestMethod.POST)
	@ResponseBody
	public MyResponse updateDataFrame(@RequestBody Map<String, Object> queryMap)
	{
		MyResponse response = new MyResponse();
		DataFrame dataFrame = new DataFrame();
		dataFrame.dataFrameId = 1;
		dataFrame.deviceCoding = (String) queryMap.get("deviceCoding");
		dataFrame.deviceAddr = (int) queryMap.get("deviceAddr");
		dataFrame.stateUpInterval = (int) queryMap.get("stateUpInterval");
		dataFrame.startOutTime = (int) queryMap.get("startOutTime");
		dataFrame.settingTem = (int) queryMap.get("settingTem");
		dataFrame.TemCBias = (int) queryMap.get("TemCBias");
		dataFrame.deviceState = (int) queryMap.get("deviceState");
		dataFrame.compreRunState = (int) queryMap.get("compreRunState");
		dataFrame.percepTem = (int) queryMap.get("percepTem");
		dataFrame.drawer1 = (int) queryMap.get("drawer1");
		dataFrame.drawer2 = (int) queryMap.get("drawer2");
		dataFrame.drawer3 = (int) queryMap.get("drawer3");
		dataFrame.drawer4 = (int) queryMap.get("drawer4"); 
		dataFrame.drawer5 = (int) queryMap.get("drawer5");
		dataFrame.drawer6 = (int) queryMap.get("drawer6");
		dataFrame.drawer7 = (int) queryMap.get("drawer7");
		dataFrame.drawer8 = (int) queryMap.get("drawer8");
		dataFrame.drawer9 = (int) queryMap.get("drawer9");
		dataFrame.drawer10 = (int) queryMap.get("drawer10");
		
		int count = dao.updateDataFrame(dataFrame);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		response.data = result;
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/Query.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse SendQuery() {
		
		MyResponse response = new MyResponse();
		
		//查询帧全部字段
		String whole;
		//需要进行校验的载荷部分
		String payLoad;
		
		//信息头
		String header = "FFFF";
		//帧长
		String flength = "0A";
		
		//帧号
		String seq;
		String chseqS = Util.decToHex(PortService.frameSeq);
		PortService.frameSeq = (PortService.frameSeq + 1) % 256;
		char[] chseqC = chseqS.toCharArray();
		while(chseqC.length < 2) {
			chseqS = "0" + chseqS;
			chseqC = chseqS.toCharArray();
		}
		seq = chseqS;
		
		//设备地址
		String deAddress;
		Map<String,Object> sqlResult = (Map<String, Object>) getDeAddress().data;
		System.out.println("设备地址：    " + (int) sqlResult.get("deviceAddr"));
		String chDeAddrS = String.valueOf((int) sqlResult.get("deviceAddr"));
		char[] chDeAddrC = chDeAddrS.toCharArray();
		while(chDeAddrC.length < 2) {
			chDeAddrS = "0" + chDeAddrS;
			chDeAddrC = chDeAddrS.toCharArray();
		}
		deAddress = chDeAddrS;
		
		//功能号
		String functionNum = "01";
		//载荷确认
		payLoad = flength + seq + deAddress + functionNum;
		
		//生成crc校验码
		String crc;
		String crcS = Util.byte2HexStr(Util.CRC16(Util.hexStr2Byte(payLoad)));
		char[] crcC = crcS.toCharArray();
		while(crcC.length < 4) {
			crcS = "0" + crcS;
			crcC = crcS.toCharArray();
		}
		crc = crcS;
		
		//结束标志
		String finishFlag = "FFF7";
		//确定查询帧全部字段
		whole = header + payLoad + crc + finishFlag;
		
		System.out.println("查询帧：" + whole);
		
		//向板子发五次，实现可靠传输
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		
		
		response.data = getDataFrame().data;
		return response;
	}
	
	
	public MyResponse getDeAddress() {
		MyResponse myResponse = new MyResponse();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> dataFramePartList =  dao.getDeviceAddr();
		int deviceAddr = 0;
		for (Map<String, Object> map : dataFramePartList) {
			deviceAddr = (int) map.get("deviceAddr");
		}
		result.put("deviceAddr", deviceAddr);
		myResponse.data = result;
		return myResponse;
	}
}
