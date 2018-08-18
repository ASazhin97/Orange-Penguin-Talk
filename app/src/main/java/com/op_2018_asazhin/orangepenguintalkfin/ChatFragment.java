package com.op_2018_asazhin.orangepenguintalkfin;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    //lists
    ArrayList<Button> _iconbuttonList;
    ArrayList<TextView> _texticonList;

    //layouts
    LinearLayout _linearView;

    //buttons
    Button _playButton;
    Button _deleteButton;

    TextToSpeech _textToSpeech;



    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View _v = inflater.inflate(R.layout.fragment_chat, container, false);

        //stats lists
        _iconbuttonList = new ArrayList<Button>();
        _texticonList = new ArrayList<TextView>();

        //get the linear view
       _linearView = _v.findViewById(R.id.chat_lin_view);

       //get the buttons
        _playButton = _v.findViewById(R.id.play_button);
        _deleteButton = _v.findViewById(R.id.delete_button);

        _playButton.setOnClickListener(this);
        _deleteButton.setOnClickListener(this);
        _deleteButton.setOnLongClickListener(this);

        //create the text to speach
        _textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    _textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });



        return _v;
    }


    public void addToChatbox(Button btnadd, TextView txtadd){
        //create copies of the btn
        Button btn = new Button(getActivity());
        Drawable dr = btnadd.getCompoundDrawables()[1];
        btn.setHeight(btnadd.getHeight());
        btn.setWidth(btnadd.getWidth());
        btn.setCompoundDrawablesWithIntrinsicBounds(null, dr, null, null);
        btn.setGravity(Gravity.BOTTOM);
        btn.setOnClickListener(this);

        //create copy of text view
        TextView txt = new TextView(getActivity());
        txt.setText(txtadd.getText());
        txt.setGravity(Gravity.CENTER);

        _iconbuttonList.add(btn);
        _texticonList.add(txt);

        //create a linear vertical layout ot add the txt and button to
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(btn);
        linearLayout.addView(txt);

        _linearView.addView(linearLayout);

    }

    public void deleteFromChatbox(){
        if(!_iconbuttonList.isEmpty() && !_texticonList.isEmpty()) {
            _linearView.removeViewAt(_iconbuttonList.size() - 1);

            _iconbuttonList.remove(_iconbuttonList.size() - 1);
            _texticonList.remove(_texticonList.size() - 1);
        }


    }

    public void clearChatbox(){
        while(!_iconbuttonList.isEmpty()){
            _iconbuttonList.removeAll(_iconbuttonList);
            _texticonList.removeAll(_texticonList);
        }

        _linearView.removeAllViews();
    }



    @Override
    public void onClick(View v) {

        if(v.equals(_deleteButton)){ //if it is the delete button
            deleteFromChatbox();
        } else if (v.equals(_playButton)){ //if its the play button
            playChat();
        } else {
            Button btnClicked = new Button(getActivity()); //if it one of the icon buttons

            for(int n = 0; n < _iconbuttonList.size(); n++){
                if(v.equals(_iconbuttonList.get(n))){
                    playIcon(n);
                }


            }


        }
    }

    //TEXT TO SPEACH METHODS
    public void playChat(){
        //for every text in the list play it
        Log.e("chat Array size", "is " + _texticonList.size());
        String toSpeak = "";

        for(int i = 0; i < _texticonList.size(); i++){
            Log.e("talking", "i is "+i);
            toSpeak = toSpeak + _texticonList.get(i).getText().toString() + " ";


        }

        _textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    public void playIcon(int index){
        String toSpeak = _texticonList.get(index).getText().toString();
        _textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public boolean onLongClick(View v) {
        if(v.equals(_deleteButton)){
            clearChatbox();
        }

        return false;
    }
}
