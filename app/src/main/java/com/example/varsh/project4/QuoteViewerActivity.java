package com.example.varsh.project4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuoteViewerActivity extends FragmentActivity implements
        TitlesFragment.ListSelectionListener, QuotesFragment.ListSelectionListener2 {


    public static ThreadClass1 t1;
    public static ThreadClass2 t2;
    public static Handler uiHandler, t1Handler, t2Handler;
    public String t1Number, t2Number;
    public static String winnerThread = "";
    public static Integer[] intArray = {0,1,2,3,4,5,6,7,8,9};
    public static List<Integer> intList = Arrays.asList(intArray);
    public static List<String> t1ResultList = new ArrayList<>();
    public static List<String> t2ResultList = new ArrayList<>();

    private final QuotesFragment mQuoteFragment = new QuotesFragment();
    private FragmentManager mFragmentManager;
    private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;


    public static final int actionCompare = 0;
    public static final int reviewed = 1;
    public static final int updateUI = 2;
    private static final String TAG = "QuoteViewerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, getClass().getSimpleName() + ": entered onCreate()");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTitleFrameLayout = (FrameLayout) findViewById(R.id.title_fragment_container);
        mQuotesFrameLayout = (FrameLayout) findViewById(R.id.quote_fragment_container);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        TitlesFragment t = new TitlesFragment();
        QuotesFragment q = new QuotesFragment();
        fragmentTransaction.add(R.id.title_fragment_container, t);
        fragmentTransaction.add(R.id.quote_fragment_container, q);
        fragmentTransaction.commit();
        mFragmentManager.executePendingTransactions();
        mFragmentManager.putFragment(new Bundle(),"title", t );
        mFragmentManager.putFragment(new Bundle(),"quote", q );
        createThreads();

        String[] strArr = String.valueOf(t1.mTitleArray).split(",")  ; // process.
        List<String> slist = Arrays.asList(strArr);
        Log.i("djvbx", "slist"+slist);

     }


     public void createThreads(){
         uiHandler = new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 Log.i("QuoteviewerActivity", "targer"+msg.arg1+" uihandler"+uiHandler);

                 if(t2.turnCount  >= 21 || t1.turnCount >= 21) {
                     dataDisplay((t1ResultList));
                     Log.i("QuoteViewer", "t1resp"+t1ResultList);
                     dataDisplay2((t2ResultList));
                     Log.i("QuoteViewer", "t2res"+t2ResultList);
                 }
                 if(msg.arg1 == 2) {
                     Log.i("QuoteviewerActivity", "executing t2's guess");
                     Log.i("kjbcfd", "t1 titlearray "+t1.mTitleArray.size());
                     for(String i : t2.mTitleArray) {
                         Log.i("Test2",  "rama " + i);
                     }

                     t2ResultList.add(String.valueOf(msg.obj));
                     t1.guess();
                 }
                 else{
                     //t1 reviews t2's guess
                     Log.i("QuoteviewerActivity", "executing t1's guess");
                     Log.i("kjbcfd", "t2 titlearray "+t2.mTitleArray.size());
                     for(String i : t2.mTitleArray) {
                         Log.i("Test1","rama " + i);
                     }
                     t1ResultList.add(String.valueOf(msg.obj));
                     t2.guess();
                 }

                 Log.i("QuoteViewer", "t1res"+t1.turnCount+" "+t2.turnCount);

                 if(t2.turnCount  >= 21 || t1.turnCount >= 21) {
                     dataDisplay((t1ResultList));
                     Log.i("QuoteViewer", "t1res"+t1ResultList);
                     dataDisplay2((t2ResultList));
                     Log.i("QuoteViewer", "t2res"+t2ResultList);
                 }
             }
         };
         t1 = new ThreadClass1("ThreadOne");
         Thread tt = new Thread(t1,"t1");
         tt.start();
//         t1.start();


         t1.onLooperPrepared();
         t1Handler = t1.tHandler;
         t2 = new ThreadClass2("ThreadTwo");
         t2.start();
         t2.onLooperPrepared();
         t2Handler = t2.tHandler;
         t1Number = t1.enterGame(intList);
         t1ResultList.add(t1Number);
         t2Number = t2.enterGame(intList);
         t2ResultList.add(t2Number);
         Log.i("QuoteViewerActivity", "numbers"+t1Number+" "+t2Number);

         t1.guess();

         for(String i : t1ResultList) {
             Log.i(String.valueOf(t1ResultList.size()),"varsha " + i);
         }

         for(String i : t2ResultList) {
             Log.i(String.valueOf(t2ResultList.size()),"varsha " + i);
         }

         dataDisplay((t1ResultList));
         dataDisplay2((t2ResultList));
     }

     private  List<String>  aaa(List<Integer> str) {
        List<String>  qq = new ArrayList<>();
        for(Integer s : str) {
            qq.add(String.valueOf(s));
        }
        return  qq;
    }

    @Override
    public void dataDisplay(List<String> intList) {
        Log.i("QuoteViewerActivity", "t1 inlist"+intList);
        ((TitlesFragment)(getSupportFragmentManager().findFragmentById(R.id.title_fragment_container))).setList(intList);
//        FragmentActivity.getSupportFragmentManager()
//            new TitlesFragment().setList(intList);
    }

    public void dataDisplay2(List<String> intList) {
        Log.i("QuoteViewerActivity", "t2 inlist"+intList);
        ((QuotesFragment)(getSupportFragmentManager().findFragmentById(R.id.quote_fragment_container))).setList(intList);
    }


}