package com.bill.virtualviewtest.event;

import android.util.Log;

import com.tmall.wireless.vaf.virtualview.event.EventData;
import com.tmall.wireless.vaf.virtualview.event.IEventProcessor;

/**
 * author : Bill
 * date : 2021/3/12
 * description :
 */
public class ExposureProcessorImpl implements IEventProcessor {

    @Override
    public boolean process(EventData data) {
        Log.d("IEventProcessor", "ExposureProcessorImpl-> event " + data.mVB + " " + data.mVB.getAction());
        return true;
    }

}