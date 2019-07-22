package com.example.mahmud.travelmate.Interface;

import com.example.mahmud.travelmate.POJO.NearBy.Result;

import java.util.List;

public interface GoToMapListener {
    void goToMapForNearByItems(List<Result> fullResult);
    void goToMapWithItemDesc(Result result);
}
