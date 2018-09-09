package com.ruijie.model.bo;

public class StatisticsResult {

    private int sum;

    private int pass;

    private int fail;

    public StatisticsResult() {
        this.sum = 0;
        this.pass = 0;
        this.fail = 0;
    }

    public StatisticsResult(int sum, int pass, int fail) {
        this.sum = sum;
        this.pass = pass;
        this.fail = fail;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }
}
