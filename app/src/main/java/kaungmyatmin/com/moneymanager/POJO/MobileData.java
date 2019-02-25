package kaungmyatmin.com.moneymanager.POJO;

public class MobileData {
private long lastCheck;
private int sms;
private float smsCost;
private long talkSec;
private int talkCost;
private long rX;
private long tX;
private long totalByte;
private float internetCost;
public long getLastCheck() {
	return lastCheck;
}
public void setLastCheck(long lastCheck) {
	this.lastCheck = lastCheck;
}
public int getSms() {
	return sms;
}
public void setSms(int sms) {
	this.sms = sms;
}
public float getSmsCost() {
	return smsCost;
}
public void setSmsCost(float smsCost) {
	this.smsCost = smsCost;
}
public long getTalkSec() {
	return talkSec;
}
public void setTalkSec(long talkSec) {
	this.talkSec = talkSec;
}
public int getTalkCost() {
	return talkCost;
}
public void setTalkCost(int talkCost) {
	this.talkCost = talkCost;
}
public long getrX() {
	return rX;
}
public void setrX(long rX) {
	this.rX = rX;
}
public long gettX() {
	return tX;
}
public void settX(long tX) {
	this.tX = tX;
}
public long getTotalByte() {
	return totalByte;
}
public void setTotalByte(long totalByte) {
	this.totalByte = totalByte;
}
public float getInternetCost() {
	return internetCost;
}
public void setInternetCost(float internetCost) {
	this.internetCost = internetCost;
}

}
