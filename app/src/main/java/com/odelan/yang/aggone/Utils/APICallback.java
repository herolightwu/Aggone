package com.odelan.yang.aggone.Utils;

public interface APICallback <T> {
    void onSuccess(T response);
    void onFailure(String error);
}
