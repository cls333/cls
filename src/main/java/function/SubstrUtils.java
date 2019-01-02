package function;

public class SubstrUtils {
    //获取自定义类型的字符串
    public static void isNumeric() {
        String str="d(1) d(2)啊 111";

        //1.判断字符串中是否全为英文
        //boolean result = str.matches("[a-zA-Z]+");//true:全文英文
        //str.matches("[a-zA-Z0-9]+")//判断英文和数字

        //2.提取字符串中所有的英文
        str = str.replaceAll("[^a-z^A-Z^0-9^(^)^.]", "");
        System.out.println(str);

        //3.判断字符串中是否含英文
        //String regex = ".*[a-zA-z].*";
        //str.matches(regex);//true：含有英文
    }

    /**
     * 获取中文字符串
     */
    public static String isConrZN(){
        StringBuilder zn = new StringBuilder();
        String str = "ddd啊啊啊111";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 0x0391 && str.charAt(i) <= 0xFFE5)
                //System.out.print(str.charAt(i));
                zn.append(str.charAt(i));
        }
        return zn.toString();
    }

    //获取英文
    public static String isCnorEn() {
        StringBuilder en = new StringBuilder();
        String str = "aaa收拾收拾.（111）";
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) >= 0x0000 && str.charAt(i) <= 0x00FF)
                //System.out.print(str.charAt(i));
                en.append(str.charAt(i));
        }
        return en.toString();
    }
}
