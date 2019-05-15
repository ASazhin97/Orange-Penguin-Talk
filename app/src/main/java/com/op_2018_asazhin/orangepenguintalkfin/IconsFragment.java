package com.op_2018_asazhin.orangepenguintalkfin;


import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class IconsFragment extends Fragment implements View.OnClickListener{
    LinearLayout _layout;
    View _view;
    ArrayList<Button> _buttonList;
    ArrayList<TextView> _nameList;
    ArrayList<Integer> _typeList;
    String _state;
    int numOfButtons;
    int buttonParam;
    ChatIconHandler _handler;

    int screenWidth;

    public IconsFragment() {
        //requried empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_icons, container, false);
        TalkActivity thisActivity = (TalkActivity) getActivity();
        _handler = thisActivity.getHandler();


        //get Screen Height to use for scaling
        screenWidth = getScreenWidth();
        buttonParam = (screenWidth/4); //can be changed later in order to get more buttons on screen

        //this gets the arguments
        Bundle arguments = this.getArguments();

        //if its empty sets it to default which will show the first categories.
        //if it isnt itll get the new state
        if(arguments != null && arguments.containsKey("STATE")) {

            _state = arguments.getString("STATE");
        } else {
            _state = "default";
        }

        //create button Lists
        _buttonList = new ArrayList<Button>();
        _nameList = new ArrayList<TextView>();
        _typeList = new ArrayList<Integer>();
        this.createButtonList();


        //get table layout
        _layout = (TableLayout) _view.findViewById(R.id.table_list_layout);

        //set Parameters for table for the icons
        numOfButtons = _buttonList.size(); //how many buttons there are
        Log.e("button number", "is " + numOfButtons);
        int perRow = 4; //how many buttons per row
        TableRow row = new TableRow(getActivity()); //creates the first row
        int rowCurrentSize = 0; //the number of buttons currently in the first row

        //CREATING BUTTONS//
        //for the amoutnt of buttons AKA Icons create that many buttons
        for(int i = 0; i < numOfButtons; i++){
            LinearLayout linLayout = new LinearLayout(getActivity());
            linLayout.setOrientation(LinearLayout.VERTICAL);

            linLayout.addView(_buttonList.get(i));
            linLayout.addView(_nameList.get(i));

            row.addView(linLayout);
            rowCurrentSize++;

            //if row maxed out start next row
            if(rowCurrentSize == perRow){
                _layout.addView(row);
                row = new TableRow(getActivity());
                rowCurrentSize = 0;

            }

            //if buttons finished but row not finished add the last row
            if((i == numOfButtons-1) && ((numOfButtons%perRow) != 0)){
                _layout.addView(row);
            }


        }

        return _view;
    }

    //sets the handler so that this fragment can interact with the chat fragmnet
    public void setHandler(ChatIconHandler handler){
        _handler = handler;
    }

    //gets the width of the screen to set the buttons may need to be changed for a static button size
    public int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    //function that deals with the button press
    @Override
    public void onClick(View v) {
        Log.e("clicked", "button");
        Button btnClicked = null;
        TextView txtClicked = null;
        int type = -1;

        //picks out the right button and text
        for(int i = 0; i < _buttonList.size(); i++){
            if(_buttonList.get(i) == v){
                btnClicked = _buttonList.get(i);
                txtClicked = _nameList.get(i);
                type = _typeList.get(i);
                Log.e("button clicked ", "is " + i);
            }

        }


        Log.e("type", "is" + type);

        //type 0 means it is a category so icons/categories within need to be displayed
        if(type == 0){
            //go to that category
            String nextCategory = txtClicked.getText().toString();

            Fragment replacement = new IconsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("STATE", nextCategory);
            replacement.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.icons_list_frame, replacement);
            ft.addToBackStack(null);
            ft.commit();

        }

        //type one means it is simply an icon and needs to be pushed up to the chat box
        if(type == 1){
            //this will go through the handler to add them to the chat box
            _handler.addToLists(btnClicked, txtClicked);

        }






    }


    /*
    This scales the pictures given according to the creen width
    this will likely require tweaking in the future as I had quite a bit of trouble of chaning
    the picture size. I did this incremental approach for now but using a ration would be best.
     */
    public Drawable scaleDrawable(Drawable drawable){
        float scale  = .70f;
        System.out.println("button width " + buttonParam);
        if (buttonParam <= 300){
            scale = .7f;
            System.out.println(" SCALE SCALE SCALE " + scale);
        } else if (buttonParam > 300 && buttonParam <= 350) {
            scale = .6f;
            System.out.println(" SCALE SCALE SCALE " + scale);
        }else if (buttonParam > 350 && buttonParam <= 400){
            scale = .4f;
        } else if(buttonParam > 400){
            scale = .2f;
            System.out.println(" SCALE SCALE SCALE " + scale);
        }

        ScaleDrawable sd = new ScaleDrawable(drawable, Gravity.TOP, scale, scale);

        int level = 800;
        sd.setLevel(level);

        Log.e("height before", "height " + sd.getIntrinsicHeight());
//        if(sd.getHeight() < buttonParam){
//            level = level - 20;
//            sd.setLevel(level);
//        }


        Log.e("height", "height " + sd.getIntrinsicHeight());
        Log.e("level", "scale " + level);
        return sd;
    }

    public void createButtonList(){
        //if first categories screen
            try {
                //get which category you need to show

                SQLHelperCategories categoriesHelper = new SQLHelperCategories(getActivity());
                SQLiteDatabase database = categoriesHelper.getReadableDatabase();
                int categoryID = 1;

                Cursor cursor = database.query("CATEGORIES",
                        new String[]{"NAME", "IMAGE_RESOURCE_ID", "TYPE"},
                        "CATEGORY = ?",
                        new String[] {_state},
                        null, null, null);



                if(cursor.moveToFirst()){
                    do {
                        Button btn = new Button(getActivity());


                        //set all of the parameters of the button
                        TextView text = new TextView(getActivity());
                        text.setText(cursor.getString(0));
                        text.setGravity(Gravity.CENTER);

                        btn.setHeight(buttonParam);
                        btn.setWidth(buttonParam);
                        btn.setGravity(Gravity.BOTTOM);
                        btn.setOnClickListener(this);



                        //take drawablea and scale it
                        Drawable drawable = getResources().getDrawable(cursor.getInt(1));// Use your image
                        //            drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.5),
//                    (int)(drawable.getIntrinsicHeight()*0.5));
                        Drawable sd = scaleDrawable(drawable);

                        btn.setCompoundDrawablePadding(100);
                        btn.setCompoundDrawablesWithIntrinsicBounds(null, sd, null, null);

                        _buttonList.add(btn);
                        _nameList.add(text);
                        _typeList.add(cursor.getInt(2));



                        //_typeList.add(cursor.getInt(2));

                    } while (cursor.moveToNext());

                }

                cursor.close();
                database.close();


            } catch (SQLiteException e) {
                Log.e("Database", "not available");
            }



    }



}
