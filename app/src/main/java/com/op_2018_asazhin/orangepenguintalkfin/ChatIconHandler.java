package com.op_2018_asazhin.orangepenguintalkfin;

import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatIconHandler {
    ChatFragment _chatFragmnet;

    ArrayList<Button> _btnList;
    ArrayList<TextView> _textList;

    public ChatIconHandler(ChatFragment ctfragmnet){
        _chatFragmnet = ctfragmnet;

        _btnList = new ArrayList<Button>();
        _textList = new ArrayList<TextView>();

    }

    //this class handes the list over to the chat fragment
    public void addToLists(Button newButton, TextView newText){
        _chatFragmnet.addToChatbox(newButton, newText);
    }

}
