package com.seeu.ywq.pay.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ywq_pay_balance", indexes = {
        @Index(name = "PAY_BALANCE_INDEX1", columnList = "bind_uid")
})
public class Balance {
    @Id
    private Long uid;

    @Column(name = "bind_uid")
    private Long bindUid;

    @Transient
    private Integer tuiguangNum; // 累计推广人数

    @Column(name = "balance")
    private Long balance;

    private Long coin; // 金币

    private Long recharge; // 充值额总计

    private Long withdraw;  // 提现额总计

    private Long rewardExpense;// 打赏额总计

    private Long rewardReceive;// 被打赏总计

    private Long publishExpense;

    private Long publishReceive; // 相册收入

    private Long wechatExpense;

    private Long wechatReceive; // 微信解锁收入

    private Long phoneExpense;     // 电话解锁支出

    private Long phoneReceive; // 电话解锁收入

    private Long sharedExpense;

    private Long sharedReceive; // 分红收入（用户一对一绑定，10 % 提成）

    private BigDecimal vipBuyExpenseRMB; // 购买VIP花费人民币

    private Long vipBuyExpenseDiamonds;  // 购买VIP花费钻石

    private Long videoExpense;      // 視頻支出

    private Long videoReceive;      // 視頻收入

    private Long sendGift;      // gift send

    private Long signInReceive; // 每日签到收获

    private Date updateTime = new Date();

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getCoin() {
        return coin;
    }

    public void setCoin(Long coin) {
        this.coin = coin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getBindUid() {
        return bindUid;
    }

    public void setBindUid(Long bindUid) {
        this.bindUid = bindUid;
    }

    public Long getRecharge() {
        return recharge;
    }

    public void setRecharge(Long recharge) {
        this.recharge = recharge;
    }

    public Long getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Long withdraw) {
        this.withdraw = withdraw;
    }

    public Long getRewardExpense() {
        return rewardExpense;
    }

    public void setRewardExpense(Long rewardExpense) {
        this.rewardExpense = rewardExpense;
    }

    public Long getRewardReceive() {
        return rewardReceive;
    }

    public void setRewardReceive(Long rewardReceive) {
        this.rewardReceive = rewardReceive;
    }

    public Long getPublishReceive() {
        return publishReceive;
    }

    public void setPublishReceive(Long publishReceive) {
        this.publishReceive = publishReceive;
    }

    public Long getWechatReceive() {
        return wechatReceive;
    }

    public void setWechatReceive(Long wechatReceive) {
        this.wechatReceive = wechatReceive;
    }

    public Long getSharedReceive() {
        return sharedReceive;
    }

    public void setSharedReceive(Long sharedReceive) {
        this.sharedReceive = sharedReceive;
    }

    public Long getPublishExpense() {
        return publishExpense;
    }

    public void setPublishExpense(Long publishExpense) {
        this.publishExpense = publishExpense;
    }

    public Long getWechatExpense() {
        return wechatExpense;
    }

    public void setWechatExpense(Long wechatExpense) {
        this.wechatExpense = wechatExpense;
    }

    public Long getPhoneExpense() {
        return phoneExpense;
    }

    public void setPhoneExpense(Long phoneExpense) {
        this.phoneExpense = phoneExpense;
    }

    public Long getPhoneReceive() {
        return phoneReceive;
    }

    public void setPhoneReceive(Long phoneReceive) {
        this.phoneReceive = phoneReceive;
    }

    public BigDecimal getVipBuyExpenseRMB() {
        return vipBuyExpenseRMB;
    }

    public void setVipBuyExpenseRMB(BigDecimal vipBuyExpenseRMB) {
        this.vipBuyExpenseRMB = vipBuyExpenseRMB;
    }

    public Long getVipBuyExpenseDiamonds() {
        return vipBuyExpenseDiamonds;
    }

    public void setVipBuyExpenseDiamonds(Long vipBuyExpenseDiamonds) {
        this.vipBuyExpenseDiamonds = vipBuyExpenseDiamonds;
    }

    public Long getSharedExpense() {
        return sharedExpense;
    }

    public void setSharedExpense(Long sharedExpense) {
        this.sharedExpense = sharedExpense;
    }

    public Long getSendGift() {
        return sendGift;
    }

    public void setSendGift(Long sendGift) {
        this.sendGift = sendGift;
    }

    public Long getVideoExpense() {
        return videoExpense;
    }

    public void setVideoExpense(Long videoExpense) {
        this.videoExpense = videoExpense;
    }

    public Long getVideoReceive() {
        return videoReceive;
    }

    public void setVideoReceive(Long videoReceive) {
        this.videoReceive = videoReceive;
    }

    public Long getSignInReceive() {
        return signInReceive;
    }

    public void setSignInReceive(Long signInReceive) {
        this.signInReceive = signInReceive;
    }

    public Integer getTuiguangNum() {
        return tuiguangNum;
    }

    public void setTuiguangNum(Integer tuiguangNum) {
        this.tuiguangNum = tuiguangNum;
    }
}
