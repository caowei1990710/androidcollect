package com.codeboy.qianghongbao.model;

/**
 * Created by snsoft on 27/4/2017.
 */

public class InfoItem {
//    "transferId": "20170425200040011100040092610625",
//            "payerNickname": "cColin",
//            "transferAmount": 0.01,
//            "transferTime": "2017-04-25 16:56:00"
    private String transferId;
    private String payerNickname;
    private String transferAmount;
    private String transferTime;

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getPayerNickname() {
        return payerNickname;
    }

    public void setPayerNickname(String payerNickname) {
        this.payerNickname = payerNickname;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }
}
