package com.wang.mykitchenapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.JsonObject;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.fragment.BBSFragment;
import com.wang.mykitchenapp.fragment.CollectionFragment;
import com.wang.mykitchenapp.fragment.DishFragment;
import com.wang.mykitchenapp.fragment.FocusFragment;
import com.wang.mykitchenapp.fragment.HomeFragment;
import com.wang.mykitchenapp.manager.DataManager;
import com.wang.mykitchenapp.utils.SharedPreferencesUtils;
import com.wang.mykitchenapp.utils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MainActivity";
    //控件
    private Toolbar mToolBar;
    private MaterialSheetFab mMaterialSheetFab; //浮动按钮
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView; //侧滑菜单
    private CircularImageView mUserAvatar;
    private TextView mUserName;
    //Fragment
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private Fragment mHomeFragment,mDishFragment;
    //侧滑菜单item
    private MenuItem mPreMenuItem;
    private MenuItem mNavItemGroupUser;
    //当前用户
    private UserInfoBean mUser;
    //
    Intent intent ;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;
    /**
     * activity初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化界面控件
        initView();

        //初始化主页面fragment
        initDefaultFragment();
    }

    /**
     * 初始化界面控件
     */
    private void initView(){
        //初始化FragmentManager
        this.mFragmentManager = getSupportFragmentManager();

        //初始化toolbar
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        //初始化浮动按钮
        //initFab();

        //初始化侧滑栏
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        //初始化侧滑菜单
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        //初始化用户头像和昵称控件
        mUserAvatar = (CircularImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_nav_header_main_user_avatar);
        mUserName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_nav_header_main_user_name);
        mUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.isLoged){
                    intent = new Intent(MainActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.isLoged){
                    intent = new Intent(MainActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        //初始化侧滑菜单控件
        mNavItemGroupUser = mNavigationView.getMenu().findItem(R.id.nav_item_group_user);
        if(true == Constant.isLoged){
            mUser = SharedPreferencesUtils.getFromSharePreferences(this,"userInfo",UserInfoBean.class,"user_data");
            //NavigationView 中的控件，不能直接通过findViewById 方法找到。
            final Context context = this;
            loadCircleImage(context,mUser.getUserAvatar(),mUserAvatar);
            mUserName.setText(mUser.getUserNickName());
        }else {
            mNavItemGroupUser.setVisible(false);
        }
    }


    /**
     * 重写返回按钮功能
     */
    private long lastBackKeyDownTick = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > Constant.MAX_DOUBLE_BACK_DURATION) {
            Snackbar.make(mNavigationView, Constant.DOUBLE_CLICK_TO_LEAVE, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }
  /**
     * 初始化tool bar 菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 绑定tool bar 菜单选项点击操作
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_visible ){
            mNavItemGroupUser.setVisible(true);
        }
        if (id == R.id.action_un_visible ){
            mNavItemGroupUser.setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 绑定侧滑菜单选项点击操作
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.nav_item_home:
                mToolBar.setTitle("首页");
                switchFragment(HomeFragment.class);
                break;
            case R.id.nav_item_dish:
                mToolBar.setTitle("料理教室");
                switchFragment(DishFragment.class);
                break;
            case R.id.nav_item_blog:
                mToolBar.setTitle("美食部落格");
                switchFragment(BBSFragment.class);
                break;
            case R.id.nav_item_collect:
                mToolBar.setTitle("我的收藏");
                switchFragment(CollectionFragment.class);
                break;
            case R.id.nav_item_focus:
                mToolBar.setTitle("我的关注");
                switchFragment(FocusFragment.class);
                break;
            case R.id.nav_item_account:
                //跳转到当前用户的个人信息
                intent = new Intent(MainActivity.this,UserInfoActivity.class);
                intent.putExtra("userName",Constant.currentUserName);
                startActivity(intent);
                break;
            case R.id.nav_item_logout://登出
                logout();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 初始化浮动按钮
     */
/*    public void initFab(){
        Log.i(TAG, "initFab");
        Fab fab = (Fab)findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.colorWhite);
        int fabColor = getResources().getColor(R.color.colorWhite);
        mMaterialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay,
                sheetColor, fabColor);
    }*/

    /**
     * 初始化fragment
     */
    private void initDefaultFragment() {
        Log.i(TAG, "initDefaultFragment");
        mHomeFragment = ViewUtils.createFragment(HomeFragment.class);
        mDishFragment = ViewUtils.createFragment(DishFragment.class);
        mCurrentFragment = mHomeFragment;
        mFragmentManager.beginTransaction().add(R.id.frame_content, mCurrentFragment).commit();
        mPreMenuItem = mNavigationView.getMenu().getItem(0);
        mPreMenuItem.setChecked(true);
        mToolBar.setTitle("主页");
    }


    /**
     * 切换fragment
     * @param clazz
     */
    private void switchFragment(Class<?> clazz) {
        Fragment to = ViewUtils.createFragment(clazz);
        if (to.isAdded()) {
            Log.i(TAG, "Added");
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(to).commitAllowingStateLoss();
        } else {
            Log.i(TAG, "Not Added");
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.frame_content, to).commitAllowingStateLoss();
        }
        mCurrentFragment = to;
    }


    /**
     * 登出当前用户
     */
    public void logout(){
        Log.v(TAG,"logout");
        String url = Constant.BASE_URL + "user/logout.do";
        Map<String,String> map = new HashMap<>();
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                    if(Constant.LOGOUT_SUCCEED == Integer.parseInt(mReturnedJsonObject.get("excuteResult").toString())){
                                        SharedPreferencesUtils.removeFromSharePreferences(MainActivity.this,"userInfo","user_data");
                                        Constant.currentUserName = "";
                                        reloadActivity();
                                    }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   Toast.makeText(getApplicationContext(),"登出失败",
                                           Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                    mReturnedJsonObject = jsonObject;
                               }
                           }));
                        Constant.isLoged = false;
    }

    private void reloadActivity(){
        switchFragment(HomeFragment.class);
        mToolBar.setTitle("主页");
        mUserAvatar.setImageResource(R.drawable.as);
        mUserName.setText("登录");
        mNavItemGroupUser.setVisible(false);


        Snackbar.make(mNavigationView, Constant.LOG_OUT_OK, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"Resume MainActivity");
    }
}
