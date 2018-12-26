package com.example.varsh.project4;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class QuotesFragment extends ListFragment {
    private static final String TAG = "QuotesFragment";
    private ListSelectionListener2 mListener = null;


    public interface ListSelectionListener2 {
        public void dataDisplay2(List<String> intList);
    }

    @Override
    public void onAttach(Context activity) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onAttach()");
        super.onAttach(activity);

        try {
            mListener = (ListSelectionListener2) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedState);

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.title_item, QuoteViewerActivity.t2.mTitleArray));

    //    getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    public void setList(List<String> intList){
        Log.i("TitlesFrag", "Coming in setList");
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.title_item, intList));
    }

}