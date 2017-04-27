package com.example.jzg.myapplication.slidingmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jzg.myapplication.R;

/**
 * Created by libo on 2017/1/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: DrawerLayout 系统侧滑菜单示例
 */
public class DrawerLayoutActivity extends AppCompatActivity {

    //声明相关变量
      private DrawerLayout mDrawerLayout;
      private DrawerListener mDrawerToggle;
       private ListView lvLeftMenu;
       private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
       private ArrayAdapter arrayAdapter;
      private ImageView ivRunningMan;
      private Button bt_slidingmenu;
      private boolean isDrawerLayoutOpen;  //当前状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawerlayout_slidingmenu);

                findViews(); //获取控件

                bt_slidingmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        mdrawerLayout.openDrawer(Gravity.LEFT);//这里设置的方向应该跟xml文件里面的gravity方向相同，不然会报错,start和LEFT都为从左边出现
//
//                        mdrawerLayout.closDrawers();//没有参数,关闭所有的出现的抽屉

                        if (!isDrawerLayoutOpen)
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                        else
                        mDrawerLayout.closeDrawers();
                    }
                });

                  mDrawerToggle = new DrawerListener() {
                      @Override
                      public void onDrawerSlide(View drawerView, float slideOffset) {
//                          Toast.makeText(DrawerLayoutActivity.this,"onDrawerSlide",Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onDrawerOpened(View drawerView) {
                          isDrawerLayoutOpen = true;
                          Toast.makeText(DrawerLayoutActivity.this,"open",Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onDrawerClosed(View drawerView) {
                          isDrawerLayoutOpen=false;
                          Toast.makeText(DrawerLayoutActivity.this,"close",Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onDrawerStateChanged(int newState) {
//                          Toast.makeText(DrawerLayoutActivity.this,"onDrawerStateChanged",Toast.LENGTH_SHORT).show();
                      }
                  };

//                mDrawerToggle.syncState();

                mDrawerLayout.setDrawerListener(mDrawerToggle);
                //设置菜单列表
                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
                lvLeftMenu.setAdapter(arrayAdapter);
            }
        private void findViews() {
                bt_slidingmenu = (Button) findViewById(R.id.bt_slidingmenu);
                ivRunningMan = (ImageView) findViewById(R.id.iv_main);
                mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
                lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
            }

}
