package function;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateTable {
    public static void main(String[] args) throws Exception{
        String filePath = "/Users/caolisha/Desktop/变更通知文档/20181031.txt";
        String buffer = readWord(filePath);

        if(buffer.contains("2.")){
            String[] split = buffer.split("2.");
            String creatContent = split[0];    //新建表的文档内容
            String alterContent = split[1];    //修改表的文档内容

            //String alterSql = getAlterSql(alterContent);   //修改表结构的sql
            //System.out.println(alterSql);
            System.out.println(creatContent);
            System.out.println(alterContent);
            //新增下发表
            String createSql = getCreateSql(creatContent);
            System.out.println(createSql);
        }else{
            String createSql = getCreateSql(buffer);
            System.out.println(createSql);
        }

    }

    /**
     * 读取word文档的内容
     * @param path
     * @return
     */
    public static String readWord(String path) {
        File file = new File(path);
        String buffer = "";
        try {
            if (path.endsWith(".doc")) {
                InputStream is = new FileInputStream(new File(path));
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                ex.close();
            } else if (path.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                buffer = extractor.getText();
                extractor.close();
            } else if(path.endsWith("txt") && file.isFile() && file.exists()){
                // 读取文件
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "gbk");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    //System.out.println(lineTxt);
                    builder.append(lineTxt).append("\n");
                }
                buffer=builder.toString();
                br.close();
            } else {
                System.out.println("此文件不是word或txt文件！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取excel表信息，存入ExcelEntity对象
     * @return
     * @throws Exception
     */
    public static List<ExcelEntity> getExcelInfo() throws Exception {
        //excel文件路径
        File excel = new File("/Users/caolisha/Desktop/变更通知文档/数据标准化拆分-大总帐-发布版.xls");

        if (excel.isFile() && excel.exists()) {     //判断文件是否存在
            String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
            Workbook wb;
            //根据文件后缀（xls/xlsx）进行判断
            if ("xls".equals(split[1])) {
                FileInputStream fis = new FileInputStream(excel);   //文件流对象
                wb = new HSSFWorkbook(fis);
                //System.out.println("我是xls类型");
            } else if ("xlsx".equals(split[1])) {
                wb = new XSSFWorkbook(excel);
                //System.out.println("我是xlsx类型");
            } else {
                System.out.println("文件类型错误!");
                return null;
            }

            List<ExcelEntity> excelEntitys = new ArrayList<ExcelEntity>();  //存放excel中每一行的对象

            //excel的页签遍历
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                // 每个页签创建一个Sheet对象，开始解析
                Sheet sheet = wb.getSheetAt(sheetIndex);     //读取sheet 0，1，2 ......

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读,从第二行读取
                int lastRowIndex = sheet.getLastRowNum();

                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    ExcelEntity excelEntity = new ExcelEntity();      //每一行建立一个对象
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();   //1
                        int lastCellIndex = row.getLastCellNum();   //n
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {     //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null) {
                                if (cIndex == firstCellIndex){
                                    excelEntity.setId(cell.toString());
                                }
                                if (cIndex == firstCellIndex+1){
                                    excelEntity.setTableName(cell.toString());
                                }
                                if (cIndex == firstCellIndex+3){
                                    excelEntity.setField(cell.toString());
                                }
                                if (cIndex == firstCellIndex+5){
                                    excelEntity.setFieldType(cell.toString());
                                }
                                if (cIndex == firstCellIndex+6){
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    excelEntity.setFieldLength(cell.getRichStringCellValue().getString());
                                    //excelEntity.setFieldLength(cell.toString());
                                }
                                //System.out.println(cell.toString());
                            }
                        }
                        excelEntitys.add(excelEntity);
                    }
                }
            }
            return excelEntitys;

        } else {
            System.out.println("Excel不存在");
            return null;
        }
    }

    /**
     * 新增下发表 ，生成创建表的sql
     * @param creatContent
     * @return
     * @throws Exception
     */
    public static String getCreateSql(String creatContent) throws Exception {
        StringBuilder sql = new StringBuilder();
        //创建表
        String[] content = creatContent.split("\\s+");
        for(String singleCon : content){
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(singleCon);
            if (singleCon.contains(".") && !m.find()) {    // && !singleCon.contains("1.新增下发表")
                //System.out.println(singleCon);
                String[] tableName = singleCon.split("\\.");   // id.name
                List<ExcelEntity> sqlInfo = new ArrayList<ExcelEntity>();  //存放要创建SQL的Excel信息
                List<ExcelEntity> excelInfo = getExcelInfo();     //获取所有excel信息
                if (excelInfo != null && excelInfo.size() != 0) {
                    for (ExcelEntity excelEntity : excelInfo) {
                        if (excelEntity.getId().equals(tableName[0]) && excelEntity.getTableName().equals(tableName[1])) {
                            sqlInfo.add(excelEntity);
                        }
                    }
                    if (sqlInfo != null && sqlInfo.size() != 0) {
                        //StringBuilder sql = new StringBuilder();
                        sql.append("\n"+singleCon+"\n"+"建表语句为：");
                        sql.append("\n" + "create table" + " ");
                        for (int i = 1; i < sqlInfo.size(); i++) {
                            //System.out.println(singleCon);
                            if (i == 1) {
                                sql.append(sqlInfo.get(i).getId());
                                sql.append(".");
                                sql.append(sqlInfo.get(i).getTableName());
                                sql.append("(" + "\n");
                                sql.append(sqlInfo.get(i).getField() + " ");
                                sql.append(sqlInfo.get(i).getFieldType()).append("(" + sqlInfo.get(i).getFieldLength() + ")");
                                sql.append("," + "\n");
                            } else if (i == sqlInfo.size() - 1) {
                                sql.append(sqlInfo.get(i).getField() + " ");
                                sql.append(sqlInfo.get(i).getFieldType()).append("(" + sqlInfo.get(i).getFieldLength() + ")");
                                sql.append("\n" + ")");
                            } else {
                                sql.append(sqlInfo.get(i).getField() + " ");
                                sql.append(sqlInfo.get(i).getFieldType()).append("(" + sqlInfo.get(i).getFieldLength() + ")");
                                sql.append("," + "\n");
                            }
                        }

                    } else {
                        System.out.println("您输入的表："+singleCon+" 在excel表中未找到！");
                    }
                } else {
                    System.out.println("没有获取到excel表信息");
                }



            }
        }

        return sql.toString();
    }

    /**
     * 生成变更表结构的sql
     * @return
     * @throws Exception
     */
    public static String getAlterSql(String alterContent) throws Exception {
        //List<> alterEntityList = new ArrayList<>();   //存放表结构变更的信息
        StringBuilder sql = new StringBuilder();
        String[] content = alterContent.split("\\s+");

        for(int i=0;i<content.length;i++){

            if(content[i].contains(".")){
                sql.append("alter table ");
                sql.append(content[i]+" ");
                sql.append("modify"+" ");
                for (int n = 0; n < content[i+1].length(); n++) {
                    if (content[i+1].charAt(n) >= 0x0000 && content[i+1].charAt(n) <= 0x00FF) {
                        sql.append(content[i+1].charAt(n));
                    }
                }
                sql.append(" "+content[i+4]);
                sql.append(";"+"\n");
            }
        }

        return sql.toString();
    }
}
