package com.geanysoftech.ridewisebilling.interfaces;

import android.view.View;

import com.geanysoftech.ridewisebilling.model.PluModel;

public interface AdapterOnItemClcikListener {

    void onItemClick(PluModel pluModel, int position);

    void onClick(View view, int position);


}
