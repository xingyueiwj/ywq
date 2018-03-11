package com.seeu.ywq.pay.service;


import com.seeu.ywq.pay.exception.BalanceNotEnoughException;
import com.seeu.ywq.pay.model.Balance;
import com.seeu.ywq.uservip.model.UserVIP;

public interface IosPayService {

    Balance payDiamond(Long uid, Long price) throws BalanceNotEnoughException;

    Balance save(Balance balance);

    UserVIP payVip(Long uid, Long time) throws BalanceNotEnoughException;

    UserVIP save(UserVIP userVIP);

    String appStore();

}
