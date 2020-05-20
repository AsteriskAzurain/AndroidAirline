package com.ishang.asterisk.application05191;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFlightActivity extends AppCompatActivity {

    private Spinner spin_from;
    private Spinner spin_to;
    private Spinner spin_cabintype;
    private List<CharSequence> fromlist;
    private List<SpinItem> spinlist;
    //private ArrayAdapter<CharSequence> adapter=null;
    private ArrayAdapter<SpinItem> adapter=null;
    private List<CharSequence> IATAlist= null;

    Button btnsearch= null;
    Button btnback = null;

    String depIATA,arrIATA,cabintype,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        bindSpinner();

        /*Intent intent=getIntent();
        final int videopos=intent.getIntExtra("videopos",0);
        System.out.println(videopos);*/

        btnsearch=(Button)findViewById(R.id.btnsearch);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date==null || date.isEmpty()) {
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMdd");
                    date = formatter.format(currentTime);
                }

                String reqstr="/api/flight/list?From="+depIATA+"&To="+arrIATA+"&CabinType="+cabintype+"&Date="+date+" &isAsc=1";
                Intent page = new Intent(SearchFlightActivity.this, SearchResultActivity.class);
                //page.putExtra("newpos",videopos);
                page.putExtra("reqstr",reqstr);
                startActivity(page);
                //Toast.makeText(SearchFlightActivity.this, "dep:"+depIATA+"; arr:"+arrIATA+"; date:"+date, Toast.LENGTH_SHORT).show();

            }
        });

        btnback=(Button)findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent page = new Intent(SearchFlightActivity.this, MainActivity.class);
                //page.putExtra("newpos",videopos);
                startActivity(page);
            }
        });
    }

    private void bindSpinner(){
        spin_from=(Spinner) findViewById(R.id.spinfrom);
        spin_to=(Spinner) findViewById(R.id.spinto);
        spin_cabintype=(Spinner) findViewById(R.id.spincabin);

        //String[] cabintype = getResources().getStringArray(R.array.cabintype);
        fromlist= new ArrayList<CharSequence>();
        IATAlist= new ArrayList<CharSequence>();
        //  从数据库获取from list
        //fromlist.add("测试数据1");
        //fromlist.add("测试数据2");

        spinlist = new ArrayList<SpinItem>();

        new Thread(){
            public void run(){

                try{
                    System.out.println("线程Thread");
                    String path="http://10.0.2.2:5000/api/airport/list";
                    URL url= new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int responseCode=conn.getResponseCode();
                    InputStream is= conn.getInputStream(); // TODO 这是啥
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int len=-1;
                    byte[] buffer= new byte[1024];
                    while((len=is.read(buffer))!=-1){
                        baos.write(buffer,0,len); //params: byte[], int off, int len
                    }
                    is.close();
                    String content=new String(baos.toByteArray());
                    Looper.prepare();
                    if(responseCode==200){
                        try{
                            //解析json
                            System.out.println(content);
                            /*
                            * I/System.out: [
                            * {"IATA":"PEK","Name":"Beijing Capital International Airport"},
                            * {"IATA":"PVG","Name":"Shanghai Pudong International Airport"},
                            * {"IATA":"HKG","Name":"Hong Kong International Airport"},
                            * {"IATA":"MFM","Name":"Aeroporto Internacional de Macau"},
                            * {"IATA":"LAX","Name":"Los Angeles International Airport"}
                            * ]
                            * */
                            JSONArray array= new JSONArray(content);
                            for(int i=0; i<array.length();i++){
                                JSONObject obj = array.getJSONObject(i);
                                String name = obj.getString("Name");
                                String IATA = obj.getString("IATA");
                                fromlist.add(name);
                                IATAlist.add(IATA);
                                SpinItem it= new SpinItem(IATA,name);
                                spinlist.add(it);

                            }
                        }catch (Exception e){
                            System.out.println("报错了2");
                            System.out.println(e.toString());
                        }

                        adapter = new ArrayAdapter<SpinItem>(getApplicationContext(), android.R.layout.simple_spinner_item, spinlist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_from.setAdapter(adapter);
                        spin_to.setAdapter(adapter);
                    }
                    Looper.loop();
                }catch (Exception e) {
                    System.out.println("报错了1");
                    System.out.println(e.toString());
                }
            }
        }.start();

        //adapter = new ArrayAdapter<Spinner>(this,android.R.layout.simple_spinner_item,fromlist);
        /*
        * 原来setAdapter的位置在这
        * 这个问题好像是异步加载数据造成的。
        * 当调用spinner.setAdapter()时候，那个adapter中还没有数据，自定义adapter也是一样问题，
        * 其实哪怕adapter中仅有一条记录，后期异步添加数据都没有问题。但一条数据都没有便会出现该问题，然而点击spinner任然会显示异步加载的数据。
        * 最好的办法是，是异步数据加载完成后在调用spinner.setAdapter()就不会存在该问题
        * 解决方法二：事先添加一条数据进去 此处未采用
        * */
        spin_from.setSelection(0,true);
        spin_to.setSelection(0,true);

        selectedaction();

    }

    private void selectedaction(){
        //当选中某一个数据项时触发该方法
        spin_cabintype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)spin_cabintype.getItemAtPosition(position);//从spinner中获取被选择的数据
                //Toast.makeText(SearchFlightActivity.this, data, Toast.LENGTH_SHORT).show();
                cabintype=data;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spin_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinItem item=(SpinItem)spin_from.getItemAtPosition(position);
                //Toast.makeText(SearchFlightActivity.this, "key:"+item.getKey()+";value:"+item.getValue(), Toast.LENGTH_SHORT).show();
                depIATA=item.getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spin_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinItem item=(SpinItem)spin_to.getItemAtPosition(position);
                arrIATA=item.getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText textdate = (EditText)findViewById(R.id.editTextDate);
        textdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                //text  输入框中改变后的字符串信息
                //start 输入框中改变后的字符串的起始位置
                //before 输入框中改变前的字符串的位置 默认为0
                //count 输入框中改变后的一共输入字符串的数量
                textView1.setText("输入后字符串 [ " + text.toString() + " ] 起始光标 [ " + start + " ] 输入数量 [ " + count+" ]");
                * */
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                 * params:
                 * //text 输入框中改变后的字符串信息
                 * //start 输入框中改变后的字符串的起始位置
                 * //before 输入框中改变前的字符串的位置 默认为0
                 * //count 输入框中改变后的一共输入字符串的数量
                 */
                //Toast.makeText(SearchFlightActivity.this, "输入后字符串 [ " + charSequence.toString() + " ] 起始光标 [ " + i + " ] 输入数量 [ " + i2+" ]", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*
                * params:
                *  //edit  输入结束呈现在输入框中的信息
                * */
                //Toast.makeText(SearchFlightActivity.this, "输入结束后的内容为 [" + editable.toString()+" ] 即将显示在屏幕上", Toast.LENGTH_SHORT).show();
                date=editable.toString();
            }
        });
    }
}
