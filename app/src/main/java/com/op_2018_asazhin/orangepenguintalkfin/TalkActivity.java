package com.op_2018_asazhin.orangepenguintalkfin;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class TalkActivity extends Activity {
    ChatFragment _ChatFragment;
    IconsFragment _ListFragment;
    ChatIconHandler _handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_talk);

        //put in the Fragments at start up
        _ChatFragment = new ChatFragment();
        _ListFragment = new IconsFragment();
        _handler = new ChatIconHandler(_ChatFragment);


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.chatbox_frame, _ChatFragment);
        ft.addToBackStack(null);
        ft.commit();


        FragmentManager fm2 = getFragmentManager();
        FragmentTransaction ft2 = fm2.beginTransaction();
        ft2.replace(R.id.icons_list_frame, _ListFragment);
        ft2.addToBackStack(null);
        ft2.commit();


    }

    public ChatIconHandler getHandler() {
        return _handler;
    }

    //inflate the action bar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //dealing with clicks
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_manage:
                //start new activity for managing?

                /*
                code to be implemented when adding new icons
                by the user is to be added
                 */
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
