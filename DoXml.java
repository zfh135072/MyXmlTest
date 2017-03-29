package com.zfh.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*主要功能：main函数接收1或2个参数，解析xml文件生成cfg配置文件
 * 1.定义Part类包装需要提取的各个xml元素，List<Part>封装Part便于输出
 * 2.判断接收到的参数1和参数2，参数1为文件时，解析当前文件；参数1为文件夹时；解析文件夹下的所有xml文件
 * 	参数2为空时，解析到当前文件夹下同名.cfg文件；参数2为存在的文件夹时，解析到文件夹下；参数2为.cfg文件时解析为该名称的文件
 * 3.解析xml，定义XmlHandler类继承DefaultHandler，具有解析xml的功能，只接收原始文件路径进行解析，解析后存入List中
 * 生命周期如下：
 * startDocument() -->  startElement(String uri, String localName, String qName, Attributes attributes) 解析出元素关存储  -->  characters(char[] ch, int start, int length)解析值，未使用
 * -->  startDocument()解析完成
 * 4.解析完成后写文件：第一行固定内容，第二行开始，依次写入Map中元素内容，并换算
 * 5.输出到参数２路径
 * 
 * 
 */

public class DoXml { // 主函数
	// args[0] args[1]
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			judgeParse(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void judgeParse(String[] args) throws Exception {
		switch (args.length) {
		case 1: // 1个参数
			File file = new File(args[0]);
			if (file.exists()) {
				if (file.isDirectory()) { // 是一个目录，列出所有xml文件，解析到当前目录
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						if (files[i].getName().endsWith(".xml")) { // 取出xml文件
							doIt(files[i].getPath(), files[i].getPath().replaceAll(".xml", ".cfg"));
						}
					}
				} else { // 是一个文件
					doIt(file.getPath(), file.getPath().replaceAll(".xml", ".cfg"));
				}
			} else {
				System.out.println("所选原文件或文件夹不存在");
			}
			break;
		case 2: // 2个参数
			File file1 = new File(args[0]);
			File file2 = new File(args[1]);
			if (file1.exists()) {
				if (file1.isDirectory()) { // 是一个目录，列出所有xml文件，解析到当前目录
					File files[] = file1.listFiles();
					for (int i = 0; i < files.length; i++) {
						if (files[i].getName().endsWith(".xml")) { // 取出xml文件
							if (file2.isDirectory()) {
								doIt(files[i].getPath(), (file2.getPath() + File.separator + files[i].getName())
										.replaceAll(".xml", ".cfg"));
							} else {
								System.out.println("参数１为目录时，参数２一定是一个目录");
							}
						}
					}
				} else { // 是一个文件
					doIt(file2.getPath(), file2.getPath().replaceAll(".xml", ".cfg"));
				}
			} else {
				System.out.println("所选原文件或文件夹不存在");
			}
			break;
		default:
			System.out.println("参数输入错误！");
			break;
		}
	}

	public static void doIt(String src, String des) throws Exception { // 主函数解析
		XmlHandler handler = XmlHandler.getInstance();
		List<Part> part = handler.getXmlParts(src);
		StringBuffer firstLine = new StringBuffer(
				"mtd_name\tmtd_type\tmtd_version\tstart_address\tfs_path\tmtd_file size\n"); // 第一行内容
		OutputStream out = null;
		for (Part p : part) {
			File desFile = new File(des);
			if (desFile.exists()) {

			} else {
				desFile.createNewFile();// 不存在则创建
			}
			out = new FileOutputStream(desFile);
			// boot 3 20201 0x0 no fastboot.bin 0x400000
			firstLine.append(p.getPartitionName() + "\t3\t20201\t0x" + p.getStart() + "\tno\t" + p.getSelectFile()
					+ "\t0x" + p.getLength() + "\n"); // 后面文本内容
		}
		byte b[] = firstLine.toString().getBytes();
		out.write(b);
		out.close();
	}
}

// 继承DefaultHandler后，可以解析xml文件
class XmlHandler extends DefaultHandler {
	private Part xmlPart = null;
	private List<Part> xmlParts = null;
	private String preTag = null; // 记录解析时的上一个节点名称
	private int count = 0; // 标记第几个Part节点
	private String start = null;	//起始位置
	private String len = null;		//文件长
	private static XmlHandler instance;

	private XmlHandler() {
	}

	public static XmlHandler getInstance() {
		if (instance == null) {
			instance = new XmlHandler();
		}

		return instance;
	}

	public void judgeParse(String[] args) {
		// TODO Auto-generated method stub
		switch (args.length) {
		case 1:
			File src = new File(args[0]);
			if (src != null) {
				if (src.isDirectory()) { // 目录

				} else if (args[0].endsWith(".xml")) { // xml文件

				}
			}
			break;
		}
	}

	// 传入参数，解析xml
	public List<Part> getXmlParts(String xmlSrc) throws Exception {
		InputStream in = new FileInputStream(xmlSrc);
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(in, instance); // 提交解析任务
		return instance.getXmlParts();
	}

	public List<Part> getXmlParts() {
		return xmlParts;
	}

	public void setXmlParts(List<Part> xmlParts) {
		this.xmlParts = xmlParts;
	}

	@Override
	// １、读入<?xml.....>时，会调用startDocument()方法
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		xmlParts = new ArrayList<Part>();
		System.out.println("开始解析文件。。。。。。");
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	// ２、读取到一个节点，运行到此
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if ("Part".equals(qName)) { // 读取到Part节点
			xmlPart = new Part();
			if("fastboot".equals(attributes.getValue(1))){	//此处由于fastboot一般是错的，直接写为boot
				xmlPart.setPartitionName("boot");
			}else{
				xmlPart.setPartitionName(attributes.getValue(1));
			}
			// xmlPart.setStart(attributes.getValue(4));
			if("0".equals(attributes.getValue(4))){		//超始位置是0
				start = "0";
			}else if(attributes.getValue(4).endsWith("K")){
				start = Long.toHexString(Long.valueOf(attributes.getValue(4).replaceAll("K", "")) * 1024).toString();
			}else if(attributes.getValue(4).endsWith("M")){
				start = Long.toHexString(Long.valueOf(attributes.getValue(4).replaceAll("M", "")) * 1024 * 1024).toString();
			}
			
			if("0".equals(attributes.getValue(5))){		
				len = "0";
			}else if(attributes.getValue(5).endsWith("K")){
				len = Long.toHexString(Long.valueOf(attributes.getValue(5).replaceAll("K", "")) * 1024).toString();
			}else if(attributes.getValue(5).endsWith("M")){
				len = Long.toHexString(Long.valueOf(attributes.getValue(5).replaceAll("M", "")) * 1024 * 1024).toString();
			}
			xmlPart.setStart(start);
			xmlPart.setLength(len);
			
			if(attributes.getValue(6) != null && attributes.getValue(6) != ""){
				xmlPart.setSelectFile(attributes.getValue(6));
			}else{
				xmlPart.setSelectFile("null");
			}			
			System.out.println(attributes.getValue(6));
			xmlParts.add(xmlPart);
		}
		count++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if ("Partition_Info".equals(qName)) {
			xmlPart = null;
		}
		count = 0;
		System.out.println("解析完成，正在生成。。。。。。");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}
}

// Part类，用来组装数据，包装element元素
class Part {
	// <Part Sel="1" PartitionName="boot" FlashType="emmc" FileSystem="none"
	// Start="0" Length="4M" SelectFile="fastboot.bin"/>
	// 以上对应vale0 1 2 3 4 5 6
	// 有用的为PartitionName,start,length,selectFile，对应vale 1 4 5 6
	private int sel;
	private String partitionName, flashType, fileSystem, selectFile, start, length;

	/**
	 * @return the partitionName
	 */
	public String getPartitionName() {
		return partitionName;
	}

	/**
	 * @param partitionName
	 *            the partitionName to set
	 */
	public void setPartitionName(String partitionName) {
		this.partitionName = partitionName;
	}

	/**
	 * @return the selectFile
	 */
	public String getSelectFile() {
		return selectFile;
	}

	/**
	 * @param selectFile
	 *            the selectFile to set
	 */
	public void setSelectFile(String selectFile) {
		this.selectFile = selectFile;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}
