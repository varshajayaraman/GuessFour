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

import static com.example.varsh.project4.QuoteViewerActivity.intArray;
import static com.example.varsh.project4.QuoteViewerActivity.reviewed;
import static com.example.varsh.project4.QuoteViewerActivity.t1;
import static com.example.varsh.project4.QuoteViewerActivity.t1Handler;
import static com.example.varsh.project4.QuoteViewerActivity.t2Handler;
import static com.example.varsh.project4.QuoteViewerActivity.uiHandler;
import static com.example.varsh.project4.QuoteViewerActivity.updateUI;
import static java.lang.Integer.parseInt;

public class ThreadClass1 implements Runnable{

    String threadName;
    public Handler tHandler;
    public List<Integer> rightGuessed = new ArrayList<Integer>();
    public List<String> mTitleArray = new ArrayList<String>();
    public int rightNumber;
    public HashMap<String, String> hashMap = new HashMap<String, String>();  //Position-Value Hashmap
    public int turnCount = 0;
    public String guessedNumber;
    public String getGuessedNumberConcatenated;
    public String result;
    public HashMap<Integer, List<Integer>> possibleNosHashMap = new HashMap<>();


    //public ArrayList<Integer> rightGuessedList = new ArrayList<>();


    public ThreadClass1(String name) {
//        super(name);
        this.threadName = name.toLowerCase();
        this.turnCount = 0;
        possibleNosHashMap.put(0,new ArrayList<Integer>());
        possibleNosHashMap.put(1,new ArrayList<Integer>());
        possibleNosHashMap.put(2,new ArrayList<Integer>());
        possibleNosHashMap.put(3,new ArrayList<Integer>());
    }

    public  void run() {

    }


    public String enterGame(List<Integer> intList) {
        Collections.shuffle(intList);
        String number;
        //Guessing 4 digits
        Random rand = new Random();
        number = Integer.toString(intList.get(0)) + Integer.toString(intList.get(1)) + Integer.toString(intList.get(2)) + Integer.toString(intList.get(3));
        this.rightNumber = parseInt(number);
        //    this.mTitleArray.add("Correct Number for Thread1: " + number);
        Log.i("Threadclass", "necessary guessed number " + this.threadName + " " + this.mTitleArray);
        return ("CORRECT NUMBER for Thread1: "+number);
    }

    public void onLooperPrepared() {

        this.tHandler = new Handler() {

            public void handleMessage(final Message msg) {

                switch (msg.what) {

                    case QuoteViewerActivity.actionCompare:
                        if (turnCount < 21) {
                            if (Integer.toString(rightNumber).equals((String) msg.obj)) {
                                Log.i("ThreadC2", "play by winner" + threadName+"tc: "+turnCount);
                                t1.turnCount =23;
                                Log.i("tc1", "t1res"+t1.turnCount);
                            //    Message message = new Message();
                            //    message.obj = "WINNER "+(String)msg.obj;
                            //    message.what = reviewed;
                            /*      */
                            } //else {
                                CommentStructure commentObj = giveComments((String) msg.obj);
                                result = concatenateResult(commentObj, String.valueOf(msg.arg1));
                                Message message = new Message();
                                message.obj = "Review for t2: "+result;
                                message.what = reviewed;
                                t2Handler.sendMessage(message);
                          //  }
                        }
                        break;
                    case QuoteViewerActivity.reviewed:
                        Message message = new Message();
                        message.what = updateUI;
                        message.obj = msg.obj;
                    //    hashMap = msg.arg1;
                        message.arg1 = 1;
                        uiHandler.sendMessage(message);
                }
            }
        };
    }

    public CommentStructure giveComments(String guessedNumber){
        guessedNumber = "0000";
        Log.i("ThreadClass1", "t1's right no."+this.rightNumber+" "+guessedNumber);
        String actualNumber =  Integer.toString(this.rightNumber);
        if(actualNumber.length()==3){
            actualNumber = "0"+actualNumber;
        }
        CommentStructure commentObj = new CommentStructure();

        for(int i=0; i<guessedNumber.length();i++){
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

    public String concatenateResult(CommentStructure commentObj, String guessedNumberFromT2){
        this.getGuessedNumberConcatenated = "0000";
        this.getGuessedNumberConcatenated += " "+commentObj.otRightGuessed+" "+commentObj.otRightHashMap;
        return this.getGuessedNumberConcatenated;
    }

    public int findFromRightGuessed(int nonZeroIndex, int index){
        while(possibleNosHashMap.get(nonZeroIndex).contains(rightGuessed.get(index))){
            index++;
        }
        return index;
    }

    public void guess(){     //SENDS GUESS TO THE OTHER THREAD WITH COMPARE CODE
        if(this.turnCount > 21){
            Log.i("fd", "Stopping play "+turnCount+" "+this.threadName);

//            this.quit();

        }
        guessedNumber = "";
        this.turnCount ++;
        Integer[] tArray = {0,0,0,0};
        //Applying winning heuristics only in case of t1. t2 simply guesses 0000.
        if (!this.hashMap.isEmpty()) {              //Exhausting info-gather from rightly-placed hashmap.
            for (String key : this.hashMap.keySet()) {
                tArray[parseInt(key)] = parseInt(this.hashMap.get(key));
            }
        }

     /*   while(true) {  //Guessing remaining values from observed right digits.
            int tArrayIndex = 0;
            int rightGuessedListIndex = 0;

            int nonZeroIndex = findIndex(tArrayIndex, tArray); //First non-zero index in tArray.
            Log.i("TC1", "Value of nonzeroIndex"+nonZeroIndex);
            if(nonZeroIndex != -1) {
                int element = findFromRightGuessed(nonZeroIndex, rightGuessedListIndex); //First element from rightGuessedList which isn't proven to be wrong in the previous runs.
                tArray[nonZeroIndex] = rightGuessed.get(i);
                Log.i("TC1", "Keeping "+rightGuessed.get(i)+" in "+nonZeroIndex+"th position");
            }

        } */

        Random random = new Random();
        int number = random.nextInt(10);
        for (int i = 0; i < 4; i++) {
            Log.i("TC1", "Hash ele for "+i+" "+possibleNosHashMap.get(i));
            while (Arrays.asList(tArray).contains(number) || possibleNosHashMap.get(i).contains(number))
                number = random.nextInt(10);
            if (tArray[i] == 0) {
                tArray[i] = number;
            }
        }



        for(Integer i=0;i<tArray.length;i++){
            List<Integer> possibleNosList = new ArrayList<Integer>();
           if(possibleNosHashMap.containsKey(i)){
                possibleNosList = possibleNosHashMap.get(i);
               Log.i("TC1", "Before initialisation"+possibleNosHashMap+"List: "+possibleNosList);
            }
            //possibleNosList.add(0);
            Log.i("TC1", "possibleNosList"+possibleNosList+" tArray "+tArray[i].getClass().getName()+"possibleNosHashMap "+possibleNosHashMap);
            possibleNosList.add(tArray[i]);
            possibleNosHashMap.put(i, possibleNosList);
            Log.i("TC1", "After initialisation"+possibleNosList);
            this.guessedNumber += tArray[i];
        }

        Message message = new Message();
        message.what = QuoteViewerActivity.actionCompare;
        message.arg1 = Integer.parseInt(this.guessedNumber);
        message.obj = guessedNumber;
        Log.i("dfnc", "play by "+threadName+" tc"+turnCount+" Guessed Number: "+guessedNumber);
        if(this.threadName.equals("threadone")) {
            t2Handler.sendMessage(message);
        }
        else {
            t1Handler.sendMessage(message);
        }


    }

    public int findIndex(int index, Integer[] tArray){
        for(int i=index+1; i<4;i++){
            if(tArray[i] == 0){
                return i;
            }
        }
        return -1;
    }
    public void postComments(CommentStructure commentObj, int destinationThreadNum){

        Message message = new Message();
        message.what = QuoteViewerActivity.reviewed;
        message.obj = (Object)commentObj;
        message.arg1 = destinationThreadNum;
        uiHandler.sendMessage(message);
    }
}

