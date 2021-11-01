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

import design.controllerObject.Compressor;
import design.dao.CompressorDao;
import design.dao.DataFrameDao;
import design.dto.MyResponse;
import design.portWrite.Com2Writer;
import design.service.PortService;
import design.util.Util;

@RequestMapping(value = "compressor")
@Controller
public class CompressorController {
	
	@Autowired
	CompressorDao dao;
	
	@Autowired
	DataFrameDao dfdao;
	
	@Autowired
	DataFrameController dfController;
	
	@RequestMapping(value = "/getComState.action", method = RequestMethod.GET)
	@ResponseBody
	public MyResponse getComState() {
		MyResponse myResponse = new MyResponse();
		Compressor compressor = new Compressor();
		List<Map<String,Object>> coMaps = dao.getComState();
		for (Map<String, Object> map : coMaps) {
			compressor.compressorState = (int) map.get("compressorState");
		}
		
		myResponse.data = compressor;
		return myResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateComState.action", method = RequestMethod.POST)
	@ResponseBody
	public MyResponse updateComState(@RequestBody Map<String, Object> queryMap) {
		System.out.println("我是updateComState，我被调用了！！");
		MyResponse response = new MyResponse();
		Compressor compressor = new Compressor();
		compressor.compressorId = 1;
		//获取客户设置的压缩机工作状态
		compressor.compressorState = (int) queryMap.get("compressorState");
		//将设置结果返回给用户
		int count = dao.updateComState(compressor);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("count", count);
		response.data = result;
		
		//启停温度控制帧全部字段
		String whole;
		//需要进行校验的载荷部分
		String payLoad;
		
		//信息头
		String header = "FFFF";
		//帧长
		String flength = "0B";
		
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
		Map<String,Object> sqlResult = (Map<String, Object>) dfController.getDeAddress().data;
		System.out.println("设备地址：    " + (int) sqlResult.get("deviceAddr"));
		String chDeAddrS = String.valueOf((int) sqlResult.get("deviceAddr"));
		char[] chDeAddrC = chDeAddrS.toCharArray();
		while(chDeAddrC.length < 2) {
			chDeAddrS = "0" + chDeAddrS;
			chDeAddrC = chDeAddrS.toCharArray();
		}
		deAddress = chDeAddrS;
		
		//功能号
		String functionNum = "02";
		
		//数据部分
		String data;
		String dataS = String.valueOf((int) queryMap.get("compressorState"));
		char[] dataC = dataS.toCharArray();
		while(dataC.length < 2) {
			dataS = "0" + dataS;
			dataC = dataS.toCharArray();
		}
		data = dataS;
		
		//载荷确认
		payLoad = flength + seq + deAddress + functionNum + data;
		
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
		//确定启停压缩机控制帧全部字段
		whole = header + payLoad + crc + finishFlag;
		
		System.out.println("启停压缩机控制帧：" + whole);
		
		//向板子发五次，实现可靠传输
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		new Com2Writer(Util.hexStr2Byte(whole));
		
		return response;
	}
	
	
}
