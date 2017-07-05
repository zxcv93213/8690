package com.zz.sharebike;


import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_search;
    private Button button_share;
    private EditText et_id;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Bmob.initialize(this,"d86e51440c49198dcb29dc9e6c4e68d6");

            button_search = (Button)findViewById(R.id.button_search);
            button_share = (Button)findViewById(R.id.button_share);

            et_id=(EditText)findViewById(R.id.id);
            et_password=(EditText)findViewById(R.id.password);
            button_search.setOnClickListener(this);
            button_share.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
            showToast("Error:"+e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String id=et_id.getText().toString();
        switch (v.getId()) {
            case R.id.button_search:
                if (id.length()<1){
                    showToast("车牌号不能为空！");
                }else{
                    queryData(Integer.parseInt(id));
                }
                break;

            case R.id.button_share:
                id=et_id.getText().toString();
                String password=et_password.getText().toString();
                if (id.length()<1){
                    showToast("车牌号不能为空！");
                }
                if (password.length()<1){
                    showToast("解锁码不能为空！");
                }
                if((id.length()>1)&&(password.length()>1)){
                    createData(Integer.parseInt(id),Integer.parseInt(password));
                }

                break;
            default:
                break;
        }
    }
    /**
     * 查询数据
     */
    public void queryData(int id){
        BmobQuery query = new BmobQuery("Bikes");
        query.addWhereEqualTo("id",id);

        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("解锁码：");
                builder.setPositiveButton("确定",null);

                if (jsonArray.length()>0){
                    String password=null;
                    try {
                        JSONObject object=jsonArray.getJSONObject(0);
                        password=object.getString("password");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    builder.setMessage(password);

                }else{
                    builder.setMessage("尚无该车牌号记录😞");

                }
                builder.create().show();

            }
        });

    }

    /**
     * 创建数据
     */
    public void createData(int id,int password){
        Bikes bike = new Bikes();
        bike.setId(id);
        bike.setPassword(password);

//        //查看数据是否存在
//        BmobQuery query = new BmobQuery("Bikes");
//        query.addWhereEqualTo("id",id);
//        query.findObjectsByTable(new QueryListener<JSONArray>() {
//            @Override
//            public void done(JSONArray jsonArray, BmobException e) {
//
//                if (jsonArray.length()>0){
//
//
//                }
//
//            }
//        });

        bike.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("共享解锁码：");
                builder.setPositiveButton("确定",null);

                if(e==null){
                    builder.setMessage("共享成功😊");
                }else{
                    builder.setMessage("共享失败😞");
                }
                builder.create().show();
            }

        });
    }

    private void showToast(String msg){
        Toast toast=Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
