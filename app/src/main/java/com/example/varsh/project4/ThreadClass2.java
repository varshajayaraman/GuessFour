package com.example.varsh.project4;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.example.varsh.project4.QuoteViewerActivity.reviewed;
import static com.example.varsh.project4.QuoteViewerActivity.t1;
import static com.example.varsh.project4.QuoteViewerActivity.t1Handler;
import static com.example.varsh.project4.QuoteViewerActivity.t2;
import static com.example.varsh.project4.QuoteViewerActivity.t2Handler;
import static com.example.varsh.project4.QuoteViewerActivity.uiHandler;
import static com.example.varsh.project4.QuoteViewerActivity.updateUI;
import static java.lang.Integer.parseInt;

public class ThreadClass2 extends HandlerThread {

    String threadName;
    public Handler tHandler;
    public List<String> mTitleArray = new ArrayList<String>();
    public int rightNumber;
    public HashMap<String, String> hashMap = new HashMap<String, String>();  //Position-Value Hashmap
    public int turnCount = 0;
    public String guessedNumber;
    public String getGuessedNumberConcatenated;
    public String result;
    public ArrayList<Integer> rightGuessedList = new ArrayList<>();


    public ThreadClass2(String name) {
        super(name);
        this.threadName = name.toLowerCase();
        this.turnCount = 0;
    }

    public String enterGame(List<Integer> intList) {
        Collections.shuffle(intList);
        String number;
        //Guessing 4 digits
        Random rand = new Random();
        number = Integer.toString(intList.get(0)) + Integer.toString(intList.get(1)) + Integer.toString(intList.get(2)) + Integer.toString(intList.get(3));
        this.rightNumber = parseInt(number);
        return ("CORRECT NUMBER for Thread2: "+number);
    }

    public void onLooperPrepared() {

        this.tHandler = new Handler(getLooper()) {

            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case QuoteViewerActivity.actionCompare:
                        if (turnCount < 21) {
                            if (Integer.toString(rightNumber).equals((String) msg.obj)) {
                                Log.i("ThreadClass 2", "play by winner" + threadName+"tc: "+turnCount);
                                t2.turnCount = 23;
                                Log.i("tc2", "t1res"+t2.turnCount);
                             //   final String guessedNumberWinner = (String)msg.obj;
                            /*    t1Handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("QuoteViewer", "runQ");
                                        QuoteViewerActivity.t1ResultList.add("WINNER");
                                        t2.turnCount = 23;
                                    }
                                }); */

                            } //else {
                                    CommentStructure commentObj = giveComments((String) msg.obj);
                                    result = concatenateResult(commentObj, String.valueOf(msg.arg1));
                                    Message message = new Message();
                                    message.what = reviewed;
                                   // message.arg1 = Integer.parseInt(String.valueOf(commentObj.otRightHashMap));
                                    t1.hashMap = commentObj.otRightHashMap;
                                    t1.rightGuessed = commentObj.otRightGuessed;
                                    message.obj = "Review for t1: "+result;
                                 //   message.arg1 = 2;
                                    try{
                                        Thread.sleep(3);
                                    } catch (InterruptedException e){
                                        Log.i("dvs", "Got interrupted. ThreadName: "+threadName);
                                    }
                                    t1Handler.sendMessage(message);
                         //
                            //
                             // }
                        }
                        break;
                    case QuoteViewerActivity.reviewed:
                        Message message = new Message();
                        message.obj = msg.obj;
                        message.what = reviewed;
                        message.arg1 = 2;
                        uiHandler.sendMessage(message);
                }
            }
        };
    }

    public CommentStructure giveComments(String guessedNumber){
        if(guessedNumber.length()==3){
            guessedNumber = "0"+guessedNumber;
        }

        String actualNumber =  Integer.toString(this.rightNumber);
        if(actualNumber.length()==3){
            actualNumber = "0"+actualNumber;
        }
        CommentStructure commentObj = new CommentStructure();

        for(int i=0; i<guessedNumber.length();i++){
            Log.i("TC2", "iVal"+i+"actualNumber"+actualNumber.length()+" "+guessedNumber.length());
            if(actualNumber.charAt(i) == guessedNumber.charAt(i)){
                commentObj.otRightHashMap.put(Integer.toString(i), Character.toString(guessedNumber.charAt(i)));
            }
            else if(actualNumber.contains(Character.toString(guessedNumber.charAt(i)))){
                commentObj.otRightGuessed.add(guessedNumber.charAt(i)-'0');
            }
        }

        Log.i("safd", "Wrong guess"+" "+commentObj.otRightGuessed+" "+commentObj.otRightHashMap+" "+actualNumber+" "+guessedNumber);
        return commentObj;
    }

    public String concatenateResult(CommentStructure commentObj, String guessedNumberFromT1){
        this.getGuessedNumberConcatenated = guessedNumberFromT1;
        this.getGuessedNumberConcatenated += " "+commentObj.otRightGuessed+" "+commentObj.otRightHashMap;
        return this.getGuessedNumberConcatenated;
    }


    public void guess(){     //SENDS GUESS TO THE OTHER THREAD WITH COMPARE CODE
        if(this.turnCount > 21){
            Log.i("fd", "Stopping play "+turnCount+" "+this.threadName);
            this.getLooper().quit();

        }
        guessedNumber = "";
        this.turnCount ++;
        Integer[] tArray = {0,0,0,0};
        for(int i : tArray){
            this.guessedNumber += i;
        }
        Message message = new Message();
        message.what = QuoteViewerActivity.actionCompare;
        message.obj = guessedNumber;
        Log.i("dfnc", "play by "+threadName+" tc"+turnCount+" Guessed Number: "+guessedNumber);
        if(this.threadName.equals("threadone")) {
            t2Handler.sendMessage(message);
        }
        else {
            t1Handler.sendMessage(message);

        }


    }

    public void postComments(CommentStructure commentObj, int destinationThreadNum){

        Message message = new Message();
        message.what = reviewed;
        message.obj = (Object)commentObj;
        message.arg1 = destinationThreadNum;
        uiHandler.sendMessage(message);
    }
}
