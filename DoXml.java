import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*��Ҫ���ܣ�main��������1��2������������xml�ļ�����cfg�����ļ�
 * 1.����Part���װ��Ҫ��ȡ�ĸ���xmlԪ�أ�List<Part>��װPart�������
 * 3.�жϽ��յ��Ĳ���1�Ͳ���2������1Ϊ�ļ�ʱ��������ǰ�ļ�������1Ϊ�ļ���ʱ�������ļ����µ�����xml�ļ�
 * 	����2Ϊ��ʱ����������ǰ�ļ�����ͬ��.cfg�ļ�������2Ϊ���ڵ��ļ���ʱ���������ļ����£�����2Ϊ.cfg�ļ�ʱ����Ϊ�����Ƶ��ļ�
 * 4.����xml������XmlHandler��̳�DefaultHandler�����н���xml�Ĺ��ܣ�ֻ����ԭʼ�ļ�·�����н��������������List��
 * �����������£�
 * startDocument() -->  startElement(String uri, String localName, String qName, Attributes attributes) ������Ԫ�عش洢  -->  characters(char[] ch, int start, int length)����ֵ��δʹ��
 * -->  startDocument()�������
 * 5.������ɺ�д�ļ�����һ�й̶����ݣ��ڶ��п�ʼ������д��Map��Ԫ�����ݣ�������
 * 6.�����������·��
 * 
 * 
 */

public class DoXml{				//������
	//args[0]		args[1]
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			try {
				judgeParse(args);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public static void judgeParse(String[] args) throws Exception{
			switch(args.length){
			case 1:	//1������
				File file = new File(args[0]);
				if(file.exists()){
					if(file.isDirectory()){	//��һ��Ŀ¼���г�����xml�ļ�����������ǰĿ¼
						File files[] = file.listFiles();
						for(int i = 0;i < files.length; i++){
							if(files[i].getName().endsWith(".xml")){	//ȡ��xml�ļ�
								doIt(files[i].getPath(),files[i].getPath().replaceAll(".xml", ".cfg"));
							}
						}
					}else{	//��һ���ļ�
						doIt(file.getPath(),file.getPath().replaceAll(".xml", ".cfg"));
					}
				}else{
					System.out.println("��ѡԭ�ļ����ļ��в�����");
				}
				break;
			case 2:	//2������
				File file1 = new File(args[0]);
				File file2 = new File(args[1]);
				if(file1.exists()){
					if(file1.isDirectory()){	//��һ��Ŀ¼���г�����xml�ļ�����������ǰĿ¼
						File files[] = file1.listFiles();
						for(int i = 0;i < files.length; i++){
							if(files[i].getName().endsWith(".xml")){	//ȡ��xml�ļ�
								if(file2.isDirectory()){
									doIt(files[i].getPath(),(file2.getPath()+File.separator+files[i].getName()).replaceAll(".xml", ".cfg"));
								}else{
									System.out.println("������ΪĿ¼ʱ��������һ����һ��Ŀ¼");
								}							
							}
						}
					}else{	//��һ���ļ�
						doIt(file2.getPath(),file2.getPath().replaceAll(".xml", ".cfg"));
					}
				}else{
					System.out.println("��ѡԭ�ļ����ļ��в�����");
				}
				break;
			default:
					System.out.println("�����������");
				break;
			}
		}
		
		public static void doIt(String src,String des) throws Exception{	//����������
			XmlHandler handler = XmlHandler.getInstance();
			List<Part> part = handler.getXmlParts(src);
			StringBuffer firstLine = new StringBuffer("mtd_name\tmtd_type\tmtd_version\tstart_address\tfs_path\tmtd_file size\n");	//��һ������
			OutputStream out = null;
			for(Part p : part){
				File desFile = new File(des);
				 if (desFile.exists()) {  
		                  
		            } else {  
		                desFile.createNewFile();// �������򴴽�  
		            }
				 out = new FileOutputStream(desFile);
				//boot       	   3  20201  0x0         no fastboot.bin  0x400000
				firstLine.append(p.getPartitionName()+"\t3\t20201\t0x"+p.getStart()+"\tno\t"+p.getSelectFile()+"\t0x"+p.getLength()+"\n");		//�����ı�����
			}
			byte b[] = firstLine.toString().getBytes();
			out.write(b);
			out.close();
		}
		
}

//�̳�DefaultHandler�󣬿��Խ���xml�ļ�
class XmlHandler extends DefaultHandler{
	private Part xmlPart = null;
	private List<Part> xmlParts = null;
	private String preTag = null;	//��¼����ʱ����һ���ڵ�����
	private int count = 0;	//��ǵڼ���Part�ڵ�
	private static XmlHandler instance;
	
	private XmlHandler(){
	}
	public static XmlHandler getInstance(){
		if(instance == null){
			instance = new XmlHandler();
		}
		
		return instance;
	}
	public  void judgeParse(String[] args) {
		// TODO Auto-generated method stub
		switch(args.length){
		case 1:
			File src = new File(args[0]);
			if(src!=null){
				if(src.isDirectory()){	//Ŀ¼
					
				}else if(args[0].endsWith(".xml")){	//xml�ļ�
					
				}
			}
			break;
		}			
	}
	
	//�������������xml
	public List<Part> getXmlParts(String xmlSrc) throws Exception{  
		InputStream in = new FileInputStream(xmlSrc);
        SAXParserFactory factory = SAXParserFactory.newInstance();  
        SAXParser parser = factory.newSAXParser();  
        parser.parse(in, instance);			//�ύ��������
        return instance.getXmlParts();
    }

	public List<Part> getXmlParts() {
		return xmlParts;
	}

	public void setXmlParts(List<Part> xmlParts) {
		this.xmlParts = xmlParts;
	}

	@Override
	//��������<?xml.....>ʱ�������startDocument()����
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		xmlParts = new ArrayList<Part>();
		System.out.println("��ʼ�����ļ�������������");
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	//������ȡ��һ���ڵ㣬���е���
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if("Part".equals(qName)){		//��ȡ��Part�ڵ�
			xmlPart = new Part();
			xmlPart.setPartitionName(attributes.getValue(1));
			//xmlPart.setStart(attributes.getValue(4));
			String start = ("0".equals(attributes.getValue(4)) )?"0":Long.toHexString(Long.valueOf(attributes.getValue(4).replaceAll("M", ""))*1024*1024).toString();
			String len = Long.toHexString(Long.valueOf(attributes.getValue(5).replaceAll("M", ""))*1024*1024).toString();
			xmlPart.setStart(start);
			xmlPart.setLength(len);
			//xmlPart.setLength(attributes.getValue(5));
			if(attributes.getValue(6) != null && attributes.getValue(6) != ""){
				xmlPart.setSelectFile(attributes.getValue(6));
			}else{
				xmlPart.setSelectFile("null");
			}
						
			System.out.println(attributes.getValue(6));				//���ݽ������ˣ��ô���List��
			xmlParts.add(xmlPart);
		}
		count++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if("Partition_Info".equals(qName)){
			xmlPart = null;
		}
		count = 0;
		System.out.println("������ɣ��������ɡ�����������");
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}
}

	//Part�࣬������װ���ݣ���װelementԪ��
class Part{
	//<Part Sel="1" PartitionName="boot" FlashType="emmc" FileSystem="none" Start="0" Length="4M" SelectFile="fastboot.bin"/>
	//���϶�Ӧvale0      1										2								3								4				5						6
	//���õ�ΪPartitionName,start,length,selectFile����Ӧvale 1		4		5		6
	private int sel;
	private String partitionName,flashType,fileSystem,selectFile,start,length;
	/**
	 * @return the partitionName
	 */
	public String getPartitionName() {
		return partitionName;
	}
	/**
	 * @param partitionName the partitionName to set
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
	 * @param selectFile the selectFile to set
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
	 * @param start the start to set
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
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}