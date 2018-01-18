package com.mbfw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;


public class ExcelUtil {
	 private ExcelUtil() {
	        throw new Error("让工具类不可实例化！");
	 }
	 /**
	     * 设置log4j文件
	     */
	private static Logger logger = Logger.getLogger(ExcelUtil.class);
	/**
     * 创建标题行
     *
     * @param wb      Excel对象
     * @param sheet   sheet对象
     * @param value   标题文字
     * @param colspan 标题跨列
     */
    public static final void createTitle(Workbook wb, Sheet sheet, String value, int colspan) {
        createTitle(wb, sheet, value, 0, colspan, 0);
    }
	/**
     * 在指定行的指定单元格创建标题式文字
     *
     * @param wb
     * @param sheet
     * @param value
     */
    public static final void createTitle(Workbook wb, Sheet sheet, String value, int start, int end, int rowIndex) {
        Row headRow = sheet.createRow(rowIndex);
        headRow.setHeightInPoints(30f);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, start, end));
        for (int i = start; i <= end; i++) {
            Cell indexCell = headRow.createCell(i);
            indexCell.setCellValue(i == 0 ? value : "");
            indexCell.setCellStyle(titleStyle(wb));
        }
    }
    /**
     * 标题样式
     *
     * @param wb
     * @return
     */
    private static final CellStyle titleStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFont(headFont(wb, 14, true));
        commonCellBorderStyle(style);
        return style;
    }
    
    /**
     * 设置头字体
     *
     * @param wb Excel对象
     * @return Font字体对象
     */
    public static final Font headFont(Workbook wb, int fontPoints, boolean boldWeight) {
        Font font = wb.createFont();
        if (boldWeight)
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) fontPoints);
        return font;
    }
    
    /**
     * 设置单元格边框
     *
     * @param style
     */
    public static final void commonCellBorderStyle(CellStyle style) {
        style.setBorderBottom((short) 1);
        style.setBorderTop((short) 1);
        style.setBorderLeft((short) 1);
        style.setBorderRight((short) 1);
        style.setWrapText(true);//自动换行
    }
    
    /**
     * 获取excel中HEAD头文件格式并创建头（在指定的行创建）
     *
     * @param wb          excel对象
     * @param sheet       sheet对象
     * @param columnName  列名
     * @param columnWidth 列宽
     * @param rowIndex
     */
    public static final void getHeader(org.apache.poi.ss.usermodel.Workbook wb, Sheet sheet, String[] columnName, int[] columnWidth, int rowIndex) {
    	getHeader(wb,sheet,columnName,columnWidth,rowIndex,commonHeadStyle(wb));
    }
    public static final void getHeader(org.apache.poi.ss.usermodel.Workbook wb, Sheet sheet, String[] columnName, int[] columnWidth, int rowIndex,CellStyle style) {
        Row row = sheet.createRow(rowIndex);
        row.setHeight((short) 600);
        for (int j = 0; j < columnName.length; j++) {
            sheet.setColumnWidth(j, columnWidth[j]);
            Cell cell = row.createCell(j);
            cell.setCellValue(columnName[j]);
            cell.setCellStyle(style);
        }
    }
    
    /**
     * 头文件样式
     *
     * @param wb Excel对象
     * @return Excel 样式
     */
    public static final CellStyle commonHeadStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern((short) 1);
        style.setFillForegroundColor((short) 55);
        commonCellBorderStyle(style);
        style.setFont(headFont(wb, 10, true));
        return style;
    }
    
    /**
     * 创建居中的样式单元格
     *
     * @param wb    Excel对象
     * @param row   行对象
     * @param index 列的下标
     * @param value 列的值
     */
    public static final void createFormatCenter(Workbook wb, Row row, int index, String value) {
        setRowHeight(row);
        Cell indexCell = row.createCell(index);
        indexCell.setCellValue(value);
        indexCell.setCellStyle(centerStyle(wb, false));
    }
    public static final void createFormatCenter(Workbook wb, CellStyle style,Row row, int index, String value) {
        setRowHeight(row);
        Cell indexCell = row.createCell(index);
        indexCell.setCellValue(value);
        indexCell.setCellStyle(centerStyle(wb, style));
    }
    
    /**
     * 设置默认行高
     *
     * @param row
     */
    private static final void setRowHeight(Row row) {
        //row.setHeightInPoints(18f);
    }
    
    /**
     * 获取单元格样式
     *
     * @param wb Excel对象
     * @return 单元样式
     */
    public static final CellStyle centerStyle(Workbook wb, boolean isBold) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if (isBold)
            style.setFont(headFont(wb, 14, isBold));
        commonCellBorderStyle(style);
        return style;
    }
    
    private static final CellStyle centerStyle(Workbook wb, CellStyle centerStyle) {
        commonCellBorderStyle(centerStyle);
        return centerStyle;
    }
    
    /**
     * 获取单元格样式
     *
     * @param wb   Excel对象
     * @param bold 字体是否加粗
     * @return 单元样式
     */
    public static final CellStyle rightStyle(Workbook wb, boolean bold) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if (bold)
            style.setFont(headFont(wb, 14, bold));
        commonCellBorderStyle(style);
        return style;
    }


    /**
     * 获取单元格样式
     *
     * @param wb Excel对象
     * @return 单元样式
     */
	private static final CellStyle rightStyle(Workbook wb, CellStyle rightStyle) {
        commonCellBorderStyle(rightStyle);
        return rightStyle;
    }
    
    /**
     * 获取单元格样式
     *
     * @param wb   Excel对象
     * @param bold 是否需要加粗字体
     * @return 单元样式
     */
    public static final CellStyle leftStyle(Workbook wb, boolean bold) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        if (bold)
            style.setFont(headFont(wb, 14, bold));
        commonCellBorderStyle(style);
        return style;
    }

    /**
     * 获取单元格样式
     *
     * @param wb        Excel对象
     * @param leftStyle
     * @return 单元样式
     */
	private static final CellStyle leftStyle(Workbook wb, CellStyle leftStyle) {
        commonCellBorderStyle(leftStyle);
        return leftStyle;
    }
    
    /**
     * @param wb        Excel对象
     * @param sheet
     * @return sheet冻结格式
     * 默认冻结行(3,2,3,2)第二行第三列开始冻结
     */
	public static final void defualFreezePane(Sheet sheet){
    	sheet.createFreezePane(3, 2, 3, 2);
    }
	public static final void defualFreezePane(Sheet sheet,int colSplit, int rowSplit, int leftmostColumn,int topRow){
    	sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
    }
	
	/** 
	 * @Title: importDataFromExcel 
	 * @Description: 将sheet中的数据保存到list中，
	 * 调用此方法时，vo的属性个数必须和excel文件每行数据的列数相同且一一对应，vo的所有属性都为String
	 *     excelFile;上传的文件
	 *     private String excelFileName;原始文件的文件名
	 * 页面的file控件name需对应File的文件名
	 * @param @param vo javaBean
	 * @param @param is 输入流
	 * @param @param excelFileName
	 * @param @return
	 * @return List<Object>
	 * @throws 
	 */

	public static final List<Object> importDataFromExcel(Object vo,InputStream is,String excelFileName)throws Exception{
		 List<Object> list = new ArrayList<Object>();
	        try {
	            //创建工作簿
	            Workbook workbook = createWorkbook(is, excelFileName);
	            //创建工作表sheet
	            Sheet sheet = getSheet(workbook, 0);
	            //获取sheet中数据的行数
	            int rows = sheet.getPhysicalNumberOfRows();
	            //获取表头单元格个数
	            //int cells = sheet.getRow(0).getPhysicalNumberOfCells();
	            //利用反射，给JavaBean的属性进行赋值
	            Field[] fields = vo.getClass().getDeclaredFields();
	            for (int i = 1; i < rows; i++) {//第一行为标题栏，从第二行开始取数据
	                Row row = sheet.getRow(i);
	                int index = 0;
	                //导入的数据标题栏和javaBean一一对应，将每行的值存入对应的属性中
	                while (index < fields.length) {
	                    Cell cell = row.getCell(index);
	                    if (null == cell) {
	                        cell = row.createCell(index);
	                    }
	                    //String value = replaceStr(getXCellVal(cell));
	                    cell.setCellType(Cell.CELL_TYPE_STRING);
	                    String value = Tools.isEmpty(cell.getStringCellValue())?"":cell.getStringCellValue();
	                    
	                    Field field = fields[index];
	                    String fieldName = field.getName();
	                    Method setMethod = getSetMethod(vo.getClass(), fieldName,field.getType());
	                    Object obj = getValueByType(field, value);
	                    setMethod.invoke(vo, new Object[]{obj});
	                    index++;
	                }
	                if (isHasValues(vo)) {//判断对象属性是否有值
	                    list.add(vo);
	                    vo.getClass().getConstructor(new Class[]{}).newInstance(new Object[]{});//重新创建一个vo对象
	                }
	                
	            }
	        } catch (Exception e) {
	            logger.error(e.getMessage());
	        }finally{
	            try {
	                is.close();//关闭流
	            } catch (Exception e2) {
	                logger.error(e2.getMessage());
	            }
	        }
	        return list;
	}
	
	/**
	 * 为了处理导入数据与新系统原有数据的逻辑
	 * 添加此方法
	 * 
	 * 如果符合逻辑，调用该方法生产vo对象
	 * rowIndex读取数据开始行
	 * StartcellIndex读取数据开始列
	 */
	public static final Object getRowsToBean(Object vo,Sheet sheet,int rowIndex,int StartcellIndex)throws Exception{
			 //利用反射，给JavaBean的属性进行赋值
	        Field[] fields = vo.getClass().getDeclaredFields();
			Row row = sheet.getRow(rowIndex);
			if(row!=null){
				int index = 0;
				//导入的数据标题栏和javaBean一一对应，将每行的值存入对应的属性中
				while (index < fields.length) {
					Cell cell = row.getCell(StartcellIndex);
					if (null == cell) {
						cell = row.createCell(StartcellIndex);
					}
					//String value = replaceStr(getXCellVal(cell));
					//设置每个cell值得类型为String
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String value = Tools.isEmpty(cell.getStringCellValue())?"":replaceStr(cell.getStringCellValue());
					//value = checkecCellValue(value)?value:null;
					
					Field field = fields[index];
					String fieldName = field.getName();
					
					Method setMethod = getSetMethod(vo.getClass(), fieldName,field.getType());
					Object obj = getValueByType(field, value);
					setMethod.invoke(vo, new Object[]{obj});
					index++;
					StartcellIndex++;
				}
			}
			return vo;
	}
	/** 
	    * @Title: getSheet 
	    * @Description: 根据sheet索引号获取对应的sheet
	    * @param @param workbook
	    * @param @param sheetIndex
	    * @param @return
	    * @return Sheet
	    * @throws 
	    */
	public static Sheet getSheet(Workbook workbook,int sheetIndex){
	        return workbook.getSheetAt(sheetIndex);        
	}
	 
	/**
	 * @throws InvalidFormatException  
	    * @Title: createWorkbook 
	    * @Description: 判断excel文件后缀名，生成不同的workbook 
	    * @param @param is
	    * @param @param excelFileName
	    * @param @return
	    * @param @throws IOException
	    * @return Workbook
	    */
	public static Workbook createWorkbook(InputStream inp,String excelFileName) throws IOException{
		 if (!inp.markSupported()) {
	            inp = new PushbackInputStream(inp, 8);
	        }
	        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
	            return new HSSFWorkbook(inp);
	        }
	        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
	}
	 /** 
	    * @Title: isHasValues 
	    * @Description: 判断一个对象所有属性是否有值，如果一个属性有值(分空)，则返回true
	    * @param @param object
	    * @param @return
	    * @return boolean
	    * @throws 
	    */
	private static boolean isHasValues(Object object){
	        Field[] fields = object.getClass().getDeclaredFields();
	        boolean flag = false;
	        for (int i = 0; i < fields.length; i++) {
	            String fieldName = fields[i].getName();
	            String methodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	            Method getMethod;
	            try {
	                getMethod = object.getClass().getMethod(methodName);
	                Object obj = getMethod.invoke(object);
	                if (null != obj && "".equals(obj)) {
	                    flag = true;
	                    break;
	                }
	            } catch (Exception e) {
	                logger.error(e.getMessage());
	            }
	            
	        }
	        return flag;
	}
	 /**
	  * 反射的属性的get和set方法
	  * @param fildeName
	  * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	  * @throws Exception
	  */
	private static final Method getSetMethod(Class<?> objectClass, String fieldName,Class<?> methodType) throws Exception {  
		StringBuffer sb = new StringBuffer();  
	       try {  
	    	   sb.append("set");  
	    	   sb.append(fieldName.substring(0, 1).toUpperCase());  
	    	   sb.append(fieldName.substring(1));  
	       } catch (Exception e) {  
	       }  
	       return objectClass.getMethod(sb.toString(), methodType); 
	}  
	private static final Method getGetMethod(Class<?> objectClass, String fieldName,Class<?> methodType) throws Exception{  
		StringBuffer sb = new StringBuffer();  
	       try {  
	    	   sb.append("get");  
	    	   sb.append(fieldName.substring(0, 1).toUpperCase());  
	    	   sb.append(fieldName.substring(1));  
	       } catch (Exception e) {  
	       }  
	       return objectClass.getMethod(sb.toString(), methodType);  
	} 
    /**
     * @author: newze
     * @param cell
     * @return String
     * 获取单元格中的值
     */
    private static String getXCellVal(Cell cell) {
    	String  val = null;
    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    	DecimalFormat df = new DecimalFormat("0.00");    
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell))  {
                    val = fmt.format(cell.getDateCellValue()); //日期型
                } else {
                    val = df.format(cell.getNumericCellValue()); //数字型
                }
                break;
            case Cell.CELL_TYPE_STRING: //文本类型
                val = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN: //布尔型
                val = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: //空白
                val = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_ERROR: //错误
                val = "错误";
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                try {
                    val = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    val = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                val = cell.getRichStringCellValue() == null ? "" : cell.getRichStringCellValue().toString();
        }
        return val;
    }
	/**
	 * 
	 * @param args
	 * @throws Exception
	 * 判断反射属性的类型，并返回该类型class
	 */
	private static final Object getValueByType(Field field,String value){
		Type genericType = field.getGenericType();
		// 如果类型是String
		if (genericType.toString().equals(
				"class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
			return value;
		}
		// 如果类型是BigDecimal
		if (genericType.toString().equals(
				"class java.math.BigDecimal")) {
	        	if("".equals(value)){
	        		value="0";
	        	}
	            return BigDecimal.valueOf(Double.valueOf(value));
		}
		// 如果类型是Integer
		if (genericType.toString().equals(
				"class java.lang.Integer")) {
			return Integer.valueOf(value);
		}
		// 如果类型是Double
		if (genericType.toString().equals(
				"class java.lang.Double")) {
			Double d;
            try {
                d = Double.valueOf(value);
            } catch (NumberFormatException nfe) {
                d = null;
            }
            return d;
		}
		// 如果类型是Date
		if (genericType.toString().equals(
				"class java.util.Date")) {
			 return value;
		}
		// 如果还需要其他的类型请自己做扩展

		return "";
	}
	private static final boolean checkFeildType1(Field field){
		if (field.getGenericType().toString().equals("class java.lang.Short")
				|| field.getGenericType().toString()
						.equals("class java.util.Date")
				|| field.getGenericType().toString().equals("boolean")
				|| field.getGenericType().toString()
						.equals("class java.lang.Boolean")
				|| field.getGenericType().toString()
						.equals("class java.lang.Double")
				|| field.getGenericType().toString()
						.equals("class java.lang.Integer")
				|| field.getGenericType().toString()
						.equals("class java.lang.String")) {
			return true;
		}
		return false;
		
	}
	/**
	 * 将vo里的属性值写到model对应属性里
	 * @param args
	 * @throws Exception
	 */
	public static final Object getConversionObjects(Object model,Object vo)throws Exception{
		try{
			Class<? extends Object> mc = model.getClass();
			Class<? extends Object> vc = vo.getClass();
			//利用反射，给JavaBean的属性进行赋值
			Field[] mfields = mc.getDeclaredFields();
			Field[] vfields = vc.getDeclaredFields();
			Field.setAccessible(vfields,true); 
			Field.setAccessible(mfields,true); 
			for (Field vf : vfields) {
				//获取属性名
				String vfieldName = vf.getName();
				Object fieldValue = vf.get(vo);
				for (Field mf : mfields) {
					if(checkFeildType1(mf)){
						String mfn = mf.getName();
						if(vfieldName.equals(mfn)){
							Method setMethod = getSetMethod(mc, mfn,mf.getType());
							if (fieldValue!=null) {
								setMethod.invoke(model, fieldValue);
							}
							break;
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info(e.toString());
		}
		
		return model;
	}
	/**
	 * 匹配非数字英文和汉字的字符串
	 * @param value
	 * @return
	 */
	private static final boolean checkecCellValue(String value){
		String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match=pattern.matcher(value);
		boolean b=match.matches();
		return b;
	}
	/**
	 * 剔除“—|-|_”
	 * @param value
	 * @return
	 */
	private static final String replaceStr(String value){
		String regex="^[—|_|-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match=pattern.matcher(value);
		String replaceAll = match.replaceAll("");
		return replaceAll;
	}
	public static final void dowloadExcelTemp(HttpServletResponse response,String path,String fileName) throws IOException{
		response.setContentType("application/vnd.ms-excel");
		//获取文件标题，转换为gb2312格式
		response.setHeader(
				"Content-disposition",
				"attachment;filename=\""
						+ new String((Tools.isEmpty(fileName)?(DateUtil.getDay()):fileName).getBytes("gb2312"),
								"iso8859-1") + "\"");
		
			InputStream is = new FileInputStream(path);
			byte b[] = new byte[1024];
			int len = 0;
			ServletOutputStream out = response.getOutputStream();
			while((len=is.read(b))!=-1){
				out.write(b, 0, len);
			}
			out.flush();
			is.close();
			out.close();
	}
	public static void main(String[] args) throws Exception {
		File f = new File("C:/Users/Administrator/Desktop/新建文本文档.txt");
		if (f.exists()) {
			System.out.println("存在");
		}else{
			System.out.println("不存在");
		}
		//System.out.println(setMethodName("kmssTestMethod"));
		/*long start=System.currentTimeMillis();   
		SysOrgPerson p = new SysOrgPerson();
		p.setHRDepID("测试反射bean转换");
		SysOrgElement e = new SysOrgElement();
		SysOrgElement ele = (SysOrgElement) getConversionObjects(e, p);
		System.out.println(ele.getHRDepID());
		long end = System.currentTimeMillis();
		System.out.println("程序运行时间： "+(end-start)+"ms");*/
		
		String content = "";
		System.out.println(replaceStr(content));
		String regex="^[—|_|-]+$";
		 Pattern pattern = Pattern.compile(regex);
		 Matcher match=pattern.matcher(content);
		 boolean b=match.matches();
		 if(b)
		 {
			 System.out.println("这是数字英文汉字的字符串");
		 }
		 else
		 {
			 System.out.println("这不是数字英文汉字的字符串"); 
		 }
	}
}
