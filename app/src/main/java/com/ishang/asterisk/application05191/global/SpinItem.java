package com.ishang.asterisk.application05191.global;

/**
 * Created by Asterisk on 5/20/2020.
 */

public class SpinItem {

    private String key;
    private String value;

    public SpinItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //快速生成方法：Alt+Insert
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
    @Override
    public String toString() {
        /*return "SpinItem{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';*/
        return value;
    }

    /*
    * 然后声明对象实例并加入到arraylist里面,并设置spinner的适配器
    Spinner Sp = (Spinner).............//
    List<CItem > lst = new ArrayList<CItem>();
    CItem  ct = new CItem ("1","测试");
    lst.Add(ct);
    ArrayAdapter<CItem > Adapter = new ArrayAdapter<CItem>(context,android.R.layout.simple_spinner_item, lst);
    Sp.SetAdapter(Adapter);

    取值：
    如果取TEXT值则可以直接取:Sp.getSelectedItem.ToString()或者:((CItem)Sp.getSelectedItem). GetValue() ;
    如果去Value值则可以这样取:((CItem)Sp.getSelectedItem).GetID();
    * */

}
