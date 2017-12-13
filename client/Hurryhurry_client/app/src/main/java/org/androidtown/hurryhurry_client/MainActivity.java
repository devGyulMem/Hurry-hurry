package org.androidtown.hurryhurry_client;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidtown.hurryhurry_client.dialog.ModifyOrderDialog;
import org.androidtown.hurryhurry_client.order_service.fragment.OrderFragment;
import org.androidtown.hurryhurry_client.order_service.fragment.RealTimeInfoFragment;
import org.androidtown.hurryhurry_client.utils.DateHelper;
import org.androidtown.hurryhurry_client.utils.HttpPostSend;
import org.androidtown.hurryhurry_client.utils.Util;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    protected static Dialog mProgressDialog;

    public static Context mContext;

/* 사용자에게 주문 현황에 대해 알려주는 아이콘들의 모임!*/
    //색칠되지 않은 조리 과정 아이콘 : 도우 만들기, 토핑 올리기, 오븐에서 굽기
    ImageView makingDough_iv;
    ImageView topping_iv;
    ImageView bakingPizza_iv;

    //색칠된 조리 과정 아이콘(현재 상황이라는 얘기임!) : 도우 만들기, 토핑 올리기, 오븐에서 굽기
    ImageView makingDough_ivColored;
    ImageView topping_ivColored;
    ImageView bakingPizza_ivColored;

    /* 사용자에게 주문 정보에 대해 알려주는 textView들의 모임!*/
    TextView memberId_tv;
    TextView arrivalTime_tv;
    TextView approvalTime_tv;
    TextView foodName_tv;

    // 남은 시간에 대해 알려주는 TextVIew;
    TextView leftTime_tv;

    //주문을 변경할 수 있는 버튼
    Button changeMenu_button;

    String arrivalTime;

    JSONTask jsonTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrivalTime = DateHelper.getCurrentDateTime();
        init();
        jsonTask.execute(setRegDataParam());
        changeMenu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyOrderDialog dialog = new ModifyOrderDialog(MainActivity.this);
                dialog.setCancelable(false);
                dialog.show();
            }
        });
   }


   private void init(){

       jsonTask = new JSONTask();
       mContext = this.getApplicationContext();

       //색칠되지 않은 조리 과정 아이콘 : 도우 만들기, 토핑 올리기, 오븐에서 굽기
       makingDough_iv = (ImageView) findViewById(R.id.making_dough);
       topping_iv = (ImageView) findViewById(R.id.topping);
       bakingPizza_iv = (ImageView) findViewById(R.id.baking_pizza);

       //색칠된 조리 과정 아이콘(현재 상황이라는 얘기임!) : 도우 만들기, 토핑 올리기, 오븐에서 굽기
       makingDough_ivColored = (ImageView) findViewById(R.id.making_dough_colored);
       topping_ivColored = (ImageView) findViewById(R.id.topping_colored);
       bakingPizza_ivColored = (ImageView) findViewById(R.id.baking_pizza_colored);

       // 사용자에게 주문 정보를 알려주는 textView
       memberId_tv = (TextView)findViewById(R.id.member_id_textView);
       arrivalTime_tv = (TextView)findViewById(R.id.arrival_time_textView);
       approvalTime_tv = (TextView)findViewById(R.id.approval_time_textView);
       foodName_tv = (TextView)findViewById(R.id.foodName_textView);

       //남은 시간에 대해 알려주는 textView
       leftTime_tv = (TextView)findViewById(R.id.left_time);

       //주문 정보를 변경하는 Button
       changeMenu_button = (Button)findViewById(R.id.changeMenu);

       mProgressDialog = Util.showProgressDialog(MainActivity.this);
   }

    //JSON 오브젝트를 string으로 바꿔준다.
    private String setRegDataParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("MEMBER_ID", "jaena96");
            jsonObject.accumulate("FOOD_NAME", "불고기 피자");
            jsonObject.accumulate("RFID_ID", "22135019");
            jsonObject.accumulate("ARRIVAL_TIME", arrivalTime );
            jsonObject.accumulate("APPROVAL_TIME", DateHelper.getCurrentDateTime());
            jsonObject.accumulate("PROCESS_1", "not yet");
            jsonObject.accumulate("PROCESS_2", "not yet");
            jsonObject.accumulate("PROCESS_3", "not yet");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //JSON을 이용한 HTTP 통신
    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            result = HttpPostSend.exeRegOrder(setRegDataParam());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
