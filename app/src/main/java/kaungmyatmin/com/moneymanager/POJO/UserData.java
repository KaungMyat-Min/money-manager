package kaungmyatmin.com.moneymanager.POJO;

public class UserData {
	private String UserName;
	private float salary;
	private float saveAmt;

	private float houseRent;
	private float cost_cloth;
	private float cost_transport;
	private float cost_sms;
	private float cost_talk;
	private float cost_internet;

	private String[] addition_cost_titiles;
	private float[] addition_cost_values;

	private boolean isDaily;

	public boolean isDaily() {
		return isDaily;
	}

	public void setDaily(boolean isDaily) {
		this.isDaily = isDaily;
	}

	public String[] getAddition_cost_titiles() {
		return addition_cost_titiles;

	}

	public void setAddition_cost_titiles(String[] addition_cost_titiles) {
		this.addition_cost_titiles = addition_cost_titiles;
	}

	public float[] getAddition_cost_values() {
		return addition_cost_values;
	}

	public void setAddition_cost_values(float[] addition_cost_values) {
		this.addition_cost_values = addition_cost_values;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public float getSaveAmt() {
		return saveAmt;
	}

	public void setSaveAmt(float saveAmt) {
		this.saveAmt = saveAmt;
	}

	public float getHouseRent() {
		return houseRent;
	}

	public void setHouseRent(float houseRent) {
		this.houseRent = houseRent;
	}

	public float getCost_cloth() {
		return cost_cloth;
	}

	public void setCost_cloth(float cost_cloth) {
		this.cost_cloth = cost_cloth;
	}

	public float getCost_transport() {
		return cost_transport;
	}

	public void setCost_transport(float cost_transport) {
		this.cost_transport = cost_transport;
	}

	public float getCost_sms() {
		return cost_sms;
	}

	public void setCost_sms(float cost_sms) {
		this.cost_sms = cost_sms;
	}

	public float getCost_talk() {
		return cost_talk;
	}

	public void setCost_talk(float cost_talk) {
		this.cost_talk = cost_talk;
	}

	public float getCost_internet() {
		return cost_internet;
	}

	public void setCost_internet(float cost_internet) {
		this.cost_internet = cost_internet;
	}

}
