package com.mai.util;

public class MyUtil {
    public static String getAddressRegion(int province, int city, int area) {
        //省份数组
        String[] provinceArr = {"请选择省", "福建省", "江苏省", "河北省"};
        //城市数组
        String[][] cityArr = {
                {"请选择市"},
                {"福州市", "泉州市", "三明市"},
                {"苏州市", "南京市", "扬州市"},
                {"石家庄", "秦皇岛", "张家口"}
        };
        //区域数组
        String[][][] countryArr = {
                {
                        {"请选择区", "鼓楼区", "仓山区", "台江区", "长乐区"},

                },
                {
                        {"鼓楼区", "仓山区", "台江区", "长乐区"},
                        {"丰泽区", "洛江区", "鲤城区", "泉港区"},
                        {"三元区", "梅列区",}
                },
                {
                        {"虎丘区", "吴中区", "相城区", "姑苏区", "吴江区"},
                        {"玄武区", "秦淮区", "建邺区", "鼓楼区", "浦口区"},
                        {"邗江区", "广陵区", "江都区"}
                },
                {
                        {"长安区", "桥西区", "新华区", "井陉矿区"},
                        {"海港区", "山海关区", "北戴河区", "抚宁区"},
                        {"桥东区", "桥西区", "宣化区", "下花园区"}
                }
        };
        return (province == 0 ? "未知-" : provinceArr[province])
                + (city == 0 ? "未知" : "-" + cityArr[province][city])
                + (area == 0 ? "未知" : "-" + countryArr[province][city][area]);
    }
}
