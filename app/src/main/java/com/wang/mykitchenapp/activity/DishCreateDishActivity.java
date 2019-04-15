package com.wang.mykitchenapp.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.adpter.DishLocalStepsAdapter;
import com.wang.mykitchenapp.bean.Dish;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DishCreateDishActivity extends AppCompatActivity {
    private final String TAG = "DishCreateDishActivity";

    private Context context = this;

    //控件
    //全局
    private Toolbar mToolbar;
    private ScrollView mSvMain;
    //overview
    private EditText mEtTitle;
    private EditText mEtDescription;
    private ImageView mIvAlbum;
    private LinearLayout mLlOverview;
    private Button mBtnOverviewNext;
    //ingredient
    private RelativeLayout mRlIngredient;
    private Button mBtnIngredientPre;
    private EditText mEtIngredientName;
    private EditText mEtIngredientMeasure;
    private TextView mTvIngredient;
    private ImageView mIvIngredientAdd;
    private Button mBtnIngredientNext;
    //steps
    private RelativeLayout mRlSteps;
    private EditText mEtStepDescription;
    private ImageView mIvStep;
    private ImageView mIvStepAdd;
    private Button mBtnPublish;

    //变量
    private String mIngredient = "";
    private Button mBtnStepsPre ;
    private RecyclerView mRecyclerView;
    private DishLocalStepsAdapter mDishStepsAdapter;
    private String mStepAttachmentUrl = "";
    private List<Dish.StepsBean> mDishSteps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_create_dish);

        initView();
    }

    private void initView(){
        initComponents();
    }

    private void initComponents(){

        //全局
        //初始化toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mSvMain = (ScrollView) findViewById(R.id.sv_main);

        //overView
        mEtTitle = (EditText) findViewById(R.id.et_dish_title);
        mEtDescription = (EditText) findViewById(R.id.et_dish_description);
        mIvAlbum = (ImageView) findViewById(R.id.iv_dish_album);
        mIvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        mBtnOverviewNext = (Button) findViewById(R.id.btn_overview_next);
        mBtnOverviewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollO2I();
            }
        });
        mLlOverview = (LinearLayout) findViewById(R.id.ll_dish_overview);
        //ingredient
        mRlIngredient = (RelativeLayout) findViewById(R.id.rl_ingredient);
        mBtnIngredientPre = (Button) findViewById(R.id.btn_ingredient_pre);
        mBtnIngredientPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollI2O();
            }
        });
        mBtnIngredientNext = (Button) findViewById(R.id.btn_ingredient_next);
        mBtnIngredientNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollI2S();
            }
        });
        mEtIngredientName = (EditText) findViewById(R.id.et_dish_ingredient_name);
        mEtIngredientMeasure = (EditText) findViewById(R.id.et_dish_ingredient_measure);
        mTvIngredient = (TextView) findViewById(R.id.tv_dish_detail_ingredient);
        mIvIngredientAdd = (ImageView) findViewById(R.id.iv_ingredient_add);
        mIvIngredientAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredient();
            }
        });

        //steps
        mRlSteps = (RelativeLayout) findViewById(R.id.rl_steps);
        mBtnStepsPre = (Button) findViewById(R.id.btn_steps_pre);
        mBtnStepsPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollS2I();
            }
        });
        initRecyclerView();
        mIvStep = (ImageView) findViewById(R.id.iv_dish_step);
        mIvStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
        mEtStepDescription = (EditText) findViewById(R.id.et_step_description);
        mIvStepAdd = (ImageView) findViewById(R.id.iv_step_add);
        mIvStepAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStep();
            }
        });
        mBtnPublish = (Button) findViewById(R.id.btn_dish_publish);
        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishCreateDishActivity.this,DishDetailActivity.class);
                intent.putExtra("dishId", 37);
                startActivity(intent);
            }
        });
    }

    private void scrollO2I(){
        mRlIngredient.setVisibility(View.VISIBLE);
        mSvMain.pageScroll(View.FOCUS_DOWN);
        mLlOverview.setVisibility(View.GONE);
    }
    private void scrollI2O(){
        mLlOverview.setVisibility(View.VISIBLE);
        mSvMain.pageScroll(View.FOCUS_UP);
        mRlIngredient.setVisibility(View.GONE);
    }
    private void scrollI2S(){
        mRlSteps.setVisibility(View.VISIBLE);
        mSvMain.pageScroll(View.FOCUS_DOWN);
        mRlIngredient.setVisibility(View.GONE);
    }
    private void scrollS2I(){
        mRlIngredient.setVisibility(View.VISIBLE);
        mSvMain.pageScroll(View.FOCUS_UP);
        mRlSteps.setVisibility(View.GONE);
    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        mDishStepsAdapter =  new DishLocalStepsAdapter(context,mDishSteps);
        mRecyclerView = (RecyclerView) findViewById(R.id.recView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRecyclerView.setAdapter(mDishStepsAdapter);
    }

    private void addStep(){
        String stepDescription = mEtStepDescription.getText().toString();
        Dish.StepsBean step = new Dish.StepsBean();
        step.setDistAttachmenturl(mStepAttachmentUrl);
        step.setDistDescription(stepDescription);
        mDishSteps.add(step);
        mDishStepsAdapter.addToList(step);
        mRecyclerView.scrollToPosition(mDishStepsAdapter.getItemCount() - 1);
        mIvStep.setImageResource(R.drawable.ic_add_white_24dp);
        mEtStepDescription.setText("");
    }
    /**
     * 添加一份原料
     */
    private void addIngredient(){
        String ingredient = mEtIngredientName.getText().toString();
        String measure = mEtIngredientMeasure.getText().toString();
        mIngredient += ingredient;
        mIngredient += " : ";
        mIngredient += measure;
        mIngredient += "\n";
        mTvIngredient.setText(mIngredient);
        mEtIngredientName.requestFocus();
        mEtIngredientName.setText("");
        mEtIngredientMeasure.setText("");
    }

    private String getRealPathFromURI(Uri contentUri) { //传入图片uri地址
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String picturePath = selectedImage.getPath();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(selectedImage));
                if(requestCode == 1) {
                /* 将Bitmap设定到ImageView */
                    mIvAlbum.setImageBitmap(bitmap);
                }else if(requestCode == 2){
                    mIvStep.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
            String path = selectedImage.toString();
            mStepAttachmentUrl = path;
            Log.d(TAG, "onActivityResult: " + "**************" + path);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
