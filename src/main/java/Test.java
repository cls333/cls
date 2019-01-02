
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static function.CreateTable.readWord;
import static function.SubstrUtils.isNumeric;

public class Test {
    public static void main(String[] args) {
        try {
            // 创建File对象
            File file = new File("/Users/caolisha/Desktop/关于12月06日数据交换平台升级的公告 .txt");
            // 创建文件字符输出流
            FileWriter fw = new FileWriter(file);
            // 定义内容字符串
            String strContent = "床前明月光，疑是地上霜。举头望明月，低头思故乡。";
            // 将字符串写入文件
            fw.write(strContent);
            // 关闭文件字符输出流
            fw.close();

            // 读取文件部分内容（从第7个字符开始取5个字符）
            String strPart = read_part_of_file(file, 7, 5);
            // 输出截取的文件内容
            System.out.println(strPart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件部分内容
     * @param file  文件
     * @param offset 起始位置
     * @param length 长度
     * @return
     */
    public static String read_part_of_file(File file, int offset, int length) {
        String content = "";
        try {
            FileReader fr = new FileReader(file);
            fr.skip(offset - 1);  // 输入流跳过若干字符
            char[] buffer = new char[length];
            fr.read(buffer);
            content = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
