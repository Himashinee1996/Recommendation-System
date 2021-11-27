package com.example.toyrecommend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> botChatList;
    private final ArrayList<String> userChatList;

    public MyListAdapter(Activity context, ArrayList<String> botChatList, ArrayList<String> userChatList) {
        super(context, R.layout.mylist, botChatList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.botChatList=botChatList;
        this.userChatList=userChatList;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView botText = (TextView) rowView.findViewById(R.id.bot);
        TextView userText = (TextView) rowView.findViewById(R.id.user);

        if(botChatList.get(position).equals("") || botChatList.get(position) == null){
            botText.setVisibility(View.GONE);
        }
        else{
            userText.setVisibility(View.GONE);
        }

        botText.setText(botChatList.get(position));
        userText.setText(userChatList.get(position));

        return rowView;

    };
}